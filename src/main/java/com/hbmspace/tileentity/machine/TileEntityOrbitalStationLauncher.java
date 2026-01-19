package com.hbmspace.tileentity.machine;

import com.hbm.api.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.items.ISatChip;
import com.hbm.items.ModItems;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.entity.missile.EntityRideableRocket;
import com.hbmspace.entity.missile.EntityRideableRocket.RocketState;
import com.hbmspace.handler.RocketStruct;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerOrbitalStationLauncher;
import com.hbmspace.inventory.gui.GUIOrbitalStationLauncher;
import com.hbmspace.inventory.slots.SlotRocket;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ItemVOTVdrive.Target;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.weapon.ItemCustomRocket;
import com.hbmspace.tileentity.bomb.TileEntityLaunchPadRocket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import com.hbmspace.tileentity.bomb.TileEntityLaunchPadRocket.SolidFuelTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AutoRegister
public class TileEntityOrbitalStationLauncher extends TileEntityOrbStation implements ITickable, ISpaceGuiProvider, IControlReceiver, IFluidStandardReceiverMK2 {

    public RocketStruct rocket;

    private OrbitalStation station;
    private EntityRideableRocket docked;

    public FluidTankNTM[] tanks;
    public SolidFuelTank solidFuel = new SolidFuelTank();

    public float rot;
    public float prevRot;

    public int currentStage;

    public boolean isBreaking;

    // Client synced state information
    public boolean hasDocked = false;
    public boolean hasRider = false;

    public TileEntityOrbitalStationLauncher() {
        // launch:			drive + fuel in + fuel out
        // construction:	capsule + stages + program drives
        super(1 + 1 + 1 +
                        1 + RocketStruct.MAX_STAGES * 3 + RocketStruct.MAX_STAGES * 2, true, false);

        tanks = new FluidTankNTM[RocketStruct.MAX_STAGES * 2]; // enough tanks for any combination of rocket stages
        for(int i = 0; i < tanks.length; i++) tanks[i] = new FluidTankNTM(Fluids.NONE, 64_000);
    }

    @Override
    protected ItemStackHandler getNewInventory(int scount, final int slotlimit) {
        return new LauncherInventory(scount);
    }

    @Override
    public String getDefaultName() {
        return "container.orbitalStationLauncher";
    }

    @Override
    public void update() {
        if(!CelestialBody.inOrbit(world)) return;

        if(!world.isRemote) {
            // This TE acts almost entirely like a port, except doesn't register itself so nothing actually tries to dock here


            // ROCKET CONSTRUCTION //
            // Setup the constructed rocket
            ItemStack fromStack = inventory.getStackInSlot(inventory.getSlots() - (RocketStruct.MAX_STAGES - currentStage) * 2);
            ItemStack toStack = inventory.getStackInSlot(inventory.getSlots() - (RocketStruct.MAX_STAGES - currentStage) * 2 + 1);

            // updates the orbital station information and syncs it to the client, if necessary
            ItemVOTVdrive.getTarget(inventory.getStackInSlot(0), world);
            ItemVOTVdrive.getTarget(fromStack, world);
            ItemVOTVdrive.getTarget(toStack, world);

            rocket = new RocketStruct(inventory.getStackInSlot(3));
            if(!inventory.getStackInSlot(3).isEmpty() && inventory.getStackInSlot(3).getItem() instanceof ISatChip) {
                rocket.satFreq = ISatChip.getFreqS(inventory.getStackInSlot(3));
            }
            for(int i = 4; i < RocketStruct.MAX_STAGES * 3 + 3; i += 3) {
                if(!inventory.getStackInSlot(i).isEmpty() && inventory.getStackInSlot(i+1).isEmpty() && inventory.getStackInSlot(i+2).isEmpty()) {
                    // Check for later stages and shift them up into empty stages
                    if(i + 3 < RocketStruct.MAX_STAGES * 3 && (!inventory.getStackInSlot(i+3).isEmpty() || !inventory.getStackInSlot(i+4).isEmpty() || !inventory.getStackInSlot(i+5).isEmpty())) {
                        inventory.setStackInSlot(i, inventory.getStackInSlot(i + 3));
                        inventory.setStackInSlot(i+1, inventory.getStackInSlot(i + 4));
                        inventory.setStackInSlot(i+2, inventory.getStackInSlot(i + 5));
                        inventory.setStackInSlot(i+3, ItemStack.EMPTY);
                        inventory.setStackInSlot(i+4, ItemStack.EMPTY);
                        inventory.setStackInSlot(i+5, ItemStack.EMPTY);
                    } else {
                        break;
                    }
                }
                rocket.addStage(inventory.getStackInSlot(i), inventory.getStackInSlot(i + 1), inventory.getStackInSlot(i + 2));
            }


            // ROCKET LAUNCHING
            updateTanks();

            // Connections
            if(world.getTotalWorldTime() % 20 == 0) {
                for(DirPos pos : getConPos()) {
                    // trySubscribe(world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());

                    if(rocket.validate()) {
                        for(FluidTankNTM tank : tanks) {
                            if(tank.getTankType() == Fluids.NONE) continue;
                            trySubscribe(tank.getTankType(), world, pos);
                        }
                    }
                }
            }

            for(FluidTankNTM tank : tanks) tank.loadTank(1, 2, inventory);
            ItemStack stack = inventory.getStackInSlot(1);

            if (!stack.isEmpty()
                    && stack.getItem() == ModItems.rocket_fuel
                    && solidFuel.level < solidFuel.max) {

                ItemStack extracted = inventory.extractItem(1, 1, false);
                if (!extracted.isEmpty()) {
                    solidFuel.level += 250;
                    if (solidFuel.level > solidFuel.max) solidFuel.level = solidFuel.max;
                }
            }

            if(docked != null && (docked.isDead || docked.getState() == RocketState.UNDOCKING)) {
                undockRocket();
            }

            hasDocked = docked != null;
            hasRider = hasDocked && docked.isBeingRidden();

            networkPackNT(250);
        } else {
            prevRot = rot;
            if(hasDocked) {
                rot += 2.25F;
                if(rot > 90) rot = 90;
            } else {
                rot -= 2.25F;
                if(rot < 0) rot = 0;
            }
        }
    }

    public void enterCapsule(EntityPlayer player) {
        if (docked == null || docked.isBeingRidden()) return;
        docked.processInitialInteract(player, EnumHand.MAIN_HAND);
    }

    public void dockRocket(EntityRideableRocket rocket) {
        docked = rocket;
    }

    public void undockRocket() {
        docked = null;
    }

    private boolean hasDrive() {
        return !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() instanceof ItemVOTVdrive;
    }

    private boolean areTanksFull() {
        for(FluidTankNTM tank : tanks) if(tank.getTankType() != Fluids.NONE && tank.getFill() < tank.getMaxFill()) return false;
        return solidFuel.level >= solidFuel.max;
    }

    private boolean canReachDestination() {
        // Check that the drive is processed
        if(!ItemVOTVdrive.getProcessed(inventory.getStackInSlot(0))) {
            return false;
        }

        SolarSystem.Body target = ItemVOTVdrive.getDestination(inventory.getStackInSlot(0)).body;
        if(target == SolarSystem.Body.ORBIT && rocket.capsule.part != ModItemsSpace.rp_capsule_20 && rocket.capsule.part != ModItemsSpace.rp_station_core_20)
            return false;

        Target from = CelestialBody.getTarget(world, pos.getX(), pos.getZ());
        Target to = ItemVOTVdrive.getTarget(inventory.getStackInSlot(0), world);

        if(!to.isValid && rocket.capsule.part != ModItemsSpace.rp_station_core_20) return false;
        if(to.isValid && rocket.capsule.part == ModItemsSpace.rp_station_core_20) return false;

        // Check if the stage can make the journey
        return rocket.hasSufficientFuel(from.body, to.body, from.inOrbit, to.inOrbit);
    }

    public boolean canLaunch() {
        return rocket.validate() && hasDrive() && areTanksFull() && canReachDestination();
    }

    public void launch(EntityPlayer player) {
        // if(docked != null) {
        // 	enterCapsule(player);
        // 	return;
        // }

        if(!canLaunch()) return;

        ItemStack stack = ItemCustomRocket.build(rocket);

        EntityRideableRocket rocket = new EntityRideableRocket(world, pos.getX() + 0.5F, pos.getY() + 1.5F, pos.getZ() + 0.5F, stack).withProgram(inventory.getStackInSlot(0)).launchedBy(player);
        rocket.posY -= rocket.height;
        world.spawnEntity(rocket);

        // Deplete all fills
        for(int i = 0; i < tanks.length; i++) tanks[i] = new FluidTankNTM(Fluids.NONE, 64_000);
        solidFuel.level = solidFuel.max = 0;

        // power -= maxPower * 0.75;

        inventory.setStackInSlot(0, null);
        for(int i = 3; i < inventory.getSlots(); i++) inventory.setStackInSlot(i, null);

        dockRocket(rocket);

        // if(rocket.canRide()) enterCapsule(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(stack == null) return true;
        return index != 0 || stack.getItem() instanceof ItemVOTVdrive;
    }

    private void updateTanks() {
        if(!rocket.validate()) return;
        TileEntityLaunchPadRocket.updateStorageTanks(rocket, tanks, solidFuel, false);
    }

    public DirPos[] getConPos() {
        return new DirPos[] {
                new DirPos(pos.getX() - 1, pos.getY() + 1, pos.getZ() + 3, ForgeDirection.NORTH),
                new DirPos(pos.getX(), pos.getY() + 1, pos.getZ() + 3, ForgeDirection.NORTH),
                new DirPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 3, ForgeDirection.NORTH),

                new DirPos(pos.getX() - 1, pos.getY() + 1, pos.getZ() - 3, ForgeDirection.SOUTH),
                new DirPos(pos.getX(), pos.getY() + 1, pos.getZ() - 3, ForgeDirection.SOUTH),
                new DirPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() - 3, ForgeDirection.SOUTH),

                new DirPos(pos.getX() + 3, pos.getY() + 1, pos.getZ() - 1, ForgeDirection.EAST),
                new DirPos(pos.getX() + 3, pos.getY() + 1, pos.getZ(), ForgeDirection.EAST),
                new DirPos(pos.getX() + 3, pos.getY() + 1, pos.getZ() + 1, ForgeDirection.EAST),

                new DirPos(pos.getX() - 3, pos.getY() + 1, pos.getZ() - 1, ForgeDirection.WEST),
                new DirPos(pos.getX() - 3, pos.getY() + 1, pos.getZ(), ForgeDirection.WEST),
                new DirPos(pos.getX() - 3, pos.getY() + 1, pos.getZ() + 1, ForgeDirection.WEST),
        };
    }

    public List<String> findIssues() {
        List<String> issues = new ArrayList<>();

        if(!rocket.validate()) return issues;

        // if(power < maxPower * 0.75) {
        // 	issues.add(TextFormatting.RED + "Insufficient power");
        // }

        TileEntityLaunchPadRocket.findTankIssues(issues, tanks, solidFuel);
        if(TileEntityLaunchPadRocket.findDriveIssues(issues, rocket, inventory.getStackInSlot(0))) return issues;

        // Check that the rocket is actually capable of reaching our destination
        Target from = CelestialBody.getTarget(world, pos.getX(), pos.getZ());
        Target to = ItemVOTVdrive.getTarget(inventory.getStackInSlot(0), world);

        TileEntityLaunchPadRocket.findTravelIssues(issues, rocket, from, to);

        return issues;
    }

    @Override
    public void serialize(ByteBuf buf) {
        rocket.writeToByteBuffer(buf);

        buf.writeBoolean(hasDocked);
        buf.writeBoolean(hasRider);

        // buf.writeLong(power);
        buf.writeInt(solidFuel.level);
        buf.writeInt(solidFuel.max);

        for (FluidTankNTM tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        rocket = RocketStruct.readFromByteBuffer(buf);

        hasDocked = buf.readBoolean();
        hasRider = buf.readBoolean();

        // power = buf.readLong();
        solidFuel.level = buf.readInt();
        solidFuel.max = buf.readInt();

        for (FluidTankNTM tank : tanks) tank.deserialize(buf);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        // nbt.setLong("power", power);
        nbt.setInteger("solid", solidFuel.level);
        nbt.setInteger("maxSolid", solidFuel.max);
        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(nbt, "t" + i);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        // power = nbt.getLong("power");
        solidFuel.level = nbt.getInteger("solid");
        solidFuel.max = nbt.getInteger("maxSolid");
        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(nbt, "t" + i);
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return isUseableByPlayer(player);
    }

    @Override
    public void receiveControl(NBTTagCompound data) { }

    @Override
    public void receiveControl(EntityPlayerMP player, NBTTagCompound data) {
        if(data.getBoolean("launch")) {
            launch(player);
        }
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerOrbitalStationLauncher(player.inventory, this);
    }

    @Override
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIOrbitalStationLauncher(player.inventory, this);
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return tanks;
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return tanks;
    }

    private class LauncherInventory extends ItemStackHandler implements SlotRocket.IStage {
        private final int scount;

        public LauncherInventory(int scount) {
            super(scount);
            this.scount = scount;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }

        @Override
        public int getSlots() {
            return scount;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public void setCurrentStage(int stage) {
            currentStage = stage;
        }
    }
}
