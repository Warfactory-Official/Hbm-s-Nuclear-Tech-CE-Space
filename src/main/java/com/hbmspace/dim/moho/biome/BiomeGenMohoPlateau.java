package com.hbmspace.dim.moho.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenMohoPlateau extends BiomeGenBaseMoho {

    public BiomeGenMohoPlateau(BiomeProperties properties) {
        super(properties);
        this.topBlock = ModBlocksSpace.moho_regolith.getDefaultState();
        this.fillerBlock = ModBlocksSpace.moho_regolith.getDefaultState(); // thiccer regolith due to uhhhhhh...................
    }

    @Override
    public void genTerrainBlocks(@NotNull World world, Random rand, @NotNull ChunkPrimer chunkPrimer, int x, int z, double noise) {
        IBlockState topBlockState = this.topBlock;
        IBlockState fillerBlockState = this.fillerBlock;
        int k = -1;
        int l = (int) (noise / 8.0D + 8.0D + rand.nextDouble() * 0.50D);
        int i1 = x & 15;
        int j1 = z & 15;
        int maxHeight = 256;

        for (int l1 = maxHeight - 1; l1 >= 0; --l1) {
            IBlockState currentState = chunkPrimer.getBlockState(i1, l1, j1);

            if (l1 <= rand.nextInt(5)) {
                chunkPrimer.setBlockState(i1, l1, j1, Blocks.BEDROCK.getDefaultState());
            } else {
                if (currentState.getBlock() != Blocks.AIR) {
                    if (currentState.getBlock() == ModBlocksSpace.moho_stone) {
                        if (k == -1) {
                            if (l <= 0) {
                                topBlockState = Blocks.AIR.getDefaultState();
                                fillerBlockState = ModBlocksSpace.moho_stone.getDefaultState();
                            } else if (l1 >= 59 && l1 <= 64) {
                                topBlockState = this.topBlock;
                                fillerBlockState = this.fillerBlock;
                            }

                            if (l1 < 63 && (topBlockState.getBlock() == Blocks.AIR)) {
                                topBlockState = this.topBlock;
                            }

                            k = l;

                            if (l1 >= 62) {
                                chunkPrimer.setBlockState(i1, l1, j1, topBlockState);
                            } else {
                                topBlockState = Blocks.AIR.getDefaultState();
                                fillerBlockState = ModBlocksSpace.moho_stone.getDefaultState();
                                chunkPrimer.setBlockState(i1, l1, j1, Blocks.GRAVEL.getDefaultState());
                            }
                        } else if (k > 0) {
                            --k;
                            chunkPrimer.setBlockState(i1, l1, j1, fillerBlockState);

                            if (k == 0 && fillerBlockState.getBlock() == Blocks.SAND) {
                                k = rand.nextInt(4) + Math.max(0, l1 - 63);
                                fillerBlockState = Blocks.SANDSTONE.getDefaultState();
                            }
                        }
                    }
                } else {
                    k = -1;
                }
            }
        }
    }
}
