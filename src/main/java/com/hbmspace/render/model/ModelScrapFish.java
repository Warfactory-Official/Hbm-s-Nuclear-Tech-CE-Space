package com.hbmspace.render.model;

import com.hbmspace.main.ResourceManagerSpace;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ModelScrapFish extends ModelBase {

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float rotationYaw, float rotationHeadYaw, float rotationPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, rotationYaw, rotationHeadYaw, rotationPitch, scale);

        GlStateManager.pushMatrix();
        {

            double cy0 = Math.sin(limbSwing % (Math.PI * 2));
            double cy1 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.2);
            double cy2 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.4);
            double cy3 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.6);
            double cy4 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.8);
            double cy5 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI);

            GlStateManager.rotate(180.0F, 0, 0, 1);
            GlStateManager.translate(0, -1.5F, 0);

            ResourceManagerSpace.scrapfish.renderPart("Body");

            // Head
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(0, 0, -0.3125F);
                GlStateManager.rotate(rotationPitch, 1, 0, 0);
                GlStateManager.rotate(rotationHeadYaw, 0, 1, 0);
                GlStateManager.translate(0, 0, 0.3125F);
                ResourceManagerSpace.scrapfish.renderPart("Head");

            }
            GlStateManager.popMatrix();

            // Side fins
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy0 * 20), 1, 0, 0);
                ResourceManagerSpace.scrapfish.renderPart("FinL");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy0 * -20), 1, 0, 0);
                ResourceManagerSpace.scrapfish.renderPart("FinR");
            }
            GlStateManager.popMatrix();

            //Ventral fins
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * 20), 1, 0, 0);
                ResourceManagerSpace.scrapfish.renderPart("VentralFL");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * -20), 1, 0, 0);
                ResourceManagerSpace.scrapfish.renderPart("VentralFR");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy2 * 20), 1, 0, 0);
                ResourceManagerSpace.scrapfish.renderPart("VentralBL");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy2 * -20), 1, 0, 0);
                ResourceManagerSpace.scrapfish.renderPart("VentralBR");
            }
            GlStateManager.popMatrix();

            // Tail fin
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy3 * 10), 0, 1, 0);
                ResourceManagerSpace.scrapfish.renderPart("Dorsal");

                GlStateManager.rotate((float)(cy4 * 6), 0, 1, 0);
                ResourceManagerSpace.scrapfish.renderPart("Tail");

                GlStateManager.rotate((float)(cy5 * 4), 0, 1, 0);
                ResourceManagerSpace.scrapfish.renderPart("TailFin");
            }
            GlStateManager.popMatrix();

        }
        GlStateManager.popMatrix();
    }

}