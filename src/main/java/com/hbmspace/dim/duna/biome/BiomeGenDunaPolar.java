package com.hbmspace.dim.duna.biome;

import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenDunaPolar extends BiomeGenBaseDuna {

    public BiomeGenDunaPolar(BiomeProperties properties) {
        super(properties);
        this.topBlock = Blocks.SNOW.getDefaultState();
        this.fillerBlock = Blocks.SNOW.getDefaultState();
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        IBlockState topState;
        IBlockState fillerState = this.fillerBlock;

        int remainingDepth = -1; // k in 1.7
        int surfaceDepth = (int) (noise / 6.0D + 6.0D + rand.nextDouble() * 0.85D); // l in 1.7

        int localX = x & 15;
        int localZ = z & 15;
        int seaLevel = world.getSeaLevel();
        int gravelBelowY = (seaLevel - 8) - surfaceDepth;

        BlockPos.MutableBlockPos tempPos = new BlockPos.MutableBlockPos();

        for (int y = 255; y >= 0; --y) {

            // 1.7 bedrock floor
            if (y <= rand.nextInt(5)) {
                primer.setBlockState(localX, y, localZ, Blocks.BEDROCK.getDefaultState());
                continue;
            }

            IBlockState state = primer.getBlockState(localX, y, localZ);

            if (state.getBlock() == Blocks.AIR) {
                remainingDepth = -1;
                continue;
            }
            if (state.getBlock() != ModBlocksSpace.duna_rock) {
                continue;
            }

            if (remainingDepth == -1) {
                topState = this.topBlock;
                fillerState = this.fillerBlock;

                if (surfaceDepth <= 0) {
                    // 1.7: block = null (air), block1 = duna_rock
                    topState = Blocks.AIR.getDefaultState();
                    fillerState = ModBlocksSpace.duna_rock.getDefaultState();
                } else if (y >= seaLevel - 5 && y <= seaLevel) { // 59..64 when seaLevel=64
                    topState = this.topBlock;
                    fillerState = this.fillerBlock;
                }

                if (y < seaLevel - 1 && topState.getBlock() == Blocks.AIR) { // <63 when seaLevel=64
                    tempPos.setPos(x, y, z);
                    this.getTemperature(tempPos);
                    topState = this.topBlock;
                }

                remainingDepth = surfaceDepth;

                if (y >= seaLevel - 2) { // >=62 when seaLevel=64
                    // 1.7: if(Math.random() > 0.4) place top else dry_ice
                    primer.setBlockState(localX, y, localZ, (rand.nextFloat() > 0.4F) ? topState : ModBlocksSpace.dry_ice.getDefaultState());
                } else if (y < gravelBelowY) {
                    // 1.7: below (56 - l): force gravel; also set filler for the following depth-fill to duna_rock
                    fillerState = ModBlocksSpace.duna_rock.getDefaultState();
                    primer.setBlockState(localX, y, localZ, Blocks.GRAVEL.getDefaultState());
                } else {
                    // 1.7: otherwise fill with block1 (fillerState)
                    primer.setBlockState(localX, y, localZ, fillerState);
                }

            } else if (remainingDepth > 0) {
                --remainingDepth;
                primer.setBlockState(localX, y, localZ, fillerState);
            }
        }
    }
}
