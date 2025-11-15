package com.hbmspace.main;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemScraps;
import com.hbm.items.special.ItemAutogen;
import com.hbm.render.icon.RGBMutatorInterpolatedComponentRemap;
import com.hbm.render.icon.TextureAtlasSpriteMutatable;
import com.hbm.render.item.BakedModelCustom;
import com.hbm.render.item.BakedModelNoFPV;
import com.hbm.render.item.TEISRBase;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.inventory.materials.MatsSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.render.tileentity.IItemRendererProviderSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = RefStrings.MODID)
public class ModEventHandlerClient {

    @SubscribeEvent
    public static void modelBaking(ModelBakeEvent evt) {
        IRegistry<ModelResourceLocation, IBakedModel> reg = evt.getModelRegistry();
        IDynamicModelsSpace.bakeModels(evt);
        for (TileEntitySpecialRenderer<? extends TileEntity> renderer : TileEntityRendererDispatcher.instance.renderers.values()) {
            if (renderer instanceof IItemRendererProviderSpace prov) {
                for (Item item : prov.getItemsForRenderer()) {
                    swapModels(item, reg);
                }
            }
        }

        for (Item renderer : Item.REGISTRY) {
            if (renderer instanceof IItemRendererProviderSpace provider) {
                for (Item item : provider.getItemsForRenderer()) {
                    swapModels(item, reg);
                }
            }
        }

        SpaceMain.proxy.registerMissileItems(reg);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Item item : ModItemsSpace.ALL_ITEMS) {
            try {
                registerModel(item, 0);
            } catch (NullPointerException e) {
                e.printStackTrace();
                SpaceMain.logger.info("Failed to register model for " + item.getRegistryName());
            }
        }
        for (Block block : ModBlocksSpace.ALL_BLOCKS) {
            registerBlockModel(block, 0);
        }

        IDynamicModelsSpace.registerModels();
        IDynamicModelsSpace.registerCustomStateMappers();
    }

    @SubscribeEvent
    public static void textureStitch(TextureStitchEvent.Pre evt) {
        TextureMap map = evt.getMap();
        IDynamicModelsSpace.registerSprites(map);
    }

    private static void registerModel(Item item, int meta) {
        if(!(item instanceof IDynamicModelsSpace dyn && dyn.INSTANCES.contains(item))) {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    private static void registerBlockModel(Block block, int meta) {
        registerModel(Item.getItemFromBlock(block), meta);
    }

    public static void swapModels(Item item, IRegistry<ModelResourceLocation, IBakedModel> reg) {
        ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
        IBakedModel model = reg.getObject(loc);
        TileEntityItemStackRenderer render = item.getTileEntityItemStackRenderer();
        if (render instanceof TEISRBase) {
            ((TEISRBase) render).itemModel = model;
            reg.putObject(loc, new BakedModelCustom((TEISRBase) render));
        }
    }

    public static void swapModelsNoFPV(Item item, IRegistry<ModelResourceLocation, IBakedModel> reg) {
        ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
        IBakedModel model = reg.getObject(loc);
        TileEntityItemStackRenderer render = item.getTileEntityItemStackRenderer();
        if (render instanceof TEISRBase) {
            ((TEISRBase) render).itemModel = model;
            reg.putObject(loc, new BakedModelNoFPV((TEISRBase) render, model));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void thickenFog(EntityViewRenderEvent.FogDensity event) {
        if(event.getEntity().world.provider instanceof WorldProviderCelestial provider) {
            float fogDensity = provider.fogDensity();

            if(fogDensity > 0) {
                if(GLContext.getCapabilities().GL_NV_fog_distance) {
                    GL11.glFogi(34138, 34139);
                }
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);

                event.setDensity(fogDensity);
                event.setCanceled(true);

            }
        }
    }

    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent.Pre event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            Minecraft mc = Minecraft.getMinecraft();
            World world = mc.world;
            RayTraceResult mop = mc.objectMouseOver;
            if(mop.typeOfHit == null) return;
            if(mop.typeOfHit == RayTraceResult.Type.ENTITY) {
                Entity entity = mop.entityHit;

                if(entity instanceof ILookOverlay) {
                    ((ILookOverlay) entity).printHook(event, world, 0, 0, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void drawTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<String> list = event.getToolTip();
        if(event.getEntityPlayer() == null) return;
        /// ORES ///
        if(SpaceConfig.showOreLocations) {
            Block block = Block.getBlockFromItem(stack.getItem());
            if(block instanceof net.minecraft.block.BlockOre || block instanceof BlockRedstoneOre) {
                BlockOre ore = BlockOre.vanillaMap.get(block);
                if(ore != null) {
                    ore.addInformation(stack, event.getEntityPlayer().getEntityWorld(), list, event.getFlags());
                } else if(block == Blocks.COAL_ORE || block == ModBlocks.ore_bedrock_oil) {
                    // we don't have any celestial coal, special case
                    list.add(TextFormatting.GOLD + "Can be found on:");
                    list.add(TextFormatting.AQUA + " - " + I18nUtil.resolveKey("body.kerbin"));
                }
            }
        }
    }
    // this probably can be debloated, so
    // TODO deal with that shit later
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        List<ResourceLocation> extraVariants = new ArrayList<>();
        for (NTMMaterial mat : MatsSpace.SPACE_MATERIALS) {
            if (mat.smeltable == NTMMaterial.SmeltingBehavior.SMELTABLE
                    || mat.smeltable == NTMMaterial.SmeltingBehavior.ADDITIVE) {

                ModelResourceLocation mrl = new ModelResourceLocation(
                        new ResourceLocation("hbm", "items/scraps-" + mat.names[0]),
                        "inventory"
                );
                extraVariants.add(new ResourceLocation("hbm:items/scraps-" + mat.names[0]));
            }
        }

        extraVariants.add(new ResourceLocation("hbm", "items/scraps_liquid"));
        extraVariants.add(new ResourceLocation("hbm", "items/scraps_additive"));
        ModelBakery.registerItemVariants(ModItems.scraps, extraVariants.toArray(new ResourceLocation[0]));
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();

        for (NTMMaterial mat : MatsSpace.SPACE_MATERIALS) {
            if (mat.smeltable == NTMMaterial.SmeltingBehavior.SMELTABLE
                    || mat.smeltable == NTMMaterial.SmeltingBehavior.ADDITIVE) {

                ResourceLocation spriteLoc = new ResourceLocation(
                        "hbm:items/scraps-" + mat.names[0]
                );
                TextureAtlasSprite sprite;
                if (mat.solidColorLight != mat.solidColorDark) {
                    sprite = new TextureAtlasSpriteMutatable(
                            spriteLoc.toString(),
                            new RGBMutatorInterpolatedComponentRemap(
                                    0xFFFFFF, 0x505050,
                                    mat.solidColorLight,
                                    mat.solidColorDark
                            )
                    );
                    ItemAutogen.iconMap.put(mat, sprite);
                } else {
                    sprite = new TextureAtlasSpriteMutatable(
                            spriteLoc.toString(),
                            new RGBMutatorInterpolatedComponentRemap(
                                    0xFFFFFF, 0x505050,
                                    0xFFFFFF, 0x505050
                            )
                    );
                }
                map.setTextureEntry(sprite);
            }
        }
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));

            for (NTMMaterial mat : MatsSpace.SPACE_MATERIALS) {
                if (mat.smeltable == NTMMaterial.SmeltingBehavior.SMELTABLE
                        || mat.smeltable == NTMMaterial.SmeltingBehavior.ADDITIVE) {

                    ResourceLocation spriteLoc = new ResourceLocation("hbm:items/scraps-" + mat.names[0]);
                    IModel retexturedModel = baseModel.retexture(ImmutableMap.of("layer0", spriteLoc.toString()));
                    IBakedModel bakedModel = retexturedModel.bake(
                            ModelRotation.X0_Y0,
                            DefaultVertexFormats.ITEM,
                            ModelLoader.defaultTextureGetter()
                    );
                    ModelResourceLocation bakedModelLocation =
                            new ModelResourceLocation(spriteLoc, "inventory");
                    event.getModelRegistry().putObject(bakedModelLocation, bakedModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
