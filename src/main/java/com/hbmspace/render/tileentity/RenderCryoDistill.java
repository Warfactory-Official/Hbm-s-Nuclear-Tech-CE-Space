package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineCryoDistill;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderCryoDistill extends TileEntitySpecialRenderer<TileEntityMachineCryoDistill> implements IItemRendererProviderSpace {


    @Override
    public void render(TileEntityMachineCryoDistill tile, double x, double y, double z, float f, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y - 2.0, z + 0.5);
        GlStateManager.enableLighting();
        GlStateManager.enableCull();

        GlStateManager.rotate(180, 0, 1, 0);

        switch(tile.getBlockMetadata() - BlockDummyable.offset) {
            case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
            case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
            case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
            case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
        }

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        bindTexture(ResourceManagerSpace.cryodistill_tex);
        ResourceManagerSpace.cryo_distill.renderAll();
        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_cryo_distill);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -3, 0);
                GlStateManager.scale(3.5, 3.5, 3.5);
            }
            public void renderCommon() {
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.cryodistill_tex);
                ResourceManagerSpace.cryo_distill.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }};
    }
}
