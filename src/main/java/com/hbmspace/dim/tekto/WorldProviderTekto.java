package com.hbmspace.dim.tekto;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.WorldChunkManagerCelestial;
import com.hbmspace.dim.WorldChunkManagerCelestial.BiomeGenLayers;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.dim.tekto.GenLayerTekto.GenLayerTektoRiverMix;
import com.hbmspace.dim.tekto.GenLayerTekto.GenlayerTektoBiomes;
import net.minecraft.block.Block;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.layer.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class WorldProviderTekto extends WorldProviderCelestial {

	@Override
	public void init() {
		this.biomeProvider = new WorldChunkManagerCelestial(createBiomeGenerators(world.getSeed()));
	}
	
	@Override
	public @NotNull IChunkGenerator createChunkGenerator() {
		return new ChunkProviderTekto(this.world, this.getSeed(), false);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getSunBrightness(float par1) {
		float imsuper = super.getSunBrightness(par1);

		return imsuper * 0.7F;
	}

	@Override
	public Block getStone() {
		return ModBlocks.basalt;
	}

	@Override
	public boolean hasIce() {
		return true;
	}

	private static BiomeGenLayers createBiomeGenerators(long seed) {
		GenLayer genlayerBiomes = new GenlayerTektoBiomes(seed);

		genlayerBiomes = new GenLayerZoom(1000L, genlayerBiomes);
		genlayerBiomes = new GenLayerZoom(1001L, genlayerBiomes);
		genlayerBiomes = new GenLayerZoom(1002L, genlayerBiomes);
		genlayerBiomes = new GenLayerZoom(1003L, genlayerBiomes);
		genlayerBiomes = new GenLayerZoom(1004L, genlayerBiomes);
		genlayerBiomes = new GenLayerZoom(1005L, genlayerBiomes);

		GenLayer genlayerRiverZoom = new GenLayerZoom(1000L, genlayerBiomes);
		GenLayer genlayerRiver = new GenLayerRiver(1001L, genlayerRiverZoom); // Your custom river layer
		GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerRiver);

		GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, genlayerBiomes);
		GenLayerTektoRiverMix genlayerrivermix = new GenLayerTektoRiverMix(100L, genlayersmooth1, genlayersmooth);
		GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
		
		return new BiomeGenLayers(genlayerrivermix, genlayervoronoizoom, seed);
	}

	@Override
	public @NotNull DimensionType getDimensionType(){return DimensionType.getById(SpaceConfig.tektoDimension);}

}