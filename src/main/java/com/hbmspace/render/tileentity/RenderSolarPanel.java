package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineSolarPanel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderSolarPanel extends TileEntitySpecialRenderer<TileEntityMachineSolarPanel> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineSolarPanel te, double x, double y, double z, float interp, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y, z + 0.5D);
        GlStateManager.enableLighting();

        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 0F, 1F, 0F);

        switch(te.getBlockMetadata() - BlockDummyable.offset) {
            case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
            case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
            case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
            case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
        }

        bindTexture(ResourceManagerSpace.solarp_tex);

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        ResourceManagerSpace.solarp.renderAll();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }
    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -3, 0);
                GlStateManager.scale(2.75, 2.75, 2.75);
            }
            public void renderCommon() {
                GlStateManager.scale(1, 1, 1);
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.solarp_tex);
                ResourceManagerSpace.solarp.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }};
    }
    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_solar);
    }

}
