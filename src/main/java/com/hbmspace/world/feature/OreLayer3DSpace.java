package com.hbmspace.world.feature;

import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class OreLayer3DSpace {
    public static int counter = 0;
    public int id;
    private NoiseGeneratorPerlin noiseX;
    private NoiseGeneratorPerlin noiseY;
    private NoiseGeneratorPerlin noiseZ;
    private double scaleH;
    private double scaleV;
    private double threshold;
    private Block block;
    private int meta;
    private int dim = 0;
    boolean allCelestials = false;

    public OreLayer3DSpace(Block block, int meta) {
        this.block = block;
        this.meta = meta;
        MinecraftForge.EVENT_BUS.register(this);
        this.id = counter++;
    }

    public OreLayer3DSpace setDimension(int dim) {
        this.dim = dim;
        return this;
    }

    // If enabled, this vein will spawn on all celestial bodies
    public OreLayer3DSpace setGlobal(boolean value) {
        this.allCelestials = value;
        return this;
    }

    public OreLayer3DSpace setScaleH(double scale) {
        this.scaleH = scale;
        return this;
    }

    public OreLayer3DSpace setScaleV(double scale) {
        this.scaleV = scale;
        return this;
    }

    public OreLayer3DSpace setThreshold(double threshold) {
        this.threshold = threshold;
        return this;
    }

    @SubscribeEvent
    public void onDecorate(DecorateBiomeEvent.Pre event) {
        World world = event.getWorld();

        if(world.provider == null) return;

        Block replace = Blocks.STONE;
        if(world.provider instanceof WorldProviderCelestial) {
            replace = ((WorldProviderCelestial)world.provider).getStone();
        }

        if(allCelestials) {
            if(!(world.provider instanceof WorldProviderCelestial) && world.provider.getDimension() != 0) return;
        } else {
            if(world.provider.getDimension() != this.dim) return;
        }

        if (this.noiseX == null) this.noiseX = new NoiseGeneratorPerlin(new Random(world.getSeed() + 101L + (long)this.id), 4);
        if (this.noiseY == null) this.noiseY = new NoiseGeneratorPerlin(new Random(world.getSeed() + 102L + (long)this.id), 4);
        if (this.noiseZ == null) this.noiseZ = new NoiseGeneratorPerlin(new Random(world.getSeed() + 103L + (long)this.id), 4);

        int cX = event.getPos().getX();
        int cZ = event.getPos().getZ();

        for(int x = cX + 8; x < cX + 24; ++x) {
            for(int z = cZ + 8; z < cZ + 24; ++z) {
                for(int y = 64; y > 5; --y) {
                    double nX = this.noiseX.getValue((double)y * this.scaleV, (double)z * this.scaleH);
                    double nY = this.noiseY.getValue((double)x * this.scaleH, (double)z * this.scaleH);
                    double nZ = this.noiseZ.getValue((double)x * this.scaleH, (double)y * this.scaleV);
                    if (nX * nY * nZ > this.threshold) {
                        BlockPos pos = new BlockPos(x, y, z);
                        IBlockState state = world.getBlockState(pos);
                        Block target = state.getBlock();
                        if (target.isNormalCube(state, world, pos) && state.getMaterial() == Material.ROCK && target.isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(replace))) {
                            world.setBlockState(pos, this.block.getStateFromMeta(this.meta), 2);
                        }
                    }
                }
            }
        }
    }
}
