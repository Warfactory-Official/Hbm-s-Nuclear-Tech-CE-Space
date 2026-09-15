package com.hbmspace.blocks;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.machine.BlockContainerBakeable;
import com.hbm.items.IDynamicModels;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public abstract class BlockContainerBakeableSpace extends BlockContainerBakeable implements IDynamicModelsSpace {

    public BlockContainerBakeableSpace(Material m, String s, BlockBakeFrame frame) {
        super(m, s, frame);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        super.bakeModel(event);
        if (getBlockState().getProperty("facing") != null) return;

        IBakedModel north = event.getModelRegistry().getObject(new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "facing=north"));
        if (north != null) {
            event.getModelRegistry().putObject(new ModelResourceLocation(getRegistryName(), "normal"), north);
        }
    }
}
