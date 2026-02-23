package com.hbmspace.dim.laythe.biome;

import com.hbmspace.dim.BiomeDecoratorCelestial;
import com.hbmspace.entity.mob.EntityScuttlecrab;

public class BiomeGenLaytheCoast extends BiomeGenBaseLaythe {
    public BiomeGenLaytheCoast(BiomeProperties properties) {
        super(properties);

        this.waterCreatures.add(new SpawnListEntry(EntityScuttlecrab.class, 4, 1, 3));

        ((BiomeDecoratorCelestial) decorator).waterPlantsPerChunk = 16;
        ((BiomeDecoratorCelestial) decorator).coralPerChunk = 32;
    }
}
