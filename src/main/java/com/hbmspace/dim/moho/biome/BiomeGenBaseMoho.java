package com.hbmspace.dim.moho.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.BiomeDecoratorCelestial;
import com.hbmspace.dim.BiomeGenBaseCelestial;
import net.minecraft.world.biome.Biome;

public abstract class BiomeGenBaseMoho extends BiomeGenBaseCelestial {

	public static final Biome mohoCrag = new BiomeGenMohoCrag(new BiomeProperties("Moho Crag").setBaseHeight(0.275F).setHeightVariation(0.666F));
	public static final Biome mohoBasalt = new BiomeGenMohoBasalt(new BiomeProperties("Moho Basalt Deltas").setBaseHeight(0).setHeightVariation(0.224F));
    public static final Biome mohoLavaSea = new BiomeGenMohoLavaSea(new BiomeProperties("Moho Lava Sea").setBaseHeight(-0.8F).setHeightVariation(0.01F));

	public BiomeGenBaseMoho(BiomeProperties properties) {
		super(properties);
		properties.setRainDisabled();

		BiomeDecoratorCelestial decorator = new BiomeDecoratorCelestial(ModBlocksSpace.moho_stone);
		decorator.lavaCount = 50;
		this.decorator = decorator;

		properties.setTemperature(1.0F);
		properties.setRainfall(0.0F);
	}

}
