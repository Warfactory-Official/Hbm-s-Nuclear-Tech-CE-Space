package com.hbmspace.main;

import com.hbm.items.RBMKItemRenderers;
import com.hbm.main.client.NTMClientRegistry;
import com.hbm.particle.helper.ParticleCreators;
import com.hbm.render.item.ItemRenderMissilePart;
import com.hbm.render.tileentity.RenderRBMKLid;
import com.hbm.sound.AudioWrapper;
import com.hbm.sound.AudioWrapperClient;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.particle.ParticleGlow;
import com.hbmspace.particle.ParticleRocketFlameSpace;
import com.hbmspace.render.misc.RocketPart;
import com.hbmspace.render.tileentity.IItemRendererProviderSpace;
import com.hbmspace.sound.AudioWrapperClientSpace;
import com.hbmspace.tileentity.machine.rbmk.TileEntityRBMKBurner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.io.File;
import java.util.Random;

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

    @Override
    public void effectNT(NBTTagCompound data) {
        World world = Minecraft.getMinecraft().world;
        if (world == null)
            return;
        EntityPlayer player = Minecraft.getMinecraft().player;
        Random rand = world.rand;
        String type = data.getString("type");
        double x = data.getDouble("posX");
        double y = data.getDouble("posY");
        double z = data.getDouble("posZ");

        if (ParticleCreators.particleCreators.containsKey(type)) {
            ParticleCreators.particleCreators.get(type).makeParticle(world, player,
                    Minecraft.getMinecraft().renderEngine, rand, x, y, z, data);
            return;
        }
        switch (type) {
            case "flare" -> {
                double mX = data.getDouble("mX");
                double mY = data.getDouble("mY");
                double mZ = data.getDouble("mZ");
                float scale = data.getFloat("scale");
                ParticleGlow particle = new ParticleGlow(world, x, y, z, mX, mY, mZ, scale);
                Minecraft.getMinecraft().effectRenderer.addEffect(particle);
            }
            case "depress" -> {

                if(player == null || new Vec3d(player.posX - x, player.posY - y, player.posZ - z).length() > 350) return;

                float scale = data.hasKey("scale") ? data.getFloat("scale") : 1F;
                double mX = data.getDouble("moX");
                double mY = data.getDouble("moY");
                double mZ = data.getDouble("moZ");

                ParticleRocketFlameSpace fx = new ParticleRocketFlameSpace(world, x, y, z).setScale(scale);
                fx.setMotion(mX, mY, mZ);
                if(data.hasKey("maxAge")) fx.setMaxAge(data.getInteger("maxAge"));
                if(data.hasKey("color")) {
                    Color color = new Color(data.getInteger("color"));
                    fx.setRBGColorF(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
                }
                Minecraft.getMinecraft().effectRenderer.addEffect(fx);
            }
        }
    }
}
