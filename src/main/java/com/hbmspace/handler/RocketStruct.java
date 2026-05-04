package com.hbmspace.handler;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.ModItems;
import com.hbm.items.weapon.ItemMissile;
import com.hbm.util.BufferUtil;
import com.hbm.util.Tuple;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.weapon.ItemCustomMissilePart;
import com.hbmspace.render.misc.RocketPart;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RocketStruct {

    public ItemMissile capsule;
    public ArrayList<RocketStage> stages = new ArrayList<>();
    public int satFreq = 0;
    public static final HashMap<Integer, Double> serverParts = new HashMap<>();
    public List<String> extraIssues = new ArrayList<>();

    public static final int MAX_STAGES = 5;

    public RocketStruct() {}

    public RocketStruct(ItemStack capsule) {
        if (!capsule.isEmpty() && capsule.getItem() instanceof ItemMissile) {
            this.capsule = (ItemMissile) capsule.getItem();
        }
    }

    public RocketStruct(ItemMissile capsule) {
        this.capsule = capsule;
    }

    public void addStage(ItemStack fuselage, ItemStack fins, ItemStack thruster) {
        addStage(
                fuselage.isEmpty() ? null : (ItemMissile) fuselage.getItem(),
                fins.isEmpty() ? null : (ItemMissile) fins.getItem(),
                thruster.isEmpty() ? null : (ItemMissile) thruster.getItem(),
                !fuselage.isEmpty() ? fuselage.getCount() : 1,
                !thruster.isEmpty() ? thruster.getCount() : 1
        );
    }

    public void addStage(ItemMissile fuselage, ItemMissile fins, ItemMissile thruster, int fuselageCount, int thrusterCount) {
        RocketStage stage = new RocketStage();
        stage.fuselage = fuselage;
        stage.fins = fins;
        stage.thruster = thruster;
        stage.fuselageCount = fuselageCount;
        stage.thrusterCount = thrusterCount;
        stages.addFirst(stage);
    }

    public boolean validate() {
        if(!extraIssues.isEmpty())
            return false;

        if(capsule == null || capsule.type != ItemMissile.PartType.WARHEAD)
            return false;

        if(capsule.attributes[0] != ItemMissile.WarheadType.APOLLO && capsule.attributes[0] != ItemMissile.WarheadType.SATELLITE)
            return false;

        if(stages.isEmpty())
            return false;

        for(RocketStage stage : stages) {
            if(stage.fuselage == null || stage.fuselage.type != ItemMissile.PartType.FUSELAGE) return false;
            if(stage.fins != null && stage.fins.type != ItemMissile.PartType.FINS) return false;
            if(stage.thruster == null || stage.thruster.type != ItemMissile.PartType.THRUSTER) return false;

            if(stage.thrusterCount > stage.fuselageCount || stage.fuselageCount % stage.thrusterCount != 0) return false;

            if(stage.fuselage.attributes[0] != ItemMissile.FuelType.ANY && stage.fuselage.attributes[0] != stage.thruster.attributes[0]) return false;
        }

        return true;
    }

    public void addIssue(String issue) {
        extraIssues.add(issue);
    }

    // Lists any validation issues so the player can rectify easily
    public List<String> findIssues(int stageNum, CelestialBody from, CelestialBody to, boolean fromOrbit, boolean toOrbit) {
        List<String> issues = new ArrayList<>();

        // If we have no parts, we have no worries
        if(capsule == null && stages.isEmpty()) return issues;

        if(capsule == null || (capsule.attributes[0] != ItemMissile.WarheadType.APOLLO && capsule.attributes[0] != ItemMissile.WarheadType.SATELLITE))
            issues.add(TextFormatting.RED + "Invalid Capsule/Satellite");

        // Current stage stats
        if(stageNum < stages.size()) {
            RocketStage stage = stages.get(stageNum);
            issues.add("Dry mass: " + getLaunchMass(stageNum) + "kg");
            issues.add("Wet mass: " + getWetMass(stageNum) + "kg");
            if(stage.thruster != null) {
                issues.add("Thrust: " + getThrust(stage) + "N");
                issues.add("ISP: " + getISP(stage) + "s");
            }
        }

        for(int i = 0; i < stages.size(); i++) {
            RocketStage stage = stages.get(i);
            if(stage.fuselage == null)
                issues.add(TextFormatting.RED + "Stage " + (i + 1) + " missing fuselage");
            if(stage.thruster == null)
                issues.add(TextFormatting.RED + "Stage " + (i + 1) + " missing thruster");

            if(stage.fuselage == null || stage.thruster == null)
                continue;

            if(stage.thrusterCount > stage.fuselageCount)
                issues.add(TextFormatting.RED + "Stage " + (i + 1) + " too many thrusters");
            if(stage.fuselageCount % stage.thrusterCount != 0)
                issues.add(TextFormatting.RED + "Stage " + (i + 1) + " uneven thrusters");

            if(stage.fuselage.attributes[0] != ItemMissile.FuelType.ANY && stage.fuselage.attributes[0] != stage.thruster.attributes[0])
                issues.add(TextFormatting.RED + "Stage " + (i + 1) + " fuel mismatch");

            if(i > 0 && stage.fins == null)
                issues.add(TextFormatting.YELLOW + "Stage " + (i + 1) + " lacks landing legs");
        }

        if(from != null && to != null) {
            int fuelRequirement = getFuelRequired(stageNum, from, to, fromOrbit, toOrbit);
            int fuelCapacity = getFuelCapacity(stageNum);

            if(fuelRequirement == Integer.MAX_VALUE) {
                issues.add(TextFormatting.YELLOW + "Insufficient thrust");
            } else if(fuelCapacity < fuelRequirement) {
                issues.add(TextFormatting.YELLOW + "Insufficient fuel: " + fuelCapacity + "/" + fuelRequirement + "mB");
            } else if(fuelCapacity > 0 && fuelRequirement > 0) {
                issues.add(TextFormatting.GREEN + "Trip possible! " + fuelCapacity + "/" + fuelRequirement + "mB");
            }
        }

        issues.addAll(extraIssues);

        return issues;
    }

    // NONE fluid is solid fuel
    public Map<FluidType, Integer> getFillRequirement() {
        Map<FluidType, Integer> tanks = new HashMap<>();

        for(RocketStage stage : stages) {
            if(stage.thruster == null || stage.fuselage == null) continue;

            FluidType fuel = stage.thruster.getFuel();
            FluidType oxidizer = stage.thruster.getOxidizer();

            if(fuel != null) {
                int amount = (int) (stage.fuselage.getTankSize() * stage.fuselageCount);
                if(tanks.containsKey(fuel)) amount += tanks.get(fuel);
                tanks.put(fuel, amount);
            }

            if(oxidizer != null) {
                int amount = (int) (stage.fuselage.getTankSize() * stage.fuselageCount);
                if(tanks.containsKey(oxidizer)) amount += tanks.get(oxidizer);
                tanks.put(oxidizer, amount);
            }
        }

        return tanks;
    }

    public boolean hasSufficientFuel(CelestialBody from, CelestialBody to, boolean fromOrbit, boolean toOrbit) {
        if(capsule == ModItemsSpace.rp_pod_20) {
            return from == to && (fromOrbit || toOrbit); // Pods can transfer, fall to orbited body, and return to station, but NOT hop on the surface
        }

        if(stages.isEmpty()) {
            return from == to && fromOrbit && !toOrbit; // Capsules can return to orbited body from orbit only
        }

        int fuelRequirement = getFuelRequired(0, from, to, fromOrbit, toOrbit);
        int fuelCapacity = getFuelCapacity(0);

        return fuelCapacity >= fuelRequirement;
    }

    private int getFuelCapacity(int stageNum) {
        if(stageNum >= stages.size()) return -1;

        RocketStage stage = stages.get(stageNum);

        if(stage.fuselage == null) return -1;

        return (int) (stage.fuselage.getTankSize() * stage.fuselageCount);
    }

    private int getFuelRequired(int stageNum, CelestialBody from, CelestialBody to, boolean fromOrbit, boolean toOrbit) {
        if(stageNum >= stages.size()) return -1;

        RocketStage stage = stages.get(stageNum);

        if(stage.fuselage == null || stage.thruster == null) return -1;

        int rocketMass = getLaunchMass(stageNum);
        int thrust = getThrust(stage);
        int isp = getISP(stage);

        return SolarSystem.getCostBetween(from, to, rocketMass, thrust, isp, fromOrbit, toOrbit);
    }

    public int getThrust() {
        return getThrust(stages.getFirst());
    }

    private int getThrust(RocketStage stage) {
        if(ItemCustomMissilePart.THRUSTER_ATTRIBUTES.containsKey(stage.thruster)) return (Integer) ItemCustomMissilePart.THRUSTER_ATTRIBUTES.get(stage.thruster)[3];
        else return stage.thruster.getThrust() * stage.thrusterCount;
    }

    private int getISP(RocketStage stage) {
        if(ItemCustomMissilePart.THRUSTER_ATTRIBUTES.containsKey(stage.thruster)) return (Integer) ItemCustomMissilePart.THRUSTER_ATTRIBUTES.get(stage.thruster)[4];
        else return stage.thruster.getISP();
    }

    public int getLaunchMass() {
        return getMass(0, false);
    }

    public int getLaunchMass(int stageNum) {
        return getMass(stageNum, false);
    }

    public int getWetMass(int stageNum) {
        return getMass(stageNum, true);
    }

    private int getMass(int stageNum, boolean wet) {
        int mass = 0;

        if(capsule != null) mass += capsule.mass;

        for(int i = stageNum; i < stages.size(); i++) {
            RocketStage stage = stages.get(i);
            if(stage.fuselage != null) mass += stage.fuselage.mass * stage.fuselageCount;
            if(stage.thruster != null) mass += stage.thruster.mass * stage.thrusterCount;

            if(stage.fuselage != null && (i > stageNum || wet)) {
                mass += (int) (stage.fuselage.getTankSize() * stage.fuselageCount / 4);
            }
        }

        return MathHelper.ceil(mass);
    }

    public double getHeight() {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) return getServerHeight();
        return getHeightClient();
    }

    public double getServerHeight() {
        double height = 0;
        if(capsule != null) height += getPartHeight(capsule);

        boolean isDeployed = true;
        for(RocketStage stage : stages) {
            if(stage.fuselage != null) {
                height += getPartHeight(stage.fuselage) * stage.getStack();
            }
            double th = 0;
            if(stage.thruster != null) {
                th = getPartHeight(stage.thruster);
            }
            double fh = 0;
            if(stage.fins != null && isDeployed) {
                fh = getPartHeight(stage.fins);
            }
            height += Math.max(th, fh);
            isDeployed = false;
        }
        return height;
    }

    public static double getPartHeight(ItemMissile item) {
        if (item == null) return 0;

        Double h = serverParts.get(Item.getIdFromItem(item));
        return h != null ? h : 0;
    }

    @SideOnly(Side.CLIENT)
    private double getHeightClient() {
        double height = 0;
        if(capsule != null) {
            RocketPart part = RocketPart.getPart(capsule);
            if(part != null) height += part.height;
        }
        boolean isDeployed = true;
        for(RocketStage stage : stages) {
            if(stage.fuselage != null) {
                RocketPart fPart = RocketPart.getPart(stage.fuselage);
                if(fPart != null) height += fPart.height * stage.getStack();
            }
            double th = 0;
            if(stage.thruster != null) {
                RocketPart tPart = RocketPart.getPart(stage.thruster);
                if(tPart != null) th = tPart.height;
            }
            double fh = 0;
            if(stage.fins != null && isDeployed) {
                RocketPart fnPart = RocketPart.getPart(stage.fins);
                if(fnPart != null) fh = fnPart.height;
            }
            height += Math.max(th, fh);
            isDeployed = false;
        }
        return height;
    }

    public double getHeight(int stageNum) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) return 0;
        return getHeightClient(stageNum);
    }

    @SideOnly(Side.CLIENT)
    private double getHeightClient(int stageNum) {
        double height = 0;

        if(!stages.isEmpty()) {
            RocketStage stage = stages.get(Math.min(stageNum, stages.size() - 1));

            if(stage.fuselage != null) {
                RocketPart fPart = RocketPart.getPart(stage.fuselage);
                if(fPart != null) height += fPart.height * stage.getStack();
            }

            double th = 0;
            if(stage.thruster != null) {
                RocketPart tPart = RocketPart.getPart(stage.thruster);
                if(tPart != null) th = tPart.height;
            }

            double fh = 0;
            if(stageNum == 0 && stage.fins != null) {
                RocketPart fnPart = RocketPart.getPart(stage.fins);
                if(fnPart != null) fh = fnPart.height;
            }

            height += Math.max(th, fh);
        }

        if(stages.isEmpty() || stageNum == stages.size() - 1) {
            if(capsule != null) {
                RocketPart cPart = RocketPart.getPart(capsule);
                if(cPart != null) height += cPart.height;
            }
        }

        return height;
    }

    public double getOffset(int stageNum) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) return 0;
        return getOffsetClient(stageNum);
    }

    @SideOnly(Side.CLIENT)
    private double getOffsetClient(int stageNum) {
        double height = 0;

        for(int i = 0; i < Math.min(stageNum, stages.size() - 1); i++) {
            RocketStage stage = stages.get(i);

            if(stage.fuselage != null) {
                RocketPart fPart = RocketPart.getPart(stage.fuselage);
                if(fPart != null) height += fPart.height * stage.getStack();
            }

            double th = 0;
            if(stage.thruster != null) {
                RocketPart tPart = RocketPart.getPart(stage.thruster);
                if(tPart != null) th = tPart.height;
            }

            double fh = 0;
            if(i == 0 && stage.fins != null) {
                RocketPart fnPart = RocketPart.getPart(stage.fins);
                if(fnPart != null) fh = fnPart.height;
            }

            height += Math.max(th, fh);
        }

        return height;
    }

    public void writeToByteBuffer(ByteBuf buf) {
        buf.writeInt(capsule != null ? Item.getIdFromItem(capsule) : 0);

        buf.writeInt(stages.size());
        for(RocketStage stage : stages) {
            buf.writeInt(stage.fuselage != null ? Item.getIdFromItem(stage.fuselage) : 0);
            buf.writeInt(stage.fins != null ? Item.getIdFromItem(stage.fins) : 0);
            buf.writeInt(stage.thruster != null ? Item.getIdFromItem(stage.thruster) : 0);
            buf.writeByte(stage.fuselageCount);
            buf.writeByte(stage.thrusterCount);
        }

        buf.writeInt(extraIssues.size());
        for(String issue : extraIssues) {
            BufferUtil.writeString(buf, issue);
        }
    }

    @Contract("_ -> new")
    public static RocketStruct readFromByteBuffer(ByteBuf buf) {
        RocketStruct rocket = new RocketStruct();
        int capId = buf.readInt();
        rocket.capsule = capId == 0 ? null : (ItemMissile) Item.getItemById(capId);

        int count = buf.readInt();
        for(int i = 0; i < count; i++) {
            RocketStage stage = new RocketStage();
            int fId = buf.readInt();
            stage.fuselage = fId == 0 ? null : (ItemMissile) Item.getItemById(fId);
            int fnId = buf.readInt();
            stage.fins = fnId == 0 ? null : (ItemMissile) Item.getItemById(fnId);
            int tId = buf.readInt();
            stage.thruster = tId == 0 ? null : (ItemMissile) Item.getItemById(tId);

            stage.fuselageCount = buf.readByte();
            stage.thrusterCount = buf.readByte();
            rocket.stages.add(stage);
        }

        count = buf.readInt();
        for(int i = 0; i < count; i++) {
            rocket.extraIssues.add(BufferUtil.readString(buf));
        }

        return rocket;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("capsule", capsule != null ? Item.getIdFromItem(capsule) : 0);

        NBTTagList stagesTag = new NBTTagList();
        for(RocketStage stage : stages) {
            NBTTagCompound stageTag = new NBTTagCompound();
            stageTag.setInteger("fuselage", stage.fuselage != null ? Item.getIdFromItem(stage.fuselage) : 0);
            stageTag.setInteger("fins", stage.fins != null ? Item.getIdFromItem(stage.fins) : 0);
            stageTag.setInteger("thruster", stage.thruster != null ? Item.getIdFromItem(stage.thruster) : 0);
            stageTag.setInteger("fc", stage.fuselageCount);
            stageTag.setInteger("tc", stage.thrusterCount);
            stagesTag.appendTag(stageTag);
        }
        nbt.setTag("stages", stagesTag);

        nbt.setInteger("freq", satFreq);
    }

    public static RocketStruct readFromNBT(NBTTagCompound nbt) {
        RocketStruct rocket = new RocketStruct();
        int capId = nbt.getInteger("capsule");
        rocket.capsule = capId == 0 ? null : (ItemMissile) Item.getItemById(capId);

        NBTTagList stagesTag = nbt.getTagList("stages", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < stagesTag.tagCount(); i++) {
            NBTTagCompound stageTag = stagesTag.getCompoundTagAt(i);
            RocketStage stage = new RocketStage();
            int fuselage = stageTag.getInteger("fuselage");
            int fins = stageTag.getInteger("fins");
            int thruster = stageTag.getInteger("thruster");
            stage.fuselage = fuselage == 0 ? null : (ItemMissile) Item.getItemById(fuselage);
            stage.fins = fins == 0 ? null : (ItemMissile) Item.getItemById(fins);
            stage.thruster = thruster == 0 ? null : (ItemMissile) Item.getItemById(thruster);
            stage.fuselageCount = Math.max(stageTag.getInteger("fc"), 1);
            stage.thrusterCount = Math.max(stageTag.getInteger("tc"), 1);
            rocket.stages.add(stage);
        }

        if(rocket.capsule == null) {
            rocket.capsule = (ItemMissile) ModItemsSpace.rp_capsule_20;
        }

        return rocket;
    }

    public static class RocketStage {

        public ItemMissile fuselage;
        public ItemMissile fins;
        public ItemMissile thruster;
        public int fuselageCount = 1;
        public int thrusterCount = 1;

        public Tuple.Pair<Integer, Integer> zipWatchable() {
            int first = ((fuselage != null ? Item.getIdFromItem(fuselage) : 0) << 16) | (fins != null ? Item.getIdFromItem(fins) : 0);
            int second = ((thruster != null ? Item.getIdFromItem(thruster) : 0) << 16) | (fuselageCount << 8) | thrusterCount;
            return new Tuple.Pair<>(first, second);
        }

        public static RocketStage unzipWatchable(Tuple.Pair<Integer, Integer> pair) {
            RocketStage stage = new RocketStage();
            int fId = (pair.key >> 16) & 0xFFFF;
            int fnId = pair.key & 0xFFFF;
            int tId = (pair.value >> 16) & 0xFFFF;

            stage.fuselage = fId == 0 ? null : (ItemMissile) Item.getItemById(fId);
            stage.fins = fnId == 0 ? null : (ItemMissile) Item.getItemById(fnId);
            stage.thruster = tId == 0 ? null : (ItemMissile) Item.getItemById(tId);

            stage.fuselageCount = (pair.value >> 8) & 0xFF;
            stage.thrusterCount = pair.value & 0xFF;
            return stage;
        }

        public int getStack() {
            return thrusterCount > 0 ? Math.max(fuselageCount / thrusterCount, 1) : 0;
        }

        public int getCluster() {
            return getStack() > 0 ? Math.max(fuselageCount / getStack(), 1) : 0;
        }

    }

    public static void registerServerParts() {
        serverParts.put(Item.getIdFromItem(ModItemsSpace.sat_war), 7D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.sat_dyson_relay), 7D);

        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_capsule_20), 3.5D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_station_core_20), 7D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_pod_20), 3.0D);

        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_10_kerosene), 1D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_10_solid), 0.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_10_xenon), 0.5D);

        //
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_kerosene), 1.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_kerosene_dual), 1D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_kerosene_triple), 1D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_solid), 0.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_solid_hexdecuple), 0.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_hydrogen), 1.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_hydrogen_dual), 1D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_balefire_short), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_balefire), 3D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_balefire_large), 3D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_15_balefire_large_rad), 3D);
        //
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_20_kerosene), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_20_kerosene_dual), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_20_kerosene_triple), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_methalox), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_methalox_dual), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_methalox_triple), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_hydrogen), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_hydrogen_dual), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_hydrogen_triple), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_20_solid), 1D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_20_solid_multi), 0.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_thruster_20_solid_multier), 0.5D);

        //////
        serverParts.put(Item.getIdFromItem(ModItems.mp_stability_10_flat), 0D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_stability_10_cruise), 0D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_stability_10_space), 0D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_stability_15_flat), 0D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_stability_15_thin), 0D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_stability_15_soyuz), 0D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_legs_20), 2.4D);
        //////

        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_camo), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_desert), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_sky), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_insulation), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_flames), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_sleek), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_metal), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_kerosene_taint), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_flames), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_insulation), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_sleek), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_soviet_glory), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_cathedral), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_moonlit), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_battery), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_solid_duracell), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_xenon), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_xenon_bhole), 4D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_camo), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_desert), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_sky), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_flames), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_insulation), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_sleek), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_metal), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_dash), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_taint), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_kerosene_vap), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_solid), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_solid_flames), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_solid_insulation), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_solid_sleek), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_solid_soviet_glory), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_solid_bullet), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_long_solid_silvermoonlight), 7D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_15_kerosene), 9D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_15_solid), 9D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_15_hydrogen), 9D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_10_15_balefire), 9D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_camo), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_desert), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_sky), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_insulation), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_metal), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_decorated), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_steampunk), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_polite), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_blackjack), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_lambda), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_minuteman), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_pip), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_taint), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_kerosene_yuck), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_insulation), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_desh), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_soviet_glory), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_soviet_stank), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_faust), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_silvermoonlight), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_snowy), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_panorama), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_roses), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_solid_mimi), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_hydrogen), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_hydrogen_cathedral), 10D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_balefire), 10D);

        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_20_kerosene), 16D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_20_kerosene_magnusson), 16D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_fuselage_15_20_solid), 16D);

        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_fuselage_20_12_hydrazine), 10D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_fuselage_20_12), 12D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_fuselage_20_6), 6D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_fuselage_20_3), 3D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.rp_fuselage_20_1), 1D);

        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_methalox), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_methalox_dual), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_methalox_triple), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_hydrogen), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_hydrogen_dual), 2D);
        serverParts.put(Item.getIdFromItem(ModItemsSpace.mp_thruster_20_hydrogen_triple), 2D);

        //////

        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_10_he), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_10_incendiary), 2.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_10_buster), 0.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_10_nuclear), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_10_nuclear_large), 2.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_10_taint), 2.25D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_10_cloud), 2.25D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_he), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_incendiary), 2D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_nuclear), 3.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_nuclear_shark), 3.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_thermo), 3.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_volcano), 3.5D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_boxcar), 2.25D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_n2), 3D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_balefire), 2.75D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_mirv), 3D);
        serverParts.put(Item.getIdFromItem(ModItems.mp_warhead_15_turbine), 2.25D);
    }
}
