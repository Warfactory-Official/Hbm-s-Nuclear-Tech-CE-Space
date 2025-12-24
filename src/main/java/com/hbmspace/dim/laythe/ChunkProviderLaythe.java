package com.hbmspace.dim.laythe;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.laythe.biome.BiomeGenBaseLaythe;
import com.hbmspace.dim.mapgen.MapGenGreg;
import com.hbmspace.dim.mapgen.MapGenTiltedSpires;
import com.hbmspace.entity.mob.EntityCreeperFlesh;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChunkProviderLaythe extends ChunkProviderCelestial {

	private MapGenGreg caveGenV3 = new MapGenGreg();
	private MapGenTiltedSpires spires = new MapGenTiltedSpires(2, 14, 0.8F);
	private MapGenTiltedSpires snowires = new MapGenTiltedSpires(2, 14, 0.8F);

    // private MapGenBubble oil = new MapGenBubble(WorldConfig.laytheOilSpawn);

	private List<Biome.SpawnListEntry> spawnedOfFlesh = new ArrayList<>();

	public ChunkProviderLaythe(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);


		spires.rock = Blocks.STONE;
		spires.regolith = ModBlocksSpace.laythe_silt;
        spires.curve = snowires.curve = true;
        spires.maxPoint = snowires.maxPoint = 6.0F;
        spires.maxTilt = snowires.maxTilt = 3.5F;

		seaBlock = Blocks.WATER;

		spawnedOfFlesh.add(new Biome.SpawnListEntry(EntityCreeperFlesh.class, 10, 4, 4));
		
		snowires.rock = Blocks.PACKED_ICE;
		snowires.regolith = Blocks.SNOW;

	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);
		
		spires.generate(worldObj, x, z, buffer);
		caveGenV3.generate(worldObj, x, z, buffer);
		if(biomesForGeneration[0] == BiomeGenBaseLaythe.laythePolar) {
			snowires.generate(worldObj, x, z, buffer);
		}

		return buffer;
	}

	@Override
	public @NotNull List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		if(creatureType == EnumCreatureType.MONSTER && worldObj.getBlockState(pos.down()) == ModBlocksSpace.tumor)
			return spawnedOfFlesh;

		return super.getPossibleCreatures(creatureType, pos);
	}

	@Override
	public boolean generateStructures(@NotNull Chunk chunkIn, int x, int z){return false;}
	@Override
	@Nullable
	public BlockPos getNearestStructurePos(@NotNull World worldIn, @NotNull String structureName, @NotNull BlockPos position, boolean findUnexplored){return null;}

}