package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityDysonLauncher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderDysonLauncher extends TileEntitySpecialRenderer<TileEntityDysonLauncher> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityDysonLauncher tileEntity, double x, double y, double z, float f, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {


            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();
            GlStateManager.enableCull();

            switch(tileEntity.getBlockMetadata() - BlockDummyable.offset) {
                case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
                case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
                case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
                case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
            }

            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            bindTexture(ResourceManagerSpace.dyson_spinlaunch_tex);
            ResourceManagerSpace.dyson_spinlaunch.renderPart("Launch");

            float t = tileEntity.lastRotation + (tileEntity.rotation - tileEntity.lastRotation) * f;

            GlStateManager.pushMatrix();
            {

                GlStateManager.translate(0, 8.5F, 0);
                GlStateManager.rotate(45.0F, 1, 0, 0);
                GlStateManager.rotate(t, 0, 1, 0);
                GlStateManager.rotate(-45.0F, 1, 0, 0);
                GlStateManager.translate(0, -8.5F, 0);

                ResourceManagerSpace.dyson_spinlaunch.renderPart("The_Thing_That_Rotates");

                if(tileEntity.satCount > 0 && !tileEntity.isSpinningDown) {
                    ResourceManagerSpace.dyson_spinlaunch.renderPart("Payload");
                }

            }
            GlStateManager.popMatrix();

            if(tileEntity.isSpinningDown) {
                float p = tileEntity.payloadTicks + f;

                GlStateManager.translate(1.0F, 8.5F, 0);
                GlStateManager.rotate(45.0F, 1, 0, 0);
                GlStateManager.rotate(90.0F, 0, 1, 0);
                GlStateManager.translate(p * 10.0F, 0, 0);
                GlStateManager.rotate(-45.0F, 1, 0, 0);
                GlStateManager.translate(0, -8.5F, 0);

                ResourceManagerSpace.dyson_spinlaunch.renderPart("Payload");
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -2, 0);
                GlStateManager.scale(3.0D, 3.0D, 3.0D);
            }
            public void renderCommon() {
                GlStateManager.scale(0.25, 0.25, 0.25);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.dyson_spinlaunch_tex);
                ResourceManagerSpace.dyson_spinlaunch.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.dyson_launcher);
    }

}
