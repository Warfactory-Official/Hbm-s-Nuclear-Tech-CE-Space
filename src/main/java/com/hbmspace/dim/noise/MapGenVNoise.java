package com.hbmspace.dim.noise;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MapGenVNoise extends MapGenBase {

    public Block fluidBlock;
    public Block surfBlock;
    public Block rockBlock;
    public int cellSize = 32;
    public double crackSize = 2.0;
    public int plateStartY = 55;
    public int plateThickness = 35;
    public double shapeExponent = 2.0;

    public Biome applyToBiome;

    @Override
    public void generate(World worldIn, int chunkX, int chunkZ, @NotNull ChunkPrimer primer) {
        long seed = worldIn.getSeed();
        int startX = chunkX * 16;
        int startZ = chunkZ * 16;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {

                if (applyToBiome != null) {
                    pos.setPos(startX + lx, 0, startZ + lz);
                    Biome biome = worldIn.getBiome(pos);
                    if (biome != applyToBiome) continue;
                }

                int gx = startX + lx;
                int gz = startZ + lz;

                double d0 = Double.MAX_VALUE, d1 = Double.MAX_VALUE;

                int cellX = gx / cellSize;
                int cellZ = gz / cellSize;

                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        double[] center = getCellCenter(cellX + dx, cellZ + dz, seed);
                        double dist = distance(gx, gz, center[0], center[1]);

                        if (dist < d0) {
                            d1 = d0;
                            d0 = dist;
                        } else if (dist < d1) {
                            d1 = dist;
                        }
                    }
                }

                double edge = d1 - d0;
                if (edge > crackSize) {
                    double alpha = (edge - crackSize) / (cellSize - crackSize);
                    alpha = Math.max(0.0, Math.min(1.0, alpha));
                    alpha = Math.pow(alpha, shapeExponent);

                    int topY = plateStartY + (int) (plateThickness * alpha);
                    if (topY <= plateStartY) continue;

                    if (topY > 256) topY = 256;

                    for (int y = plateStartY; y < topY; y++) {
                        if (y < 0) continue;

                        IBlockState state = primer.getBlockState(lx, y, lz);
                        Block b = state.getBlock();

                        if (b == Blocks.AIR || b == fluidBlock) {
                            primer.setBlockState(lx, y, lz, rockBlock.getDefaultState());
                        }
                    }

                    int surfY = topY - 1;
                    if (surfY >= 0) {
                        primer.setBlockState(lx, surfY, lz, surfBlock.getDefaultState());
                    }
                }
            }
        }
    }

    private double[] getCellCenter(int cellX, int cellZ, long worldSeed) {
        long hash = (long) cellX * 341873128712L + (long) cellZ * 132897987541L + worldSeed;
        Random rand = new Random(hash);

        double offsetX = (rand.nextDouble() - 0.5D) * cellSize;
        double offsetZ = (rand.nextDouble() - 0.5D) * cellSize;

        double centerX = cellX * (double) cellSize + cellSize / 2.0D + offsetX;
        double centerZ = cellZ * (double) cellSize + cellSize / 2.0D + offsetZ;

        return new double[] { centerX, centerZ };
    }

    private double distance(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }
}
