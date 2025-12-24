package com.hbmspace.dim.dres;

import com.hbm.blocks.ModBlocks;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbm.config.GeneralConfig;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorDres implements IWorldGenerator {
    
    public WorldGeneratorDres(){
        // TODO add structures in the constructor
        
        NBTStructure.registerNullWeight(SpaceConfig.dresDimension, 16);

        BlockOre.addValidBody(ModBlocksSpace.ore_shale, SolarSystem.Body.DRES);
        BlockOre.addValidBody(ModBlocksSpace.ore_lanthanium, SolarSystem.Body.DRES);
        BlockOre.addValidBody(ModBlocksSpace.ore_niobium, SolarSystem.Body.DRES);
        BlockOre.addValidBody(ModBlocks.ore_coltan, SolarSystem.Body.DRES);
        BlockOre.addValidBody(ModBlocksSpace.ore_lanthanium, SolarSystem.Body.DRES);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.dresDimension) {
			generateDres(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateDres(World world, Random rand, int i, int j) {
        int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();

        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.cobaltSpawn, 4, 3, 22, ModBlocksSpace.ore_cobalt.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.copperSpawn, 9, 4, 27, ModBlocksSpace.ore_iron.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, 12,  8, 1, 33, ModBlocksSpace.ore_niobium.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, GeneralConfig.coltanRate, 4, 15, 40, ModBlocks.ore_coltan.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, 1, 6, 4, 64, ModBlocksSpace.ore_lanthanium.getStateFromMeta(meta), stone);

        DungeonToolbox.generateOre(world, rand, i, j, 1, 12, 8, 32, ModBlocksSpace.ore_shale.getStateFromMeta(meta), stone);
	}
}