package com.hbmspace.dim.laythe.biome;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenLaythePolar extends BiomeGenBaseLaythe {

    public BiomeGenLaythePolar(BiomeProperties properties) {
        super(properties);

        this.topBlock = Blocks.SNOW.getDefaultState();
        this.fillerBlock = Blocks.ICE.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        IBlockState topState = this.topBlock;
        IBlockState fillerState = this.fillerBlock;

        int layerDepthRemaining = -1;
        int layerThickness = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D);

        int localX = x & 15;
        int localZ = z & 15;

        for (int y = 255; y >= 0; --y) {

            IBlockState currentState = primer.getBlockState(localX, y, localZ);

            // Bedrock thickness (keep original behavior)
            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            // Standardized air test
            if (currentState.getMaterial() == Material.AIR) {
                layerDepthRemaining = -1;
                continue;
            }

            // 1.7: only act on stone
            if (currentState.getBlock() != Blocks.STONE) {
                continue;
            }

            if (layerDepthRemaining == -1) {
                if (layerThickness <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = Blocks.STONE.getDefaultState();
                } else if (y >= 59 && y <= 64) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < 63 && topState.getMaterial() == Material.AIR) {
                    topState = this.topBlock;
                }

                layerDepthRemaining = layerThickness;

                // IMPORTANT parity fix: 1.7 uses 65, not 62
                if (y >= 65) {
                    primer.setBlockState(localX, y, localZ, topState);
                } else { // y < 65
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = Blocks.STONE.getDefaultState();

                    // "World random": use passed-in rand (replaces Math.random()).
                    primer.setBlockState(
                            localX, y, localZ,
                            rand.nextFloat() > 0.4F
                                    ? Blocks.SNOW.getDefaultState()
                                    : Blocks.PACKED_ICE.getDefaultState()
                    );
                }

                continue;
            }

            if (layerDepthRemaining > 0) {
                --layerDepthRemaining;
                primer.setBlockState(localX, y, localZ, fillerState);

                // Kept for parity with the vanilla template; effectively unused here.
                if (layerDepthRemaining == 0 && fillerState.getBlock() == Blocks.SAND) {
                    layerDepthRemaining = rand.nextInt(4) + Math.max(0, y - 63);
                    fillerState = Blocks.SANDSTONE.getDefaultState();
                }
            }
        }
    }
}
