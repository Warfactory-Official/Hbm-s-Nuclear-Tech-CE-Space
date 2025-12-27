package com.hbmspace.dim.eve.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenEveOcean extends BiomeGenBaseEve {

    public BiomeGenEveOcean(BiomeProperties properties) {
        super(properties);

        this.topBlock = ModBlocksSpace.eve_silt.getDefaultState();
        this.fillerBlock = ModBlocksSpace.eve_silt.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunkPrimer, int x, int z, double noise) {
        IBlockState topBlockState = this.topBlock;
        IBlockState fillerBlockState = this.fillerBlock;

        int depthRemaining = -1;
        int surfaceDepth = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D);

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

            if (currentState.getBlock() != ModBlocksSpace.duna_rock) {
                continue;
            }

            if (depthRemaining == -1) {
                if (surfaceDepth <= 0) {
                    topBlockState = Blocks.AIR.getDefaultState();
                    fillerBlockState = ModBlocksSpace.duna_rock.getDefaultState();
                } else if (y >= 59 && y <= 64) {
                    topBlockState = this.topBlock;
                    fillerBlockState = this.fillerBlock;
                }

                if (y < 63 && topBlockState.getMaterial() == Material.AIR) {
                    topBlockState = this.topBlock;
                }

                depthRemaining = surfaceDepth;

                if (y >= 62) {
                    chunkPrimer.setBlockState(localX, y, localZ, topBlockState);
                } else {
                    topBlockState = Blocks.AIR.getDefaultState();
                    fillerBlockState = ModBlocksSpace.duna_rock.getDefaultState();
                    chunkPrimer.setBlockState(localX, y, localZ, (rand.nextFloat() > 0.4F) ? ModBlocksSpace.duna_rock.getDefaultState() : ModBlocksSpace.duna_sands.getDefaultState());
                }
            } else if (depthRemaining > 0) {
                --depthRemaining;
                chunkPrimer.setBlockState(localX, y, localZ, fillerBlockState);
                if (depthRemaining == 0 && fillerBlockState.getBlock() == Blocks.SAND) {
                    depthRemaining = rand.nextInt(4) + Math.max(0, y - 63);
                    fillerBlockState = Blocks.SANDSTONE.getDefaultState();
                }
            }
        }
    }
}
