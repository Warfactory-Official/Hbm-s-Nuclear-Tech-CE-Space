package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.oil.TileEntityMachineAlkylation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderAlkylation extends TileEntitySpecialRenderer<TileEntityMachineAlkylation> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineAlkylation tile, double x, double y, double z, float f, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y, z + 0.5);
        GlStateManager.enableLighting();
        GlStateManager.enableCull();

        switch(tile.getBlockMetadata() - BlockDummyable.offset) {
            case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
            case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
            case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
            case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
        }

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        bindTexture(ResourceManagerSpace.alkyl_tex);
        ResourceManagerSpace.alkyl.renderAll();
        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_alkylation);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -2, 0);
                GlStateManager.scale(5, 5, 5);
            }
            public void renderCommon() {
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.alkyl_tex);
                ResourceManagerSpace.alkyl.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }};
    }
}
