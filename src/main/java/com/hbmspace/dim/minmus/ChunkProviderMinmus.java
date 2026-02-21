package com.hbmspace.dim.minmus;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.mapgen.MapGenCrater;
import com.hbmspace.dim.mapgen.MapGenVanillaCaves;
import com.hbmspace.world.gen.terrain.MapGenBubble;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ChunkProviderMinmus extends ChunkProviderCelestial {
	
	private final MapGenBase caveGenerator = new MapGenVanillaCaves(ModBlocksSpace.minmus_stone).withLava(ModBlocksSpace.minmus_smooth);

	private final MapGenCrater smallCrater = new MapGenCrater(5);
	private final MapGenCrater largeCrater = new MapGenCrater(96);

    private final MapGenBubble brine = new MapGenBubble(WorldConfigSpace.minmusBrineSpawn);

	public ChunkProviderMinmus(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);

		smallCrater.setSize(6, 24);
		largeCrater.setSize(64, 128);

		smallCrater.regolith = largeCrater.regolith = ModBlocksSpace.minmus_regolith;
		smallCrater.rock = largeCrater.rock = ModBlocksSpace.minmus_stone;

		brine.block = ModBlocksSpace.ore_brine;
		brine.meta = (byte) CelestialBody.getMeta(world);
		brine.replace = ModBlocksSpace.minmus_stone;
		brine.setSize(8, 16);
		
		stoneBlock = ModBlocksSpace.minmus_stone;
		seaBlock = ModBlocksSpace.minmus_smooth;
		seaLevel = 63;
	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);

		caveGenerator.generate(worldObj, x, z, buffer);
		smallCrater.generate(worldObj, x, z, buffer);
		largeCrater.generate(worldObj, x, z, buffer);
		brine.generate(worldObj, x, z, buffer);
		
		return buffer;
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
