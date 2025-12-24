package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityAirScrubber;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderAirScrubber extends TileEntitySpecialRenderer<TileEntityAirScrubber> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityAirScrubber te, double x, double y, double z, float interp, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();

            GlStateManager.rotate(180, 0F, 1F, 0F);

            switch(te.getBlockMetadata() - BlockDummyable.offset) {
                case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
                case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
                case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
                case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
            }

            bindTexture(ResourceManagerSpace.air_scrubber_tex);

            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            ResourceManagerSpace.air_scrubber.renderPart("Base");

            float rotation = te.prevRot + (te.rot - te.prevRot) * interp;

            GlStateManager.rotate(rotation, 0, 1, 0);
            ResourceManagerSpace.air_scrubber.renderPart("Fan");

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -4, 0);
                GlStateManager.scale(4, 4, 4);
            }
            public void renderCommon() {
                GlStateManager.scale(1.5, 1.5, 1.5);
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.air_scrubber_tex);
                ResourceManagerSpace.air_scrubber.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.air_scrubber);
    }

}
