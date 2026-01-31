package com.hbmspace.main;

import com.hbm.items.RBMKItemRenderers;
import com.hbm.main.client.NTMClientRegistry;
import com.hbm.render.item.ItemRenderMissilePart;
import com.hbm.render.tileentity.RenderRBMKLid;
import com.hbm.sound.AudioWrapper;
import com.hbm.sound.AudioWrapperClient;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.render.misc.RocketPart;
import com.hbmspace.render.tileentity.IItemRendererProviderSpace;
import com.hbmspace.sound.AudioWrapperClientSpace;
import com.hbmspace.tileentity.machine.rbmk.TileEntityRBMKBurner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ClientProxy extends ServerProxy {

    @Override
    public File getDataDir() {
        return Minecraft.getMinecraft().gameDir;
    }

    @Override
    public void registerRenderInfo() {
        if (!Minecraft.getMinecraft().getFramebuffer().isStencilEnabled())
            Minecraft.getMinecraft().getFramebuffer().enableStencil();

        AutoRegistrySpace.registerRenderInfo();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRBMKBurner.class, new RenderRBMKLid());
        ModelLoader.setCustomStateMapper(ModBlocksSpace.ccl_block, new StateMap.Builder().ignore(BlockFluidClassic.LEVEL).build());
    }
    @Override
    public void preInit(FMLPreInitializationEvent evt) {
        AutoRegistrySpace.preInitClient();
        for (TileEntitySpecialRenderer<? extends TileEntity> renderer : TileEntityRendererDispatcher.instance.renderers.values()) {
            if (renderer instanceof IItemRendererProviderSpace prov) {
                for (Item item : prov.getItemsForRenderer()) {
                    item.setTileEntityItemStackRenderer(prov.getRenderer(item));
                }
            }
        }

        // same crap but for items directly because why invent a new solution when this shit works just fine
        for (Item renderer : Item.REGISTRY) {
            if (renderer instanceof IItemRendererProviderSpace provider) {
                for (Item item : provider.getItemsForRenderer()) {
                    item.setTileEntityItemStackRenderer(provider.getRenderer(item));
                }
            }
        }
        Item.getItemFromBlock(ModBlocksSpace.rbmk_burner).setTileEntityItemStackRenderer(RBMKItemRenderers.RBMK_PASSIVE);
    }

    @Override
    public void registerMissileItems(IRegistry<ModelResourceLocation, IBakedModel> reg) {
        RocketPart.registerClientParts();

        RocketPart.parts.values().forEach(part -> registerItemRenderer(part.part, new ItemRenderMissilePart(part), reg));
    }

    public static void registerItemRenderer(Item i, TileEntityItemStackRenderer render, IRegistry<ModelResourceLocation, IBakedModel> reg) {
        i.setTileEntityItemStackRenderer(render);
        NTMClientRegistry.swapModels(i, reg);
    }

    @Override
    public AudioWrapper getLoopedSound(SoundEvent sound, SoundCategory cat, float x, float y, float z, float volume, float range, float pitch, int keepAlive) {
        AudioWrapperClient audio = new AudioWrapperClient(sound, cat, true);
        audio.updatePosition(x, y, z);
        audio.updateVolume(volume);
        audio.updateRange(range);
        audio.setKeepAlive(keepAlive);
        return audio;
    }
    @Override
    public AudioWrapper getLoopedSound(SoundEvent sound, SoundCategory cat, float x, float y, float z, float volume, float range, float pitch) {
        AudioWrapperClient audio = new AudioWrapperClient(sound, cat, true);
        audio.updatePosition(x, y, z);
        audio.updateVolume(volume);
        audio.updateRange(range);
        return audio;
    }

    @Override
    public AudioWrapper getLoopedSound(SoundEvent sound, SoundCategory cat, Entity entity, float volume, float range, float pitch, int keepAlive) {
        AudioWrapperClientSpace audio = new AudioWrapperClientSpace(sound, cat, entity);
        audio.updateVolume(volume);
        audio.updateRange(range);
        audio.setKeepAlive(keepAlive);
        return audio;
    }
}
