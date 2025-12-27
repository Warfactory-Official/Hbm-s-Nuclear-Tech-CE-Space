package com.hbmspace.dim.eve.GenLayerEve;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenEveSpike extends WorldGenerator {

    private static boolean isReplaceable(IBlockState state) {
        Block block = state.getBlock();
        return state.getMaterial() == Material.AIR || block == ModBlocksSpace.eve_silt || block == ModBlocksSpace.eve_rock;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos origin) {
        BlockPos.MutableBlockPos groundPos = new BlockPos.MutableBlockPos(origin);
        IBlockState groundState = world.getBlockState(groundPos);
        while (groundPos.getY() > 2 && groundState.getMaterial() == Material.AIR) {
            groundPos.move(EnumFacing.DOWN);
            groundState = world.getBlockState(groundPos);
        }

        if (groundState.getBlock() != ModBlocksSpace.eve_silt) {
            return false;
        }

        // 1.7: y += rand.nextInt(4)
        int baseX = groundPos.getX();
        int baseZ = groundPos.getZ();
        int baseY = groundPos.getY() + rand.nextInt(4);

        int spikeHeight = 10 + rand.nextInt(4);          // l
        int spikeRadiusBase = (spikeHeight / 4) + rand.nextInt(2); // i1

        // 1.7: if(i1 > 1 && rand.nextInt(2) == 0) y += 10 + rand.nextInt(30)
        if (rand.nextInt(2) == 0) {
            baseY += 10 + rand.nextInt(30);
        }

        IBlockState rockState = ModBlocksSpace.eve_rock.getDefaultState();
        BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos columnPos = new BlockPos.MutableBlockPos();

        for (int layer = 0; layer < spikeHeight; ++layer) {
            float radiusF = (1.0F - (float) layer / (float) spikeHeight) * (float) spikeRadiusBase;
            float radiusSq = radiusF * radiusF;
            int radius = MathHelper.ceil(radiusF);

            for (int dx = -radius; dx <= radius; ++dx) {
                float dxF = (float) MathHelper.abs(dx) - 0.25F;

                for (int dz = -radius; dz <= radius; ++dz) {
                    float dzF = (float) MathHelper.abs(dz) - 0.25F;

                    float distSq = dxF * dxF + dzF * dzF;
                    boolean inside = (dx == 0 && dz == 0) || (distSq <= radiusSq);
                    boolean onEdge = (dx == -radius || dx == radius || dz == -radius || dz == radius);

                    if (inside && (!onEdge || rand.nextFloat() <= 0.75F)) {
                        placePos.setPos(baseX + dx, baseY + layer, baseZ + dz);
                        IBlockState state = world.getBlockState(placePos);

                        if (isReplaceable(state)) {
                            world.setBlockState(placePos, rockState, 2 | 16);
                        }

                        if (layer != 0 && radius > 1) {
                            belowPos.setPos(baseX + dx, baseY - layer, baseZ + dz);
                            IBlockState belowState = world.getBlockState(belowPos);

                            if (isReplaceable(belowState)) {
                                world.setBlockState(belowPos, rockState, 2 | 16);
                            }
                        }
                    }
                }
            }
        }

        int columnRadius = spikeRadiusBase - 1;
        if (columnRadius < 0) {
            columnRadius = 0;
        } else if (columnRadius > 1) {
            columnRadius = 1;
        }

        for (int dx = -columnRadius; dx <= columnRadius; ++dx) {
            for (int dz = -columnRadius; dz <= columnRadius; ++dz) {
                int y = baseY - 1;
                int runLeft = 50;

                if (Math.abs(dx) == 1 && Math.abs(dz) == 1) {
                    runLeft = rand.nextInt(5);
                }

                while (true) {
                    if (y > 50) {
                        columnPos.setPos(baseX + dx, y, baseZ + dz);
                        IBlockState state = world.getBlockState(columnPos);
                        Block block = state.getBlock();

                        // mlbv: 1.7 had repeated "eve_silt" checks; they are redundant.
                        // semantically identically, we can replace if air OR eve_silt OR eve_rock.
                        if (block.isAir(state, world, columnPos) || block == ModBlocksSpace.eve_silt || block == ModBlocksSpace.eve_rock) {
                            world.setBlockState(columnPos, rockState, 2 | 16);

                            --y;
                            --runLeft;

                            if (runLeft <= 0) {
                                y -= rand.nextInt(5) + 1;
                                runLeft = rand.nextInt(5);
                            }

                            continue;
                        }
                    }

                    break;
                }
            }
        }

        return true;
    }
}
