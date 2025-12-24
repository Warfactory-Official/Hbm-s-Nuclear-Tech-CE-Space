package com.hbmspace.render.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModelMoonCow extends ModelCow {

    private static final ResourceLocation glass = new ResourceLocation("hbm", "textures/blocks/glass_boron.png");

    @Override
    public void render(@NotNull Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);

        GlStateManager.pushMatrix();
        {

            Minecraft.getMinecraft().renderEngine.bindTexture(glass);

            if(isChild) {
                GlStateManager.translate(0.0F, this.childYOffset * f5, this.childZOffset * f5);
            }

            GlStateManager.translate(head.offsetX, head.offsetY, head.offsetZ);

            GlStateManager.translate(head.rotationPointX * f5, head.rotationPointY * f5, head.rotationPointZ * f5);
            GlStateManager.rotate(head.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(head.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(head.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);

            GlStateManager.translate(0, 0, -0.25F);

            GlStateManager.scale(0.7F, 0.7F, 0.7F);

            renderBlock(Tessellator.getInstance());

        }
        GlStateManager.popMatrix();
    }

    public static void renderBlock(Tessellator tessellator) {
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(org.lwjgl.opengl.GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(-0.5, +0.5, -0.5).tex(1, 1).endVertex();
        buffer.pos(+0.5, +0.5, -0.5).tex(0, 1).endVertex();
        buffer.pos(+0.5, -0.5, -0.5).tex(0, 0).endVertex();
        buffer.pos(-0.5, -0.5, -0.5).tex(1, 0).endVertex();

        buffer.pos(-0.5, +0.5, +0.5).tex(1, 1).endVertex();
        buffer.pos(-0.5, +0.5, -0.5).tex(0, 1).endVertex();
        buffer.pos(-0.5, -0.5, -0.5).tex(0, 0).endVertex();
        buffer.pos(-0.5, -0.5, +0.5).tex(1, 0).endVertex();

        buffer.pos(+0.5, +0.5, +0.5).tex(1, 1).endVertex();
        buffer.pos(-0.5, +0.5, +0.5).tex(0, 1).endVertex();
        buffer.pos(-0.5, -0.5, +0.5).tex(0, 0).endVertex();
        buffer.pos(+0.5, -0.5, +0.5).tex(1, 0).endVertex();

        buffer.pos(+0.5, +0.5, -0.5).tex(1, 1).endVertex();
        buffer.pos(+0.5, +0.5, +0.5).tex(0, 1).endVertex();
        buffer.pos(+0.5, -0.5, +0.5).tex(0, 0).endVertex();
        buffer.pos(+0.5, -0.5, -0.5).tex(1, 0).endVertex();

        buffer.pos(-0.5, -0.5, -0.5).tex(1, 1).endVertex();
        buffer.pos(+0.5, -0.5, -0.5).tex(0, 1).endVertex();
        buffer.pos(+0.5, -0.5, +0.5).tex(0, 0).endVertex();
        buffer.pos(-0.5, -0.5, +0.5).tex(1, 0).endVertex();

        buffer.pos(+0.5, +0.5, -0.5).tex(1, 1).endVertex();
        buffer.pos(-0.5, +0.5, -0.5).tex(0, 1).endVertex();
        buffer.pos(-0.5, +0.5, +0.5).tex(0, 0).endVertex();
        buffer.pos(+0.5, +0.5, +0.5).tex(1, 0).endVertex();

        tessellator.draw();
    }

}
