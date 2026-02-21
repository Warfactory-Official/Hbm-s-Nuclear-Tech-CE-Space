package com.hbmspace.dim.tekto;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.mapgen.MapGenGreg;
import com.hbmspace.dim.mapgen.MapGenVolcano;
import com.hbmspace.dim.tekto.biome.BiomeGenBaseTekto;
import com.hbmspace.world.gen.terrain.MapGenBedrockOil;
import com.hbmspace.world.gen.terrain.MapGenBubble;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class ChunkProviderTekto extends ChunkProviderCelestial {
    private final MapGenGreg caveGenV3 = new MapGenGreg();
    private final MapGenVolcano volcano = new MapGenVolcano(12);

    private final MapGenBubble tektonic = new MapGenBubble(WorldConfigSpace.tektoOilSpawn);
    private final MapGenBedrockOil bedtonic = new MapGenBedrockOil(WorldConfigSpace.tektoBedrockOilSpawn);
	
	public ChunkProviderTekto(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);
        reclamp = false; // please kill this, we spend more time calculating perlin noise than anything then skip two whole octaves of it!
        caveGenV3.stoneBlock = ModBlocks.basalt;

        volcano.setSize(8, 16);
        volcano.setMaterial(ModBlocksSpace.geysir_chloric, ModBlocksSpace.vinyl_sand);

        tektonic.block = ModBlocksSpace.ore_tekto;
        tektonic.meta = (byte) CelestialBody.getMeta(world);
        tektonic.replace = ModBlocks.basalt;
        tektonic.setSize(8, 16);

        bedtonic.replace = ModBlocks.basalt;
        bedtonic.meta = (byte) SolarSystem.Body.TEKTO.ordinal();

        stoneBlock = ModBlocks.basalt;
        seaBlock = ModBlocksSpace.ccl_block;
	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {
		ChunkPrimer buffer = super.getChunkPrimer(x, z);

		if(biomesForGeneration[0] == BiomeGenBaseTekto.vinylsands) {
			volcano.generate(worldObj, x, z, buffer);
		}

		caveGenV3.generate(worldObj, x, z, buffer);

		tektonic.generate(worldObj, x, z, buffer);
		bedtonic.generate(worldObj, x, z, buffer);

		return buffer;
	}

	@Override
	public boolean generateStructures(@NotNull Chunk chunkIn, int x, int z){return false;}
	@Override
	@Nullable
	public BlockPos getNearestStructurePos(@NotNull World worldIn, @NotNull String structureName, @NotNull BlockPos position, boolean findUnexplored){return null;}
}