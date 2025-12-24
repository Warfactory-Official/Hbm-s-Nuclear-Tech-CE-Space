package com.hbmspace.dim.duna;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.deco.TileEntityLanternBehemoth;
import com.hbm.util.LootGenerator;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorDuna implements IWorldGenerator {

    public WorldGeneratorDuna() {
        /*NBTStructure.registerStructure(com.hbm.config.SpaceConfig.dunaDimension, new SpawnCondition("duna_comms") {{
            structure = new JigsawPiece("duna_comms", StructureManager.duna_comms, -1);
            canSpawn = biome -> biome.heightVariation < 0.1F;
            spawnWeight = 6;
        }});*/
        NBTStructure.registerNullWeight(com.hbm.config.SpaceConfig.dunaDimension, 18);

        BlockOre.addValidBody(ModBlocks.ore_oil, SolarSystem.Body.DUNA);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == SpaceConfig.dunaDimension) {
			generateDuna(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateDuna(World world, Random rand, int i, int j) {
        int meta = CelestialBody.getMeta(world);
        Block stone = ((WorldProviderCelestial) world.provider).getStone();

        DungeonToolbox.generateOre(world, rand, i, j, WorldConfigSpace.ironSpawn, 8, 32, 64, ModBlocksSpace.ore_iron.getStateFromMeta(meta), stone);
        DungeonToolbox.generateOre(world, rand, i, j, WorldConfigSpace.zincSpawn, 9, 4, 27, ModBlocksSpace.ore_zinc.getStateFromMeta(meta), stone);

        // Basalt rich in minerals, but only in basaltic caves!
        /*DungeonToolbox.generateOre(world, rand, i, j, 12, 6, 0, 16, ModBlocksSpace.ore_basalt, 0, ModBlocks.basalt);
        DungeonToolbox.generateOre(world, rand, i, j, 8, 8, 0, 16, ModBlocksSpace.ore_basalt, 1, ModBlocks.basalt);
        DungeonToolbox.generateOre(world, rand, i, j, 8, 9, 0, 16, ModBlocksSpace.ore_basalt, 2, ModBlocks.basalt);
        DungeonToolbox.generateOre(world, rand, i, j, 2, 4, 0, 16, ModBlocksSpace.ore_basalt, 3, ModBlocks.basalt);
        DungeonToolbox.generateOre(world, rand, i, j, 6, 10, 0, 16, ModBlocksSpace.ore_basalt, 4, ModBlocks.basalt);*/


        /*if(i == 0 && j == 0 && world.getWorldInfo().getTerrainType() == WorldTypeTeleport.martian) {
            int x = 0;
            int z = 0;
            int y = world.getHeightValue(x, z) - 1;

            Spotlight.disableOnGeneration = false;
            StructureManager.martian.build(world, x, y, z);
            Spotlight.disableOnGeneration = true;
        }*/

        if (rand.nextInt(1234) == 0) {
            int x = i + rand.nextInt(16);
            int z = j + rand.nextInt(16);

            BlockPos topPos = world.getHeight(new BlockPos(x, 0, z));
            int y = topPos.getY();

            BlockPos pos = new BlockPos(x, y, z);
            BlockPos belowPos = pos.down();

            if (world.getBlockState(belowPos).getBlock().canPlaceTorchOnTop(world.getBlockState(belowPos), world, belowPos)
                    && world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {

                world.setBlockState(pos, ModBlocks.lantern_behemoth.getStateFromMeta(12), 3);
                MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] { 4, 0, 0, 0, 0, 0 }, ModBlocks.lantern_behemoth, EnumFacing.NORTH);

                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityLanternBehemoth lantern) {
                    lantern.isBroken = true;
                }

                if (rand.nextInt(2) == 0) {
                    LootGenerator.setBlock(world, x, y, z - 2);
                    LootGenerator.lootBooklet(world, x, y, z - 2);
                }

                if (GeneralConfig.enableDebugMode) {
                    MainRegistry.logger.info("[Debug] Successfully spawned lantern at " + x + " " + y + " " + z);
                }
            }
        }
	}
	
}