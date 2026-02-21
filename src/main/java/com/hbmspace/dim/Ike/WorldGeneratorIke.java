package com.hbmspace.dim.Ike;

import com.hbm.blocks.ModBlocks;
import com.hbm.world.dungeon.AncientTombStructure;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.WorldGeneratorCelestial;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorIke implements IWorldGenerator {

    public WorldGeneratorIke() {
        /*NBTStructure.registerStructure(com.hbm.config.SpaceConfig.ikeDimension, new SpawnCondition("ike_artifact") {{
            structure = new JigsawPiece("ike_artifact", StructureManager.ike_artifact, -5);
            spawnWeight = 4;
        }});*/

        NBTStructure.registerNullWeight(SpaceConfig.ikeDimension, 12);

        BlockOre.addValidBody(ModBlocksSpace.ore_mineral, SolarSystem.Body.IKE);
        BlockOre.addValidBody(ModBlocksSpace.ore_lithium, SolarSystem.Body.IKE);
        BlockOre.addValidBody(ModBlocks.ore_coltan, SolarSystem.Body.IKE);
        BlockOre.addValidBody(ModBlocksSpace.ore_asbestos, SolarSystem.Body.IKE);

        BlockOre.addValidBody(ModBlocksSpace.ore_brine, SolarSystem.Body.IKE);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.ikeDimension) {
			generateIke(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateIke(World world, Random rand, int i, int j) {
		int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();

        WorldGeneratorCelestial.generateOre(world, rand, i, j, WorldConfig.asbestosSpawn, 8, 3, 22, ModBlocksSpace.ore_asbestos.getStateFromMeta(meta), stone);
        WorldGeneratorCelestial.generateOre(world, rand, i, j, WorldConfig.copperSpawn, 9, 4, 27, ModBlocksSpace.ore_copper.getStateFromMeta(meta), stone);
        WorldGeneratorCelestial.generateOre(world, rand, i, j, WorldConfigSpace.ironSpawn,  8, 1, 33, ModBlocksSpace.ore_iron.getStateFromMeta(meta), stone);
        WorldGeneratorCelestial.generateOre(world, rand, i, j, WorldConfig.lithiumSpawn,  6, 4, 8, ModBlocksSpace.ore_lithium.getStateFromMeta(meta), stone);
        WorldGeneratorCelestial.generateOre(world, rand, i, j, 2, 4, 15, 40, ModBlocks.ore_coltan.getStateFromMeta(meta), stone);
		
		//okay okay okay, lets say on duna you DO make solvent, this is now awesome because you can now make gallium arsenide to then head to
		//dres and the likes :)
	
		
        WorldGeneratorCelestial.generateOre(world, rand, i, j, WorldConfigSpace.mineralSpawn, 10, 12, 32, ModBlocksSpace.ore_mineral.getStateFromMeta(meta), ModBlocksSpace.moho_stone);

        if(WorldConfig.pyramidStructure > 0 && rand.nextInt(WorldConfig.pyramidStructure) == 0) {
            int x = i + rand.nextInt(16);
            int z = j + rand.nextInt(16);
            int y = world.getHeight(x, z);

            AncientTombStructure.INSTANCE.generate(world, rand, new BlockPos(x, y, z));
        }
	}
}