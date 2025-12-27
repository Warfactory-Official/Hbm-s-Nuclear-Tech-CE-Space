package com.hbmspace.dim.moho.biome;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenMohoBasalt extends BiomeGenBaseMoho {

    public BiomeGenMohoBasalt(BiomeProperties properties) {
        super(properties);

        this.topBlock = ModBlocks.basalt.getDefaultState();
        this.fillerBlock = ModBlocks.basalt.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        IBlockState topState = this.topBlock;
        IBlockState fillerState = this.fillerBlock;

        int layerDepthRemaining = -1;
        int surfaceDepth = (int) (noise / 8.0D + 8.0D + rand.nextDouble() * 0.50D);

        int localX = x & 15;
        int localZ = z & 15;

        int maxY = world.getActualHeight() - 1;

        for (int y = maxY; y >= 0; --y) {
            boolean contour = (noise > -0.25D && noise < 1.0D) || (noise > 1.5D && noise < 2.0D) || (noise > 2.5D && noise < 3.0D);

            double offset = contour ? 0.0D : Math.pow(rand.nextDouble(), 4) * 8.0D;
            int offsetLayers = (int) Math.ceil(offset);

            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            IBlockState state = primer.getBlockState(localX, y, localZ);
            if (state.getMaterial() == Material.AIR) {
                layerDepthRemaining = -1;
                continue;
            }

            if (state.getBlock() != ModBlocksSpace.moho_stone) {
                continue;
            }

            if (layerDepthRemaining == -1) {
                if (surfaceDepth <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.moho_stone.getDefaultState();
                } else if (y >= 59 && y <= 64) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < 63 && topState.getMaterial() == Material.AIR) {
                    topState = this.topBlock;
                }

                layerDepthRemaining = surfaceDepth;

                if (y >= 62) {
                    primer.setBlockState(localX, y, localZ, topState);
                } else if (y < 56 - surfaceDepth) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.moho_stone.getDefaultState();
                    primer.setBlockState(localX, y, localZ, this.topBlock);
                } else {
                    primer.setBlockState(localX, y, localZ, fillerState);
                }
                if (offsetLayers > 0) {
                    for (int oy = 0; oy < offsetLayers; oy++) {
                        int yy = y + oy;
                        if (yy > maxY) break;
                        primer.setBlockState(localX, yy, localZ, this.topBlock);
                    }
                }
            } else if (layerDepthRemaining > 0) {
                --layerDepthRemaining;
                primer.setBlockState(localX, y, localZ, fillerState);
            }
        }
    }
}
