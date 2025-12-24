package com.hbmspace.dim.eve;

import com.hbm.blocks.ModBlocks;
import com.hbm.world.OilBubble;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.dim.eve.GenLayerEve.WorldGenElectricVolcano;
import com.hbmspace.dim.eve.GenLayerEve.WorldGenEveSpike;
import com.hbmspace.dim.eve.biome.BiomeGenBaseEve;
import com.hbmspace.util.GenUtil;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorEve implements IWorldGenerator {

	WorldGenElectricVolcano volcano = new WorldGenElectricVolcano(30, 22, ModBlocksSpace.eve_silt, ModBlocksSpace.eve_rock);

    public WorldGeneratorEve() {
        NBTStructure.registerNullWeight(SpaceConfig.eveDimension, 24);

        BlockOre.addValidBody(ModBlocksSpace.ore_niobium, SolarSystem.Body.EVE);
        BlockOre.addValidBody(ModBlocksSpace.ore_iodine, SolarSystem.Body.EVE);
        BlockOre.addValidBody(ModBlocksSpace.ore_schrabidium, SolarSystem.Body.EVE);
        BlockOre.addValidBody(ModBlocksSpace.ore_gas, SolarSystem.Body.EVE);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.eveDimension) {
			generateEve(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateEve(World world, Random rand, int i, int j) {
		int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();

        DungeonToolbox.generateOre(world, rand, i, j, 12,  8, 1, 33, ModBlocksSpace.ore_niobium.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, 8,  4, 5, 48, ModBlocksSpace.ore_iodine.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, 1,  4, 1, 16, ModBlocksSpace.ore_schrabidium.getStateFromMeta(meta), stone);

		int x = i + rand.nextInt(16);
		int z = j + rand.nextInt(16);
		int y = world.getHeight(x, z);
		BlockPos pos = new BlockPos(x, y, z);

		Biome biome = world.getBiomeForCoordsBody(pos);
		if(biome == BiomeGenBaseEve.eveSeismicPlains) {
			new WorldGenEveSpike().generate(world, rand, pos);
		}

		if(rand.nextInt(100) == 0) {
			volcano.generate(world, rand, pos);
		}
	}

}
