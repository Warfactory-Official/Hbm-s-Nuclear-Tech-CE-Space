package com.hbmspace.dim.duna;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.duna.biome.BiomeGenBaseDuna;
import com.hbmspace.dim.mapgen.ExperimentalCaveGenerator;
import com.hbmspace.dim.mapgen.MapGenPlateau;
import com.hbmspace.world.gen.terrain.MapGenBubble;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ChunkProviderDuna extends ChunkProviderCelestial {

    private final ExperimentalCaveGenerator caveGenSmall = new ExperimentalCaveGenerator(2, 12, 0.12F);
	private final ExperimentalCaveGenerator caveGenV2 = new ExperimentalCaveGenerator(2, 40, 8.0F);

    private final MapGenPlateau genPlateau = new MapGenPlateau(worldObj);

    private final MapGenBubble oil = new MapGenBubble(WorldConfigSpace.dunaOilSpawn);

    public ChunkProviderDuna(World world, long seed, boolean hasMapFeatures) {
        super(world, seed, hasMapFeatures);
		stoneBlock = ModBlocksSpace.duna_rock;

        caveGenV2.lavaBlock = ModBlocks.basalt;
        caveGenV2.stoneBlock = ModBlocksSpace.duna_rock;
        caveGenSmall.lavaBlock = ModBlocksSpace.duna_sands;
        caveGenSmall.stoneBlock = ModBlocksSpace.duna_rock;

        caveGenSmall.smallCaveSize = 0.1F;

        caveGenV2.onlyBiome = BiomeGenBaseDuna.dunaLowlands;
        caveGenSmall.ignoreBiome = BiomeGenBaseDuna.dunaLowlands;

        genPlateau.surfrock = ModBlocksSpace.duna_sands;
        genPlateau.stoneBlock = ModBlocksSpace.duna_rock;
        genPlateau.fillblock = ModBlocksSpace.duna_sands;
        genPlateau.applyToBiome = BiomeGenBaseDuna.dunaHills;

        oil.block = ModBlocks.ore_oil;
        oil.meta = (byte) CelestialBody.getMeta(world);
        oil.replace = ModBlocksSpace.duna_rock;
        oil.setSize(8, 16);
    }

    @Override
    public ChunkPrimer getChunkPrimer(int x, int z) {
        ChunkPrimer primer = new ChunkPrimer();

        generateBlocks(x, z, primer);

        biomesForGeneration = worldObj.getBiomeProvider()
                .getBiomesForGeneration(biomesForGeneration, x * 16, z * 16, 16, 16);

        boolean hasLowlands = false;
        boolean hasNotLowlands = false;
        boolean hasPlateau = false;

        for (Biome biome : biomesForGeneration) {
            if (biome == BiomeGenBaseDuna.dunaLowlands) hasLowlands = true;
            else hasNotLowlands = true;

            if (biome == BiomeGenBaseDuna.dunaHills) hasPlateau = true;

            if (hasLowlands && hasNotLowlands && hasPlateau) break;
        }

        // Pre-biome blocks
        if (hasPlateau) {
            genPlateau.generate(worldObj, x, z, primer);
        }

        replaceBlocksForBiome(x, z, primer, biomesForGeneration);

        // Post-biome blocks
        if (hasLowlands) {
            caveGenV2.generate(worldObj, x, z, primer);
        }
        if (hasNotLowlands) {
            caveGenSmall.generate(worldObj, x, z, primer);
        }

        oil.generate(worldObj, x, z, primer);

        return primer;
    }

    @Override
    public boolean generateStructures(@NotNull Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    @Nullable
    public BlockPos getNearestStructurePos(@NotNull World worldIn, @NotNull String structureName, @NotNull BlockPos position, boolean findUnexplored) {
        return null;
    }
}