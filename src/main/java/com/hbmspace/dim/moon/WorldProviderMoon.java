package com.hbmspace.dim.moon;

import com.hbmspace.config.SpaceConfig;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class WorldProviderMoon extends WorldProviderCelestial {

	@Override
	public void init() {
		this.biomeProvider = new BiomeProviderSingle(ForgeRegistries.BIOMES.getValue(new ResourceLocation("hbm", "moon")));
	}
	
	@Override
	public @NotNull IChunkGenerator createChunkGenerator() {
		return new ChunkProviderMoon(this.world, this.getSeed(), false);
	}

	@Override
	public Block getStone() {
		return ModBlocksSpace.moon_rock;
	}

	@Override
	public @NotNull DimensionType getDimensionType(){return DimensionType.getById(SpaceConfig.moonDimension);}

}
