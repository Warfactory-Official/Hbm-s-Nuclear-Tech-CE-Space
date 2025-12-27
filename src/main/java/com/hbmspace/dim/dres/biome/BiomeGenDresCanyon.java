package com.hbmspace.dim.dres.biome;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenDresCanyon extends BiomeGenBaseDres {

    public BiomeGenDresCanyon(BiomeProperties properties) {
        super(properties);
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        IBlockState topState;
        IBlockState fillerState = this.fillerBlock;

        int remainingDepth = -1; // k
        int surfaceDepth = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D);

        int localX = x & 15;
        int localZ = z & 15;

        for (int y = 255; y >= 0; --y) {
            IBlockState current = primer.getBlockState(localX, y, localZ);

            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            if (current.getBlock() == Blocks.AIR) {
                remainingDepth = -1;
                continue;
            }
            if (current.getBlock() != ModBlocksSpace.dres_rock) {
                continue;
            }

            if (remainingDepth == -1) {
                topState = this.topBlock;
                fillerState = this.fillerBlock;

                if (surfaceDepth <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocks.sellafield_slaked.getDefaultState();
                } else if (y >= 59 && y <= 64) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < 63 && topState.getBlock() == Blocks.AIR) {
                    topState = this.topBlock;
                }

                remainingDepth = surfaceDepth;

                if (y >= 62) {
                    primer.setBlockState(localX, y, localZ, topState);
                } else {
                    fillerState = ModBlocks.sellafield_slaked.getDefaultState();
                    primer.setBlockState(localX, y, localZ, (rand.nextFloat() > 0.4F) ? ModBlocks.sellafield_slaked.getStateFromMeta(8) : ModBlocks.sellafield_slaked.getStateFromMeta(7));
                }
            } else if (remainingDepth > 0) {
                --remainingDepth;
                primer.setBlockState(localX, y, localZ, fillerState);
                if (remainingDepth == 0 && fillerState.getBlock() == Blocks.SAND) {
                    remainingDepth = rand.nextInt(4) + Math.max(0, y - 63);
                    fillerState = Blocks.SANDSTONE.getDefaultState();
                }
            }
        }
    }
}
