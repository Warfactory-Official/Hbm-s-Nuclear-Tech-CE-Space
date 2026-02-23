package com.hbmspace.render.model;

import com.hbmspace.main.ResourceManagerSpace;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ModelScutter extends ModelBase {

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float rotationYaw, float rotationHeadYaw, float rotationPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, rotationYaw, rotationHeadYaw, rotationPitch, scale);

        GlStateManager.pushMatrix();
        {

            double cy0 = Math.sin(limbSwing % (Math.PI * 2));
            double cy1 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.2);
            double cy2 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.4);
            double cy3 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.6);

            GlStateManager.rotate(180.0F, 0, 0, 1);
            GlStateManager.translate(0, -1.5F, 0);

            ResourceManagerSpace.scutterfish.renderPart("body");

            // Head
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(rotationPitch, 1, 0, 0);
                GlStateManager.rotate(rotationHeadYaw, 0, 1, 0);
                ResourceManagerSpace.scutterfish.renderPart("head");
            }
            GlStateManager.popMatrix();

            // Side fins
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy0 * 20), 0, 0, 1);
                ResourceManagerSpace.scutterfish.renderPart("leftfin1");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy0 * -20), 0, 0, 1);
                ResourceManagerSpace.scutterfish.renderPart("rightfin1");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * 20), 0, 0, 1);
                ResourceManagerSpace.scutterfish.renderPart("leftfin2");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * -20), 0, 0, 1);
                ResourceManagerSpace.scutterfish.renderPart("rightfin2");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy2 * 20), 0, 0, 1);
                ResourceManagerSpace.scutterfish.renderPart("leftfin3");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy2 * -20), 0, 0, 1);
                ResourceManagerSpace.scutterfish.renderPart("rightfin3");
            }
            GlStateManager.popMatrix();

            // Tail fin
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * 10), 0, 0, 1);
                GlStateManager.rotate((float)(cy2 * -5), 1, 0, 0);
                ResourceManagerSpace.scutterfish.renderPart("tailmeat");
                GlStateManager.rotate((float)(cy3 * -5), 1, 0, 0);
                ResourceManagerSpace.scutterfish.renderPart("tailthin");
            }
            GlStateManager.popMatrix();

        }
        GlStateManager.popMatrix();
    }

}