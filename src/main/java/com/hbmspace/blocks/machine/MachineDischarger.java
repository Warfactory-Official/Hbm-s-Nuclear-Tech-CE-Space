package com.hbmspace.blocks.machine;

import com.hbm.main.MainRegistry;
import com.hbm.render.block.BlockBakeFrame;
import com.hbm.tileentity.IPersistentNBT;
import com.hbmspace.blocks.BlockContainerBakeableSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineDischarger;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class MachineDischarger extends BlockContainerBakeableSpace {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    private final Random field_149933_a = new Random();
    private static boolean keepInventory;

    public MachineDischarger(Material p_i45386_1_, String s) {
        super(p_i45386_1_, s, BlockBakeFrame.bottomTop("discharger_side", "discharger_top", "discharger_bottom"));
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocksSpace.machine_discharger);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote)
        {
            return true;
        } else if(!player.isSneaking())
        {
            TileEntityMachineDischarger entity = (TileEntityMachineDischarger) world.getTileEntity(pos);
            if(entity != null)
            {
                FMLNetworkHandler.openGui(player, MainRegistry.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World p_149915_1_, int p_149915_2_) {
        return new TileEntityMachineDischarger();
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byIndex(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(@NotNull IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public @NotNull IBlockState withRotation(@NotNull IBlockState state, @NotNull Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull IBlockState withMirror(@NotNull IBlockState state, @NotNull Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public void dropBlockAsItemWithChance(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, float chance, int fortune) {
    }

    @Override
    public void onBlockHarvested(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player) {
        IPersistentNBT.onBlockHarvested(worldIn, pos, player);
    }

    @Override
    public void harvestBlock(@NotNull World world, EntityPlayer player, @NotNull BlockPos pos, @NotNull IBlockState state, TileEntity te, @NotNull ItemStack stack) {
        player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)), 1);
        player.addExhaustion(0.025F);
    }

    @Override
    public void breakBlock(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!keepInventory) {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof TileEntityMachineDischarger tile) {

                for (int i = 0; i < tile.inventory.getSlots(); ++i) {
                    ItemStack itemstack = tile.inventory.getStackInSlot(i);

                    if (!itemstack.isEmpty()) {
                        float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

                        while (!itemstack.isEmpty()) {
                            int j1 = this.field_149933_a.nextInt(21) + 10;
                            if (j1 > itemstack.getCount()) {
                                j1 = itemstack.getCount();
                            }

                            ItemStack split = itemstack.splitStack(j1);

                            EntityItem entityitem = new EntityItem(
                                    world,
                                    pos.getX() + f, pos.getY() + f1, pos.getZ() + f2,
                                    split
                            );

                            float f3 = 0.05F;
                            entityitem.motionX = this.field_149933_a.nextGaussian() * (double) f3;
                            entityitem.motionY = this.field_149933_a.nextGaussian() * (double) f3 + 0.2D;
                            entityitem.motionZ = this.field_149933_a.nextGaussian() * (double) f3;
                            world.spawnEntity(entityitem);
                        }
                    }
                }

                world.notifyNeighborsOfStateChange(pos, state.getBlock(), false);
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, EntityLivingBase player, @NotNull ItemStack itemStack) {
        IPersistentNBT.onBlockPlacedBy(world, pos, itemStack);

        EnumFacing facing = player.getHorizontalFacing().getOpposite();
        world.setBlockState(pos, state.withProperty(FACING, facing), 2);

        if (itemStack.hasDisplayName()) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityMachineDischarger) {
                ((TileEntityMachineDischarger) te).setCustomName(itemStack.getDisplayName());
            }
        }
    }
}
