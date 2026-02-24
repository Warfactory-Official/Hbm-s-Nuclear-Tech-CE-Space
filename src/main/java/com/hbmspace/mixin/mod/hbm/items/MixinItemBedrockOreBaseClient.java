package com.hbmspace.mixin.mod.hbm.items;

import com.google.common.collect.ImmutableMap;
import com.hbm.items.IDynamicModels;
import com.hbm.items.special.ItemBedrockOreBase;
import com.hbm.items.special.ItemBedrockOreNew;
import com.hbm.items.tool.ItemOreDensityScanner;
import com.hbm.util.I18nUtil;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.util.BedrockOreUtil;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Locale;

import static com.hbm.items.ItemEnumMulti.ROOT_PATH;

@Mixin(ItemBedrockOreBase.class)
public class MixinItemBedrockOreBaseClient extends Item implements IDynamicModels {

    /**
     * @author Th3_Sl1ze
     * @reason
     */
    @Overwrite
    public void addInformation(@NotNull ItemStack stack, World worldIn, List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        SolarSystem.Body body = BedrockOreUtil.getOreBody(stack);
        tooltip.add("Mined on: " + I18nUtil.resolveKey("body." + body.name().toLowerCase(Locale.US)));

        for(ItemBedrockOreNew.BedrockOreType type : BedrockOreUtil.getTypesForBody(body)) {
            double amount = BedrockOreUtil.getOreAmount(stack, type);
            String typeName = I18n.format("item.bedrock_ore.type." + type.suffix + ".name");
            tooltip.add(typeName + ": " + ((int) (amount * 100)) / 100D + " (" + ItemOreDensityScanner.getColor(amount) + I18nUtil.resolveKey(ItemOreDensityScanner.translateDensity(amount)) + TextFormatting.GRAY + ")");
        }
    }


    @Override
    public void registerModel() {
        ModelResourceLocation modelLocation = new ModelResourceLocation("hbm:items/bedrock_ore_base", "inventory");
        for (int meta = 1; meta <= 10; meta++) {
            ModelLoader.setCustomModelResourceLocation(this, meta, modelLocation);
        }
    }

    @Override
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));
            ResourceLocation spriteLoc = new ResourceLocation("hbm", ROOT_PATH + "bedrock_ore_new");
            IModel retexturedModel = baseModel.retexture(
                    ImmutableMap.of(
                            "layer0", spriteLoc.toString()
                    )

            );
            IBakedModel bakedModel = retexturedModel.bake(ModelRotation.X0_Y0, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
            ModelResourceLocation bakedModelLocation = new ModelResourceLocation(new ResourceLocation("hbm", ROOT_PATH + "bedrock_ore_base"), "inventory");
            event.getModelRegistry().putObject(bakedModelLocation, bakedModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerSprite(TextureMap map) {
        map.registerSprite(new ResourceLocation("hbm", ROOT_PATH + "bedrock_ore_new"));
    }
}
