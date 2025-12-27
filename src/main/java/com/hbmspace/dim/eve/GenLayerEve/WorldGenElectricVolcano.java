package com.hbmspace.dim.eve.GenLayerEve;

import com.hbm.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenElectricVolcano extends WorldGenerator {

    private final Block volcanoBlock;
    private final Block ventBlock;

    private final int baseHeight;
    private final int width;

    private final Block surfaceBlock;
    private final Block stoneBlock;

    public WorldGenElectricVolcano(int height, int width, Block surfaceBlock, Block stoneBlock) {
        this.baseHeight = height;
        this.width = width;
        this.surfaceBlock = surfaceBlock;
        this.stoneBlock = stoneBlock;

        this.volcanoBlock = ModBlocks.basalt;
        this.ventBlock = Blocks.OBSIDIAN; // ModBlocksSpace.geysir_electric;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos origin) {
        if (rand.nextInt(10) != 0) return false;
        BlockPos.MutableBlockPos basePos = new BlockPos.MutableBlockPos(origin);
        IBlockState baseState = world.getBlockState(basePos);
        while (basePos.getY() > 2 && baseState.getMaterial() == Material.AIR) {
            basePos.move(EnumFacing.DOWN);
            baseState = world.getBlockState(basePos);
        }

        if (baseState.getBlock() != surfaceBlock) {
            return false;
        }

        // 1.7 had `y += rand.nextInt(1)` here.
        // nextInt(1) is always 0, i.e., it never changes placement.

        int volcanoHeight = baseHeight + rand.nextInt(4);
        int baseRadius = (volcanoHeight / 4) + rand.nextInt(width);

        if (baseRadius > 1 && rand.nextInt(5) == 0) {
            rand.nextInt(30);
        }

        int baseX = basePos.getX();
        int baseY = basePos.getY();
        int baseZ = basePos.getZ();

        BlockPos.MutableBlockPos probePos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();

        for (int layerY = 0; layerY < volcanoHeight; layerY++) {
            float radiusF = (1.0F - (float) layerY / (float) volcanoHeight) * (float) baseRadius;
            float radiusSq = radiusF * radiusF;
            int radius = MathHelper.ceil(radiusF);

            for (int dx = -radius; dx <= radius; dx++) {
                float dxF = (float) MathHelper.abs(dx) - 0.25F;

                for (int dz = -radius; dz <= radius; dz++) {
                    float dzF = (float) MathHelper.abs(dz) - 0.75F;
                    float distSq = dxF * dxF + dzF * dzF;

                    boolean inside = (dx == 0 && dz == 0) || (distSq <= radiusSq);
                    boolean onEdge = (dx == -radius || dx == radius || dz == -radius || dz == radius);

                    if (inside && (!onEdge || rand.nextFloat() <= 0.75F)) {
                        placePos.setPos(baseX + dx, baseY + layerY, baseZ + dz);
                        IBlockState state = world.getBlockState(placePos);

                        if (isReplaceable(state)) {
                            world.setBlockState(placePos, volcanoBlock.getDefaultState(), 2 | 16);
                        }

                        if (layerY != 0 && radius > 1) {
                            placePos.setPos(baseX + dx, baseY - layerY, baseZ + dz);
                            IBlockState below = world.getBlockState(placePos);

                            if (isReplaceable(below)) {
                                world.setBlockState(placePos, ModBlocks.ore_depth_nether_neodymium.getDefaultState(), 2 | 16);
                            }
                        }
                    }
                    if ((dx == 0 && layerY == 0) || distSq > radiusSq) {
                        probePos.setPos(baseX + dx, baseY + layerY, baseZ + dz);
                        IBlockState probe = world.getBlockState(probePos);
                        if (isReplaceable(probe)) {
                            placePos.setPos(baseX, baseY + layerY, baseZ);
                            world.setBlockState(placePos, Blocks.AIR.getDefaultState(), 2 | 16);
                            placePos.setPos(baseX, baseY + 5, baseZ);
                            world.setBlockState(placePos, ventBlock.getDefaultState(), 2 | 16);
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean isReplaceable(IBlockState state) {
        Block block = state.getBlock();
        return state.getMaterial() == Material.AIR || block == surfaceBlock || block == stoneBlock;
    }
}
