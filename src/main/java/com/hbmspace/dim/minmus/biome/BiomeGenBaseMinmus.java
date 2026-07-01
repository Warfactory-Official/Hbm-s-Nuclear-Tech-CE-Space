package com.hbmspace.dim.minmus.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.BiomeDecoratorCelestial;
import com.hbmspace.dim.BiomeGenBaseCelestial;
import net.minecraft.world.biome.Biome;

public abstract class BiomeGenBaseMinmus extends BiomeGenBaseCelestial {

    public static final Biome minmusPlains = new BiomeGenMinmusHills(new BiomeProperties("Minmus Hills").setBaseHeight(0.325F).setHeightVariation(0.08F).setTemperature(-1.0F).setRainfall(0.0F));
    public static final Biome minmusCanyon = new BiomeGenMinmusBasin(new BiomeProperties("Minmus Basins").setBaseHeight(-1F).setHeightVariation(0.02F).setTemperature(-1.0F).setRainfall(0.0F));
    
    public BiomeGenBaseMinmus(BiomeProperties properties) {
        super(properties);
		properties.setRainDisabled();
        
        this.decorator = new BiomeDecoratorCelestial(ModBlocksSpace.minmus_regolith);
        this.decorator.generateFalls = false;
        
        this.topBlock = ModBlocksSpace.minmus_regolith.getDefaultState(); //remind me to send roadhog his daily modmail
        this.fillerBlock = ModBlocksSpace.minmus_regolith.getDefaultState();
    }
}