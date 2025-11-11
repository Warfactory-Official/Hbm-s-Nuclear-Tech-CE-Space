package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityTransporterRocket;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderTransporterRocket extends TileEntitySpecialRenderer<TileEntityTransporterRocket> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityTransporterRocket pad, double x, double y, double z, float interp, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            GlStateManager.rotate(90, 0, 1, 0);

            switch (pad.getBlockMetadata() - BlockDummyable.offset) {
                case 2 -> GlStateManager.rotate(0, 0F, 1F, 0F);
                case 4 -> GlStateManager.rotate(90, 0F, 1F, 0F);
                case 3 -> GlStateManager.rotate(180, 0F, 1F, 0F);
                case 5 -> GlStateManager.rotate(270, 0F, 1F, 0F);
            }

            bindTexture(ResourceManagerSpace.transporter_pad_tex);
            ResourceManagerSpace.transporter_pad.renderPart("base");

            GlStateManager.pushMatrix();
            {

                float rot = MathHelper.clamp(getPipeEngage(pad, interp), 0, 1);

                GlStateManager.translate(0.0F, 0.75F, -0.75F);
                GlStateManager.rotate(rot * -30.0F, 1, 0, 0);
                GlStateManager.translate(0.0F, -0.75F, 0.75F);

                ResourceManagerSpace.transporter_pad.renderPart("pipe");

            }
            GlStateManager.popMatrix();

            if(pad.launchTicks < 100) {
                GlStateManager.pushMatrix();
                {

                    GlStateManager.translate(0.0F, 0.75F + MathHelper.clamp(pad.launchTicks + (pad.hasRocket ? -interp : interp), 0, 200), 0.0F);
                    GlStateManager.disableCull();

                    bindTexture(ResourceManager.minerRocket_tex);
                    ResourceManager.minerRocket.renderAll();

                    GlStateManager.enableCull();
                }
                GlStateManager.popMatrix();
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    private float getPipeEngage(TileEntityTransporterRocket pad, float interp) {
        if(pad.launchTicks >= 0) {
            return !pad.hasRocket ? (pad.launchTicks + interp) * 0.25F : 1.0F;
        } else {
            return 1 - (-pad.launchTicks - 1 + interp) * 0.25F;
        }
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                // GL11.glTranslated(0, -0.5, 0);
                GlStateManager.scale(3D, 3D, 3D);
            }
            public void renderCommon() {
                GlStateManager.translate(0.5, 0, 0);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);

                bindTexture(ResourceManagerSpace.transporter_pad_tex);
                ResourceManagerSpace.transporter_pad.renderAll();

                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.transporter_rocket);
    }

}
