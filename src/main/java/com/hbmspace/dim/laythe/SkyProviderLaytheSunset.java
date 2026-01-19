package com.hbmspace.dim.laythe;

import com.hbmspace.dim.SkyProviderCelestial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11; import net.minecraft.client.renderer.GlStateManager;

public class SkyProviderLaytheSunset extends SkyProviderCelestial {

    public SkyProviderLaytheSunset() {
        super();
    }

    @Override
    protected void renderSunset(float partialTicks, WorldClient world, Minecraft mc, float solarAngle, float pressure, ResourceLocation surfaceTexture) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float[] sunsetColor = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

        if (sunsetColor != null) {
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            float[] anaglyphColor = mc.gameSettings.anaglyph ? applyAnaglyph(sunsetColor) : sunsetColor;
            byte segments = 16;

            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);

                buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(0.0D, 150.0D, 0.0D).color(anaglyphColor[0], anaglyphColor[1], anaglyphColor[2], sunsetColor[3]).endVertex();

                for (int j = 0; j <= segments; ++j) {
                    float angle = (float) j * (float) Math.PI * 2.0F / (float) segments;
                    float sin = MathHelper.sin(angle);
                    float cos = MathHelper.cos(angle);
                    buffer.pos(sin * 160.0F, cos * 160.0F, -cos * 90.0F * sunsetColor[3])
                            .color(sunsetColor[0], sunsetColor[1], sunsetColor[2], 0.0F).endVertex();
                }
                tessellator.draw();
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(135.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -60.0F, 0.0F);

                buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(0.0D, 100.0D, 0.0D).color(anaglyphColor[0], anaglyphColor[1], anaglyphColor[2], sunsetColor[3]).endVertex();

                for (int j = 0; j <= segments; ++j) {
                    float angle = (float) j * (float) Math.PI * 2.0F / (float) segments;
                    float sin = MathHelper.sin(angle);
                    float cos = MathHelper.cos(angle);
                    buffer.pos(sin * 100.0F, cos * 100.0F, -cos * 90.0F)
                            .color(sunsetColor[0], sunsetColor[1], sunsetColor[2], 0.0F).endVertex();
                }
                tessellator.draw();
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(135.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -30.0F, 0.0F);

                buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(0.0D, 80.0D, 0.0D).color(anaglyphColor[0], anaglyphColor[1], anaglyphColor[2], sunsetColor[3]).endVertex();

                for (int j = 0; j <= segments; ++j) {
                    float angle = (float) j * (float) Math.PI * 2.0F / (float) segments;
                    float sin = MathHelper.sin(angle);
                    float cos = MathHelper.cos(angle);
                    buffer.pos(sin * 100.0F, cos * 100.0F, -cos * 90.0F)
                            .color(sunsetColor[0], sunsetColor[1] * 0.2F, sunsetColor[2], 0.0F).endVertex();
                }
                tessellator.draw();
            }
            GlStateManager.popMatrix();

            GlStateManager.shadeModel(GL11.GL_FLAT);
            GlStateManager.enableTexture2D();
        }
    }


}
