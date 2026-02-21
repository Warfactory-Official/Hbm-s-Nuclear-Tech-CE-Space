package com.hbmspace.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockBakeBaseSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;

public class RubberGrass extends BlockBakeBaseSpace {

    public RubberGrass(Material m, String s, boolean tick) {
        super(m, s, new BlockBakeFrame("rubber_grass_top", "rubber_grass_side", "rubber_silt"));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (this == ModBlocksSpace.rubber_grass) {
            BlockPos upPos = pos.up();
            IBlockState upState = world.getBlockState(upPos);
            Block b = upState.getBlock();
            if (b instanceof BlockLiquid || b instanceof BlockFluidBase || upState.isNormalCube()) {
                world.setBlockState(pos, ModBlocksSpace.rubber_silt.getDefaultState());
            }
        }
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return false;
    }
}
