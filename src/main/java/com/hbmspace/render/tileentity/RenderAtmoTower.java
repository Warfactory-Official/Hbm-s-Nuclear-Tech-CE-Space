package com.hbmspace.render.tileentity;

import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityAtmoTower;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderAtmoTower extends TileEntitySpecialRenderer<TileEntityAtmoTower> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityAtmoTower tileEntity, double x, double y, double z, float f, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.0D, y, z + 0.0D);
        GlStateManager.enableLighting();
        GlStateManager.disableCull();
        GlStateManager.rotate(180F, 0F, 1F, 0F);
        switch(tileEntity.getBlockMetadata() - 10) {
            case 2:
                GlStateManager.rotate(0F, 0F, 1F, 0F);
                GlStateManager.translate(0F, 0F, -1F);
                break;
            case 3:
                GlStateManager.rotate(180F, 0F, 1F, 0F);
                GlStateManager.translate(1F, 0F, 0F);
                break;
            case 4:
                GlStateManager.rotate(90F, 0F, 1F, 0F);
                GlStateManager.translate(1F, 0F, -1F);
                break;
            case 5:
                GlStateManager.rotate(270F, 0F, 1F, 0F);
                break;
        }
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        bindTexture(ResourceManagerSpace.atmo_tower_tex);
        ResourceManagerSpace.atmo_tower.renderAll();
        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_atmo_tower);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -5, 0);
                GlStateManager.scale(4, 4, 4);
            }

            public void renderCommon() {
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.atmo_tower_tex); ResourceManagerSpace.atmo_tower.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }};
    }
}
