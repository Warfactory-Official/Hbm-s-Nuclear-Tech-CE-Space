package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.items.ModItems;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.Library;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.InventoryUtil;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockCrop;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereHandler;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerHydroponic;
import com.hbmspace.inventory.gui.GUIHydroponic;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister
public class TileEntityHydroponic extends TileEntityMachineBase implements ITickable, ISpaceGuiProvider, IFluidStandardTransceiverMK2, IEnergyReceiverMK2 {

    public FluidTankNTM[] tanks;
    public long power;
    public static long maxPower = 2_000;

    public int fertilizer;
    public static int maxFertilizer = 200;

    private boolean lightsOn = false;
    private final int[] prevMeta = new int[3];

    public TileEntityHydroponic() {
        super(6, true, true);
        tanks = new FluidTankNTM[2];
        tanks[0] = new FluidTankNTM(Fluids.CARBONDIOXIDE, 16_000);
        tanks[1] = new FluidTankNTM(Fluids.OXYGEN, 16_000);
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerHydroponic(player.inventory, this.inventory);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIHydroponic(player.inventory, this);
    }

    @Override
    public String getDefaultName() {
        return "container.hydrobay";
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
            ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

            for (DirPos pos : getFluidPos()) {
                trySubscribe(tanks[0].getTankType(), world, pos);
                tryProvide(tanks[1].getTankType(), world, pos);
            }

            for (DirPos pos : getPowerPos()) {
                trySubscribe(world, pos);
            }

            if (power > 0) {
                power = Math.max(power - 25, 0);
            }

            ItemStack fertStack = inventory.getStackInSlot(1);
            if (!fertStack.isEmpty()) {
                int strength = getFertilizerStrength(fertStack);
                if (strength > 0 && fertilizer <= maxFertilizer - strength) {
                    fertStack.shrink(1);
                    fertilizer += strength;
                    inventory.setStackInSlot(1, fertStack.isEmpty() ? ItemStack.EMPTY : fertStack);
                    markDirty();
                }
            }

            power = Library.chargeTEFromItems(inventory, 2, power, maxPower);

            BlockDummyable.safeRem = true;
            {
                int lx = pos.getX() - rot.offsetX;
                int ly = pos.getY() + 2;
                int lz = pos.getZ() - rot.offsetZ;

                int rx = pos.getX() + rot.offsetX;
                int ry = pos.getY() + 2;
                int rz = pos.getZ() + rot.offsetZ;

                int lMeta = world.getBlockState(new BlockPos(lx, ly, lz)).getBlock().getMetaFromState(
                        world.getBlockState(new BlockPos(lx, ly, lz))
                );
                int rMeta = world.getBlockState(new BlockPos(rx, ry, rz)).getBlock().getMetaFromState(
                        world.getBlockState(new BlockPos(rx, ry, rz))
                );

                if (power >= 200) {
                    if (!lightsOn) {
                        world.setBlockState(new BlockPos(lx, ly, lz), ModBlocksSpace.dummy_beam.getStateFromMeta(lMeta), 3);
                        world.setBlockState(new BlockPos(rx, ry, rz), ModBlocksSpace.dummy_beam.getStateFromMeta(rMeta), 3);
                        lightsOn = true;
                    }
                } else {
                    if (lightsOn) {
                        world.setBlockState(new BlockPos(lx, ly, lz), ModBlocksSpace.hydrobay.getStateFromMeta(lMeta), 3);
                        world.setBlockState(new BlockPos(rx, ry, rz), ModBlocksSpace.hydrobay.getStateFromMeta(rMeta), 3);
                        lightsOn = false;
                    }
                }
            }
            BlockDummyable.safeRem = false;

            for (int i = 0; i < 3; i++) {
                int x = pos.getX() + rot.offsetX * (i - 1);
                int y = pos.getY() + 1;
                int z = pos.getZ() + rot.offsetZ * (i - 1);

                BlockPos plantPos = new BlockPos(x, y, z);
                IBlockState plantState = world.getBlockState(plantPos);
                Block currentPlant = plantState.getBlock();

                // Minimum CO2 pressure required to start growing
                if (power >= 200 && tanks[0].getFill() >= 100) {

                    // Attempt planting a new plant
                    // Only allows single block crops
                    if (!(currentPlant instanceof IGrowable)) {
                        ItemStack seedStack = inventory.getStackInSlot(0);
                        if (seedStack.isEmpty() || !(seedStack.getItem() instanceof IPlantable plantable)) continue;

                        if (plantable.getPlantType(world, plantPos) != EnumPlantType.Crop) continue;

                        IBlockState toPlant = plantable.getPlant(world, plantPos);
                        Block toPlantBlock = toPlant.getBlock();
                        if (!(toPlantBlock instanceof IGrowable) || toPlantBlock instanceof BlockStem) continue;

                        if (toPlantBlock instanceof BlockCrop && !((BlockCrop) toPlantBlock).canHydro) continue;

                        world.setBlockState(plantPos, toPlant, 3);
                        prevMeta[i] = 0;

                        seedStack.shrink(1);
                        inventory.setStackInSlot(0, seedStack.isEmpty() ? ItemStack.EMPTY : seedStack);

                        markDirty();

                        // refresh references after planting
                        plantState = world.getBlockState(plantPos);
                        currentPlant = plantState.getBlock();
                    }

                    IGrowable currentGrowable = (IGrowable) currentPlant;

                    // Increase growth speed by about x10
                    if (world.rand.nextInt(120) == 0) {
                        currentPlant.updateTick(world, plantPos, plantState, world.rand);
                        plantState = world.getBlockState(plantPos);
                    }

                    boolean fullyGrown = false;
                    if (currentGrowable.canGrow(world, plantPos, plantState, world.isRemote)) {
                        if (fertilizer > 0 && world.rand.nextInt(60) == 0) {
                            if (currentGrowable.canUseBonemeal(world, world.rand, plantPos, plantState)) {
                                currentGrowable.grow(world, world.rand, plantPos, plantState);
                                world.playEvent(2005, plantPos, 0);
                                plantState = world.getBlockState(plantPos);
                            }
                            fertilizer--;
                        }
                    } else {
                        fullyGrown = true;
                    }

                    int newMeta = currentPlant.getMetaFromState(plantState);

                    if (newMeta != prevMeta[i]) {
                        // each growth stage sequesters 5mb of carbon
                        int toProduce = Math.max(newMeta - prevMeta[i], 0) * ChunkAtmosphereHandler.CROP_GROWTH_CONVERSION;
                        tanks[0].setFill(Math.max(tanks[0].getFill() - toProduce, 0));
                        tanks[1].setFill(Math.min(tanks[1].getFill() + toProduce, tanks[1].getMaxFill()));
                        prevMeta[i] = newMeta;
                    }

                    // after collecting produced O2, break any fully grown plants
                    // unless there is no space to collect the drops
                    if (fullyGrown) {
                        List<ItemStack> drops = currentPlant.getDrops(world, plantPos, plantState, 0);
                        if (attemptHarvest(drops)) {
                            world.setBlockToAir(plantPos);
                            markDirty();
                        }
                    }

                } else if (currentPlant instanceof IGrowable) {
                    // pause growth until sufficient CO2 added
                    int meta = prevMeta[i];
                    IBlockState paused = currentPlant.getStateFromMeta(meta);
                    world.setBlockState(plantPos, paused, 2);
                }
            }

            networkPackNT(15);
        }
    }

    public ItemStack[] getValidFertilizers() {
        return new ItemStack[] {
                new ItemStack(Items.DYE, 1, 15),
                new ItemStack(ModItems.powder_fertilizer),
        };
    }

    private int getFertilizerStrength(ItemStack stack) {
        if(stack == null) return 0;
        if(stack.getItem() == Items.DYE && stack.getItemDamage() == 15) return 1;
        if(stack.getItem() == ModItems.powder_fertilizer) return 9;
        return 0;
    }

    private boolean attemptHarvest(List<ItemStack> drops) {
        ItemStack[] originals = new ItemStack[3];
        originals[0] = inventory.getStackInSlot(3);
        originals[1] = inventory.getStackInSlot(4);
        originals[2] = inventory.getStackInSlot(5);

        for(ItemStack drop : drops) {
            if(InventoryUtil.tryAddItemToInventory(inventory, 3, 5, drop) != ItemStack.EMPTY) {
                inventory.setStackInSlot(3, originals[0]);
                inventory.setStackInSlot(4, originals[1]);
                inventory.setStackInSlot(5, originals[2]);
                return false;
            }
        }

        return true;
    }

    private DirPos[] getFluidPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        return new DirPos[] {
                new DirPos(pos.getX() + rot.offsetX * 2 + dir.offsetX, pos.getY(), pos.getZ() + rot.offsetZ * 2 + dir.offsetZ, dir),
                new DirPos(pos.getX() - rot.offsetX * 2 + dir.offsetX, pos.getY(), pos.getZ() - rot.offsetZ * 2 + dir.offsetZ, dir),
                new DirPos(pos.getX() + rot.offsetX * 2 - dir.offsetX, pos.getY(), pos.getZ() + rot.offsetZ * 2 - dir.offsetZ, dir.getOpposite()),
                new DirPos(pos.getX() - rot.offsetX * 2 - dir.offsetX, pos.getY(), pos.getZ() - rot.offsetZ * 2 - dir.offsetZ, dir.getOpposite()),
        };
    }

    private DirPos[] getPowerPos() {
        return new DirPos[] {
                new DirPos(pos.getX(), pos.getY() + 3, pos.getZ(), ForgeDirection.UP),
        };
    }

    @Override
    public void serialize(ByteBuf buf) {
        for (FluidTankNTM tank : tanks) tank.serialize(buf);
        buf.writeLong(power);
        buf.writeInt(fertilizer);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        for (FluidTankNTM tank : tanks) tank.deserialize(buf);
        power = buf.readLong();
        fertilizer = buf.readInt();
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(nbt, "t" + i);
        for(int i = 0; i < 3; i++) nbt.setInteger("p" + i, prevMeta[i]);

        nbt.setLong("power", power);
        nbt.setBoolean("lights", lightsOn);
        nbt.setInteger("fertilizer", fertilizer);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(nbt, "t" + i);
        for(int i = 0; i < 3; i++) prevMeta[i] = nbt.getInteger("p" + i);

        power = nbt.getLong("power");
        lightsOn = nbt.getBoolean("lights");
        fertilizer = nbt.getInteger("fertilizer");
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == 0) return itemStack.getItem() instanceof IPlantable;
        if(slot == 1) return getFertilizerStrength(itemStack) > 0;
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(EnumFacing e) {
        return new int[] {0, 1, 2, 3, 4, 5};
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return slot >= 3;
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 2,
                    pos.getY(),
                    pos.getZ() - 2,
                    pos.getX() + 3,
                    pos.getY() + 2,
                    pos.getZ() + 3
            );
        }

        return bb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[] { tanks[0] }; }
    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[] { tanks[1] }; }
    @Override public FluidTankNTM[] getAllTanks() { return tanks; }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

}
