package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityDysonConverterTU;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderDysonConverterTU extends TileEntitySpecialRenderer<TileEntityDysonConverterTU> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityDysonConverterTU tileEntity, double x, double y, double z, float f, int destroyStage, float alpha) {
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
            bindTexture(ResourceManagerSpace.dyson_tu_converter_tex);
            ResourceManagerSpace.dyson_tu_converter.renderAll();

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(3, 1, 0);
                GlStateManager.scale(3.0D, 3.0D, 3.0D);
            }
            public void renderCommon() {
                GlStateManager.scale(0.55, 0.55, 0.55);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.dyson_tu_converter_tex);
                ResourceManagerSpace.dyson_tu_converter.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.dyson_converter_tu);
    }

}
