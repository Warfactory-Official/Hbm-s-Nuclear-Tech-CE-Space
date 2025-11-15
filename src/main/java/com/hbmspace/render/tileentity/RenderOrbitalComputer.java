package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStationComputer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderOrbitalComputer extends TileEntitySpecialRenderer<TileEntityOrbitalStationComputer> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityOrbitalStationComputer computer, double x, double y, double z, float interp, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();

            switch (computer.getBlockMetadata() - BlockDummyable.offset) {
                case 2 -> GlStateManager.rotate(0, 0F, 1F, 0F);
                case 4 -> GlStateManager.rotate(90, 0F, 1F, 0F);
                case 3 -> GlStateManager.rotate(180, 0F, 1F, 0F);
                case 5 -> GlStateManager.rotate(270, 0F, 1F, 0F);
            }

            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            bindTexture(ResourceManagerSpace.orbital_computer_tex);
            ResourceManagerSpace.orbital_computer.renderAllExcept("Drive");

            if(computer.hasDrive) {
                bindTexture(ResourceManagerSpace.drive_processor_tex);
                ResourceManagerSpace.orbital_computer.renderPart("Drive");
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -2.5, 0);
                GlStateManager.scale(6, 6, 6);
            }
            public void renderCommon() {
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.orbital_computer_tex);
                ResourceManagerSpace.orbital_computer.renderAllExcept("Drive");
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.orbital_station_computer);
    }

}
