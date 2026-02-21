package com.hbmspace.dim;

import java.util.Random;

import com.hbmspace.blocks.generic.BlockRubberCacti.EnumBushType;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.tekto.biome.BiomeGenBaseTekto;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.jetbrains.annotations.NotNull;

public class WorldGenRubberPlant extends WorldGenerator {

    @Override
    public boolean generate(World world, @NotNull Random rand, @NotNull BlockPos position) {
        boolean flag = false;
        Biome currentBiome = world.getBiome(position);

        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        for (int l = 0; l < 64; ++l) {
            int px = x + rand.nextInt(8) - rand.nextInt(8);
            int py = y + rand.nextInt(4) - rand.nextInt(4);
            int pz = z + rand.nextInt(8) - rand.nextInt(8);

            BlockPos pos = new BlockPos(px, py, pz);

            if ((world.provider.hasSkyLight() || py < 254) && world.getBlockState(pos.down()).getBlock() == ModBlocksSpace.rubber_grass) {

                int plantType = rand.nextInt(3);  // 0, 1, or 2

                world.setBlockState(pos, ModBlocksSpace.rubber_plant.getStateFromMeta(plantType), 2);
                flag = true;

            } else if ((world.provider.hasSkyLight() || py < 254) && world.getBlockState(pos.down()).getBlock() == ModBlocksSpace.vinyl_sand) {
                if(rand.nextInt(64) == 0) {
                    BlockPos topPos = world.getTopSolidOrLiquidBlock(new BlockPos(px, 0, pz));
                    world.setBlockState(topPos, ModBlocksSpace.spike_cacti.getStateFromMeta(EnumBushType.CACT.ordinal()), 2);
                }
            }

            if (currentBiome == BiomeGenBaseTekto.forest) {
                if ((world.provider.hasSkyLight() || py < 254) && world.getBlockState(pos.down()).getBlock() == ModBlocksSpace.rubber_grass) {
                    int plantType = rand.nextInt(2);

                    plantType = switch (plantType) {
                        case 0 -> EnumBushType.BUSH.ordinal();
                        case 1 -> EnumBushType.FLOWER.ordinal();
                        default -> plantType;
                    };

                    int otherPlantType = rand.nextInt(3);

                    if(rand.nextInt(4) == 0) {
                        world.setBlockState(pos, ModBlocksSpace.spike_cacti.getStateFromMeta(plantType), 2);
                    } else {
                        world.setBlockState(pos, ModBlocksSpace.rubber_plant.getStateFromMeta(otherPlantType), 2);

                    }
                    if(rand.nextInt(10) == 0) {
                        world.setBlockState(pos, ModBlocksSpace.spike_cacti.getStateFromMeta(EnumBushType.CACT.ordinal()), 2);
                    }
                    flag = true;
                }
            }
        }

        return flag;
    }

}