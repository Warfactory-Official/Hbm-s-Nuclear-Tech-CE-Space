package com.hbmspace.util;

import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbmspace.blocks.generic.BlockOreFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OilSpaceUtil {
    public static void defaultOnSuck(TileEntity te, BlockOreFluid block, BlockPos targetPos, FluidTankNTM[] tanks) {
        World world = te.getWorld();
        IBlockState state = world.getBlockState(targetPos);
        int meta = block.getMetaFromState(state);

        tanks[0].setTankType(block.getPrimaryFluid(meta));
        tanks[1].setTankType(block.getSecondaryFluid(meta));

        tanks[0].setFill(Math.min(tanks[0].getFill() + getPrimaryFluidAmount(block, meta), tanks[0].getMaxFill()));
        if (tanks[1].getTankType() != Fluids.NONE) {
            tanks[1].setFill(Math.min(tanks[1].getFill() + getSecondaryFluidAmount(block, meta), tanks[1].getMaxFill()));
        }

        attemptDrain(te, block, targetPos, meta);
    }



    protected static int getPrimaryFluidAmount(BlockOreFluid block, int meta) {
        return block.getPrimaryFluidAmount(meta);
    }

    protected static int getSecondaryFluidAmount(BlockOreFluid block, int meta) {
        return block.getSecondaryFluidAmount(meta);
    }

    protected static void attemptDrain(TileEntity te, BlockOreFluid block, BlockPos targetPos, int meta) {
        World world = te.getWorld();
        block.drain(world, targetPos, meta, 1);
    }
}
