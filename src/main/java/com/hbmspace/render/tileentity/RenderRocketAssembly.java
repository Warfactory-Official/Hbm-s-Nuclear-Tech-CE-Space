package com.hbmspace.render.tileentity;

import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.handler.RocketStruct;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.render.misc.RocketPart;
import com.hbmspace.render.misc.RocketPronter;
import com.hbmspace.tileentity.machine.TileEntityMachineRocketAssembly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderRocketAssembly extends TileEntitySpecialRenderer<TileEntityMachineRocketAssembly> implements IItemRendererProviderSpace {

    public RenderRocketAssembly() { }

    @Override
    public void render(TileEntityMachineRocketAssembly te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();

        GlStateManager.translate((float)(x + 0.5), (float)(y - 2.0), (float)(z + 0.5));
        GlStateManager.enableLighting();
        GlStateManager.enableCull();

        switch (te.getBlockMetadata()) {
            case 2 -> GlStateManager.rotate(180F, 0F, 1F, 0F);
            case 4 -> GlStateManager.rotate(270F, 0F, 1F, 0F);
            case 3 -> GlStateManager.rotate(0F, 0F, 1F, 0F);
            case 5 -> GlStateManager.rotate(90F, 0F, 1F, 0F);
        }

        bindTexture(ResourceManagerSpace.rocket_assembly_tex);
        ResourceManagerSpace.rocket_assembly.renderPart("Base");

        if (te.rocket != null && te.rocket.extraIssues.size() == 0) {
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(0F, 2.95F, 0F);

                if (te.rocket.stages.size() > 0) {
                    RocketStruct.RocketStage stage = te.rocket.stages.get(0);
                    if (stage.thruster != null) {
                        GlStateManager.translate(0F, (float)(-RocketPart.getPart(stage.thruster).height), 0F);
                    }
                }

                RocketPronter.prontRocket(te.rocket, Minecraft.getMinecraft().getTextureManager(), false);
            }
            GlStateManager.popMatrix();

            bindTexture(ResourceManagerSpace.rocket_assembly_tex);

            for (int i = 0; i < te.rocket.stages.size(); i++) {
                RocketStruct.RocketStage stage = te.rocket.stages.get(i);
                RocketStruct.RocketStage nextStage = i < te.rocket.stages.size() - 1 ? te.rocket.stages.get(i + 1) : null;

                double targetHeight = 0;
                if (stage.fuselage != null) targetHeight += RocketPart.getPart(stage.fuselage).height * stage.getStack();
                if (nextStage != null && nextStage.thruster != null) targetHeight += RocketPart.getPart(nextStage.thruster).height;

                while (targetHeight > 1) {
                    GlStateManager.translate(0F, 1F, 0F);
                    ResourceManagerSpace.rocket_assembly.renderPart("Support");
                    targetHeight--;
                }
                GlStateManager.translate(0F, (float) targetHeight, 0F);

                ResourceManagerSpace.rocket_assembly.renderPart("Level");
            }
        }

        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0F, -1F, 0F);
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
            }
            public void renderCommon() {
                GlStateManager.scale(0.55F, 0.55F, 0.55F);
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.rocket_assembly_tex);
                ResourceManagerSpace.rocket_assembly.renderPart("Base");
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_rocket_assembly);
    }

}
