package com.hbmspace.dim.moho;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.WorldConfig;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbm.blocks.bomb.BlockVolcano;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorMoho implements IWorldGenerator {

    public WorldGeneratorMoho() {
        /*NBTStructure.registerStructure(com.hbm.config.SpaceConfig.mohoDimension, new SpawnCondition("moho_base") {{
            spawnWeight = 4;
            minHeight = 63 - 11;
            maxHeight = 63;
            sizeLimit = 64;
            rangeLimit = 64;
            startPool = "start";
            pools = new HashMap<String, JigsawPool>() {{
                put("start", new JigsawPool() {{
                    add(new JigsawPiece("moho_core", StructureManager.moho_core) {{ heightOffset = -11; }}, 1);
                }});
                put("default", new JigsawPool() {{
                    add(new JigsawPiece("moho_corner_lab", StructureManager.moho_corner_lab) {{ heightOffset = -14; }}, 2);
                    add(new JigsawPiece("moho_corner_heffer", StructureManager.moho_corner_heffer) {{ heightOffset = -17; }}, 2);
                    add(new JigsawPiece("moho_corner_extension", StructureManager.moho_corner_extension) {{ heightOffset = -11; }}, 1);
                    add(new JigsawPiece("moho_corner_empty", StructureManager.moho_corner_empty) {{ heightOffset = -11; }}, 1);
                    fallback = "fallback";
                }});
                put("room", new JigsawPool() {{
                    add(new JigsawPiece("moho_room_tape", StructureManager.moho_room_tape) {{ heightOffset = -11; }}, 1);
                    add(new JigsawPiece("moho_room_reception", StructureManager.moho_room_reception) {{ heightOffset = -11; }}, 1);
                    add(new JigsawPiece("moho_room_kitchen", StructureManager.moho_room_kitchen) {{ heightOffset = -11; }}, 1);
                    fallback = "room";
                }});
                put("fallback", new JigsawPool() {{
                    add(new JigsawPiece("moho_fall", StructureManager.moho_corner_cap) {{ heightOffset = -11; }}, 1);
                }});
                put("snorkel", new JigsawPool() {{
                    add(new JigsawPiece("moho_snorkel", StructureManager.moho_snorkel) {{ heightOffset = -11; }}, 1);
                }});
            }};
        }});*/

        NBTStructure.registerNullWeight(SpaceConfig.mohoDimension, 20);

        BlockOre.addValidBody(ModBlocksSpace.ore_mineral, SolarSystem.Body.MOHO);
        BlockOre.addValidBody(ModBlocksSpace.ore_shale, SolarSystem.Body.MOHO);
        BlockOre.addValidBody(ModBlocksSpace.ore_glowstone, SolarSystem.Body.MOHO);
        BlockOre.addValidBody(ModBlocksSpace.ore_fire, SolarSystem.Body.MOHO);
        BlockOre.addValidBody(ModBlocksSpace.ore_australium, SolarSystem.Body.MOHO);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.mohoDimension) {
			generateMoho(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateMoho(World world, Random rand, int i, int j) {
		int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();

        DungeonToolbox.generateOre(world, rand, i, j, WorldConfigSpace.mineralSpawn, 10, 12, 32, ModBlocksSpace.ore_mineral.getStateFromMeta(meta), stone);

        DungeonToolbox.generateOre(world, rand, i, j, 14, 12, 5, 30, ModBlocksSpace.ore_glowstone.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.netherPhosphorusSpawn, 6, 8, 64, ModBlocksSpace.ore_fire.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, 8, 4, 0, 24, ModBlocksSpace.ore_australium.getStateFromMeta(meta), stone);

        DungeonToolbox.generateOre(world, rand, i, j, 1, 12, 8, 32, ModBlocksSpace.ore_shale.getStateFromMeta(meta), stone);

        DungeonToolbox.generateOre(world, rand, i, j, 10, 32, 0, 128, ModBlocks.basalt.getDefaultState(), stone);
		
		// More basalt ores!
		//DungeonToolbox.generateOre(world, rand, i, j, 16, 6, 16, 64, ModBlocksSpace.ore_basalt, 0, ModBlocksSpace.basalt);
		//DungeonToolbox.generateOre(world, rand, i, j, 12, 8, 8, 32, ModBlocksSpace.ore_basalt, 1, ModBlocksSpace.basalt);
		//DungeonToolbox.generateOre(world, rand, i, j, 8, 9, 8, 48, ModBlocksSpace.ore_basalt, 2, ModBlocksSpace.basalt);
		//DungeonToolbox.generateOre(world, rand, i, j, 2, 4, 0, 24, ModBlocksSpace.ore_basalt, 3, ModBlocksSpace.basalt);
		//DungeonToolbox.generateOre(world, rand, i, j, 8, 10, 16, 64, ModBlocksSpace.ore_basalt, 4, ModBlocksSpace.basalt);

		for(int k = 0; k < 2; k++){
			int x = i + rand.nextInt(16);
			int z = j + rand.nextInt(16);
			int d = 16 + rand.nextInt(96);

			for(int y = d - 5; y <= d; y++) {
				BlockPos pos = new BlockPos(x, y, z);
				Block b = world.getBlockState(pos).getBlock();
				if(world.getBlockState(pos.up()).getBlock() == Blocks.AIR && (b == ModBlocksSpace.moho_stone || b == ModBlocksSpace.moho_regolith)) {
					world.setBlockState(pos, ModBlocks.geysir_nether.getDefaultState());
					world.setBlockState(pos.add(1, 0, 0), Blocks.NETHERRACK.getDefaultState());
					world.setBlockState(pos.add(-1, 0, 0), Blocks.NETHERRACK.getDefaultState());
					world.setBlockState(pos.add(0, 0, 1), Blocks.NETHERRACK.getDefaultState());
					world.setBlockState(pos.add(0, 0, -1), Blocks.NETHERRACK.getDefaultState());
					world.setBlockState(pos.add(1, -1, 0), Blocks.NETHERRACK.getDefaultState());
					world.setBlockState(pos.add(-1, -1, 0), Blocks.NETHERRACK.getDefaultState());
					world.setBlockState(pos.add(0, -1, 1), Blocks.NETHERRACK.getDefaultState());
					world.setBlockState(pos.add(0, -1, -1), Blocks.NETHERRACK.getDefaultState());
				}
			}
		}

		// Kick the volcanoes into action, and fix SOME floating lava
		// a full fix for floating lava would cause infinite cascades so we uh, don't
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				for(int y = 32; y < 128; y++) {
					int ox = i + x;
					int oz = j + z;
					BlockPos oPos = new BlockPos(ox, y, oz);
					Block b = world.getBlockState(oPos).getBlock();

					if(b == Blocks.LAVA && world.getBlockState(oPos.down()).getBlock() == Blocks.AIR) {
						world.setBlockState(oPos.down(), Blocks.FLOWING_LAVA.getDefaultState(), 0);
						world.notifyBlockUpdate(oPos.down(), world.getBlockState(oPos.down()), Blocks.FLOWING_LAVA.getDefaultState(), 3);
					} else if(b == ModBlocks.volcano_core) {
						world.setBlockState(oPos, ModBlocks.volcano_core.getStateFromMeta(BlockVolcano.META_STATIC_EXTINGUISHING), 0);
						world.notifyBlockUpdate(oPos, world.getBlockState(oPos), ModBlocks.volcano_core.getDefaultState(), 3);
					}
				}
			}
		}
	}

}