package com.hbmspace.dim.minmus;

import com.hbm.blocks.BlockEnums;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorMinmus implements IWorldGenerator {

    public WorldGeneratorMinmus() {
        /*Map<Block, StructureComponent.BlockSelector> concrete = new HashMap<Block, StructureComponent.BlockSelector>() {{
            put(ModBlocks.concrete_colored, new DesertConcrete());
        }};

        JigsawPiece minmusBase = new JigsawPiece("minmus_base", StructureManager.mun_base) {{ alignToTerrain = true; heightOffset = -1; blockTable = concrete; }};

        NBTStructure.registerStructure(com.hbm.config.SpaceConfig.minmusDimension, new SpawnCondition("minmus_base") {{
            spawnWeight = 6;
            sizeLimit = 32;
            startPool = "start";
            pools = new HashMap<String, JigsawPool>() {{
                put("start", new JigsawPool() {{
                    add(minmusBase, 1);
                }});
                put("default", new JigsawPool() {{
                    add(minmusBase, 1);
                    add(new JigsawPiece("minmus_flag", StructureManager.mun_flag_uk) {{ alignToTerrain = true; heightOffset = -1; }}, 2);
                    add(new JigsawPiece("minmus_panels", StructureManager.mun_panels) {{ alignToTerrain = true; heightOffset = -1; }}, 6);
                    add(new JigsawPiece("minmus_stardar", StructureManager.mun_stardar) {{ alignToTerrain = true; heightOffset = -1; }}, 1);
                    add(new JigsawPiece("minmus_tanks", StructureManager.mun_tanks) {{ alignToTerrain = true; heightOffset = -1; }}, 6);
                }});
                put("connect", new JigsawPool() {{
                    add(new JigsawPiece("minmus_connector_1", StructureManager.mun_connector_1), 1);
                    add(new JigsawPiece("minmus_connector_2", StructureManager.mun_connector_2), 1);
                    add(new JigsawPiece("minmus_connector_3", StructureManager.mun_connector_3), 1);
                    add(new JigsawPiece("minmus_connector_s", StructureManager.mun_connector_s), 1);
                    add(new JigsawPiece("minmus_connector_l", StructureManager.mun_connector_l), 1);
                    add(new JigsawPiece("minmus_connector_t", StructureManager.mun_connector_t), 1);
                }});
            }};
        }});*/

        NBTStructure.registerNullWeight(SpaceConfig.minmusDimension, 18);

        //BlockOre.addValidBody(ModBlocks.ore_brine, SolarSystem.Body.MINMUS);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.minmusDimension) {
			generateMinmus(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateMinmus(World world, Random rand, int i, int j) {
		int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();

        DungeonToolbox.generateOre(world, rand, i, j, WorldConfigSpace.nickelSpawn, 8, 1, 43, ModBlocksSpace.ore_nickel.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.titaniumSpawn, 12, 4, 27, ModBlocksSpace.ore_titanium.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, 1, 16, 6, 40, ModBlocksSpace.stone_resource.getStateFromMeta(BlockEnums.EnumStoneType.MALACHITE.ordinal()), stone);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.copperSpawn * 3, 12, 8, 56, ModBlocksSpace.ore_copper.getStateFromMeta(meta), stone);
	}



}