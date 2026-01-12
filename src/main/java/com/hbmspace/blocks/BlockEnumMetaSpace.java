package com.hbmspace.blocks;

import com.hbm.blocks.BlockEnumMeta;
import com.hbm.blocks.ModBlocks;
import com.hbm.items.IDynamicModels;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockEnumMetaSpace<E extends Enum<E>> extends BlockEnumMeta<E> implements IDynamicModelsSpace {
    public BlockEnumMetaSpace(Material mat, SoundType type, String registryName, E[] blockEnum, boolean multiName, boolean multiTexture) {
        super(mat, type, registryName, blockEnum, multiName, multiTexture);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }
}
