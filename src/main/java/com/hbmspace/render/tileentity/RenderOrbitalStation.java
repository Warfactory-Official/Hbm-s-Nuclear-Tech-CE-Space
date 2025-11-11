package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.tileentity.IItemRendererProvider;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStation;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderOrbitalStation extends TileEntitySpecialRenderer<TileEntityOrbitalStation> implements IItemRendererProvider {

    @Override
    public void render(TileEntityOrbitalStation station, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y + 1.0D, z + 0.5D);
            GlStateManager.enableLighting();

            switch (station.getBlockMetadata() - BlockDummyable.offset) {
                case 2 -> GlStateManager.rotate(0, 0F, 1F, 0F);
                case 4 -> GlStateManager.rotate(90, 0F, 1F, 0F);
                case 3 -> GlStateManager.rotate(180, 0F, 1F, 0F);
                case 5 -> GlStateManager.rotate(270, 0F, 1F, 0F);
            }

            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            bindTexture(ResourceManagerSpace.docking_port_tex);
            ResourceManagerSpace.docking_port.renderPart("Port");

            float rotation = station.prevRot + (station.rot - station.prevRot) * partialTicks;

            for(int i = 0; i < 4; i++) {
                GlStateManager.pushMatrix();
                {

                    // one hop this time
                    GlStateManager.translate(0, -1.75F, -2);

                    // criss cross
                    GlStateManager.rotate(-rotation, 1, 0, 0);

                    // one hop this time
                    GlStateManager.translate(0, 1.75F, 2);

                    // let's go to work
                    ResourceManagerSpace.docking_port.renderPart("ArmZP");

                }
                GlStateManager.popMatrix();

                // cha cha real smooth
                GlStateManager.rotate(90, 0, 1, 0);
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public Item[] getItemsForRenderer(){
        return new Item[] {
                Item.getItemFromBlock(ModBlocksSpace.orbital_station),
                Item.getItemFromBlock(ModBlocksSpace.orbital_station_port)
        };
    }
    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.orbital_station_port);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, 2, 0);
                GlStateManager.scale(2, 2, 2);
            }

            public void renderCommon() {
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.docking_port_tex);
                ResourceManagerSpace.docking_port.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }
        };
    }

}
