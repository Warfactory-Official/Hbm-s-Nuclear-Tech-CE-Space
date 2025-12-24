package com.hbmspace.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenUtil {
    // thanks mov, had to copy that just to make it static
    public static void spawnOil(World world, int x, int y, int z, int radius, Block block, int meta, Block target) {
        int r = radius;
        int r2 = r * r;
        int r22 = r2 / 2;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy * 3;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22) {
                        pos.setPos(X, Y, Z);
                        if(world.getBlockState(pos).getBlock() == target)
                            world.setBlockState(pos, block.getStateFromMeta(meta), 2 | 16);
                    }
                }
            }
        }
    }
}
