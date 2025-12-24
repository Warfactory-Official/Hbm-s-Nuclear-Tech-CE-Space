package com.hbmspace.blocks.machine;

import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockContainerBakeableSpace;
import com.hbmspace.tileentity.machine.TileEntityStationPropulsionCreative;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static com.hbm.render.block.BlockBakeFrame.BlockForm.ALL;

public class BlockStationPropulsionCreative extends BlockContainerBakeableSpace {

    public BlockStationPropulsionCreative(Material mat, String s, String tex) {
        super(mat, s, new BlockBakeFrame(ALL, tex));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityStationPropulsionCreative();
    }

}
