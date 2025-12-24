package com.hbmspace.dim.Ike;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.WorldConfig;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.mapgen.MapGenCrater;
import com.hbmspace.dim.mapgen.MapGenTiltedSpires;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;

import javax.annotation.Nullable;
import java.util.List;

public class ChunkProviderIke extends ChunkProviderCelestial {
	
	private MapGenBase caveGenerator = new MapGenCaves();
	private MapGenTiltedSpires spires = new MapGenTiltedSpires(6, 6, 0F);

    //private MapGenBubble brine = new MapGenBubble(WorldConfig.ikeBrineSpawn);
    private MapGenCrater sellafield = new MapGenCrater(WorldConfig.radfreq / 10);

	public ChunkProviderIke(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);

		spires.rock = ModBlocksSpace.ike_stone;
		spires.regolith = ModBlocksSpace.ike_regolith;
		spires.mid = 86;

        /*brine.block = ModBlocks.ore_brine;
        brine.meta = (byte)CelestialBody.getMeta(world);
        brine.replace = ModBlocks.ike_stone;
        brine.setSize(8, 16);*/

        sellafield.regolith = ModBlocks.sellafield;
        sellafield.rock = ModBlocks.sellafield_slaked;

		stoneBlock = ModBlocksSpace.ike_stone;
	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);

		spires.generate(worldObj, x, z, buffer);
		caveGenerator.generate(worldObj, x, z, buffer);
        sellafield.generate(worldObj, x, z, buffer);
		
		return buffer;
	}

	// man fuck Ike, why you gotta be spawning shit again
	@SuppressWarnings("rawtypes")
	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return null;
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
