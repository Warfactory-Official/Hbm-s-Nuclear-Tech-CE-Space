package com.hbmspace.render.model;

import com.hbmspace.main.ResourceManagerSpace;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ModelSifterEel extends ModelBase {

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float rotationYaw, float rotationHeadYaw, float rotationPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, rotationYaw, rotationHeadYaw, rotationPitch, scale);

        GlStateManager.pushMatrix();
        {

            double cy0 = Math.sin(limbSwing % (Math.PI * 2));
            double cy1 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.2);
            double cy2 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.4);
            double cy3 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.6);
            double cy4 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.9);
            double cy5 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 1.2);
            double cy6 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 1.5);
            double cy7 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 1.9);

            GlStateManager.rotate(180.0F, 0, 0, 1);
            GlStateManager.translate(0, -1.5F, 0);

            // Head
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(0, 0, -0.5F);
                GlStateManager.rotate(rotationPitch, 1, 0, 0);
                GlStateManager.rotate(rotationHeadYaw, 0, 1, 0);
                GlStateManager.translate(0, 0, 0.5F);
                ResourceManagerSpace.sifter_eel.renderPart("Head");
                ResourceManagerSpace.sifter_eel.renderPart("Jaw");

            }
            GlStateManager.popMatrix();

            // Side fins
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy0 * 20), 1, 0, 0);
                ResourceManagerSpace.sifter_eel.renderPart("FinL");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy0 * -20), 1, 0, 0);
                ResourceManagerSpace.sifter_eel.renderPart("FinR");
            }
            GlStateManager.popMatrix();

            // Tail fin
            GlStateManager.pushMatrix();
            {
                ResourceManagerSpace.sifter_eel.renderPart("Body1");

                GlStateManager.rotate((float)(cy1 * 10), 0, 1, 0);
                GlStateManager.rotate((float)(cy2 * -2), 0, 1, 0);
                ResourceManagerSpace.sifter_eel.renderPart("Dorsal1");


                ResourceManagerSpace.sifter_eel.renderPart("Body2");
                GlStateManager.rotate((float)(cy3 * -2), 0, 1, 0);
                ResourceManagerSpace.sifter_eel.renderPart("Dorsal2");
                ResourceManagerSpace.sifter_eel.renderPart("Dorsal3");
                GlStateManager.rotate((float)(cy4 * -6), 0, 1, 0);

                ResourceManagerSpace.sifter_eel.renderPart("Body3");

                ResourceManagerSpace.sifter_eel.renderPart("Ventral1");
                ResourceManagerSpace.sifter_eel.renderPart("Ventral2");
                ResourceManagerSpace.sifter_eel.renderPart("Ventral3");
                GlStateManager.rotate((float)(cy5 * -4), 0, 1, 0);
                ResourceManagerSpace.sifter_eel.renderPart("Dorsal4");
                ResourceManagerSpace.sifter_eel.renderPart("Ventral4");
                ResourceManagerSpace.sifter_eel.renderPart("Body4");
                GlStateManager.rotate((float)(cy6 * -6), 0, 1, 0);
                ResourceManagerSpace.sifter_eel.renderPart("Body5");

                ResourceManagerSpace.sifter_eel.renderPart("Body6");

                GlStateManager.rotate((float)(cy7 * -5), 0, 1, 0);
                ResourceManagerSpace.sifter_eel.renderPart("Tail");

            }

            GlStateManager.popMatrix();

        }
        GlStateManager.popMatrix();
    }

}