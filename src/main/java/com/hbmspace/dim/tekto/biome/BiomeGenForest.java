package com.hbmspace.dim.tekto.biome;

import java.util.Random;

import com.hbm.blocks.ModBlocks;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

public class BiomeGenForest extends BiomeGenBaseTekto {

    public BiomeGenForest(Biome.BiomeProperties properties) {
        super(properties);

        this.topBlock = ModBlocksSpace.rubber_grass.getDefaultState();
        this.fillerBlock = ModBlocksSpace.rubber_silt.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(@NotNull World worldIn, @NotNull Random rand, @NotNull ChunkPrimer chunkPrimerIn, int x, int z, double noise) {
        // Forest uses Basalt as base and Smooth Basalt as alternate
        this.genTektoBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noise, ModBlocks.basalt.getDefaultState(), ModBlocks.basalt_smooth.getDefaultState());
    }
}
