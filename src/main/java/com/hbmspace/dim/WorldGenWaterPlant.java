package com.hbmspace.dim;

import java.util.Random;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.jetbrains.annotations.NotNull;

public class WorldGenWaterPlant extends WorldGenerator {

    public int seaLevel = 64;

    @Override
    public boolean generate(@NotNull World world, @NotNull Random rand, BlockPos position) {
        boolean flag = false;
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        for(int l = 0; l < 128; ++l) {
            int px = x + rand.nextInt(8) - rand.nextInt(8);
            int py = y + rand.nextInt(4) - rand.nextInt(4);
            int pz = z + rand.nextInt(8) - rand.nextInt(8);

            BlockPos pos = new BlockPos(px, py, pz);

            if(py < seaLevel - 1 && world.getBlockState(pos).getBlock() == Blocks.WATER
                    && world.getBlockState(pos.up()).getBlock() == Blocks.WATER && world.getBlockState(pos.down()).getBlock() == ModBlocksSpace.laythe_silt) {
                int type = rand.nextInt(7);

                switch(type) {
                    case 0:
                    case 1:
                    case 2:
                        world.setBlockState(pos, ModBlocksSpace.laythe_short.getDefaultState(), 2);
                        break;
                    case 3:
                        world.setBlockState(pos, ModBlocksSpace.laythe_glow.getDefaultState(), 2);
                        break;
                    case 4:
                    case 5:
                        if(py < seaLevel - 2 && world.getBlockState(pos.up(2)).getBlock() == Blocks.WATER) {
                            world.setBlockState(pos, ModBlocksSpace.plant_tall_laythe.getStateFromMeta(0), 2);
                            world.setBlockState(pos.up(), ModBlocksSpace.plant_tall_laythe.getStateFromMeta(8), 2);
                        }
                        break;
                    case 6:
                        if(py < seaLevel - 4) {
                            int height = 2 + rand.nextInt(Math.min(8, seaLevel - py - 2));
                            for(int h = 0; h < height; ++h) {
                                if(world.getBlockState(pos.up(h + 1)).getBlock() == Blocks.WATER) {
                                    world.setBlockState(pos.up(h), ModBlocksSpace.laythe_kelp.getStateFromMeta(0), 1);
                                }
                            }
                        }
                        break;
                }

                flag = true;
            }
        }

        return flag;
    }

}