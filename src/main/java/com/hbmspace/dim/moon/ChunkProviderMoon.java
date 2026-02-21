package com.hbmspace.dim.moon;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.mapgen.MapGenCrater;
import com.hbmspace.dim.mapgen.MapGenGreg;
import com.hbmspace.dim.mapgen.MapgenRavineButBased;
import com.hbmspace.world.gen.terrain.MapGenBubble;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ChunkProviderMoon extends ChunkProviderCelestial {

	private MapGenGreg caveGenV3 = new MapGenGreg();
	private MapgenRavineButBased rgen = new MapgenRavineButBased();

	private MapGenCrater smallCrater = new MapGenCrater(6);
	private MapGenCrater largeCrater = new MapGenCrater(64);

    private MapGenBubble brine = new MapGenBubble(WorldConfigSpace.munBrineSpawn);

	public ChunkProviderMoon(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);
		caveGenV3.stoneBlock = ModBlocksSpace.moon_rock;
		rgen.stoneBlock = ModBlocksSpace.moon_rock;

		smallCrater.setSize(8, 32);
		largeCrater.setSize(96, 128);

		smallCrater.regolith = largeCrater.regolith = ModBlocks.basalt;
		smallCrater.rock = largeCrater.rock = ModBlocksSpace.moon_rock;

		brine.block = ModBlocksSpace.ore_brine;
		brine.meta = (byte) CelestialBody.getMeta(world);
		brine.replace = ModBlocksSpace.moon_rock;
		brine.setSize(8, 16);

		stoneBlock = ModBlocksSpace.moon_rock;
		seaBlock = ModBlocks.basalt;
		seaLevel = 64;
	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);
		
		// NEW CAVES
		caveGenV3.generate(worldObj, x, z, buffer);
		rgen.generate(worldObj, x, z, buffer);
		smallCrater.generate(worldObj, x, z, buffer);
		largeCrater.generate(worldObj, x, z, buffer);
		brine.generate(worldObj, x, z, buffer);

		return buffer;
	}

	@Override
	public boolean generateStructures(@NotNull Chunk chunkIn, int x, int z) { return false; }
	@Override
	@Nullable
	public BlockPos getNearestStructurePos(@NotNull World worldIn, @NotNull String structureName, @NotNull BlockPos position, boolean findUnexplored) { return null; }

}
