package com.hbmspace.dim.eve.biome;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenEvePlains extends BiomeGenBaseEve {

    public BiomeGenEvePlains(BiomeProperties properties) {
        super(properties);

        this.topBlock = ModBlocksSpace.eve_silt.getDefaultState();
        this.fillerBlock = ModBlocksSpace.eve_silt.getDefaultState();
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
            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }
            if (currentState.getMaterial() == Material.AIR) {
                layerDepthRemaining = -1;
                continue;
            }
            if (currentState.getBlock() != ModBlocksSpace.eve_rock) {
                continue;
            }

            if (layerDepthRemaining == -1) {
                if (layerThickness <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.eve_rock.getDefaultState();
                } else if (y >= 59 && y <= 64) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < 63 && topState.getMaterial() == Material.AIR) {
                    topState = this.topBlock;
                }

                layerDepthRemaining = layerThickness;

                if (y >= 62) {
                    primer.setBlockState(localX, y, localZ, topState);
                } else {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.eve_rock.getDefaultState();
                    primer.setBlockState(localX, y, localZ,
                            rand.nextFloat() > 0.4F ? ModBlocksSpace.eve_rock.getDefaultState() : ModBlocks.basalt_smooth.getDefaultState()
                    );
                }
                continue;
            }

            if (layerDepthRemaining > 0) {
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
