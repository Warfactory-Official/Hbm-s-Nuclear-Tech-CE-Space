package com.hbmspace.dim.eve.biome;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenEveSeismicPlains extends BiomeGenBaseEve {

    public BiomeGenEveSeismicPlains(BiomeProperties properties) {
        super(properties);

        this.topBlock = ModBlocksSpace.eve_silt.getDefaultState();
        this.fillerBlock = ModBlocksSpace.eve_silt.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int worldX, int worldZ, double noise) {
        IBlockState topState = this.topBlock;
        IBlockState fillerState = this.fillerBlock;

        int surfaceDepth = -1;
        int layerThickness = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D);

        int localX = worldX & 15;
        int localZ = worldZ & 15;


        for (int y = 255; y >= 0; --y) {
            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            IBlockState state = primer.getBlockState(localX, y, localZ);
            if (state.getMaterial() == Material.AIR) {
                surfaceDepth = -1;
                continue;
            }

            if (state.getBlock() != ModBlocksSpace.eve_rock) {
                continue;
            }

            if (surfaceDepth == -1) {
                if (layerThickness <= 0) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.eve_rock.getDefaultState();
                } else if (y >= 59 && y <= 64) {
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < 63 && topState.getBlock() == Blocks.AIR) {
                    topState = this.topBlock;
                }

                surfaceDepth = layerThickness;

                if (y >= 62) {
                    if (rand.nextFloat() > 0.4F) {
                        primer.setBlockState(localX, y, localZ, topState);
                    } else {
                        primer.setBlockState(localX, y, localZ, ModBlocksSpace.eve_rock.getDefaultState());
                    }
                } else if (y < 56 - layerThickness) {
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.eve_rock.getDefaultState();
                    primer.setBlockState(localX, y, localZ, ModBlocks.basalt.getDefaultState());
                } else {
                    primer.setBlockState(localX, y, localZ, fillerState);
                }
            } else if (surfaceDepth > 0) {
                --surfaceDepth;
                primer.setBlockState(localX, y, localZ, fillerState);

                if (surfaceDepth == 0 && fillerState.getBlock() == Blocks.SAND) {
                    surfaceDepth = rand.nextInt(4) + Math.max(0, y - 63);
                    fillerState = Blocks.SANDSTONE.getDefaultState();
                }
            }
        }
    }
}
