package com.hbmspace.render.entity.projectile;

import com.hbm.main.ResourceManager;
import com.hbmspace.entity.projectile.EntitySiegeLaser;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@AutoRegister(factory = "FACTORY")
public class RenderSiegeLaser extends Render<EntitySiegeLaser> {
    
    public static final IRenderFactory<EntitySiegeLaser> FACTORY = RenderSiegeLaser::new;

    protected RenderSiegeLaser(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntitySiegeLaser laser, double x, double y, double z, float f0, float f1) {

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(laser.prevRotationYaw + (laser.rotationYaw - laser.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(laser.prevRotationPitch + (laser.rotationPitch - laser.prevRotationPitch) * f1 + 180, 0.0F, 0.0F, 1.0F);

        this.renderDart(laser);

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(@NotNull EntitySiegeLaser entity) {
        return ResourceManager.universal;
    }

    private void renderDart(EntitySiegeLaser laser) {

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GlStateManager.depthMask(false);

        GlStateManager.scale(1F / 4F, 1F / 8F, 1F / 8F);
        GlStateManager.scale(-1, 1, 1);

        GlStateManager.scale(2, 2, 2);

        int color = laser.getColor();
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        // front
        buf.begin(4, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(3, -1, -1).color(r, g, b, 0).endVertex();
        buf.pos(3, 1, -1).color(r, g, b, 0).endVertex();

        buf.pos(3, -1, 1).color(r, g, b, 0).endVertex();
        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(3, 1, 1).color(r, g, b, 0).endVertex();

        buf.pos(3, -1, -1).color(r, g, b, 0).endVertex();
        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(3, -1, 1).color(r, g, b, 0).endVertex();

        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(3, 1, -1).color(r, g, b, 0).endVertex();
        buf.pos(3, 1, 1).color(r, g, b, 0).endVertex();
        tess.draw();

        // mid
        buf.begin(4, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(4, -0.5, -0.5).color(r, g, b, 255).endVertex();
        buf.pos(4, 0.5, -0.5).color(r, g, b, 255).endVertex();

        buf.pos(4, -0.5, 0.5).color(r, g, b, 255).endVertex();
        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(4, 0.5, 0.5).color(r, g, b, 255).endVertex();

        buf.pos(4, -0.5, -0.5).color(r, g, b, 255).endVertex();
        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(4, -0.5, 0.5).color(r, g, b, 255).endVertex();

        buf.pos(6, 0, 0).color(r, g, b, 255).endVertex();
        buf.pos(4, 0.5, -0.5).color(r, g, b, 255).endVertex();
        buf.pos(4, 0.5, 0.5).color(r, g, b, 255).endVertex();
        tess.draw();

        // tail
        buf.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(4, 0.5, -0.5).color(r, g, b, 255).endVertex();
        buf.pos(4, 0.5, 0.5).color(r, g, b, 255).endVertex();
        buf.pos(0, 0.5, 0.5).color(r, g, b, 0).endVertex();
        buf.pos(0, 0.5, -0.5).color(r, g, b, 0).endVertex();

        buf.pos(4, -0.5, -0.5).color(r, g, b, 255).endVertex();
        buf.pos(4, -0.5, 0.5).color(r, g, b, 255).endVertex();
        buf.pos(0, -0.5, 0.5).color(r, g, b, 0).endVertex();
        buf.pos(0, -0.5, -0.5).color(r, g, b, 0).endVertex();

        buf.pos(4, -0.5, 0.5).color(r, g, b, 255).endVertex();
        buf.pos(4, 0.5, 0.5).color(r, g, b, 255).endVertex();
        buf.pos(0, 0.5, 0.5).color(r, g, b, 0).endVertex();
        buf.pos(0, -0.5, 0.5).color(r, g, b, 0).endVertex();

        buf.pos(4, -0.5, -0.5).color(r, g, b, 255).endVertex();
        buf.pos(4, 0.5, -0.5).color(r, g, b, 255).endVertex();
        buf.pos(0, 0.5, -0.5).color(r, g, b, 0).endVertex();
        buf.pos(0, -0.5, -0.5).color(r, g, b, 0).endVertex();
        tess.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);

        GlStateManager.popMatrix();
    }
}
