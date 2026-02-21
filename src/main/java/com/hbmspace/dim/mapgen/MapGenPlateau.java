package com.hbmspace.dim.mapgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import org.jetbrains.annotations.NotNull;

public class MapGenPlateau extends MapGenBase {

    public Block stoneBlock;
    public Block surfrock;
    public Block fillblock;

    public NoiseGeneratorPerlin plateauNoise;
    public double noiseScale = 0.05;
    public int maxPlateauAddition = 12;
    public int stepHeight = 6;
    public int topsoilThickness = 2;

    public Biome applyToBiome;

    public MapGenPlateau(World world) {
        this.plateauNoise = new NoiseGeneratorPerlin(world.rand, 4);
    }

    @Override
    public void recursiveGenerate(@NotNull World world, int chunkX, int chunkZ, int originalX, int originalZ, @NotNull ChunkPrimer primer) {
        if (chunkX != originalX || chunkZ != originalZ) {
            return;
        }

        int[][] plateauTops = new int[16][16];
        int[][] baseHeights = new int[16][16];

        for(int localX = 0; localX < 16; localX++) {
            for(int localZ = 0; localZ < 16; localZ++) {
                int baseHeight = getSurfaceHeight(primer, localX, localZ);
                baseHeights[localX][localZ] = baseHeight;
                double noiseVal = plateauNoise.getValue((originalX * 16 + localX) * noiseScale, (originalZ * 16 + localZ) * noiseScale);
                int plateauAddition = (int)(((noiseVal + 1) / 2.0) * maxPlateauAddition);
                plateauAddition = (plateauAddition / stepHeight) * stepHeight;
                plateauTops[localX][localZ] = baseHeight + plateauAddition;
            }
        }

        for(int localX = 0; localX < 16; localX++) {
            for(int localZ = 0; localZ < 16; localZ++) {
                if(applyToBiome != null) {
                    Biome biome = world.getBiome(new BlockPos(localX + originalX * 16, 0, localZ + originalZ * 16));
                    if(biome != applyToBiome) continue;
                }
                int baseHeight = baseHeights[localX][localZ];
                int plateauTop = plateauTops[localX][localZ];

                for(int y = baseHeight + 1; y < 256; y++) {
                    if(y < plateauTop - topsoilThickness) {
                        if(stoneBlock != null) primer.setBlockState(localX, y, localZ, stoneBlock.getDefaultState());
                    } else if(y < plateauTop) {
                        boolean sameLeft  = (localX - 1 < 0)    || (plateauTops[localX - 1][localZ] == plateauTop);
                        boolean sameRight = (localX + 1 >= 16)  || (plateauTops[localX + 1][localZ] == plateauTop);
                        boolean sameFront = (localZ - 1 < 0)    || (plateauTops[localX][localZ - 1] == plateauTop);
                        boolean sameBack  = (localZ + 1 >= 16)  || (plateauTops[localX][localZ + 1] == plateauTop);

                        if(y == plateauTop - 1 && sameLeft && sameRight && sameFront && sameBack) {
                            if(fillblock != null) primer.setBlockState(localX, y, localZ, fillblock.getDefaultState());
                        } else {
                            if(surfrock != null) primer.setBlockState(localX, y, localZ, surfrock.getDefaultState());
                        }
                    } else {
                        primer.setBlockState(localX, y, localZ, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    private int getSurfaceHeight(ChunkPrimer primer, int localX, int localZ) {
        int baseHeight = 0;
        for(int y = 255; y >= 0; y--) {
            IBlockState state = primer.getBlockState(localX, y, localZ);
            if(state.getBlock() != Blocks.AIR) {
                baseHeight = y;
                break;
            }
        }
        return baseHeight;
    }
}