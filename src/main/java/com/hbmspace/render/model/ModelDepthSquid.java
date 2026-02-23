package com.hbmspace.render.model;

import com.hbmspace.main.ResourceManagerSpace;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import org.jetbrains.annotations.NotNull;

public class ModelDepthSquid extends ModelBase {

    private float interp; // why not just pass this into the fucking render method, Morbwank?

    @Override
    public void setLivingAnimations(@NotNull EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.interp = partialTickTime;
    }

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float rotationYaw, float rotationHeadYaw, float rotationPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, rotationYaw, rotationHeadYaw, rotationPitch, scale);

        float tentacleAngle = 0;
        float squidPitch = 0;
        if(entity instanceof EntitySquid squid) {

            tentacleAngle = squid.lastTentacleAngle + (squid.tentacleAngle - squid.lastTentacleAngle) * interp;
            squidPitch = squid.prevSquidPitch + (squid.squidPitch - squid.prevSquidPitch) * interp;
        }

        GlStateManager.pushMatrix();
        {
            double cy0 = Math.sin(limbSwing % (Math.PI * 2));
            double cy1 = Math.sin(limbSwing % (Math.PI * 2) - Math.PI * 0.4);

            GlStateManager.translate(0F, 1.0F, 0F);
            GlStateManager.rotate(squidPitch, 1F, 0F, 0F);

            ResourceManagerSpace.depthsquid.renderPart("Body");

            // Side fins
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * 20), 1F, 0F, 0F);
                GlStateManager.rotate((float)(cy0 * 10), 0F, 1F, 0F);
                ResourceManagerSpace.depthsquid.renderPart("FinL");
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate((float)(cy1 * 20), 1F, 0F, 0F);
                GlStateManager.rotate((float)(cy0 * -10), 0F, 1F, 0F);
                ResourceManagerSpace.depthsquid.renderPart("FinR");
            }
            GlStateManager.popMatrix();

            // dibblies
            for(int i = 0; i < 8; i++) {
                GlStateManager.pushMatrix();
                {
                    GlStateManager.translate(0D, -0.9D, 0D);
                    GlStateManager.rotate((float)i * -45.0F, 0F, 1F, 0F);
                    GlStateManager.rotate(tentacleAngle * (180F / (float)Math.PI), 1F, 0F, 0F);
                    GlStateManager.translate(0D, 0.9D, 0D);
                    ResourceManagerSpace.depthsquid.renderPart("T0");
                }
                GlStateManager.popMatrix();
            }

        }
        GlStateManager.popMatrix();
    }

}
