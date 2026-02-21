package com.hbmspace.world.gen.terrain;

import java.util.function.Predicate;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import org.jetbrains.annotations.NotNull;

public class MapGenBubble extends MapGenBase {

    private final int frequency;
    private int minSize = 8;
    private int maxSize = 64;

    public int minY = 0;
    public int rangeY = 25;

    public boolean fuzzy;

    public Block block;
    public int meta = 0; // Changed byte to int to match getStateFromMeta
    public Block replace = Blocks.STONE;

    public Predicate<Biome> canSpawn;

    public MapGenBubble(int frequency) {
        this.frequency = frequency;
    }

    public void setSize(int minSize, int maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;

        this.range = (maxSize / 8) + 1;
    }

    @Override
    public void recursiveGenerate(@NotNull World world, int chunkX, int chunkZ, int originalX, int originalZ, @NotNull ChunkPrimer primer) {
        if(rand.nextInt(frequency) == frequency - 1 && (canSpawn == null || canSpawn.test(world.getBiome(new BlockPos(chunkX * 16, 64, chunkZ * 16))))) {

            int xCoord = (originalX - chunkX) * 16 + rand.nextInt(16);
            int zCoord = (originalZ - chunkZ) * 16 + rand.nextInt(16);

            int yCoord = rand.nextInt(rangeY) + minY;

            double radius = rand.nextInt(maxSize - minSize) + minSize;
            double radiusSqr = (radius * radius) / 2;

            int yMin = Math.max(1, MathHelper.floor(yCoord - radius));
            int yMax = Math.min(127, MathHelper.ceil(yCoord + radius));

            for(int bx = 15; bx >= 0; bx--) // bx, bz is the coordinate of the block we're modifying
                for(int bz = 15; bz >= 0; bz--)
                    for(int by = yMin; by < yMax; by++) {

                        IBlockState currentState = primer.getBlockState(bx, by, bz);

                        if(currentState.getBlock() == replace) {
                            int x = xCoord + bx;
                            int z = zCoord + bz;
                            int y = yCoord - by;

                            double rSqr = x * x + z * z + y * y * 3;
                            if(fuzzy) rSqr -= rand.nextDouble() * radiusSqr / 3;

                            if(rSqr < radiusSqr) {
                                primer.setBlockState(bx, by, bz, block.getStateFromMeta(meta));
                            }
                        }
                    }
        }
    }

}