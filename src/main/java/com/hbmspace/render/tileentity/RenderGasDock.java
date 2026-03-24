package com.hbmspace.render.tileentity;

import com.hbm.render.item.ItemRenderBase;
import com.hbm.main.ResourceManager;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.tileentity.machine.TileEntityMachineGasDock;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

@AutoRegister
public class RenderGasDock extends TileEntitySpecialRenderer<TileEntityMachineGasDock> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineGasDock tileentity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.rotate(180, 0F, 0F, 1F);

        GlStateManager.enableLighting();

        GlStateManager.rotate(180, 0F, 0F, 1F);
        GlStateManager.translate(0, -1.5F, 0);

        this.bindTexture(ResourceManagerSpace.sat_dock_tex);
        ResourceManagerSpace.sat_dock.renderAll();

        if (tileentity.launchTicks < 100) {
            GlStateManager.pushMatrix();

            float lift = 0.75F + MathHelper.clamp(tileentity.launchTicks + (tileentity.hasRocket ? -partialTicks : partialTicks), 0, 200);

            GlStateManager.translate(0.0F, lift, 0.0F);
            GlStateManager.disableCull();

            this.bindTexture(ResourceManager.minerRocket_tex);
            ResourceManager.minerRocket.renderAll();

            GlStateManager.enableCull();

            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.gas_dock);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.scale(3, 3, 3);
            }
            public void renderCommon() {
                GlStateManager.rotate(90, 0, -1, 0);
                bindTexture(ResourceManagerSpace.sat_dock_tex); ResourceManagerSpace.sat_dock.renderAll();
            }
        };
    }
}
