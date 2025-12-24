package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityHydroponic;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderHydroponic extends TileEntitySpecialRenderer<TileEntityHydroponic> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityHydroponic tile, double x, double y, double z, float interp, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5, y, z + 0.5);
            GlStateManager.enableLighting();
            GlStateManager.enableCull();

            switch(tile.getBlockMetadata() - BlockDummyable.offset) {
                case 2: GlStateManager.rotate(90, 0F, 1F, 0F); break;
                case 4: GlStateManager.rotate(180, 0F, 1F, 0F); break;
                case 3: GlStateManager.rotate(270, 0F, 1F, 0F); break;
                case 5: GlStateManager.rotate(0, 0F, 1F, 0F); break;
            }

            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            bindTexture(ResourceManagerSpace.hydroponic_tex);
            ResourceManagerSpace.hydroponic.renderPart("Base");

            if(tile.power >= 200) {
                GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
                GlStateManager.disableCull();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

                GL11.glDepthMask(false);
                ResourceManagerSpace.hydroponic.renderPart("Lights");
                GL11.glDepthMask(true);

                GlStateManager.disableBlend();
                GlStateManager.enableCull();
                GL11.glPopAttrib();
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.hydrobay);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -1.75, 0);
                GlStateManager.scale(3, 3, 3);
            }
            public void renderCommon() {
                bindTexture(ResourceManagerSpace.hydroponic_tex);
                ResourceManagerSpace.hydroponic.renderAll();
            }
        };
    }

}
