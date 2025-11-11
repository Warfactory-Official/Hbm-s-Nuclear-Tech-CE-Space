package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineVacuumCircuit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
@AutoRegister
public class RenderVacuumCircuit extends TileEntitySpecialRenderer<TileEntityMachineVacuumCircuit> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineVacuumCircuit solderer, double x, double y, double z, float interp, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5, y, z + 0.5);
            GlStateManager.enableLighting();
            GlStateManager.enableAlpha();
            GlStateManager.enableCull();

            switch (solderer.getBlockMetadata() - BlockDummyable.offset) {
                case 2 -> GlStateManager.rotate(90, 0F, 1F, 0F);
                case 4 -> GlStateManager.rotate(180, 0F, 1F, 0F);
                case 3 -> GlStateManager.rotate(270, 0F, 1F, 0F);
                case 5 -> GlStateManager.rotate(0, 0F, 1F, 0F);
            }

            bindTexture(ResourceManagerSpace.vac_cir_station_tex);
            ResourceManagerSpace.vac_cir_station.renderAll();

            // Do not change the translation and scale here, it's a 1.12.2 specific workaround
            if (!solderer.display.isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0D, 1.125D, 0D);
                GlStateManager.enableLighting();
                GlStateManager.rotate(90, 0F, 1F, 0F);
                GlStateManager.rotate(-90, 1F, 0F, 0F);
                ItemStack stack = solderer.display.copy();
                stack.setCount(1);
                // 0.5128205 * 1.5 = 0.76923075, net.minecraft.client.renderer.entity.RenderItem line 203
                GlStateManager.scale(0.76923075F, 0.76923075F, 0.76923075F);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
                GlStateManager.popMatrix();
            }

        }
        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_vacuum_circuit);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase( ) {
            public void renderInventory() {
                GlStateManager.translate(0, -1, 0);
                GlStateManager.scale(3.5, 3.5, 3.5);
            }
            public void renderCommon() {
                bindTexture(ResourceManagerSpace.vac_cir_station_tex);
                ResourceManagerSpace.vac_cir_station.renderAll();
            }};
    }
}
