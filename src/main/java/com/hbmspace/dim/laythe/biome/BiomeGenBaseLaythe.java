/*******************************************************************************
 * Copyright 2015 SteveKunG - More Planets Mod
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package com.hbmspace.dim.laythe.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.BiomeDecoratorCelestial;
import com.hbmspace.dim.BiomeGenBaseCelestial;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public abstract class BiomeGenBaseLaythe extends BiomeGenBaseCelestial {

	public static final Biome laytheIsland = new BiomeGenLaytheIslands(new BiomeProperties("Laythe Islands").setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(0.2F).setRainfall(0.2F).setWaterColor(0x5b209a));
	public static final Biome laytheOcean = new BiomeGenLaytheOcean(new BiomeProperties("Sagan Sea").setBaseHeight(-0.6F).setHeightVariation(0.01F).setTemperature(0.2F).setRainfall(0.2F));
	public static final Biome laythePolar = new BiomeGenLaythePolar(new BiomeProperties("Laythe Poles").setBaseHeight(-0.1F).setHeightVariation(0.05F).setTemperature(0.2F).setRainfall(0.2F).setWaterColor(0xC1F4FF));

	public BiomeGenBaseLaythe(BiomeProperties properties) {
		super(properties);
		properties.setWaterColor(0x5b009a);
		// no-no-no, mister fish! you won't go to your family, you will go in this ebaniy tazik blyat
		//this.waterCreatures.add(new BiomeGenBase.SpawnListEntry(EntityScutterfish.class, 10, 4, 4));

		BiomeDecoratorCelestial decorator = new BiomeDecoratorCelestial(Blocks.STONE);
		decorator.waterPlantsPerChunk = 32;
		this.decorator = decorator;
		this.decorator.generateFalls = false;
        
        this.topBlock = ModBlocksSpace.laythe_silt.getDefaultState();
        this.fillerBlock = ModBlocksSpace.laythe_silt.getDefaultState();
	}

    // copy-paste minus gravel

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            } else {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR) {
                    j = -1;
                } else if (iblockstate2.getBlock() == Blocks.STONE) {
                    if (j == -1) {
                        if (k <= 0) {
                            iblockstate = AIR;
                            iblockstate1 = STONE;
                        } else if (j1 >= i - 4 && j1 <= i + 1) {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                            if (this.getTemperature(pos.setPos(x, j1, z)) < 0.15F) {
                                iblockstate = ICE;
                            } else {
                                iblockstate = WATER;
                            }
                        }

                        j = k;

                        if (j1 >= i - 1) {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        } else if (j1 < i - 7 - k) {
                            iblockstate = AIR;
                            iblockstate1 = STONE;
                            chunkPrimerIn.setBlockState(i1, j1, l, this.topBlock);
                        } else {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }

                    } else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == Blocks.SAND && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                        }
                    }
                }
            }
        }
    }
}