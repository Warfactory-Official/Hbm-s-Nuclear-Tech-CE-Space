package com.hbmspace.dim;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenWaterPlant extends WorldGenerator {

    @Override // TODO: port the plants
    public boolean generate(World world, Random rand, BlockPos origin) {
        boolean placedAny = false;/*
        int seaLevel = world.getSeaLevel();
        BlockPos.MutableBlockPos plantPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

        for (int attempt = 0; attempt < 128; ++attempt) {
            int px = origin.getX() + rand.nextInt(8) - rand.nextInt(8);
            int py = origin.getY() + rand.nextInt(4) - rand.nextInt(4);
            int pz = origin.getZ() + rand.nextInt(8) - rand.nextInt(8);
            if (py >= seaLevel - 1) continue;

            plantPos.setPos(px, py, pz);
            if (world.getBlockState(plantPos).getBlock() != Blocks.WATER) continue;

            belowPos.setPos(px, py - 1, pz);
            if (world.getBlockState(belowPos).getBlock() != ModBlocksSpace.laythe_silt) continue;

            int type = rand.nextInt(7);

            switch (type) {
                case 0:
                case 1:
                case 2: {
                    IBlockState state = ModBlocksSpace.laythe_short.getStateFromMeta(0);
                    world.setBlockState(plantPos, state, 2);
                    break;
                }
                case 3: {
                    IBlockState state = ModBlocksSpace.laythe_glow.getStateFromMeta(0);
                    world.setBlockState(plantPos, state, 2);
                    break;
                }
                case 4:
                case 5: {
                    if (py < seaLevel - 2) {
                        IBlockState bottom = ModBlocksSpace.plant_tall_laythe.getStateFromMeta(0);
                        IBlockState top = ModBlocksSpace.plant_tall_laythe.getStateFromMeta(8);
                        world.setBlockState(plantPos, bottom, 2);

                        plantPos.setPos(px, py + 1, pz);
                        world.setBlockState(plantPos, top, 2);
                        plantPos.setPos(px, py, pz);
                    }
                    break;
                }
                case 6: {
                    if (py < seaLevel - 4) {
                        int maxExtra = Math.min(8, seaLevel - py - 2);
                        if (maxExtra > 0) {
                            int height = 2 + rand.nextInt(maxExtra);
                            IBlockState kelp = ModBlocksSpace.laythe_kelp.getStateFromMeta(0);

                            for (int h = 0; h < height; ++h) {
                                plantPos.setPos(px, py + h, pz);
                                world.setBlockState(plantPos, kelp, 1);
                            }
                            plantPos.setPos(px, py, pz);
                        }
                    }
                    break;
                }
                default:
                    break;
            }

            placedAny = true;
        }*/

        return placedAny;
    }
}
