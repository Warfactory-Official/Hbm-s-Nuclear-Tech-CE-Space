package com.hbmspace.render.model;

import com.hbmspace.main.ResourceManagerSpace;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ModelScuttlecrab extends ModelBase {

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float rotationYaw, float rotationHeadYaw, float rotationPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, rotationYaw, rotationHeadYaw, rotationPitch, scale);

        GlStateManager.pushMatrix();
        {

            double cy0 = Math.sin(limbSwing * 6 % (Math.PI * 2));
            double cy1 = Math.sin(limbSwing * 6 % (Math.PI * 2) - Math.PI * 0.4);
            double cy2 = Math.sin(limbSwing * 6 % (Math.PI * 2) - Math.PI * 0.8);
            double cy3 = Math.sin(limbSwing * 6 % (Math.PI * 2) - Math.PI * 1.2);
            double cy4 = Math.sin(limbSwing * 6 % (Math.PI * 2) - Math.PI * 1.6);

            GlStateManager.rotate(180.0F, 0, 0, 1);
            GlStateManager.translate(0, -1.5F, 0);

            ResourceManagerSpace.scuttlecrab.renderPart("Body");

            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(-0.125F, 0.5625F, -0.3125F);
                GlStateManager.rotate(rotationPitch, 1, 0, 0);
                GlStateManager.rotate(rotationHeadYaw, 0, 1, 0);
                GlStateManager.translate(0.125F, -0.5625F, 0.3125F);
                ResourceManagerSpace.scuttlecrab.renderPart("EyeL");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(0.125F, 0.5625F, -0.3125F);
                GlStateManager.rotate(rotationPitch, 1, 0, 0);
                GlStateManager.rotate(rotationHeadYaw, 0, 1, 0);
                GlStateManager.translate(-0.125F, -0.5625F, 0.3125F);
                ResourceManagerSpace.scuttlecrab.renderPart("EyeR");
            }
            GlStateManager.popMatrix();


            ResourceManagerSpace.scuttlecrab.renderPart("ClawL");
            ResourceManagerSpace.scuttlecrab.renderPart("ClawR");


            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy0 * 8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegL1");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * 8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegL2");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy2 * 8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegL3");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy3 * 8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegL4");
            }
            GlStateManager.popMatrix();


            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * -8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegR1");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy2 * -8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegR2");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy3 * -8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegR3");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy4 * -8), 0, 0, 1);
                ResourceManagerSpace.scuttlecrab.renderPart("LegR4");
            }
            GlStateManager.popMatrix();

        }
        GlStateManager.popMatrix();
    }

}