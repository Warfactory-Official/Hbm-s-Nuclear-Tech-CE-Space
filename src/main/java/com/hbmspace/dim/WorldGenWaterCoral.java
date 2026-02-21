package com.hbmspace.dim;

import java.util.Random;

import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockCoral;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.jetbrains.annotations.NotNull;

public class WorldGenWaterCoral extends WorldGenerator {

    public int seaLevel = 64;

    @Override
    public boolean generate(@NotNull World world, @NotNull Random rand, BlockPos position) {
        boolean flag = false;
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        for(int l = 0; l < 16; ++l) {
            int px = x + rand.nextInt(8) - rand.nextInt(8);
            int py = y + rand.nextInt(4) - rand.nextInt(4);
            int pz = z + rand.nextInt(8) - rand.nextInt(8);

            BlockPos pos = new BlockPos(px, py, pz);

            if(!world.isBlockLoaded(pos)) continue;

            if(py < seaLevel - 1 && world.getBlockState(pos).getMaterial() == Material.WATER && world.getBlockState(pos.down()).getBlock() == ModBlocksSpace.laythe_silt) {

                int meta = rand.nextInt(BlockCoral.EnumCoral.values().length);

                world.setBlockState(pos.down(), ModBlocksSpace.laythe_coral_block.getStateFromMeta(meta), 2);

                int oy = 0;
                int ox = 0;
                int oz = 0;
                while(oy < 8) {
                    if(py + oy > seaLevel - 3) break;
                    if(rand.nextBoolean()) break;

                    if(rand.nextBoolean()) {
                        if(rand.nextBoolean()) {
                            ox += rand.nextBoolean() ? 1 : -1;
                        } else {
                            oz += rand.nextBoolean() ? 1 : -1;
                        }
                    }

                    BlockPos curPos = pos.add(ox, oy, oz);

                    if(world.isBlockLoaded(curPos)) world.setBlockState(curPos, ModBlocksSpace.laythe_coral_block.getStateFromMeta(meta), 2);

                    if(rand.nextBoolean()) placeCoral(world, curPos.east(), meta, EnumFacing.EAST);
                    if(rand.nextBoolean()) placeCoral(world, curPos.west(), meta, EnumFacing.WEST);
                    if(rand.nextBoolean()) placeCoral(world, curPos.south(), meta, EnumFacing.SOUTH);
                    if(rand.nextBoolean()) placeCoral(world, curPos.north(), meta, EnumFacing.NORTH);

                    oy++;
                }

                BlockPos finalPos = pos.add(ox, oy, oz);
                if(world.isBlockLoaded(finalPos)) world.setBlockState(finalPos, ModBlocksSpace.laythe_coral.getStateFromMeta(meta), 2);

                flag = true;
            }
        }

        return flag;
    }

    private void placeCoral(World world, BlockPos pos, int meta, EnumFacing facing) {
        if(world.isBlockLoaded(pos)) {
            world.setBlockState(pos, ModBlocksSpace.laythe_coral.getStateFromMeta(meta), 2);
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof BlockCoral.TileEntityCoral) {
                ((BlockCoral.TileEntityCoral) te).facing = facing;
            }
        }
    }

}