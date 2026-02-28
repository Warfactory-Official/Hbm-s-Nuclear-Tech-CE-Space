package com.hbmspace.world.feature;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.BlockFallingBaseSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import java.util.Random;

public class OilSpotSpace {
    public static NoiseGeneratorPerlin crackGeneratorPerlin = new NoiseGeneratorPerlin(new Random(73470), 4);

    public static void generateCrack(World world, int x, int z, int width, int count) {
        count *= 10; // only 10% of checks actually pass, so increase count

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos posBelow = new BlockPos.MutableBlockPos();

        for(int i = 0; i < count; i++) {
            int rX = x + (int)(world.rand.nextGaussian() * width);
            int rZ = z + (int)(world.rand.nextGaussian() * width);

            double crack = crackGeneratorPerlin.getValue(rX * 0.25D, rZ * 0.25D);

            if(crack < 0.9D) continue;

            int rY = world.getHeight(new BlockPos(rX, 0, rZ)).getY();

            for(int y = rY; y > rY - 4; y--) {
                pos.setPos(rX, y, rZ);
                posBelow.setPos(rX, y - 1, rZ);

                IBlockState stateBelow = world.getBlockState(posBelow);
                IBlockState stateGround = world.getBlockState(pos);
                Block ground = stateGround.getBlock();
                int meta = ground.getMetaFromState(stateGround);

                if(stateBelow.isNormalCube() && ground != ModBlocks.plant_dead) {
                    if(ground == ModBlocksSpace.rubber_plant) {
                        world.setBlockState(pos, ModBlocks.plant_dead.getStateFromMeta(meta), 3);
                    }
                }

                if(ground == ModBlocksSpace.rubber_grass) {
                    world.setBlockState(pos, ModBlocksSpace.rubber_silt.getDefaultState());
                    break;
                }

                if(ground == ModBlocksSpace.rubber_silt || ground == ModBlocksSpace.vinyl_sand || ground == ModBlocks.sellafield_slaked || ground == ModBlocks.basalt) {
                    if(stateBelow.getBlockHardness(world, posBelow) == -1.0F) break;

                    world.setBlockToAir(posBelow);

                    // manually drop any non BlockFalling blocks
                    if(ground != ModBlocksSpace.vinyl_sand) {
                        byte range = 64;
                        if(!BlockFalling.fallInstantly && world.isAreaLoaded(new BlockPos(x - range, y - range, z - range), new BlockPos(x + range, y + range, z + range))) {
                            EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, rX + 0.5D, y + 0.5D, rZ + 0.5D, stateGround);
                            entityfallingblock.shouldDropItem = false;
                            world.spawnEntity(entityfallingblock);
                        } else {
                            world.setBlockToAir(pos);

                            while(BlockFallingBaseSpace.canFallThrough(world.getBlockState(posBelow)) && y > 0) {
                                --y;
                                posBelow.setPos(rX, y - 1, rZ);
                            }

                            if(y > 0) {
                                world.setBlockState(new BlockPos(rX, y, rZ), stateGround, 3);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
