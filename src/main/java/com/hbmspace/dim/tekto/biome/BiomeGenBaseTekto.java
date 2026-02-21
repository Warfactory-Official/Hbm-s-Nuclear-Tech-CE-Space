

package com.hbmspace.dim.tekto.biome;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.BiomeDecoratorCelestial;
import com.hbmspace.dim.BiomeGenBaseCelestial;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public abstract class BiomeGenBaseTekto extends BiomeGenBaseCelestial {

	public static final Biome polyvinylPlains = new BiomeGenPolyvinylPlains(
			new Biome.BiomeProperties("Polyvinyl Plains").setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(1.0F).setRainfall(0.5F).setWaterColor(0x5b009a));

	public static final Biome halogenHills = new BiomeGenHalogenHills(
			new Biome.BiomeProperties("Halogen Hills").setBaseHeight(0.525F).setHeightVariation(0.4F).setTemperature(1.0F).setRainfall(0.5F).setWaterColor(0x5b009a));

	public static final Biome tetrachloricRiver = new BiomeGenTetrachloricRiver(
			new Biome.BiomeProperties("Tetrachloride River").setBaseHeight(-0.7F).setHeightVariation(0.0F).setTemperature(1.0F).setRainfall(0.5F).setWaterColor(0x5b009a));

	public static final Biome forest = new BiomeGenForest(
			new Biome.BiomeProperties("Tekto Forest").setBaseHeight(0.425F).setHeightVariation(0.3F).setTemperature(1.0F).setRainfall(0.5F).setWaterColor(0x5b009a));

	public static final Biome vinylsands = new BiomeGenVinylSands(
			new Biome.BiomeProperties("Vinyl Desert").setBaseHeight(0.425F).setHeightVariation(0.3F).setTemperature(1.0F).setRainfall(0.5F).setWaterColor(0x5b009a));

	public BiomeGenBaseTekto(Biome.BiomeProperties properties) {
		super(properties);

		BiomeDecoratorCelestial decorator = new BiomeDecoratorCelestial(ModBlocks.basalt);
		decorator.rubberPlantsPerChunk = 64;
		this.decorator = decorator;
		this.decorator.generateFalls = false;

		this.topBlock = ModBlocksSpace.vinyl_sand.getDefaultState();
		this.fillerBlock = ModBlocksSpace.vinyl_sand.getDefaultState();
	}

	protected void genTektoBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noise, IBlockState baseStone, IBlockState altStone) {
		int i = x & 15;
		int j = z & 15;

		IBlockState top = this.topBlock;
		IBlockState filler = this.fillerBlock;

		int k = -1;
		int l = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D);

		for (int y = 255; y >= 0; --y) {
			if (y <= rand.nextInt(5)) {
				chunkPrimerIn.setBlockState(i, y, j, Blocks.BEDROCK.getDefaultState());
			} else {
				IBlockState currentState = chunkPrimerIn.getBlockState(i, y, j);

				if (currentState.getMaterial() != Material.AIR) {
					if (currentState.getBlock() == baseStone.getBlock()) {
						if (k == -1) {
							if (l <= 0) {
								top = Blocks.AIR.getDefaultState();
								filler = baseStone;
							} else if (y >= 59 && y <= 64) {
								top = this.topBlock;
								filler = this.fillerBlock;
							}

							if (y < 63 && (top.getMaterial() == Material.AIR)) {
                                top = this.topBlock;
                            }

							k = l;

							if (y >= 62) {
								chunkPrimerIn.setBlockState(i, y, j, top);
							} else {
								top = Blocks.AIR.getDefaultState();
								filler = baseStone;
								if (Math.random() > 0.4) {
									chunkPrimerIn.setBlockState(i, y, j, baseStone);
								} else {
									chunkPrimerIn.setBlockState(i, y, j, altStone);
								}
							}
						} else if (k > 0) {
							--k;
							chunkPrimerIn.setBlockState(i, y, j, filler);

							if (k == 0 && filler.getBlock() == Blocks.SAND) {
								k = rand.nextInt(4) + Math.max(0, y - 63);
								filler = Blocks.SANDSTONE.getDefaultState();
							}
						}
					}
				} else {
					k = -1;
				}
			}
		}
	}
}