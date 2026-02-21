package com.hbmspace.dim.moho.genlayer;

import com.hbmspace.dim.moho.biome.BiomeGenBaseMoho;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.jetbrains.annotations.NotNull;

public class GenLayerMohoPlateauExtend extends GenLayer {

    public GenLayerMohoPlateauExtend(long seed, GenLayer parent) {
        super(seed);
        this.parent = parent;
    }

    @Override
    public int @NotNull [] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int i = areaX - 1;
        int j = areaY - 1;
        int k = areaWidth + 2;
        int l = areaHeight + 2;

        int[] in = this.parent.getInts(i, j, k, l);
        int[] out = IntCache.getIntCache(areaWidth * areaHeight);

        final int cragId = Biome.getIdForBiome(BiomeGenBaseMoho.mohoCrag);
        final int plateauId = Biome.getIdForBiome(BiomeGenBaseMoho.mohoPlateau);

        for (int y = 0; y < areaHeight; ++y) {
            for (int x = 0; x < areaWidth; ++x) {
                this.initChunkSeed(x + areaX, y + areaY);

                int center = in[x + 1 + (y + 1) * k];

                if (center == cragId) {
                    int north = in[x + 1 + (y) * k];
                    int east = in[x + 2 + (y + 1) * k];
                    int west = in[x + (y + 1) * k];
                    int south = in[x + 1 + (y + 2) * k];

                    boolean surrounded = ((north == cragId || north == plateauId)
                            && (east == cragId || east == plateauId)
                            && (west == cragId || west == plateauId)
                            && (south == cragId || south == plateauId));

                    if (surrounded) {
                        center = plateauId;
                    }
                }

                out[x + y * areaWidth] = center;
            }
        }

        return out;
    }
}