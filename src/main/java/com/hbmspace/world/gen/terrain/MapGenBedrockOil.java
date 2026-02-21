package com.hbmspace.world.gen.terrain;

import com.hbm.blocks.ModBlocks;

import com.hbm.blocks.PlantEnums;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import org.jetbrains.annotations.NotNull;

public class MapGenBedrockOil extends MapGenBase {

    /**
     * Similar to oil bubbles, but with a few more behaviours, like adding oily dirt
     * no porous stone don't @ me
     */

    private final int frequency;

    public Block block = ModBlocks.ore_bedrock_oil;
    public Block replace = Blocks.STONE;
    public int meta = 0;

    public int spotWidth = 5;
    public int spotCount = 50;
    public boolean addWillows = true;

    public MapGenBedrockOil(int frequency) {
        this.frequency = frequency;
        this.range = 4;
    }

    @Override
    public void recursiveGenerate(@NotNull World world, int chunkX, int chunkZ, int originalX, int originalZ, @NotNull ChunkPrimer primer) {
        if(rand.nextInt(frequency) == frequency - 2) {
            int xCoord = (originalX - chunkX) * 16 + rand.nextInt(16);
            int zCoord = (originalZ - chunkZ) * 16 + rand.nextInt(16);

            // Add the bedrock oil spot
            for(int bx = 15; bx >= 0; bx--) {
                for(int bz = 15; bz >= 0; bz--) {
                    for(int y = 0; y < 5; y++) {
                        IBlockState currentState = primer.getBlockState(bx, y, bz);

                        if(currentState.getBlock() == replace || currentState.getBlock() == Blocks.BEDROCK) {
                            // x, z are the coordinates relative to the target virtual chunk origin
                            int x = xCoord + bx;
                            int z = zCoord + bz;

                            if(Math.abs(x) < 5 && Math.abs(z) < 5 && Math.abs(x) + Math.abs(y) + Math.abs(z) <= 6) {
                                primer.setBlockState(bx, y, bz, block.getStateFromMeta(meta));
                            }
                        }
                    }
                }
            }

            int deadMetaCount = PlantEnums.EnumDeadPlantType.values().length;

            // Add oil spot damage
            for(int i = 0; i < spotCount; i++) {
                int rx = (int)(rand.nextGaussian() * spotWidth) - xCoord;
                int rz = (int)(rand.nextGaussian() * spotWidth) - zCoord;

                if(rx >= 0 && rx < 16 && rz >= 0 && rz < 16) {
                    // find ground level
                    for(int y = 127; y >= 0; y--) {
                        IBlockState state = primer.getBlockState(rx, y, rz);

                        if(state.getBlock() != Blocks.AIR && state.isOpaqueCube()) {
                            for(int oy = 1; oy > -3; oy--) {
                                int subY = y + oy;

                                // Prevent out-of-bounds error on chunk primer height limits
                                if(subY < 0 || subY > 255) continue;

                                IBlockState subState = primer.getBlockState(rx, subY, rz);
                                Block subBlock = subState.getBlock();

                                if(subBlock == Blocks.GRASS || subBlock == Blocks.DIRT) {
                                    primer.setBlockState(rx, subY, rz, rand.nextInt(10) == 0 ? ModBlocks.dirt_oily.getDefaultState() : ModBlocks.dirt_dead.getDefaultState());

                                    if(addWillows && oy == 0 && rand.nextInt(50) == 0 && subY + 1 < 256) {
                                        primer.setBlockState(rx, subY + 1, rz, ModBlocks.plant_flower.getStateFromMeta(PlantEnums.EnumFlowerPlantType.MUSTARD_WILLOW_0.ordinal()));
                                    }

                                    // this generation occurs BEFORE decoration, so we have no plants to modify
                                    // so we'll instead just add some new ones right now
                                    if(oy == 0 && rand.nextInt(20) == 0 && subY + 1 < 256) {
                                        primer.setBlockState(rx, subY + 1, rz, ModBlocks.plant_dead.getStateFromMeta(rand.nextInt(deadMetaCount)));
                                    }

                                    break;
                                } else if(subBlock == Blocks.SAND || subBlock == ModBlocks.ore_oil_sand) {
                                    if(subBlock.getMetaFromState(subState) == 1) {
                                        primer.setBlockState(rx, subY, rz, ModBlocks.sand_dirty_red.getDefaultState());
                                    } else {
                                        primer.setBlockState(rx, subY, rz, ModBlocks.sand_dirty.getDefaultState());
                                    }
                                    break;
                                } else if(subBlock == Blocks.STONE) {
                                    primer.setBlockState(rx, subY, rz, ModBlocks.stone_cracked.getDefaultState());
                                    break;
                                } else if(subBlock == ModBlocksSpace.rubber_silt || subBlock == ModBlocksSpace.rubber_grass || subBlock == ModBlocksSpace.vinyl_sand) {
                                    primer.setBlockState(rx, subY, rz, ModBlocks.sellafield_slaked.getDefaultState());
                                    break;
                                }
                            }

                            break;
                        }
                    }
                }
            }
        }
    }

}