package com.hbmspace.dim;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenLiquidsCelestial extends WorldGenerator {

    // Identical to WorldGenLiquids except you can specify the stone block to replace

    private final Block liquidBlock;
    private final Block targetBlock;

    public WorldGenLiquidsCelestial(Block liquidBlock, Block targetBlock) {
        this.liquidBlock = liquidBlock;
        this.targetBlock = targetBlock;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        BlockPos.MutableBlockPos mp = new BlockPos.MutableBlockPos();

        mp.setPos(x, y + 1, z);
        if (worldIn.getBlockState(mp).getBlock() != targetBlock) {
            return false;
        }

        mp.setPos(x, y - 1, z);
        if (worldIn.getBlockState(mp).getBlock() != targetBlock) {
            return false;
        }

        IBlockState centerState = worldIn.getBlockState(position);
        if (!(centerState.getMaterial() == Material.AIR) && centerState.getBlock() != targetBlock) {
            return false;
        }

        int i = 0;

        mp.setPos(x - 1, y, z);
        if (worldIn.getBlockState(mp).getBlock() == targetBlock) ++i;

        mp.setPos(x + 1, y, z);
        if (worldIn.getBlockState(mp).getBlock() == targetBlock) ++i;

        mp.setPos(x, y, z - 1);
        if (worldIn.getBlockState(mp).getBlock() == targetBlock) ++i;

        mp.setPos(x, y, z + 1);
        if (worldIn.getBlockState(mp).getBlock() == targetBlock) ++i;

        int j = 0;

        mp.setPos(x - 1, y, z);
        {
            IBlockState s = worldIn.getBlockState(mp);
            if (s.getMaterial() == Material.AIR) ++j;
        }

        mp.setPos(x + 1, y, z);
        {
            IBlockState s = worldIn.getBlockState(mp);
            if (s.getMaterial() == Material.AIR) ++j;
        }

        mp.setPos(x, y, z - 1);
        {
            IBlockState s = worldIn.getBlockState(mp);
            if (s.getMaterial() == Material.AIR) ++j;
        }

        mp.setPos(x, y, z + 1);
        {
            IBlockState s = worldIn.getBlockState(mp);
            if (s.getMaterial() == Material.AIR) ++j;
        }

        if (i == 3 && j == 1) {
            IBlockState liquidState = this.liquidBlock.getDefaultState();
            worldIn.setBlockState(position, liquidState, 2);
            worldIn.immediateBlockTick(position, liquidState, rand);
        }

        return true;
    }

}
