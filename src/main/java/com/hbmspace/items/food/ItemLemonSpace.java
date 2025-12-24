package com.hbmspace.items.food;

import com.google.common.collect.ImmutableMap;
import com.hbm.items.ModItems;
import com.hbm.items.food.ItemLemon;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import static com.hbm.items.ItemEnumMulti.ROOT_PATH;

public class ItemLemonSpace extends ItemLemon implements IDynamicModelsSpace {
    String texturePath;
    public ItemLemonSpace(int amount, float saturation, boolean isWolfFood, String s){
        super(amount, saturation, isWolfFood, s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        texturePath = s;
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public ItemLemonSpace(int amount, float saturation, boolean isWolfFood, String s, String tex){
        super(amount, saturation, isWolfFood, s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        texturePath = tex;
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));
            ResourceLocation spriteLoc = new ResourceLocation("hbm", ROOT_PATH + texturePath);
            IModel retexturedModel = baseModel.retexture(
                    ImmutableMap.of(
                            "layer0", spriteLoc.toString()
                    )

            );
            IBakedModel bakedModel = retexturedModel.bake(ModelRotation.X0_Y0, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
            ModelResourceLocation bakedModelLocation = new ModelResourceLocation(spriteLoc, "inventory");
            event.getModelRegistry().putObject(bakedModelLocation, bakedModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation("hbm", ROOT_PATH + texturePath), "inventory"));
    }

    @Override
    public void registerSprite(TextureMap map) {
        map.registerSprite(new ResourceLocation("hbm", ROOT_PATH + texturePath));
    }
}
