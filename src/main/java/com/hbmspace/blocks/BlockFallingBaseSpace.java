package com.hbmspace.blocks;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.BlockFallingBase;
import com.hbm.blocks.ModBlocks;
import com.hbm.items.IDynamicModels;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import static com.hbm.render.block.BlockBakeFrame.BlockForm.ALL;

public class BlockFallingBaseSpace extends BlockFallingBase implements IDynamicModelsSpace {
    protected BlockBakeFrame blockFrame;


    public BlockFallingBaseSpace(Material materialIn, String name, SoundType type) {
        super(materialIn, name, type);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        this.blockFrame = new BlockBakeFrame(ALL, name);
        IDynamicModelsSpace.INSTANCES.add(this);
    }


    public BlockFallingBaseSpace(
            Material materialIn, String name, SoundType type, BlockBakeFrame blockFrame) {
        super(materialIn, name, type);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        this.blockFrame = blockFrame;
        IDynamicModels.INSTANCES.add(this);

    }

    public BlockFallingBaseSpace(
            Material materialIn, String name, SoundType type, String texture) {
        super(materialIn, name, type);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        this.blockFrame = new BlockBakeFrame(ALL, texture);
        IDynamicModels.INSTANCES.add(this);

    }

    public BlockFallingBaseSpace(
            Material materialIn, String name, SoundType type, String textureTop, String textureSide) {
        super(materialIn, name, type);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        this.blockFrame = new BlockBakeFrame(textureTop, textureSide);
        IDynamicModels.INSTANCES.add(this);

    }

    @Override
    public void bakeModel(ModelBakeEvent event) {

        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(blockFrame.getBaseModel()));
            ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();

            blockFrame.putTextures(textureMap);
            IModel retexturedModel = baseModel.retexture(textureMap.build());
            IBakedModel bakedModel = retexturedModel.bake(
                    ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter()
            );

            ModelResourceLocation modelLocation = new ModelResourceLocation(getRegistryName(), "inventory");
            event.getModelRegistry().putObject(modelLocation, bakedModel);
            ModelResourceLocation worldLocation = new ModelResourceLocation(getRegistryName(), "normal");
            event.getModelRegistry().putObject(worldLocation, bakedModel);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public void registerSprite(TextureMap map) {
        blockFrame.registerBlockTextures(map);
    }
}
