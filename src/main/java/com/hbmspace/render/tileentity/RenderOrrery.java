package com.hbmspace.render.tileentity;

import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.blocks.generic.BlockOrrery.TileEntityOrrery;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.render.util.OrreryPronter;
import com.hbmspace.render.util.SpaceRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
@AutoRegister
public class RenderOrrery extends TileEntitySpecialRenderer<TileEntityOrrery> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityOrrery tile, double x, double y, double z, float interp, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {

            GlStateManager.enableLighting();
            GlStateManager.enableCull();

            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            OrreryPronter.render(Minecraft.getMinecraft(), tile.getWorld(), interp);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.orrery);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, 3, 0);
                GlStateManager.scale(6, 6, 6);
            }
            public void renderCommon() {
                Minecraft.getMinecraft().getTextureManager().bindTexture(SolarSystem.kerbol.texture);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                SpaceRenderUtil.renderBlock(Tessellator.getInstance(), 0.375, 0.625);
            }
        };
    }

}
