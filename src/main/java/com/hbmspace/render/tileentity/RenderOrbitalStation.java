package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.render.misc.RocketPronter;
import com.hbmspace.tileentity.machine.TileEntityOrbStation;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStation;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStationLauncher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderOrbitalStation extends TileEntitySpecialRenderer<TileEntityOrbStation> implements IItemRendererProviderSpace {

    @Override
    public void render(@NotNull TileEntityOrbStation te, double x, double y, double z, float interp, int destroyStage, float alpha) {
        float armRotation;
        if(te instanceof TileEntityOrbitalStation orbitalStation) {
            armRotation = orbitalStation.prevRot + (orbitalStation.rot - orbitalStation.prevRot) * interp;
            bindTexture(ResourceManagerSpace.docking_port_tex);
        } else if(te instanceof TileEntityOrbitalStationLauncher launcher) {
            armRotation = launcher.prevRot + (launcher.rot - launcher.prevRot) * interp;
            bindTexture(ResourceManagerSpace.docking_port_launcher_tex);
        } else {
            return;
        }

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y + 1.0D, z + 0.5D);
            GlStateManager.enableLighting();

            switch(te.getBlockMetadata() - BlockDummyable.offset) {
                case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
                case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
                case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
                case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
            }

            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            ResourceManagerSpace.docking_port.renderPart("Port");

            for(int i = 0; i < 4; i++) {
                GlStateManager.pushMatrix();
                {

                    // one hop this time
                    GlStateManager.translate(0, -1.75F, -2);

                    // criss cross
                    GlStateManager.rotate(-armRotation, 1, 0, 0);

                    // one hop this time
                    GlStateManager.translate(0, 1.75F, 2);

                    // let's go to work
                    ResourceManagerSpace.docking_port.renderPart("ArmZP");

                }
                GlStateManager.popMatrix();

                // cha cha real smooth
                GlStateManager.rotate(90, 0, 1, 0);
            }

            if(te instanceof TileEntityOrbitalStationLauncher launcher) {

                if(launcher.rocket != null && launcher.rocket.extraIssues.isEmpty()) {
                    GlStateManager.pushMatrix();
                    {

                        GlStateManager.translate(0, -launcher.rocket.getHeight() - 0.5, 0);

                        RocketPronter.prontRocket(launcher.rocket, Minecraft.getMinecraft().getTextureManager(), false);

                    }
                    GlStateManager.popMatrix();
                }
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, 2, 0);
                GlStateManager.scale(2, 2, 2);
            }
            public void renderCommon(ItemStack stack) {
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                ItemBlock itemBlock = (ItemBlock) stack.getItem();
                if(itemBlock.getBlock() == ModBlocksSpace.orbital_station_launcher) {
                    bindTexture(ResourceManagerSpace.docking_port_launcher_tex);
                } else {
                    bindTexture(ResourceManagerSpace.docking_port_tex);
                }
                ResourceManagerSpace.docking_port.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.orbital_station_port);
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {
                Item.getItemFromBlock(ModBlocksSpace.orbital_station),
                Item.getItemFromBlock(ModBlocksSpace.orbital_station_port),
                Item.getItemFromBlock(ModBlocksSpace.orbital_station_launcher),
        };
    }

}
