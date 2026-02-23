package com.hbmspace.dim.laythe.biome;


import com.hbmspace.entity.mob.EntityDepthSquid;
import com.hbmspace.entity.mob.EntityScrapFish;
import com.hbmspace.entity.mob.EntitySifterEel;

public class BiomeGenLaytheOcean extends BiomeGenBaseLaythe {

	public BiomeGenLaytheOcean(BiomeProperties properties) {
		super(properties);

		this.waterCreatures.add(new SpawnListEntry(EntityScrapFish.class, 2, 1, 4));
		this.waterCreatures.add(new SpawnListEntry(EntitySifterEel.class, 1, 1, 4));
		this.waterCreatures.add(new SpawnListEntry(EntityDepthSquid.class, 1, 1, 4));
	}
}

	