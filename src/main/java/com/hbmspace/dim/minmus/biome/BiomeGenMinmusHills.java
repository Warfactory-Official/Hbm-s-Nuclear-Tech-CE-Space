package com.hbmspace.dim.minmus.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenMinmusHills extends BiomeGenBaseMinmus {

    public BiomeGenMinmusHills(BiomeProperties properties) {
        super(properties);
        this.topBlock = ModBlocksSpace.minmus_regolith.getDefaultState();
        this.fillerBlock = ModBlocksSpace.minmus_regolith.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunkPrimer, int x, int z, double noise) {
        IBlockState topBlockState = this.topBlock;
        IBlockState fillerBlockState = this.fillerBlock;

        int depthRemaining = -1;
        int surfaceDepth = (int) (noise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);

        int localX = x & 15;
        int localZ = z & 15;

        for (int y = 255; y >= 0; --y) {

            IBlockState currentState = chunkPrimer.getBlockState(localX, y, localZ);
            if (y <= rand.nextInt(5)) {
                chunkPrimer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            if (currentState.getMaterial() == Material.AIR) {
                depthRemaining = -1;
                continue;
            }

            if (currentState.getBlock() != ModBlocksSpace.minmus_stone) {
                continue;
            }

            if (depthRemaining == -1) {
                if (surfaceDepth <= 0) {
                    topBlockState = Blocks.AIR.getDefaultState();
                    fillerBlockState = ModBlocksSpace.minmus_stone.getDefaultState();
                } else if (y >= 59 && y <= 74) { // FIX: was 64 in your port; 1.7 uses 74
                    topBlockState = this.topBlock;
                    fillerBlockState = this.fillerBlock;
                }

                if (y < 63 && topBlockState.getMaterial() == Material.AIR) {
                    topBlockState = this.topBlock;
                }

                depthRemaining = surfaceDepth;

                if (y >= 62) {
                    chunkPrimer.setBlockState(localX, y, localZ, topBlockState);
                } else if (y < 56 - surfaceDepth) {
                    topBlockState = Blocks.AIR.getDefaultState();
                    fillerBlockState = ModBlocksSpace.minmus_stone.getDefaultState();
                    chunkPrimer.setBlockState(localX, y, localZ, Blocks.GRAVEL.getDefaultState());
                } else {
                    chunkPrimer.setBlockState(localX, y, localZ, fillerBlockState);
                }
            } else if (depthRemaining > 0) {
                --depthRemaining;
                chunkPrimer.setBlockState(localX, y, localZ, fillerBlockState);
            }
        }
    }
}
