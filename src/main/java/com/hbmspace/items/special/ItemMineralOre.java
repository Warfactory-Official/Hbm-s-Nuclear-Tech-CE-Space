package com.hbmspace.items.special;

import com.hbm.main.MainRegistry;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Function;

public class ItemMineralOre extends Item implements IDynamicModelsSpace {

    public static final Mineral[] itemTypes = new Mineral[6];

    /* item types */
    public static final int CLUMP_PEROXIDE = 0;
    public static final int CLUMP_NITRIC = 1;
    public static final int CLUMP_SULFURIC = 2;
    public static final int CLUMP_SOLVENT = 3;
    public static final int CLUMP_HYDROCHLORIC = 4;
    public static final int CLUMP_SCHRABIDIC = 5;

    @SideOnly(Side.CLIENT)
    private ModelResourceLocation[] modelLocations;

    public ItemMineralOre(String s) {
        this.setTranslationKey(s);
        this.setRegistryName(s);
        this.setCreativeTab(MainRegistry.controlTab);
        this.setHasSubtypes(true);
        this.setCreativeTab(MainRegistry.partsTab);
        init();

        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        switch (stack.getMetadata()) {
            case CLUMP_PEROXIDE:
                tooltip.add(TextFormatting.YELLOW + "[Peroxide]");
                break;
            case CLUMP_NITRIC:
                tooltip.add(TextFormatting.GOLD + "[Nitric]");
                break;
            case CLUMP_SULFURIC:
                tooltip.add(TextFormatting.YELLOW + "[Sulfuric]");
                break;
            case CLUMP_SOLVENT:
                tooltip.add(TextFormatting.RED + "[Solvent]");
                break;
            case CLUMP_HYDROCHLORIC:
                tooltip.add(TextFormatting.GREEN + "[Chloric]");
                break;
            case CLUMP_SCHRABIDIC:
                tooltip.add(TextFormatting.BLUE + "[Schrabidic]");
                break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!this.isInCreativeTab(tab)) return;

        items.add(new ItemStack(this, 1, CLUMP_PEROXIDE));
        items.add(new ItemStack(this, 1, CLUMP_NITRIC));
        items.add(new ItemStack(this, 1, CLUMP_SULFURIC));
        items.add(new ItemStack(this, 1, CLUMP_SOLVENT));
        items.add(new ItemStack(this, 1, CLUMP_HYDROCHLORIC));
        items.add(new ItemStack(this, 1, CLUMP_SCHRABIDIC));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        int meta = stack.getMetadata();
        int idx = Math.abs(meta) % itemTypes.length;
        return "item." + itemTypes[idx].name;
    }

    public abstract class Mineral {
        public final String name;

        public Mineral(String name) {
            this.name = name;
        }
    }

    private void init() {
        itemTypes[CLUMP_PEROXIDE] = new Mineral("clump_peroxide") {};
        itemTypes[CLUMP_NITRIC] = new Mineral("clump_nitric") {};
        itemTypes[CLUMP_SULFURIC] = new Mineral("clump_sulfuric") {};
        itemTypes[CLUMP_SOLVENT] = new Mineral("clump_solvent") {};
        itemTypes[CLUMP_HYDROCHLORIC] = new Mineral("clump_hydrochloric") {};
        itemTypes[CLUMP_SCHRABIDIC] = new Mineral("clump_schrabidic") {};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        this.modelLocations = new ModelResourceLocation[itemTypes.length];

        for (int i = 0; i < itemTypes.length; i++) {
            ResourceLocation rl = new ResourceLocation("hbm", itemTypes[i].name);
            this.modelLocations[i] = new ModelResourceLocation(rl, "inventory");
        }

        ModelBakery.registerItemVariants(this, this.modelLocations);

        ModelLoader.setCustomMeshDefinition(this, stack -> {
            int meta = stack.getMetadata();
            if (meta < 0 || meta >= itemTypes.length) meta = 0;
            return this.modelLocations[meta];
        });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        for (int i = 0; i < itemTypes.length; i++) {
            map.registerSprite(new ResourceLocation("hbm", "items/" + itemTypes[i].name));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        if (this.modelLocations == null || this.modelLocations.length != itemTypes.length) {
            this.modelLocations = new ModelResourceLocation[itemTypes.length];
            for (int i = 0; i < itemTypes.length; i++) {
                this.modelLocations[i] = new ModelResourceLocation(
                        new ResourceLocation("hbm", itemTypes[i].name),
                        "inventory"
                );
            }
        }

        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        Function<ResourceLocation, TextureAtlasSprite> getter =
                rl -> map.getAtlasSprite(rl.toString());

        for (int i = 0; i < itemTypes.length; i++) {
            ResourceLocation tex = new ResourceLocation("hbm", "items/" + itemTypes[i].name);

            ItemLayerModel model =
                    new ItemLayerModel(com.google.common.collect.ImmutableList.of(tex));

            IBakedModel baked = model.bake(
                    TRSRTransformation.identity(),
                    DefaultVertexFormats.ITEM,
                    getter
            );

            event.getModelRegistry().putObject(this.modelLocations[i], baked);
        }
    }
}
