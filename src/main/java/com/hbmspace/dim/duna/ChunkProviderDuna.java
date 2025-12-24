package com.hbmspace.dim.duna;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.WorldConfig;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.duna.biome.BiomeGenBaseDuna;
import com.hbmspace.dim.mapgen.ExperimentalCaveGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nullable;

public class ChunkProviderDuna extends ChunkProviderCelestial {

    private ExperimentalCaveGenerator caveGenSmall = new ExperimentalCaveGenerator(2, 12, 0.12F);
	private ExperimentalCaveGenerator caveGenV2 = new ExperimentalCaveGenerator(2, 40, 8.0F);
    // TODO
    /*private MapGenPlateau genPlateau = new MapGenPlateau(worldObj);

    private MapGenBubble oil = new MapGenBubble(WorldConfig.dunaOilSpawn);*/

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

        /*genPlateau.surfrock = ModBlocksSpace.duna_sands;
        genPlateau.stoneBlock = ModBlocksSpace.duna_rock;
        genPlateau.fillblock = ModBlocksSpace.duna_sands;
        genPlateau.applyToBiome = BiomeGenBaseDuna.dunaHills;

        oil.block = ModBlocks.ore_oil;
        oil.meta = (byte) CelestialBody.getMeta(world);
        oil.replace = ModBlocksSpace.duna_rock;
        oil.setSize(8, 16);*/
    }

    @Override
    public ChunkPrimer getChunkPrimer(int x, int z) {
        ChunkPrimer primer = new ChunkPrimer();

        generateBlocks(x, z, primer);

        this.biomesForGeneration = this.worldObj.getBiomeProvider()
                .getBiomesForGeneration(this.biomesForGeneration, x * 16, z * 16, 16, 16);

        boolean hasLowlands = false;
        boolean hasNotLowlands = false;
        boolean hasPlateau = false;

        for (int i = 0; i < this.biomesForGeneration.length; i++) {
            if (this.biomesForGeneration[i] == BiomeGenBaseDuna.dunaLowlands) hasLowlands = true;
            else hasNotLowlands = true;

            if (this.biomesForGeneration[i] == BiomeGenBaseDuna.dunaHills) hasPlateau = true;

            if (hasLowlands && hasNotLowlands && hasPlateau) break;
        }

        // Pre-biome blocks
        if (hasPlateau) {
            //this.genPlateau.generate(this.world, x, z, primer);
        }

        replaceBlocksForBiome(x, z, primer, this.biomesForGeneration);

        // Post-biome blocks
        if (hasLowlands) {
            this.caveGenV2.generate(this.worldObj, x, z, primer);
        }
        if (hasNotLowlands) {
            this.caveGenSmall.generate(this.worldObj, x, z, primer);
        }

        //this.oil.generate(this.world, x, z, primer);

        return primer;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z){return false;}
    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored){return null;}
    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z){};
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos){return false;}

}