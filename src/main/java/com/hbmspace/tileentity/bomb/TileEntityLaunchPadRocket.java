package com.hbmspace.tileentity.bomb;

import com.hbm.api.energymk2.IBatteryItem;
import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardReceiver;
import com.hbm.blocks.BlockDummyable;
import com.hbm.handler.CompatHandler;
import com.hbm.interfaces.IClimbable;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.items.ModItems;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.Library;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.entity.missile.EntityRideableRocket;
import com.hbmspace.handler.RocketStruct;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerLaunchPadRocket;
import com.hbmspace.inventory.gui.GUILaunchPadRocket;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.weapon.ItemCustomRocket;
import io.netty.buffer.ByteBuf;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Optional.InterfaceList({@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")})
@AutoRegister
public class TileEntityLaunchPadRocket extends TileEntityMachineBase implements ITickable, IControlReceiver, IEnergyReceiverMK2, IFluidStandardReceiver, IGUIProvider, SimpleComponent, CompatHandler.OCComponent, IClimbable {

    public long power;
    public final long maxPower = 100_000;

    public int solidFuel = 0;
    public int maxSolidFuel = 0;

    public FluidTankNTM[] tanks;

    public boolean canSeeSky = true;
    public RocketStruct rocket;
    public int height;

    public TileEntityLaunchPadRocket() {
        super(5, true, true); // 0 rocket, 1 drive, 2 battery, 3/4 liquid/solid fuel in/out
        tanks = new FluidTankNTM[RocketStruct.MAX_STAGES * 2]; // enough tanks for any combination of rocket stages
        for(int i = 0; i < tanks.length; i++) tanks[i] = new FluidTankNTM(Fluids.NONE, 64_000);
    }

    @Override
    public String getDefaultName() {
        return "container.launchPadRocket";
    }

    @Override
    public void update() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        if(!world.isRemote) {
            ItemVOTVdrive.getTarget(inventory.getStackInSlot(1), world);

            // Setup tanks required for the current rocket
            updateTanks();

            // Connections
            if(world.getTotalWorldTime() % 20 == 0) {
                for(DirPos pos : getConPos()) {
                    trySubscribe(world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());

                    if(hasRocket()) {
                        for(FluidTankNTM tank : tanks) {
                            if(tank.getTankType() == Fluids.NONE) continue;
                            trySubscribe(tank.getTankType(), world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
                        }
                    }
                }
            }

            // Fills, note that the liquid input also takes solid fuel
            power = Library.chargeTEFromItems(inventory, 2, power, maxPower);
            for(FluidTankNTM tank : tanks) tank.loadTank(3, 4, inventory);
            if(!inventory.getStackInSlot(3).isEmpty() && inventory.getStackInSlot(3).getItem() == ModItems.rocket_fuel && solidFuel < maxSolidFuel) {
                decrStackSize(3, 1);
                solidFuel += 250;
                if(solidFuel > maxSolidFuel) solidFuel = maxSolidFuel;
            }

            rocket = ItemCustomRocket.get(inventory.getStackInSlot(0));
            if(rocket != null) {
                int newHeight = MathHelper.floor(rocket.getHeight() - rocket.capsule.height + 1);
                if(newHeight <= 8) newHeight = 0;

                if(newHeight != height) {
                    // Check that the pad is entirely unobstructed
                    canSeeSky = !isPadObstructed();

                    if(canSeeSky) {
                        // Fill in the tower with structure blocks
                        BlockDummyable.safeRem = true;

                        int meta = ForgeDirection.UP.ordinal();

                        // Build tower
                        if(newHeight > height) {
                            for(int oy = height + 3; oy < newHeight + 3; oy++) {
                                if(pos.getY() + oy > 255) break;
                                IBlockState pad = ModBlocksSpace.launch_pad_rocket.getDefaultState().withProperty(BlockDummyable.META, meta);
                                world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 4, pos.getY() + oy, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 4), pad, 3);
                                world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 5, pos.getY() + oy, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 5), pad, 3);
                                world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 6, pos.getY() + oy, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 6), pad, 3);

                                world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 3 - dir.offsetX * 4, pos.getY() + oy, pos.getZ() - rot.offsetZ * 3 - dir.offsetZ * 4), pad, 3);
                                world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 4 - dir.offsetX * 4, pos.getY() + oy, pos.getZ() - rot.offsetZ * 4 - dir.offsetZ * 4), pad, 3);

                                world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 3 - dir.offsetX * 6, pos.getY() + oy, pos.getZ() - rot.offsetZ * 3 - dir.offsetZ * 6), pad, 3);
                                world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 4 - dir.offsetX * 6, pos.getY() + oy, pos.getZ() - rot.offsetZ * 4 - dir.offsetZ * 6), pad, 3);
                            }
                        } else {
                            for(int oy = height + 3; oy >= newHeight + 3; oy--) {
                                if(pos.getY() + oy > 255) continue;

                                world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 4, pos.getY() + oy, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 4));
                                world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 5, pos.getY() + oy, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 5));
                                world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 6, pos.getY() + oy, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 6));

                                world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 3 - dir.offsetX * 4, pos.getY() + oy, pos.getZ() - rot.offsetZ * 3 - dir.offsetZ * 4));
                                world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 4 - dir.offsetX * 4, pos.getY() + oy, pos.getZ() - rot.offsetZ * 4 - dir.offsetZ * 4));

                                world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 3 - dir.offsetX * 6, pos.getY() + oy, pos.getZ() - rot.offsetZ * 3 - dir.offsetZ * 6));
                                world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 4 - dir.offsetX * 6, pos.getY() + oy, pos.getZ() - rot.offsetZ * 4 - dir.offsetZ * 6));
                            }
                        }

                        // Build standable platform after removing old platform
                        if(height >= 8) {
                            world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 3, pos.getY() + height + 2, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 3));

                            world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX, pos.getY() + height + 2, pos.getZ() - rot.offsetZ - dir.offsetZ));
                            world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 2, pos.getY() + height + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 2));
                            world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 3, pos.getY() + height + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 3));
                            world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 4, pos.getY() + height + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 4));
                            world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 5, pos.getY() + height + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 5));
                            world.setBlockToAir(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 6, pos.getY() + height + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 6));

                            world.setBlockToAir(new BlockPos(pos.getX() - dir.offsetX, pos.getY() + height + 2, pos.getZ() - dir.offsetZ));
                            world.setBlockToAir(new BlockPos(pos.getX() - dir.offsetX * 2, pos.getY() + height + 2, pos.getZ() - dir.offsetZ * 2));
                            world.setBlockToAir(new BlockPos(pos.getX() - dir.offsetX * 3, pos.getY() + height + 2, pos.getZ() - dir.offsetZ * 3));
                            world.setBlockToAir(new BlockPos(pos.getX() - dir.offsetX * 4, pos.getY() + height + 2, pos.getZ() - dir.offsetZ * 4));
                            world.setBlockToAir(new BlockPos(pos.getX() - dir.offsetX * 5, pos.getY() + height + 2, pos.getZ() - dir.offsetZ * 5));
                            world.setBlockToAir(new BlockPos(pos.getX() - dir.offsetX * 6, pos.getY() + height + 2, pos.getZ() - dir.offsetZ * 6));

                            world.setBlockToAir(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX, pos.getY() + height + 2, pos.getZ() + rot.offsetZ - dir.offsetZ));
                            world.setBlockToAir(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 2, pos.getY() + height + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 2));
                            world.setBlockToAir(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 3, pos.getY() + height + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 3));
                            world.setBlockToAir(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 4, pos.getY() + height + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 4));
                            world.setBlockToAir(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 5, pos.getY() + height + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 5));
                            world.setBlockToAir(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 6, pos.getY() + height + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 6));
                        }

                        if(newHeight >= 8) {
                            IBlockState pad1 = ModBlocksSpace.launch_pad_rocket.getDefaultState().withProperty(BlockDummyable.META, dir.ordinal());
                            IBlockState pad2 = ModBlocksSpace.launch_pad_rocket.getDefaultState().withProperty(BlockDummyable.META, rot.ordinal());
                            world.setBlockState(new BlockPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 3, pos.getY() + newHeight + 2, pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 3), pad1, 3);

                            world.setBlockState(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX, pos.getY() + newHeight + 2, pos.getZ() - rot.offsetZ - dir.offsetZ), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 2, pos.getY() + newHeight + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 2), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 3, pos.getY() + newHeight + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 3), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 4, pos.getY() + newHeight + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 4), pad2, 3);
                            world.setBlockState(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 5, pos.getY() + newHeight + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 5), pad2, 3);
                            world.setBlockState(new BlockPos(pos.getX() - rot.offsetX - dir.offsetX * 6, pos.getY() + newHeight + 2, pos.getZ() - rot.offsetZ - dir.offsetZ * 6), pad2, 3);

                            world.setBlockState(new BlockPos(pos.getX() - dir.offsetX, pos.getY() + newHeight + 2, pos.getZ() - dir.offsetZ), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() - dir.offsetX * 2, pos.getY() + newHeight + 2, pos.getZ() - dir.offsetZ * 2), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() - dir.offsetX * 3, pos.getY() + newHeight + 2, pos.getZ() - dir.offsetZ * 3), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() - dir.offsetX * 4, pos.getY() + newHeight + 2, pos.getZ() - dir.offsetZ * 4), pad2, 3);
                            world.setBlockState(new BlockPos(pos.getX() - dir.offsetX * 5, pos.getY() + newHeight + 2, pos.getZ() - dir.offsetZ * 5), pad2, 3);
                            world.setBlockState(new BlockPos(pos.getX() - dir.offsetX * 6, pos.getY() + newHeight + 2, pos.getZ() - dir.offsetZ * 6), pad2, 3);

                            world.setBlockState(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX, pos.getY() + newHeight + 2, pos.getZ() + rot.offsetZ - dir.offsetZ), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 2, pos.getY() + newHeight + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 2), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 3, pos.getY() + newHeight + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 3), pad1, 3);
                            world.setBlockState(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 4, pos.getY() + newHeight + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 4), pad2, 3);
                            world.setBlockState(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 5, pos.getY() + newHeight + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 5), pad2, 3);
                            world.setBlockState(new BlockPos(pos.getX() + rot.offsetX - dir.offsetX * 6, pos.getY() + newHeight + 2, pos.getZ() + rot.offsetZ - dir.offsetZ * 6), pad2, 3);
                        }

                        BlockDummyable.safeRem = false;
                    }

                    height = newHeight;
                }
            }

            networkPackNT(250);
        }
    }

    private boolean isPadObstructed() {
        for(int ox = 0; ox <= 0; ox++) {
            for(int oz = 0; oz <= 0; oz++) {
                if(!world.canBlockSeeSky(pos.add(ox, 2, oz))) {
                    return true;
                }
            }
        }

        return false;
    }

    private DirPos[] conPos;

    public DirPos[] getConPos() {
        if(conPos == null) {
            conPos = new DirPos[13]; // 12 + 1 inputs

            ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
            ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

            int i = 0;

            for(int or = 1; or < 5; or++) {
                for(int oy = 0; oy < 3; oy++) {
                    conPos[i++] = new DirPos(pos.getX() - rot.offsetX * or - dir.offsetX * 8, pos.getY() + oy, pos.getZ() - rot.offsetZ * or - dir.offsetZ * 8, dir.getOpposite());
                }
            }
            conPos[i++] = new DirPos(pos.getX() + rot.offsetX * 3 - dir.offsetX * 8, pos.getY(), pos.getZ() + rot.offsetZ * 3 - dir.offsetZ * 8, dir.getOpposite());
        }

        return conPos;
    }

    public void launch(EntityPlayer player) {
        if(!canLaunch()) return;

        EntityRideableRocket rocket = new EntityRideableRocket(world, pos.getX() + 0.5F, pos.getY() + 3.0F, pos.getZ() + 0.5F, inventory.getStackInSlot(0)).withProgram(inventory.getStackInSlot(1)).launchedBy(player);
        world.spawnEntity(rocket);

        // Deplete all fills
        for(int i = 0; i < tanks.length; i++) tanks[i] = new FluidTankNTM(Fluids.NONE, 64_000);
        solidFuel = maxSolidFuel = 0;

        power -= maxPower * 0.75;

        inventory.setStackInSlot(0, ItemStack.EMPTY);
        inventory.setStackInSlot(1, ItemStack.EMPTY);
    }

    private boolean hasRocket() {
        return ItemCustomRocket.get(inventory.getStackInSlot(0)) != null;
    }

    private boolean hasDrive() {
        return !inventory.getStackInSlot(1).isEmpty() && inventory.getStackInSlot(1).getItem() instanceof ItemVOTVdrive;
    }

    private boolean areTanksFull() {
        for(FluidTankNTM tank : tanks) if(tank.getTankType() != Fluids.NONE && tank.getFill() < tank.getMaxFill()) return false;
        return solidFuel >= maxSolidFuel;
    }

    private boolean canReachDestination() {
        // Check that the drive is processed
        if(!ItemVOTVdrive.getProcessed(inventory.getStackInSlot(1))) {
            return false;
        }

        SolarSystem.Body target = ItemVOTVdrive.getDestination(inventory.getStackInSlot(1)).body;
        if(target == SolarSystem.Body.ORBIT && rocket.capsule.part != ModItemsSpace.rp_capsule_20 && rocket.capsule.part != ModItemsSpace.rp_station_core_20)
            return false;

        ItemVOTVdrive.Target from = CelestialBody.getTarget(world, pos.getX(), pos.getZ());
        ItemVOTVdrive.Target to = ItemVOTVdrive.getTarget(inventory.getStackInSlot(1), world);

        RocketStruct rocket = ItemCustomRocket.get(inventory.getStackInSlot(0));

        if(!to.isValid && rocket.capsule.part != ModItemsSpace.rp_station_core_20) return false;
        if(to.isValid && rocket.capsule.part == ModItemsSpace.rp_station_core_20) return false;

        // Check if the stage can make the journey
        return rocket.hasSufficientFuel(from.body, to.body, from.inOrbit, to.inOrbit);
    }

    public boolean canLaunch() {
        return hasRocket() && hasDrive() && power >= maxPower * 0.75 && areTanksFull() && canReachDestination();
    }

    private void updateTanks() {
        if(!hasRocket()) return;

        RocketStruct rocket = ItemCustomRocket.get(inventory.getStackInSlot(0));
        Map<FluidType, Integer> fuels = rocket.getFillRequirement();

        // If the rocket is already fueled, unmark it and fill the tanks
        boolean hasFuel = ItemCustomRocket.hasFuel(inventory.getStackInSlot(0));
        if(hasFuel) ItemCustomRocket.setFuel(inventory.getStackInSlot(0), false);

        // Remove solid fuels (listed as NONE fluid) from tank updates
        if(fuels.containsKey(Fluids.NONE)) {
            maxSolidFuel = fuels.get(Fluids.NONE);
            if(hasFuel) solidFuel = maxSolidFuel;
            fuels.remove(Fluids.NONE);
        } else {
            maxSolidFuel = 0;
        }

        // Check to see if any of the current tanks already fulfil fuelling requirements
        List<FluidTankNTM> keepTanks = new ArrayList<>();
        for(FluidTankNTM tank : tanks) {
            if(fuels.containsKey(tank.getTankType())) {
                tank.changeTankSize(fuels.get(tank.getTankType()));
                keepTanks.add(tank);
                fuels.remove(tank.getTankType());
            }
        }

        // Add new tanks
        for(Map.Entry<FluidType, Integer> entry : fuels.entrySet()) {
            keepTanks.add(new FluidTankNTM(entry.getKey(), entry.getValue()));
        }

        // Fill tanks if rocket had fuel
        if(hasFuel) {
            for(FluidTankNTM tank : keepTanks) {
                tank.setFill(tank.getMaxFill());
            }
        }

        // Sort and fill the tank array to place NONE at the end
        keepTanks.sort((a, b) -> b.getTankType().getID() - a.getTankType().getID());
        while(keepTanks.size() < RocketStruct.MAX_STAGES * 2) {
            keepTanks.add(new FluidTankNTM(Fluids.NONE, 64_000));
        }

        tanks = keepTanks.toArray(new FluidTankNTM[RocketStruct.MAX_STAGES * 2]);
    }

    public List<String> findIssues() {
        List<String> issues = new ArrayList<>();

        if(!hasRocket()) return issues;

        // Check that the rocket is fully fueled and capable of leaving our starting planet
        RocketStruct rocket = ItemCustomRocket.get(inventory.getStackInSlot(0));

        if(!canSeeSky) {
            issues.add(TextFormatting.RED + "Pad is obstructed");
        }

        if(power < maxPower * 0.75) {
            issues.add(TextFormatting.RED + "Insufficient power");
        }

        for(FluidTankNTM tank : tanks) {
            if(tank.getTankType() == Fluids.NONE) continue;
            int fill = tank.getFill();
            int maxFill = tank.getMaxFill();
            String tankName = tank.getTankType().getLocalizedName();
            if(tankName.contains(" ")) {
                String[] split = tankName.split(" ");
                tankName = split[split.length - 1];
            }
            if(fill < maxFill) {
                issues.add(TextFormatting.YELLOW + "" + fill + "/" + maxFill + "mB " + tankName);
            } else {
                issues.add(TextFormatting.GREEN + "" + fill + "/" + maxFill + "mB " + tankName);
            }
        }

        if(maxSolidFuel > 0) {
            if(solidFuel < maxSolidFuel) {
                issues.add(TextFormatting.YELLOW + "" + solidFuel + "/" + maxSolidFuel + "kg Solid Fuel");
            } else {
                issues.add(TextFormatting.GREEN + "" + solidFuel + "/" + maxSolidFuel + "kg Solid Fuel");
            }
        }

        if(!hasDrive()) {
            issues.add(TextFormatting.YELLOW + "No destination drive installed");
            return issues;
        }

        if(!ItemVOTVdrive.getProcessed(inventory.getStackInSlot(1))) {
            issues.add(TextFormatting.RED + "Destination drive needs processing");
            return issues;
        }

        SolarSystem.Body target = ItemVOTVdrive.getDestination(inventory.getStackInSlot(1)).body;
        if(target == SolarSystem.Body.ORBIT && rocket.capsule.part != ModItemsSpace.rp_capsule_20 && rocket.capsule.part != ModItemsSpace.rp_station_core_20) {
            issues.add(TextFormatting.RED + "Satellite target must be a planet");
            return issues;
        }

        // Check that the rocket is actually capable of reaching our destination
        ItemVOTVdrive.Target from = CelestialBody.getTarget(world, pos.getX(), pos.getZ());
        ItemVOTVdrive.Target to = ItemVOTVdrive.getTarget(inventory.getStackInSlot(1), world);

        if(to.inOrbit && !to.isValid && rocket.capsule.part != ModItemsSpace.rp_station_core_20) {
            issues.add(TextFormatting.RED + "Station not yet launched");
        }

        if(to.inOrbit && to.isValid && rocket.capsule.part == ModItemsSpace.rp_station_core_20) {
            issues.add(TextFormatting.RED + "Station already launched");
        }

        if(!rocket.hasSufficientFuel(from.body, to.body, from.inOrbit, to.inOrbit)) {
            issues.add(TextFormatting.RED + "Rocket can't reach destination");
        }

        return issues;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(stack.isEmpty()) return true;
        if(index == 0 && !(stack.getItem() instanceof ItemCustomRocket)) return false;
        if(index == 1 && !(stack.getItem() instanceof ItemVOTVdrive)) return false;
        return index != 2 || stack.getItem() instanceof IBatteryItem || stack.getItem() == ModItems.battery_creative;
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeLong(power);
        buf.writeInt(solidFuel);
        buf.writeInt(maxSolidFuel);

        buf.writeInt(height);
        buf.writeBoolean(canSeeSky);

        if(rocket != null) {
            buf.writeBoolean(true);
            rocket.writeToByteBuffer(buf);
        } else {
            buf.writeBoolean(false);
        }

        for (FluidTankNTM tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        power = buf.readLong();
        solidFuel = buf.readInt();
        maxSolidFuel = buf.readInt();

        height = buf.readInt();
        canSeeSky = buf.readBoolean();

        if(buf.readBoolean()) {
            rocket = RocketStruct.readFromByteBuffer(buf);
        } else {
            rocket = null;
        }

        for (FluidTankNTM tank : tanks) tank.deserialize(buf);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("power", power);
        nbt.setInteger("solid", solidFuel);
        nbt.setInteger("maxSolid", maxSolidFuel);
        nbt.setInteger("height", height);
        nbt.setBoolean("sky", canSeeSky);
        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(nbt, "t" + i);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        power = nbt.getLong("power");
        solidFuel = nbt.getInteger("solid");
        maxSolidFuel = nbt.getInteger("maxSolid");
        height = nbt.getInteger("height");
        canSeeSky = nbt.getBoolean("sky");
        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(nbt, "t" + i);
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return this.isUseableByPlayer(player);
    }

    @Override
    public void receiveControl(NBTTagCompound data) { }

    @Override
    public void receiveControl(EntityPlayerMP player, NBTTagCompound data) {
        if(data.getBoolean("launch")) {
            launch(player);
        }
    }

    // yo i was promised some crazy things if i did this so here you go
    @Override
    @Optional.Method(modid = "opencomputers")
    public String getComponentName() {
        return "ntm_rocket_pad";
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getEnergyInfo(Context context, Arguments args) {
        return new Object[] {getPower(), getMaxPower()};
    }

    @Callback(direct = true) // this doesn't return a set amount of tanks sadly.
    @Optional.Method(modid = "opencomputers")
    public Object[] getFuel(Context context, Arguments args) {
        List<Object[]> returnValues = new ArrayList<>();
        for (FluidTankNTM tank : tanks) {
            if(tank.getTankType() != Fluids.NONE) {
                returnValues.add(new Object[] {
                        tank.getFill(),
                        tank.getMaxFill(),
                        tank.getTankType().getName()
                });
            }
        }
		/* the return format should look something like the following:
			{{tank_1_fill, tank_1_max, tank_1_type},
			{tank_2_fill, tank_2_max, tank_2_type}}
		 */
        return returnValues.toArray();
    }

    @Callback(direct = true) // this doesn't return a set amount of tanks sadly
    @Optional.Method(modid = "opencomputers")
    public Object[] getSolidFuel(Context context, Arguments args) {
        return new Object[] {solidFuel, maxSolidFuel};
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] canLaunch(Context context, Arguments args) {
        return new Object[] {canLaunch()};
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getRocketStats(Context context, Arguments args) {
        if(hasRocket()) {
            return new Object[] {
                    rocket.stages.size(),
                    rocket.getLaunchMass(),
                    rocket.getHeight()
            };
        }
        return new Object[] {null, ""};
    }

    @Callback(direct = true, limit = 4)
    @Optional.Method(modid = "opencomputers")
    public Object[] launch(Context context, Arguments args) {
        // doesn't really "launch" it per-say, just spawns the rocket, so I guess this works
        launch(null);
        // update: it so worked!
        return new Object[] {};
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getDestination(Context context, Arguments args) {
        if(hasDrive()) { // ok maybe I should actually check if there's an item there first
            return new Object[] {null, "No destination drive."};
        }
        ItemVOTVdrive.Target target = ItemVOTVdrive.getTarget(inventory.getStackInSlot(1), null);
        if(target.body != null) {
            return new Object[] {target.body.name.toLowerCase()};
        }
        return new Object[] {null, "Drive has no destination."};
    }

    @Override
    @Optional.Method(modid = "opencomputers")
    public boolean canConnectNode(EnumFacing side) {
        // Get direction of ports.
        EnumFacing dir = EnumFacing.byIndex(this.getBlockMetadata() - BlockDummyable.offset);
        // Only connect if port is facing outwards, mainly to prevent component clutter with the ports connecting to eachother.
        return side == dir;
    }

    @Override
    @Optional.Method(modid = "opencomputers")
    public String[] methods() {
        return new String[] {
                "getEnergyInfo",
                "getFuel",
                "getSolidFuel",
                "canLaunch",
                "getRocketStats",
                "getDestination",
                "launch"
        };
    }

    @Override
    @Optional.Method(modid = "opencomputers")
    public Object[] invoke(String method, Context context, Arguments args) throws Exception {
        switch (method) {
            case ("getEnergyInfo") -> {
                return getEnergyInfo(context, args);
            }
            case ("getFuel") -> {
                return getFuel(context, args);
            }
            case ("getSolidFuel") -> {
                return getSolidFuel(context, args);
            }
            case ("canLaunch") -> {
                return canLaunch(context, args);
            }
            case ("getRocketStats") -> {
                return getRocketStats(context, args);
            }
            case ("getDestination") -> {
                return getDestination(context, args);
            }
            case ("launch") -> {
                return launch(context, args);
            }
        }
        throw new NoSuchMethodException();
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }
    @Override public FluidTankNTM[] getAllTanks() { return this.tanks; }
    @Override public FluidTankNTM[] getReceivingTanks() { return this.tanks; }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerLaunchPadRocket(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUILaunchPadRocket(player.inventory, this);
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB; // hi martin ;)
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if(world.getTileEntity(pos) != this) {
            return false;
        } else {
            return player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 1024;
        }
    }

    public ItemStack decrStackSize(int slot, int amount) {
        if(!inventory.getStackInSlot(slot).isEmpty()) {

            if(inventory.getStackInSlot(slot).getCount() <= amount) {
                ItemStack itemStack = inventory.getStackInSlot(slot);
                inventory.setStackInSlot(slot, ItemStack.EMPTY);
                return itemStack;
            }

            ItemStack itemStack1 = inventory.getStackInSlot(slot).splitStack(amount);
            if(inventory.getStackInSlot(slot).getCount() == 0) {
                inventory.setStackInSlot(slot, ItemStack.EMPTY);
            }

            return itemStack1;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    private AxisAlignedBB ladderAABB = null;

    private AxisAlignedBB getLadderAABB(){
        if (ladderAABB == null){
            ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
            ForgeDirection rot = dir.getRotation(ForgeDirection.UP);
            ladderAABB = new AxisAlignedBB(pos.getX() + 0.25, pos.getY(), pos.getZ() + 0.25, pos.getX() + 0.75, pos.getY() + 3, pos.getZ() + 0.75).offset(-rot.offsetX * 6.5 - dir.offsetX * 5, 0, -rot.offsetZ * 6.5 - dir.offsetZ * 5);
            if(height >= 8) {
                ladderAABB.union(new AxisAlignedBB(pos.getX() + 0.25, pos.getY() + 3, pos.getZ() + 0.25, pos.getX() + 0.75, pos.getY() + 3 + height, pos.getZ() + 0.75).offset(-rot.offsetX * 2.5 - dir.offsetX * 5, 0, -rot.offsetZ * 2.5 - dir.offsetZ * 5));
            }
        }
        return ladderAABB;
    }

    @Override
    public boolean isEntityInClimbAABB(@NotNull EntityLivingBase entity) {
        return entity.getEntityBoundingBox().intersects(getLadderAABB());
    }

    @Override
    public @Nullable AxisAlignedBB getClimbAABBForIndexing() {
        return getLadderAABB();
    }

}
