package com.hbmspace.dim.orbit;

import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.BiomeGenBaseCelestial;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenOrbit extends BiomeGenBaseCelestial {
	public static final Biome biome = new BiomeGenOrbit(new BiomeProperties("Space").setRainDisabled());

	public BiomeGenOrbit(BiomeProperties properties) {
		super(properties);
	}

	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		// NOTHING
	}

	public void decorate(World world, Random rand, int x, int z) {
		// EVEN LESS
	}
	
}
