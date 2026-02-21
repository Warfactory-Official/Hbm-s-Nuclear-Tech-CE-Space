package com.hbmspace.dim.laythe.biome;

import com.hbmspace.dim.BiomeDecoratorCelestial;

public class BiomeGenLaytheCoast extends BiomeGenBaseLaythe {
    public BiomeGenLaytheCoast(BiomeProperties properties) {
        super(properties);
        ((BiomeDecoratorCelestial) decorator).waterPlantsPerChunk = 16;
        ((BiomeDecoratorCelestial) decorator).coralPerChunk = 32;
    }
}
