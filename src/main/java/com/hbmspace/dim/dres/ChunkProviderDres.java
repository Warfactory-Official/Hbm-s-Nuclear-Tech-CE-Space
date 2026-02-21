package com.hbmspace.dim.dres;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.mapgen.MapGenCrater;
import com.hbmspace.dim.mapgen.MapGenVanillaCaves;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ChunkProviderDres extends ChunkProviderCelestial {
	
	private final MapGenBase caveGenerator = new MapGenVanillaCaves(ModBlocksSpace.dres_rock);

	private final MapGenCrater smallCrater = new MapGenCrater(6);
	private final MapGenCrater largeCrater = new MapGenCrater(64);

	public ChunkProviderDres(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);

		smallCrater.setSize(8, 32);
		largeCrater.setSize(96, 128);

		smallCrater.regolith = largeCrater.regolith = ModBlocksSpace.dres_rock;
		smallCrater.rock = largeCrater.rock = ModBlocksSpace.dres_rock;

		stoneBlock = ModBlocksSpace.dres_rock;
	}
	
	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);

		caveGenerator.generate(worldObj, x, z, buffer);
		smallCrater.generate(worldObj, x, z, buffer);
		largeCrater.generate(worldObj, x, z, buffer);
		
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
