package com.hbmspace.dim.laythe;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.util.GenUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorLaythe implements IWorldGenerator {

    public WorldGeneratorLaythe() {
        /*NBTStructure.registerStructure(com.hbm.config.SpaceConfig.laytheDimension, new SpawnCondition("laythe_nuke_sub") {{
            structure = new JigsawPiece("laythe_nuke_sub", StructureManager.nuke_sub);
            canSpawn = biome -> biome == BiomeGenBaseLaythe.laytheOcean;
            maxHeight = 54;
            spawnWeight = 6;
        }});
        NBTStructure.registerStructure(com.hbm.config.SpaceConfig.laytheDimension, new SpawnCondition("laythe_vertibird") {{
            structure = new JigsawPiece("laythe_vertibird", StructureManager.vertibird, -3);
            canSpawn = biome -> biome.rootHeight >= 0;
            spawnWeight = 6;
        }});
        NBTStructure.registerStructure(com.hbm.config.SpaceConfig.laytheDimension, new SpawnCondition("laythe_crashed_vertibird") {{
            structure = new JigsawPiece("laythe_crashed_vertibird", StructureManager.crashed_vertibird, -10);
            canSpawn = biome -> biome.rootHeight >= 0;
            spawnWeight = 6;
        }});*/

        BlockOre.addValidBody(ModBlocksSpace.ore_emerald, SolarSystem.Body.LAYTHE);
        BlockOre.addValidBody(ModBlocksSpace.ore_lapis, SolarSystem.Body.LAYTHE);
        BlockOre.addValidBody(ModBlocksSpace.ore_asbestos, SolarSystem.Body.LAYTHE);
        BlockOre.addValidBody(ModBlocks.ore_oil, SolarSystem.Body.LAYTHE);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(world.provider.getDimension() == SpaceConfig.laytheDimension) {
            generateLaythe(world, random, chunkX * 16, chunkZ * 16);
        }
    }

	private void generateLaythe(World world, Random rand, int i, int j) {
		int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();
        // a'ight, I'll leave the old oil generation here for now
		if(WorldConfig.laytheOilSpawn > 0 && rand.nextInt(WorldConfig.laytheOilSpawn) == 0) {
			int randPosX = i + rand.nextInt(16);
			int randPosY = rand.nextInt(25);
			int randPosZ = j + rand.nextInt(16);

			GenUtil.spawnOil(world, randPosX, randPosY, randPosZ, 10 + rand.nextInt(7), ModBlocks.ore_oil, meta, Blocks.STONE);
		}

        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.asbestosSpawn, 4, 16, 16, ModBlocksSpace.ore_asbestos.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.berylliumSpawn, 4, 5, 30, ModBlocksSpace.ore_beryllium.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.rareSpawn, 5, 5, 20, ModBlocksSpace.ore_rare.getStateFromMeta(meta), stone);

    }
    
}
