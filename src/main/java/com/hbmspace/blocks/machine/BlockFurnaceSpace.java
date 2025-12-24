package com.hbmspace.blocks.machine;

import com.hbmspace.handler.atmosphere.IBlockSealable;
import com.hbmspace.tileentity.machine.TileEntityFurnaceSpace;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*public class BlockFurnaceSpace extends BlockFurnace implements IBlockSealable {

    public BlockFurnaceSpace(boolean lit) {
        super(lit);
    }

    public static void updateFurnaceBlockState(boolean lit, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);

        BlockFurnace.keepInventory = true;

        if (lit) {
            world.setBlockState(pos, ModBlocksSpace.lit_furnace.getDefaultState()
                    .withProperty(BlockFurnace.FACING, state.getValue(BlockFurnace.FACING)), 3);
        } else {
            world.setBlockState(pos, ModBlocksSpace.furnace.getDefaultState()
                    .withProperty(BlockFurnace.FACING, state.getValue(BlockFurnace.FACING)), 3);
        }

        BlockFurnace.keepInventory = false;

        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockFurnace.FACING, state.getValue(BlockFurnace.FACING)), 3);

        if (te != null) {
            te.validate();
            world.setTileEntity(pos, te);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFurnaceSpace();
    }

    @Override
    public boolean isSealed(World world, int x, int y, int z) {
        return false;
    }
}*/
