package com.hbmspace.dim.moho;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.GenLayerDiversify;
import com.hbmspace.dim.WorldChunkManagerCelestial;
import com.hbmspace.dim.WorldChunkManagerCelestial.BiomeGenLayers;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.dim.moho.biome.BiomeGenBaseMoho;
import com.hbmspace.dim.moho.genlayer.GenLayerMohoBiomes;
import com.hbmspace.dim.moho.genlayer.GenLayerMohoPlateauExtend;
import net.minecraft.block.Block;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.layer.*;
import org.jetbrains.annotations.NotNull;

public class WorldProviderMoho extends WorldProviderCelestial {

	@Override
	public void init() {
		this.biomeProvider = new WorldChunkManagerCelestial(createBiomeGenerators(world.getSeed()));
	}
	
	@Override
	public @NotNull IChunkGenerator createChunkGenerator() {
		return new ChunkProviderMoho(this.world, this.getSeed(), false);
	}

	@Override
	public Block getStone() {
		return ModBlocksSpace.moho_stone;
	}

	private static BiomeGenLayers createBiomeGenerators(long seed) {
		GenLayer biomes = new GenLayerMohoBiomes(seed);

        biomes = new GenLayerFuzzyZoom(2000L, biomes);
        biomes = new GenLayerDiversify(1234L, biomes, 6, BiomeGenBaseMoho.mohoBasalt);
        biomes = new GenLayerZoom(2001L, biomes);
        biomes = new GenLayerMohoPlateauExtend(9944L, biomes);
        biomes = new GenLayerZoom(1006L, biomes);
        biomes = new GenLayerSmooth(700L, biomes);
        biomes = new GenLayerZoom(1000L, biomes);
        biomes = new GenLayerZoom(107L, biomes);

        // biome detail layer should ignore rivers (for monster spawns and biome block replacement)
        GenLayer genlayerVoronoiZoom = new GenLayerVoronoiZoom(10L, biomes);

        GenLayer genlayerRiver = new GenLayerRiver(1004L, biomes);
        genlayerRiver = new GenLayerZoom(105L, genlayerRiver);
        genlayerRiver = new GenLayerZoom(106L, genlayerRiver); // Added extra zoom for more frequent rivers

        // apply the rivers to the biome map
        GenLayer genlayerRiverMix = new GenLayerRiverMix(100L, biomes, genlayerRiver);

		return new BiomeGenLayers(genlayerRiverMix, genlayerVoronoiZoom, seed);
	}

	@Override
	public @NotNull DimensionType getDimensionType(){return DimensionType.getById(SpaceConfig.mohoDimension);}

}