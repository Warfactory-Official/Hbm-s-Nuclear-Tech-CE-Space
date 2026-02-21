package com.hbmspace.dim.tekto;

import java.util.Random;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.block.BlockLog.LOG_AXIS;

public class TTree extends WorldGenAbstractTree {

    int offset;
    int smallest;
    int tallest;
    int xz;
    int y;
    boolean vines;
    Block logBlock;
    Block leavBlock;

    public TTree(boolean notify, int offset, int smallest, int tallest, int xz, int y, boolean vines, Block log, Block leaf) {
        super(notify);
        this.offset = offset;
        this.smallest = smallest;
        this.tallest = tallest;
        this.xz = xz;
        this.y = y;
        this.vines = vines;
        this.logBlock = log;
        this.leavBlock = leaf;
    }

    @Override
    public boolean generate(@NotNull World world, Random rand, BlockPos position) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        int height = rand.nextInt(smallest) + tallest;

        if (y < 1 || y + height + 1 > 256) {
            return false;
        }

        Block blockBelow = world.getBlockState(position.down()).getBlock();
        if (blockBelow != ModBlocksSpace.rubber_grass && blockBelow != ModBlocksSpace.rubber_silt) {
            return false;
        }

        // Generate log
        for (int i = 0; i < height; i++) {
            this.setBlockAndNotifyAdequately(world, position.up(i), logBlock.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        }

        int bulbStartY = y + height - offset;
        int bulbRadiusXz = xz;
        int bulbRadiusY = this.y;

        for (int dy = -bulbRadiusY; dy <= bulbRadiusY; dy++) {
            for (int dx = -bulbRadiusXz; dx <= bulbRadiusXz; dx++) {
                for (int dz = -bulbRadiusXz; dz <= bulbRadiusXz; dz++) {

                    // Ellipsoid check
                    if (Math.pow(dx / (double)bulbRadiusXz, 2) + Math.pow(dy / (double)bulbRadiusY, 2) + Math.pow(dz / (double)bulbRadiusXz, 2) <= 1) {

                        BlockPos leafPos = new BlockPos(x + dx, bulbStartY + dy, z + dz);
                        IBlockState state = world.getBlockState(leafPos);

                        // Check if space is valid for leaves (Air or replaceable)
                        if (state.getBlock().isAir(state, world, leafPos) || state.getBlock().isLeaves(state, world, leafPos)) {

                            this.setBlockAndNotifyAdequately(world, leafPos, leavBlock.getDefaultState());

                            if (vines) {
                                for (int i = 0; i < 4; i++) {
                                    int vineX = x + dx + (i % 2 == 0 ? (i - 1) : 0);
                                    int vineZ = z + dz + (i % 2 == 1 ? (i - 2) : 0);

                                    if (rand.nextInt(12) == 0) {
                                        for (int j = 0; j < rand.nextInt(15); j++) {
                                            int vineY = bulbStartY + dy - j;
                                            BlockPos vinePos = new BlockPos(vineX, vineY, vineZ);

                                            if (world.isAirBlock(vinePos)) {
                                                int meta = 0;

                                                if (vineX > x + dx) {
                                                    meta = 2;
                                                } else if (vineX < x + dx) {
                                                    meta = 8;
                                                } else if (vineZ > z + dz) {
                                                    meta = 4;
                                                } else if (vineZ < z + dz) {
                                                    meta = 1;
                                                }

                                                this.setBlockAndNotifyAdequately(world, vinePos, ModBlocksSpace.vinyl_vines.getStateFromMeta(meta));
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

}
