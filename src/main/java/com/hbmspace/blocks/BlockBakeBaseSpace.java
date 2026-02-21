package com.hbmspace.blocks;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockBakeBase;
import com.hbm.items.IDynamicModels;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.material.Material;

public class BlockBakeBaseSpace extends BlockBakeBase implements IDynamicModelsSpace {
    public BlockBakeBaseSpace(Material m, String s) {
        super(m, s);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public BlockBakeBaseSpace(Material m, String s, BlockBakeFrame blockFrame) {
        super(m, s, blockFrame);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }
}
