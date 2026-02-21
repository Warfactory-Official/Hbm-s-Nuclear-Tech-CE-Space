package com.hbmspace.dim.laythe;

import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.WorldChunkManagerCelestial;
import com.hbmspace.dim.WorldChunkManagerCelestial.BiomeGenLayers;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.dim.laythe.GenLayerLaythe.*;
import net.minecraft.block.material.Material;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.layer.*;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderLaythe extends WorldProviderCelestial {

	@Override
	public void init() {
		this.biomeProvider = new WorldChunkManagerCelestial(createBiomeGenerators(world.getSeed()));
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkProviderLaythe(this.world, this.getSeed(), false);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		return new SkyProviderLaytheSunset();
	}

	@Override
	public boolean hasLife() {
		return true;
	}

	@Override
	public int getWaterOpacity() {
		return 1;
	}

	@Override
	public float fogDensity(EntityViewRenderEvent.FogDensity event) {
		if(event.getState().getMaterial() == Material.WATER) {
			return 0.03F;
		}

		return super.fogDensity(event);
	}

	@Override
	public boolean updateLightmap(int[] lightmap) {
		for(int i = 0; i < 256; i++) {
			float sun = getSunBrightness(1.0F);
			float sky = lightBrightnessTable[i / 16];
			float jool = Math.max(sky - sun, 0);

			int[] color = unpackColor(lightmap[i]);

			color[1] += jool * 60;
			if(color[1] > 255) color[1] = 255;

			lightmap[i] = packColor(color);
		}
		return true;
	}

	private static BiomeGenLayers createBiomeGenerators(long seed) {
		GenLayer biomes = new GenLayerLaytheBiomes(seed);
		biomes = new GenLayerLaythePolar(1000L, biomes);

		biomes = new GenLayerZoom(1040L, biomes);
		biomes = GenLayerZoom.magnify(1000L, biomes, 1);
		biomes = new GenLayerLaythePolar(1300L, biomes);

		biomes = new GenLayerDiversifyLaythe(1000L, biomes);
		biomes = new GenLayerLaytheOceans(56000L, biomes);
		biomes = new GenLayerLaytheIslands(200L, biomes);

		biomes = new GenLayerZoom(1200L, biomes);
		biomes = new GenLayerZoom(1001L, biomes);

		biomes = new GenLayerLaytheOceans(4000L, biomes);
		biomes = new GenLayerLaytheOceans(4001L, biomes);

		biomes = new GenLayerZoom(1003L, biomes);
		biomes = new GenLayerLaytheOceans(4342L, biomes);
		biomes = new GenLayerSmooth(700L, biomes);

		biomes = new GenLayerZoom(1006L, biomes);

		GenLayer genLayerVoronoiZoom = new GenLayerVoronoiZoom(10L, biomes);

		return new BiomeGenLayers(biomes, genLayerVoronoiZoom, seed);
	}

	@Override
	public DimensionType getDimensionType(){return DimensionType.getById(SpaceConfig.laytheDimension);}

}