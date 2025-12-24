package com.hbmspace.blocks;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.machine.BlockContainerBakeable;
import com.hbm.items.IDynamicModels;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.material.Material;

public abstract class BlockContainerBakeableSpace extends BlockContainerBakeable implements IDynamicModelsSpace {

    public BlockContainerBakeableSpace(Material m, String s, BlockBakeFrame frame) {
        super(m, s, frame);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }
}
