package com.hbmspace.dim.moon;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbm.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.dim.CelestialBody;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.dim.SolarSystem;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorMoon implements IWorldGenerator {

	public WorldGeneratorMoon() {
		BlockOre.addValidBody(ModBlocksSpace.ore_lithium, SolarSystem.Body.MUN);
		BlockOre.addValidBody(ModBlocksSpace.ore_quartz, SolarSystem.Body.MUN);
		BlockOre.addValidBody(ModBlocksSpace.ore_shale, SolarSystem.Body.MUN);

		//BlockOre.addValidBody(ModBlocksSpace.ore_brine, SolarSystem.Body.MUN);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.moonDimension) {
			generateMoon(world, random, chunkX * 16, chunkZ * 16); 
		}
	}

	private void generateMoon(World world, Random rand, int i, int j) {
		int meta = CelestialBody.getMeta(world);

		DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.lithiumSpawn,  6, 4, 8, ModBlocksSpace.ore_lithium.getDefaultState().withProperty(BlockOre.META, meta), ModBlocksSpace.moon_rock);
		DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.aluminiumSpawn,  6, 5, 40, ModBlocksSpace.ore_aluminium.getDefaultState().withProperty(BlockOre.META, meta), ModBlocksSpace.moon_rock);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.fluoriteSpawn, 4, 5, 45, ModBlocksSpace.ore_fluorite.getDefaultState().withProperty(BlockOre.META, meta), ModBlocksSpace.moon_rock);
        DungeonToolbox.generateOre(world, rand, i, j, 10, 13, 5, 64, ModBlocksSpace.ore_quartz.getDefaultState().withProperty(BlockOre.META, meta), ModBlocksSpace.moon_rock);

        DungeonToolbox.generateOre(world, rand, i, j, 1, 12, 8, 32, ModBlocksSpace.ore_shale.getDefaultState().withProperty(BlockOre.META, meta), ModBlocksSpace.moon_rock);
	}
}