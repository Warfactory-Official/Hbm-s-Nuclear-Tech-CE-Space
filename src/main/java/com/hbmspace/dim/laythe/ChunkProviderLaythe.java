package com.hbmspace.dim.laythe;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.laythe.biome.BiomeGenBaseLaythe;
import com.hbmspace.dim.mapgen.MapGenGreg;
import com.hbmspace.dim.mapgen.MapGenTiltedSpires;
import com.hbmspace.entity.mob.EntityCreeperFlesh;
import com.hbmspace.world.gen.terrain.MapGenBubble;
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

	private final MapGenGreg caveGenV3 = new MapGenGreg();
	private final MapGenTiltedSpires spires = new MapGenTiltedSpires(2, 14, 0.8F);
	private final MapGenTiltedSpires snowires = new MapGenTiltedSpires(2, 14, 0.8F);

    private final MapGenBubble oil = new MapGenBubble(WorldConfigSpace.laytheOilSpawn);

	private final List<Biome.SpawnListEntry> spawnedOfFlesh = new ArrayList<>();

	public ChunkProviderLaythe(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);


		spires.rock = Blocks.STONE;
		spires.regolith = ModBlocksSpace.laythe_silt;
        spires.curve = snowires.curve = true;
        spires.maxPoint = snowires.maxPoint = 6.0F;
        spires.maxTilt = snowires.maxTilt = 3.5F;

		oil.block = ModBlocks.ore_oil;
		oil.meta = (byte) CelestialBody.getMeta(world);
		oil.replace = Blocks.STONE;
		oil.setSize(8, 16);

		seaBlock = Blocks.WATER;

		spawnedOfFlesh.add(new Biome.SpawnListEntry(EntityCreeperFlesh.class, 10, 4, 4));
		
		snowires.rock = Blocks.PACKED_ICE;
		snowires.regolith = Blocks.SNOW;

	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);
		
		if(biomesForGeneration[0] == BiomeGenBaseLaythe.laythePolar) {
			snowires.generate(worldObj, x, z, buffer);
		} else {
			spires.generate(worldObj, x, z, buffer);
		}
		caveGenV3.generate(worldObj, x, z, buffer);
		oil.generate(worldObj, x, z, buffer);

		return buffer;
	}

	@Override
	public @NotNull List<Biome.SpawnListEntry> getPossibleCreatures(@NotNull EnumCreatureType creatureType, @NotNull BlockPos pos) {
		if(creatureType == EnumCreatureType.MONSTER && worldObj.getBlockState(pos.down()) == ModBlocksSpace.tumor)
			return spawnedOfFlesh;

		return super.getPossibleCreatures(creatureType, pos);
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