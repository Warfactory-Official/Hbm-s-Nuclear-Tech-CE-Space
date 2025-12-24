package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityDysonConverterAnatmogenesis;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderDysonConverterAnatmogenesis extends TileEntitySpecialRenderer<TileEntityDysonConverterAnatmogenesis> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityDysonConverterAnatmogenesis tileEntity, double x, double y, double z, float f, int destroyStage, float alpha) {
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
            bindTexture(ResourceManagerSpace.dyson_anatmogenesis_tex);
            ResourceManagerSpace.dyson_anatmogenesis.renderPart("Anatmogenesis");

            if(tileEntity.isConverting) {
                float t = tileEntity.getWorld().getTotalWorldTime() + f;

                GlStateManager.translate(0, Math.sin(t * 0.1) * 0.025 + tileEntity.getWorld().rand.nextFloat() * 0.02, 0);
            }

            ResourceManagerSpace.dyson_anatmogenesis.renderPart("Coils");

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -2, 0);
                GlStateManager.scale(2.5D, 2.5D, 2.5D);
            }
            public void renderCommon() {
                GlStateManager.scale(0.55, 0.55, 0.55);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.dyson_anatmogenesis_tex);
                ResourceManagerSpace.dyson_anatmogenesis.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.dyson_converter_anatmogenesis);
    }

}
