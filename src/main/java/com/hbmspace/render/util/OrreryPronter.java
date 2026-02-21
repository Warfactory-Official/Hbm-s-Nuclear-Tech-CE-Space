package com.hbmspace.render.util;

import com.hbm.util.Vec3NT;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class OrreryPronter {

    private static long lastTick;
    private static float lastPartial;

    private static List<SolarSystem.OrreryMetric> metrics;

    public static void render(Minecraft mc, World world, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        CelestialBody sun = CelestialBody.getStar(world);

        // Update metrics
        if(metrics == null || lastTick != world.getTotalWorldTime() || lastPartial != partialTicks) {
            metrics = SolarSystem.calculatePositionsOrrery(world, partialTicks);
            lastTick = world.getTotalWorldTime();
            lastPartial = partialTicks;
        }

        // Setup glow
        // GlStateManager does not wrap glPushAttrib, so we use GL11 directly for this specific call.
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        GlStateManager.pushMatrix();
        {

            // Draw sun
            GlStateManager.pushMatrix();
            {

                mc.getTextureManager().bindTexture(sun.texture);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                SpaceRenderUtil.renderBlock(tessellator, 0.375, 0.625);

            }
            GlStateManager.popMatrix();

            // Scale so sun is 1x1x1
            double scale = 1 / Math.min(sun.radiusKm, SolarSystem.ORRERY_MAX_RADIUS);
            GlStateManager.scale((float)scale, (float)scale, (float)scale);


            // Draw bodies
            for(SolarSystem.OrreryMetric metric : metrics) {
                mc.getTextureManager().bindTexture(metric.body.texture);

                GlStateManager.pushMatrix();
                {

                    double bodyScale = metric.body.radiusKm;
                    if(bodyScale < 2_000) bodyScale = (bodyScale / 63) * (bodyScale / 63) + 1_000;

                    // Vec3NT works similarly to Vec3d (x, y, z fields). Note the swapped Y/Z in the input.
                    GlStateManager.translate((float)metric.position.x, (float)metric.position.z, (float)metric.position.y);
                    GlStateManager.scale((float)bodyScale, (float)bodyScale, (float)bodyScale);

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    SpaceRenderUtil.renderBlock(tessellator);

                }
                GlStateManager.popMatrix();

                GlStateManager.disableTexture2D();
                GlStateManager.color(metric.body.color[0], metric.body.color[1], metric.body.color[2]);

                buffer.begin(org.lwjgl.opengl.GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

                for(int i = 1; i < metric.orbitalPath.length; i++) {
                    Vec3NT from = metric.orbitalPath[i-1];
                    Vec3NT to = metric.orbitalPath[i];

                    buffer.pos(from.x, from.z, from.y).endVertex();
                    buffer.pos(to.x, to.z, to.y).endVertex();
                }

                Vec3NT first = metric.orbitalPath[0];
                Vec3NT last = metric.orbitalPath[metric.orbitalPath.length - 1];

                buffer.pos(last.x, last.z, last.y).endVertex();
                buffer.pos(first.x, first.z, first.y).endVertex();

                tessellator.draw();

                GlStateManager.enableTexture2D();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

        }
        GlStateManager.popMatrix();


        GlStateManager.disableBlend();
        GL11.glPopAttrib();
    }

}
