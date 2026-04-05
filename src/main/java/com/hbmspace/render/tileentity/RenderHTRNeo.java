package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.util.Clock;
import com.hbm.util.RenderUtil;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineHTRNeo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderHTRNeo extends TileEntitySpecialRenderer<TileEntityMachineHTRNeo> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineHTRNeo rocket, double x, double y, double z, float interp, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y - 2.0D, z + 0.5D);
        GlStateManager.enableLighting();

        switch(rocket.getBlockMetadata() - BlockDummyable.offset) {
            case 3: GlStateManager.rotate(270, 0F, 1F, 0F); break;
            case 5: GlStateManager.rotate(0, 0F, 1F, 0F); break;
            case 2: GlStateManager.rotate(90, 0F, 1F, 0F); break;
            case 4: GlStateManager.rotate(180, 0F, 1F, 0F); break;
        }
        float rot = rocket.prevRotor + (rocket.rotor - rocket.prevRotor) * interp;

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        bindTexture(ResourceManagerSpace.htrf4_neo_tex);
        ResourceManagerSpace.htrf4_neo.renderOnly("Base", "Engine");

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(0, 2.5F, 0);
            GlStateManager.rotate(rot, 0, 0, 1);
            GlStateManager.translate(0, -2.5F, 0);
            ResourceManagerSpace.htrf4_neo.renderOnly("Rotor", "Rotor2");

        }
        GlStateManager.popMatrix();


        float trailStretch = rocket.getWorld().rand.nextFloat();
        trailStretch = 1.2F - (trailStretch * trailStretch * 0.1F);
        trailStretch *= rocket.thrustAmount;

        if(trailStretch > 0) {
            GlStateManager.color(rocket.plasmaR, rocket.plasmaG, rocket.plasmaB, rocket.thrustAmount);

            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            RenderUtil.pushAttrib(GL11.GL_LIGHTING_BIT);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
            GlStateManager.depthMask(false);

            GlStateManager.translate(0, 0, 12);
            GlStateManager.scale(1, 1, trailStretch);
            GlStateManager.translate(0, 0, -12);

            bindTexture(ResourceManagerSpace.htrf4_exhaust_tex);
            ResourceManagerSpace.htrf4_neo.renderPart("Exhaust");


            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();

            long time = Clock.get_ms();
            double sparkleSpin = time / 250D % 1D;

            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GlStateManager.color(rocket.plasmaR * 2, rocket.plasmaG * 2, rocket.plasmaB * 2, rocket.thrustAmount * 0.75F);

            GlStateManager.rotate(-90, 0, 1, 0); // it's wrong but it looks cooler as a series of concentric rings so whatever
            GlStateManager.translate(0, sparkleSpin, 0);

            bindTexture(ResourceManager.fusion_plasma_sparkle_tex);
            ResourceManagerSpace.htrf4_neo.renderPart("Exhaust");

            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);



            GlStateManager.depthMask(true);
            RenderUtil.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.disableBlend();

            GlStateManager.color(1, 1, 1, 1);
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_htrf4neo);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -1, 0);
                GlStateManager.scale(1.5, 1.5, 1.5);
            }
            public void renderCommon() {
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.htrf4_neo_tex);
                ResourceManagerSpace.htrf4_neo.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }
}
