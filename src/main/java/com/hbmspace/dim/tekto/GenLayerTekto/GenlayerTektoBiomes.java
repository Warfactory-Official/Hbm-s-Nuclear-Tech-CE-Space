package com.hbmspace.dim.tekto.GenLayerTekto;

import com.hbmspace.dim.tekto.biome.BiomeGenBaseTekto;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.jetbrains.annotations.NotNull;

public class GenlayerTektoBiomes extends GenLayer {

    private static final Biome[] biomes = new Biome[] { BiomeGenBaseTekto.polyvinylPlains, BiomeGenBaseTekto.halogenHills, BiomeGenBaseTekto.forest, BiomeGenBaseTekto.vinylsands};

    public GenlayerTektoBiomes(long l) {
        super(l);
    }

    @Override
    public int @NotNull [] getInts(int x, int z, int width, int depth) {
        int[] dest = IntCache.getIntCache(width * depth);

        for(int dz = 0; dz < depth; dz++) {
            for(int dx = 0; dx < width; dx++) {
                this.initChunkSeed(dx + x, dz + z);

                dest[dx + dz * width] = Biome.getIdForBiome(biomes[this.nextInt(biomes.length)]);
            }
        }
        return dest;
    }
}
