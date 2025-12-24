package com.hbmspace.dim;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.jetbrains.annotations.NotNull;

public class GenLayerDiversify extends GenLayer {

    private final Biome[] biomes;
    private final int chance;

    public GenLayerDiversify(long seed, GenLayer parent, int chance, Biome... biomes) {
        super(seed);
        this.parent = parent;
        this.biomes = biomes;
        this.chance = chance;
    }

    @Override
    public int @NotNull [] getInts(int x, int z, int width, int height) {
        int[] input = this.parent.getInts(x, z, width, height);
        int[] output = IntCache.getIntCache(width * height);

        for (int zOut = 0; zOut < height; zOut++) {
            for (int xOut = 0; xOut < width; xOut++) {
                int i = xOut + zOut * width;
                int center = input[i];

                this.initChunkSeed(xOut + x, zOut + z);

                if (this.nextInt(this.chance) == 0) {
                    Biome chosen = this.biomes[this.nextInt(this.biomes.length)];
                    output[i] = Biome.getIdForBiome(chosen);
                } else {
                    output[i] = center;
                }
            }
        }

        return output;
    }
}
