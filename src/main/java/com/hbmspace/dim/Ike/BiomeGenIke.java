package com.hbmspace.dim.Ike;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.BiomeDecoratorCelestial;
import com.hbmspace.dim.BiomeGenBaseCelestial;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenIke extends BiomeGenBaseCelestial {

    public BiomeGenIke(BiomeProperties properties) {
        super(properties);
        properties.setBaseBiome("Ike");
        properties.setRainDisabled();

        BiomeDecoratorCelestial decorator = new BiomeDecoratorCelestial(ModBlocksSpace.ike_stone);
        decorator.lakeChancePerChunk = 8;
        decorator.lakeBlock = ModBlocks.bromine_block;
        this.decorator = decorator;
        this.decorator.generateFalls = false;

        this.topBlock = ModBlocksSpace.ike_regolith.getDefaultState();
        this.fillerBlock = ModBlocksSpace.ike_regolith.getDefaultState(); // thiccer regolith due to uhhhhhh...................
        this.creatures.clear();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        IBlockState topState = this.topBlock;
        IBlockState fillerState = this.fillerBlock;

        int layerDepthRemaining = -1;
        int surfaceDepth = (int) (noise / 8.0D + 8.0D + rand.nextDouble() * 0.50D);

        int localX = x & 15;
        int localZ = z & 15;

        int maxY = world.getActualHeight() - 1; // 255 in vanilla

        for (int y = maxY; y >= 0; --y) {

            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            IBlockState state = primer.getBlockState(localX, y, localZ);

            if (state.getMaterial() == Material.AIR) {
                layerDepthRemaining = -1;
                continue;
            }

            if (state.getBlock() != ModBlocksSpace.ike_stone) {
                continue;
            }

            if (layerDepthRemaining == -1) {
                if (surfaceDepth <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.ike_stone.getDefaultState();
                } else if (y >= 59 && y <= 64) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                // legacy parity: redundant branches, keep call behavior
                if (y < 63 && topState.getMaterial() == Material.AIR) {
                    topState = this.topBlock;
                }

                layerDepthRemaining = surfaceDepth;

                if (y >= 62) {
                    primer.setBlockState(localX, y, localZ, topState);
                } else if (y < 56 - surfaceDepth) {
                    // 1.7 parity: gravel below (56 - l)
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.ike_stone.getDefaultState();
                    primer.setBlockState(localX, y, localZ, Blocks.GRAVEL.getDefaultState());
                } else {
                    primer.setBlockState(localX, y, localZ, fillerState);
                }
            } else if (layerDepthRemaining > 0) {
                --layerDepthRemaining;
                primer.setBlockState(localX, y, localZ, fillerState);

                if (layerDepthRemaining == 0 && fillerState.getBlock() == Blocks.SAND) {
                    layerDepthRemaining = rand.nextInt(4) + Math.max(0, y - 63);
                    fillerState = Blocks.SANDSTONE.getDefaultState();
                }
            }
        }
    }
}
