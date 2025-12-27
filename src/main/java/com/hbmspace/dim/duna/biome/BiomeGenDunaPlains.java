package com.hbmspace.dim.duna.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenDunaPlains extends BiomeGenBaseDuna {

    public BiomeGenDunaPlains(BiomeProperties properties) {
        super(properties);
        this.topBlock = ModBlocksSpace.duna_sands.getDefaultState();
        this.fillerBlock = ModBlocksSpace.duna_sands.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        IBlockState topState;
        IBlockState fillerState = this.fillerBlock;

        int remainingDepth = -1;
        int surfaceDepth = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D); // l in 1.7

        int localX = x & 15;
        int localZ = z & 15;
        int seaLevel = world.getSeaLevel(); // 64 in vanilla

        for (int y = 255; y >= 0; --y) {
            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            IBlockState state = primer.getBlockState(localX, y, localZ);
            if (state.getBlock() == Blocks.AIR) {
                remainingDepth = -1;
                continue;
            }
            if (state.getBlock() != ModBlocksSpace.duna_rock) {
                continue;
            }

            if (remainingDepth == -1) {
                topState = this.topBlock;
                fillerState = this.fillerBlock;

                if (surfaceDepth <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.duna_rock.getDefaultState();
                } else if (y >= seaLevel - 5 && y <= seaLevel) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < seaLevel - 1 && topState.getBlock() == Blocks.AIR) {
                    topState = this.topBlock;
                }

                remainingDepth = surfaceDepth;

                if (y >= seaLevel - 2) {
                    primer.setBlockState(localX, y, localZ, topState);
                } else {
                    fillerState = ModBlocksSpace.duna_rock.getDefaultState();
                    primer.setBlockState(localX, y, localZ, (rand.nextFloat() > 0.4F) ? ModBlocksSpace.duna_rock.getDefaultState() : ModBlocksSpace.duna_sands.getDefaultState());
                }

            } else if (remainingDepth > 0) {
                --remainingDepth;
                primer.setBlockState(localX, y, localZ, fillerState);
            }
        }
    }
}
