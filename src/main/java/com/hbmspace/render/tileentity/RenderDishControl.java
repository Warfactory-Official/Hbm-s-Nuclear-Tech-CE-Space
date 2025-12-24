package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityDishControl;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderDishControl extends TileEntitySpecialRenderer<TileEntityDishControl> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityDishControl te, double x, double y, double z, float interp, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            GlStateManager.rotate(90, 0, 1, 0);

            switch(te.getBlockMetadata() - BlockDummyable.offset) {
                case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
                case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
                case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
                case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
            }

            bindTexture(ResourceManagerSpace.dish_controller_tex);
            ResourceManagerSpace.dish_controller.renderPart("dish_control");
            ResourceManagerSpace.dish_controller.renderPart("joystick");

            if (te.starDarHasDisk()) {
                ResourceManagerSpace.dish_controller.renderPart("drive");
            }

            //if(te.isLinked) {
            //TileEntityMachineStardar stardar = (TileEntityMachineStardar) te.getWorldObj().getTileEntity(te.linkPosition[0], te.linkPosition[1], te.linkPosition[2]);
            //}

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -0.5, 0);
                GlStateManager.scale(6D, 6D, 6D);
            }
            public void renderCommon() {
                GlStateManager.translate(0.5, 0, 0);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.dish_controller_tex);
                ResourceManagerSpace.dish_controller.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_dish_controller);
    }
}
