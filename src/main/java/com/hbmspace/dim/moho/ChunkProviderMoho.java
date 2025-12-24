package com.hbmspace.dim.moho;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.mapgen.ExperimentalCaveGenerator;
import com.hbmspace.dim.mapgen.MapGenCrater;
import com.hbmspace.dim.mapgen.MapGenVolcano;
import com.hbmspace.dim.mapgen.MapgenRavineButBased;
import com.hbmspace.dim.moho.biome.BiomeGenBaseMoho;
import com.hbmspace.dim.noise.MapGenVNoise;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nullable;

public class ChunkProviderMoho extends ChunkProviderCelestial {

	private ExperimentalCaveGenerator caveGenV2 = new ExperimentalCaveGenerator(1, 52, 10.0F);
	private MapgenRavineButBased rgen = new MapgenRavineButBased();

    private MapGenVNoise noise = new MapGenVNoise();
	private MapGenCrater smallCrater = new MapGenCrater(6);
	private MapGenCrater largeCrater = new MapGenCrater(64);
	private MapGenVolcano volcano = new MapGenVolcano(72);
    //private MapGenPlateau plateau = new MapGenPlateau(worldObj);

	public ChunkProviderMoho(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);

		smallCrater.setSize(8, 32);
		largeCrater.setSize(96, 128);
		volcano.setSize(64, 128);

		smallCrater.regolith = largeCrater.regolith = ModBlocksSpace.moho_regolith;
		smallCrater.rock = largeCrater.rock = ModBlocksSpace.moho_stone;

		caveGenV2.stoneBlock = ModBlocksSpace.moho_stone;
		rgen.stoneBlock = ModBlocksSpace.moho_stone;
		stoneBlock = ModBlocksSpace.moho_stone;
		seaBlock = Blocks.LAVA;

        noise.surfBlock = ModBlocksSpace.moho_stone;
        noise.rockBlock = ModBlocksSpace.moho_stone;
        noise.fluidBlock = Blocks.LAVA;
        noise.crackSize = 0.5;
        noise.cellSize = 27;
        noise.plateStartY = 62;
        noise.plateThickness = 25;
        noise.applyToBiome = BiomeGenBaseMoho.mohoLavaSea;

        /*plateau.maxPlateauAddition = 6;
		plateau.surfrock = ModBlocks.moho_regolith;
		plateau.stoneBlock = ModBlocks.moho_stone;
		plateau.fillblock = Blocks.lava;
		plateau.maxPlateauAddition = 6;
		plateau.stepHeight = 2;
		plateau.noiseScale = 0.03;
		plateau.applyToBiome = BiomeGenBaseMoho.mohoPlateau;*/
	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);

		// how many times do I gotta say BEEEEG
		caveGenV2.generate(worldObj, x, z, buffer);
		rgen.generate(worldObj, x, z, buffer);
		smallCrater.generate(worldObj, x, z, buffer);
		largeCrater.generate(worldObj, x, z, buffer);
		volcano.generate(worldObj, x, z, buffer);

		return buffer;
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
