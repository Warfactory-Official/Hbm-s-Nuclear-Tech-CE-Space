package com.hbmspace.dim.orbit;

import com.hbmspace.dim.BiomeGenBaseCelestial;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenOrbit extends BiomeGenBaseCelestial {
	public static final Biome biome = new BiomeGenOrbit(new BiomeProperties("Space").setRainDisabled());

	public BiomeGenOrbit(BiomeProperties properties) {
		super(properties);
	}

	@Override
	public void genTerrainBlocks(@NotNull World worldIn, @NotNull Random rand, @NotNull ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		// NOTHING
	}
}
