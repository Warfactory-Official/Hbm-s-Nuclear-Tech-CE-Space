package com.hbmspace.dim.tekto.biome;

import java.util.Random;

import com.hbm.blocks.ModBlocks;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

public class BiomeGenVinylSands extends BiomeGenBaseTekto {

    public BiomeGenVinylSands(Biome.BiomeProperties properties) {
        super(properties);

        this.topBlock = ModBlocksSpace.vinyl_sand.getDefaultState();
        this.fillerBlock = ModBlocksSpace.vinyl_sand.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(@NotNull World worldIn, @NotNull Random rand, @NotNull ChunkPrimer chunkPrimerIn, int x, int z, double noise) {
        this.genTektoBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noise, ModBlocks.basalt.getDefaultState(), ModBlocks.basalt_smooth.getDefaultState());
    }
}
