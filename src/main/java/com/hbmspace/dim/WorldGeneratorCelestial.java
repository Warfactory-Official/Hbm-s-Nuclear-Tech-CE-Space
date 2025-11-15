package com.hbmspace.dim;

import com.google.common.base.Predicate;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.config.WorldConfig;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.items.ModItems;
import com.hbm.main.MainRegistry;
import com.hbm.world.feature.BedrockOre;
import com.hbm.world.feature.DepthDeposit;
import com.hbm.world.generator.CellularDungeonFactory;
import com.hbm.world.generator.DungeonToolbox;
import com.hbm.world.generator.MeteorDungeonStructure;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.laythe.biome.BiomeGenBaseLaythe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Random;

public class WorldGeneratorCelestial implements IWorldGenerator {

    public WorldGeneratorCelestial() {
        // Specify which ores spawn where
        BlockOre.addAllBodies(ModBlocksSpace.ore_iron);
        BlockOre.addAllBodies(ModBlocksSpace.ore_gold);
        BlockOre.addAllBodies(ModBlocksSpace.ore_diamond);
        BlockOre.addAllBodies(ModBlocksSpace.ore_redstone);

        BlockOre.addValidBody(ModBlocksSpace.ore_emerald, SolarSystem.Body.KERBIN);
        BlockOre.addValidBody(ModBlocksSpace.ore_lapis, SolarSystem.Body.KERBIN);

        BlockOre.addAllBodies(ModBlocksSpace.ore_uranium);
        BlockOre.addAllBodies(ModBlocksSpace.ore_thorium);
        BlockOre.addAllBodies(ModBlocksSpace.ore_titanium);
        BlockOre.addAllBodies(ModBlocksSpace.ore_sulfur);
        BlockOre.addAllBodies(ModBlocksSpace.ore_aluminium);
        BlockOre.addAllBodies(ModBlocksSpace.ore_copper);
        BlockOre.addAllBodies(ModBlocksSpace.ore_zinc);
        BlockOre.addAllBodies(ModBlocksSpace.ore_fluorite);
        BlockOre.addAllBodies(ModBlocksSpace.ore_niter);
        BlockOre.addAllBodies(ModBlocksSpace.ore_tungsten);
        BlockOre.addAllBodies(ModBlocksSpace.ore_lead);
        BlockOre.addAllBodies(ModBlocksSpace.ore_beryllium);
        BlockOre.addAllBodies(ModBlocksSpace.ore_rare);
        BlockOre.addAllBodies(ModBlocksSpace.ore_cinnabar);
        BlockOre.addAllBodies(ModBlocksSpace.ore_cobalt);
        BlockOre.addAllBodies(ModBlocksSpace.cluster_iron);
        BlockOre.addAllBodies(ModBlocksSpace.cluster_titanium);
        BlockOre.addAllBodies(ModBlocksSpace.cluster_aluminium);
        BlockOre.addAllBodies(ModBlocksSpace.cluster_copper);

        BlockOre.addValidBody(ModBlocksSpace.ore_asbestos, SolarSystem.Body.KERBIN);
        BlockOre.addValidBody(ModBlocks.ore_lignite, SolarSystem.Body.KERBIN);
        BlockOre.addValidBody(ModBlocks.ore_oil, SolarSystem.Body.KERBIN);
        BlockOre.addValidBody(ModBlocks.ore_bedrock_oil, SolarSystem.Body.KERBIN);
        BlockOre.addValidBody(ModBlocks.ore_coltan, SolarSystem.Body.KERBIN);

        BlockOre.addAllExcept(ModBlocksSpace.ore_nickel, SolarSystem.Body.KERBIN);
    }

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(!(world.provider instanceof WorldProviderCelestial celestialProvider))
            return;

        if(world.provider.getDimension() == 0)
            return;

        Block blockToReplace = celestialProvider.getStone();
        FluidStack drillAcid = celestialProvider.getBedrockAcid();
        boolean hasIce = celestialProvider.hasIce();
        int meta = CelestialBody.getMeta(world);

        generateStructures(world, rand, chunkX * 16, chunkZ * 16);

        if(blockToReplace != Blocks.STONE) {
            generateVanillaOres(world, rand, chunkX * 16, chunkZ * 16, blockToReplace, meta);
        }

        generateNTMOres(world, rand, chunkX * 16, chunkZ * 16, blockToReplace, meta);
        generateBedrockOres(world, rand, chunkX * 16, chunkZ * 16, blockToReplace, hasIce, drillAcid);
    }

    public void generateStructures(World world, Random rand, int x, int z) {
		Biome biome = world.getBiomeProvider().getBiome(new BlockPos(x, 150, z));

		if(WorldConfig.meteorStructure > 0 && rand.nextInt(WorldConfig.meteorStructure) == 0 && biome != Biomes.OCEAN && biome != Biomes.DEEP_OCEAN && biome != BiomeGenBaseLaythe.laytheOcean) {
			int px = x + rand.nextInt(16) + 8;
			int pz = z + rand.nextInt(16) + 8;

            new MeteorDungeonStructure(CellularDungeonFactory.meteor, 10).generate(world, rand, new BlockPos(px, 10, pz), true);

            if(GeneralConfig.enableDebugMode)
				MainRegistry.logger.info("[Debug] Successfully spawned meteor dungeon at " + px + " 10 " + pz);
			
			int y = world.getHeight(px, pz);
			
			for(int f = 0; f < 3; f++)
				world.setBlockState(new BlockPos(px, y + f, pz), ModBlocks.meteor_pillar.getDefaultState());
			world.setBlockState(new BlockPos(px, y + 3, pz), ModBlocks.meteor_brick_chiseled.getDefaultState());
		}
    }

    public void generateNTMOres(World world, Random rand, int x, int z, Block planetStone, int meta) {

        DepthDeposit.generateCondition(world, x, 0, 3, z, 5, 0.6D, ModBlocks.cluster_depth_iron, rand, 24, planetStone, ModBlocks.stone_depth);
        DepthDeposit.generateCondition(world, x, 0, 3, z, 5, 0.6D, ModBlocks.cluster_depth_titanium, rand, 32, planetStone, ModBlocks.stone_depth);
        DepthDeposit.generateCondition(world, x, 0, 3, z, 5, 0.6D, ModBlocks.cluster_depth_tungsten, rand, 32, planetStone, ModBlocks.stone_depth);
        DepthDeposit.generateCondition(world, x, 0, 3, z, 5, 0.8D, ModBlocks.ore_depth_cinnabar, rand, 16, planetStone, ModBlocks.stone_depth);
        DepthDeposit.generateCondition(world, x, 0, 3, z, 5, 0.8D, ModBlocks.ore_depth_zirconium, rand, 16, planetStone, ModBlocks.stone_depth);
        DepthDeposit.generateCondition(world, x, 0, 3, z, 5, 0.8D, ModBlocks.ore_depth_borax, rand, 16, planetStone, ModBlocks.stone_depth);

        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.uraniumSpawn, 5, 5, 20, ModBlocksSpace.ore_uranium.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.thoriumSpawn, 5, 5, 25, ModBlocksSpace.ore_thorium.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.titaniumSpawn, 6, 5, 30, ModBlocksSpace.ore_titanium.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.sulfurSpawn, 8, 5, 30, ModBlocksSpace.ore_sulfur.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.aluminiumSpawn, 6, 5, 40, ModBlocksSpace.ore_aluminium.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.copperSpawn, 6, 5, 45, ModBlocksSpace.ore_copper.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfigSpace.nickelSpawn, 6, 5, 10, ModBlocksSpace.ore_nickel.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfigSpace.zincSpawn, 6, 5, 32, ModBlocksSpace.ore_zinc.getStateFromMeta(meta), planetStone);
        //DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.mineralSpawn, 10, 12, 32, ModBlocks.ore_mineral, meta, planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.fluoriteSpawn, 4, 5, 45, ModBlocksSpace.ore_fluorite.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.niterSpawn, 6, 5, 30, ModBlocksSpace.ore_niter.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.tungstenSpawn, 8, 5, 30, ModBlocksSpace.ore_tungsten.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.leadSpawn, 9, 5, 30, ModBlocksSpace.ore_lead.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.berylliumSpawn, 4, 5, 30, ModBlocksSpace.ore_beryllium.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.rareSpawn, 5, 5, 20, ModBlocksSpace.ore_rare.getStateFromMeta(meta), planetStone);
        // DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.asbestosSpawn, 4, 16, 16, ModBlocks.ore_asbestos, meta, planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.cinnabarSpawn, 4, 8, 16, ModBlocksSpace.ore_cinnabar.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.cobaltSpawn, 4, 4, 8, ModBlocksSpace.ore_cobalt.getStateFromMeta(meta), planetStone);

        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.ironClusterSpawn, 6, 15, 45, ModBlocksSpace.cluster_iron.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.titaniumClusterSpawn, 6, 15, 30, ModBlocksSpace.cluster_titanium.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.aluminiumClusterSpawn, 6, 15, 35, ModBlocksSpace.cluster_aluminium.getStateFromMeta(meta), planetStone);
        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.copperClusterSpawn, 6, 15, 20, ModBlocksSpace.cluster_copper.getStateFromMeta(meta), planetStone);

        DungeonToolbox.generateOre(world, rand, x, z, WorldConfig.limestoneSpawn, 12, 25, 30, ModBlocksSpace.stone_resource.getStateFromMeta(BlockEnumsSpace.EnumStoneType.CALCIUM.ordinal()), planetStone);

        if(rand.nextInt(4) == 0) {
            int rx = x + rand.nextInt(16) + 8;
            int ry = 6 + rand.nextInt(13);
            int rz = z + rand.nextInt(16) + 8;
            IBlockState state = world.getBlockState(new BlockPos(rx, ry, rz));
            if(state.getBlock().isReplaceableOreGen(state, world, new BlockPos(rx, ry, rz), BlockMatcher.forBlock(Blocks.STONE))) {
                world.setBlockState(new BlockPos(rx, ry, rz), ModBlocks.stone_keyhole.getDefaultState());
            }
        }
    }


    public void generateBedrockOres(World world, Random rand, int x, int z, Block planetStone, boolean hasIce, FluidStack drillAcid) {
        if(rand.nextInt(10) == 0) {
            int randPosX = x + rand.nextInt(2) + 8;
            int randPosZ = z + rand.nextInt(2) + 8;
            // FIXME ig
            BedrockOre.generate(world, randPosX, randPosZ, new ItemStack(ModItems.bedrock_ore_base), drillAcid, 0xD78A16, 1, ModBlocks.stone_depth);
        } else if(hasIce && rand.nextInt(3) == 0) {
            int randPosX = x + rand.nextInt(2) + 8;
            int randPosZ = z + rand.nextInt(2) + 8;

            BedrockOre.generate(world, randPosX, randPosZ, new ItemStack(Blocks.PACKED_ICE, 8 * 4), null, 0xAAFFFF, 1);
        }
    }

    // This will generate vanilla ores for the chunk when the biome decorator fails to find any regular stone
    public void generateVanillaOres(World world, Random rand, int x, int z, Block planetStone, int meta) {
        genVanillaOre(world, rand, x, z, 0, 64, 20, 8, ModBlocksSpace.ore_iron, planetStone, meta);
        genVanillaOre(world, rand, x, z, 0, 32, 2, 8, ModBlocksSpace.ore_gold, planetStone, meta);
        genVanillaOre(world, rand, x, z, 0, 16, 8, 7, ModBlocksSpace.ore_redstone, planetStone, meta);
        genVanillaOre(world, rand, x, z, 0, 16, 1, 7, ModBlocksSpace.ore_diamond, planetStone, meta);
        // what the fuck is a lapis lazuli
        // emeralds also spawn in a special way but... like... fuck emeralds
    }

    // Called by ModEventHandler to handle vanilla ore generation events
    public static void onGenerateOre(GenerateMinable event) {
        // No coal on celestial bodies, no dead dinodoys :(
        if(event.getType() == GenerateMinable.EventType.COAL) {
            event.setResult(Event.Result.DENY);
        }
    }

    // A simple reimplementation of `genStandardOre1` without needing to instance a BiomeDecorator
    private void genVanillaOre(World world, Random rand, int x, int z, int yMin, int yMax, int count, int numberOfBlocks, Block ore, Block target, int meta) {
        IBlockState oreState = ore.getStateFromMeta(meta);
        Predicate<IBlockState> targetPredicate = BlockMatcher.forBlock(target);

        WorldGenMinable worldGenMinable = new WorldGenMinable(oreState, numberOfBlocks, targetPredicate);

        for (int l = 0; l < count; ++l) {
            int genX = x + rand.nextInt(16);
            int genY = rand.nextInt(yMax - yMin) + yMin; // millenial supremacy
            int genZ = z + rand.nextInt(16);
            worldGenMinable.generate(world, rand, new BlockPos(genX, genY, genZ));
        }
    }

}
