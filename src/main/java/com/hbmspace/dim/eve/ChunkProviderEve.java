package com.hbmspace.dim.eve;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.ChunkProviderCelestial;
import com.hbmspace.dim.eve.biome.BiomeGenBaseEve;
import com.hbmspace.dim.noise.MapGenVNoise;
import com.hbmspace.world.gen.terrain.MapGenBubble;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class ChunkProviderEve extends ChunkProviderCelestial {

    private final NoiseGeneratorPerlin crackNoise;
    private final MapGenVNoise noise = new MapGenVNoise();

    private final MapGenBubble oil = new MapGenBubble(WorldConfigSpace.eveGasSpawn);

	public ChunkProviderEve(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);
		reclamp = false;
		stoneBlock = ModBlocksSpace.eve_rock;
		seaBlock = ModBlocks.mercury_block;
        this.crackNoise = new NoiseGeneratorPerlin(world.rand, 4);

        noise.fluidBlock = ModBlocks.mercury_block;
        noise.rockBlock = ModBlocksSpace.eve_rock;
        noise.surfBlock = ModBlocksSpace.eve_silt;
        noise.cellSize = 72;
        noise.crackSize = 2.0;
        noise.plateThickness = 35;
        noise.shapeExponent = 2.0;
        noise.plateStartY = 57;

        noise.applyToBiome = BiomeGenBaseEve.eveOcean;

        oil.block = ModBlocksSpace.ore_gas;
        oil.meta = (byte) CelestialBody.getMeta(world);
        oil.replace = ModBlocksSpace.eve_rock;
        oil.setSize(8, 16);
	}

    @Override
    public ChunkPrimer getChunkPrimer(int x, int z) {
        ChunkPrimer primer = super.getChunkPrimer(x, z);

        boolean hasOcean = false;
        boolean hasSeismic = false;

        for (Biome biome : biomesForGeneration) {
            if (biome == BiomeGenBaseEve.eveOcean) hasOcean = true;
            if (biome == BiomeGenBaseEve.eveSeismicPlains) hasSeismic = true;
            if (hasOcean && hasSeismic) break;
        }

        if (hasSeismic) generateCracks(x, z, primer);
        if (hasOcean) noise.generate(worldObj, x, z, primer);

        oil.generate(worldObj, x, z, primer);

        return primer;
    }

    private void generateCracks(int chunkX, int chunkZ, ChunkPrimer primer) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (this.biomesForGeneration[x + z * 16] != BiomeGenBaseEve.eveSeismicPlains) continue;

                double crackValue = this.crackNoise.getValue((chunkX * 16 + x) * 0.3D, (chunkZ * 16 + z) * 0.3D);

                if (crackValue > 0.9D) {
                    int bedrockY = -1;

                    for (int y = 0; y < 256; y++) {
                        IBlockState state = primer.getBlockState(x, y, z);

                        if (state.getBlock() == Blocks.BEDROCK) {
                            if (bedrockY == -1) bedrockY = y;
                        } else {
                            primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
                        }
                    }

                    if (bedrockY != -1) {
                        int maxY = Math.min(bedrockY + 10, 256);
                        for (int y = bedrockY + 1; y < maxY; y++) {
                            primer.setBlockState(x, y, z, Blocks.LAVA.getDefaultState());
                        }
                    }
                }
            }
        }
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