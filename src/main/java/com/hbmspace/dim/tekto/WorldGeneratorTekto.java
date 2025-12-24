package com.hbmspace.dim.tekto;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.WorldConfig;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorTekto implements IWorldGenerator {

    public WorldGeneratorTekto() {
        NBTStructure.registerNullWeight(SpaceConfig.tektoDimension, 24);

        //BlockOre.addValidBody(ModBlocksSpace.ore_tekto, SolarSystem.Body.TEKTO);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.tektoDimension) {
			generateTekto(world, random, chunkX * 16, chunkZ * 16);
		}
	}

    private void generateTekto(World world, Random rand, int i, int j) {
        int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();

        DungeonToolbox.generateOre(world, rand, i, j, WorldConfig.cobaltSpawn * 2, 6, 4, 8, ModBlocks.ore_cobalt.getStateFromMeta(meta), stone);

        Biome biome = world.getBiome(new BlockPos(i + 16, 0, j + 16));
        // TODO
        /*for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 32; y < 128; y++) {
                    int ox = i + x + 8;
                    int oz = j + z + 8;

                    BlockPos pos = new BlockPos(ox, y, oz);
                    IBlockState state = world.getBlockState(pos);

                    if (state.getBlock() == ModBlocks.geysir_chloric) {
                        world.setBlockState(pos, ModBlocks.geysir_chloric.getDefaultState(), 2);
                        world.notifyBlockUpdate(pos, state, ModBlocks.geysir_chloric.getDefaultState(), 3);
                    }
                }
            }
        }

        if (biome == BiomeGenBaseTekto.polyvinylPlains) {
            for (int o = 0; o < 2; o++) {
                if (rand.nextInt(10) == 0) {
                    int x = i + rand.nextInt(16) + 8;
                    int z = j + rand.nextInt(16) + 8;
                    int y = world.getHeight(x, z);

                    WorldGenAbstractTree customTreeGen =
                            new TTree(false, 4, 2, 10, 2, 4, false, ModBlocksSpace.pvc_log, ModBlocksSpace.rubber_leaves);
                    customTreeGen.generate(world, rand, new BlockPos(x, y, z));
                }

                if (rand.nextInt(8) == 0) {
                    int x = i + rand.nextInt(16) + 8;
                    int z = j + rand.nextInt(16) + 8;
                    int y = world.getHeight(x, z);

                    WorldGenAbstractTree chopped =
                            new TTree(false, 2, 4, 5, 3, 2, false, ModBlocksSpace.vinyl_log, ModBlocksSpace.pet_leaves);
                    chopped.generate(world, rand, new BlockPos(x, y, z));
                }
            }
        }

        if (biome == BiomeGenBaseTekto.halogenHills) {
            if (rand.nextInt(12) == 0) {
                for (int o = 0; o < 4; o++) {
                    int x = i + rand.nextInt(16) + 8;
                    int z = j + rand.nextInt(16) + 8;
                    int y = world.getHeight(x, z);

                    WorldGenAbstractTree customTreeGen =
                            new TTree(false, 3, 2, 14, 3, 3, false, ModBlocksSpace.pvc_log, ModBlocksSpace.rubber_leaves);
                    customTreeGen.generate(world, rand, new BlockPos(x, y, z));
                }
            }
        }

        if (biome == BiomeGenBaseTekto.forest) {
            for (int o = 0; o < 8; o++) {
                int x = i + rand.nextInt(16) + 8;
                int z = j + rand.nextInt(16) + 8;
                int y = world.getHeight(x, z);

                if (rand.nextInt(2) == 0) {
                    WorldGenAbstractTree customTreeGen =
                            new TTree(false, 3, 2, 20, 3, 5, true, ModBlocksSpace.pvc_log, ModBlocksSpace.rubber_leaves);
                    customTreeGen.generate(world, rand, new BlockPos(x, y, z));
                } else {
                    WorldGenAbstractTree tustomTreeGen =
                            new TTree(false, 3, 1, 1, 3, 5, false, ModBlocksSpace.pvc_log, ModBlocksSpace.rubber_leaves);
                    tustomTreeGen.generate(world, rand, new BlockPos(x, y, z));
                }
            }
        }*/
    }
}
