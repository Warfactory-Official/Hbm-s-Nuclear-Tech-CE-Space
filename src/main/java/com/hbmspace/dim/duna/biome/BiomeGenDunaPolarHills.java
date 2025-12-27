package com.hbmspace.dim.duna.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenDunaPolarHills extends BiomeGenBaseDuna {

    public BiomeGenDunaPolarHills(BiomeProperties properties) {
        super(properties);
        this.topBlock = Blocks.SNOW.getDefaultState();
        this.fillerBlock = Blocks.SNOW.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        IBlockState topState = this.topBlock;
        IBlockState fillerState = this.fillerBlock;

        int layerDepthRemaining = -1;
        int layerThickness = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D);

        int localX = x & 15;
        int localZ = z & 15;

        int seaLevel = world.getSeaLevel(); // vanilla 63, but dimension may differ

        for (int y = 255; y >= 0; --y) {
            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            IBlockState currentState = primer.getBlockState(localX, y, localZ);
            Block currentBlock = currentState.getBlock();

            if (currentState.getMaterial() == Material.AIR) {
                layerDepthRemaining = -1;
                continue;
            }

            if (currentBlock != ModBlocksSpace.duna_rock) {
                continue;
            }

            if (layerDepthRemaining == -1) {
                if (layerThickness <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.duna_rock.getDefaultState();
                } else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < seaLevel && topState.getBlock() == Blocks.AIR) {
                    topState = this.topBlock;
                }

                layerDepthRemaining = layerThickness;
                if (y >= seaLevel - 1) {
                    if (rand.nextFloat() > 0.4F) {
                        primer.setBlockState(localX, y, localZ, topState);
                    } else {
                        primer.setBlockState(localX, y, localZ, ModBlocksSpace.dry_ice.getDefaultState());
                    }
                } else if (y < (seaLevel - 7) - layerThickness) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.duna_rock.getDefaultState();
                    primer.setBlockState(localX, y, localZ, Blocks.GRAVEL.getDefaultState());
                } else {
                    primer.setBlockState(localX, y, localZ, fillerState);
                }
            } else if (layerDepthRemaining > 0) {
                --layerDepthRemaining;
                primer.setBlockState(localX, y, localZ, fillerState);
                if (layerDepthRemaining == 0 && fillerState.getBlock() == Blocks.SAND) {
                    layerDepthRemaining = rand.nextInt(4) + Math.max(0, y - seaLevel);
                    fillerState = Blocks.SANDSTONE.getDefaultState();
                }
            }
        }
    }
}
