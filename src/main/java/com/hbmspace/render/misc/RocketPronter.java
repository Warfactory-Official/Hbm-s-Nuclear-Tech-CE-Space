package com.hbmspace.render.misc;

import com.hbmspace.entity.missile.EntityRideableRocket;
import com.hbmspace.handler.RocketStruct;
import com.hbm.main.ResourceManager;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class RocketPronter {
    private static DoubleBuffer buffer;

    public static void prontRocket(RocketStruct rocket, TextureManager tex) {
        prontRocket(rocket, null, tex, true, 0, 0, 0);
    }

    public static void prontRocket(RocketStruct rocket, TextureManager tex, boolean isDeployed) {
        prontRocket(rocket, null, tex, isDeployed, 0, 0, 0);
    }

    // Attaches a set of stages together
    public static void prontRocket(RocketStruct rocket, EntityRideableRocket entity, TextureManager tex, boolean isDeployed, int decoupleTimer, int shroudTimer, float interp) {
        GlStateManager.pushMatrix();

        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        boolean hasShroud = false;

        if(buffer == null)
            buffer = GLAllocation.createDirectByteBuffer(8 * 4).asDoubleBuffer(); // four doubles

        for(RocketStruct.RocketStage stage : rocket.stages) {
            int stack = stage.getStack();
            int cluster = stage.getCluster();

            if(isDeployed && stage.thruster != null && stage.fins != null && stage.fins.height > stage.thruster.height) {
                GlStateManager.translate(0, stage.fins.height - stage.thruster.height, 0);
            }

            for(int c = 0; c < cluster; c++) {
                GlStateManager.pushMatrix();
                {

                    if(decoupleTimer > 0) {
                        float decoupleLerp = decoupleTimer + interp;
                        GlStateManager.translate(0, -decoupleLerp, 0);
                        GlStateManager.rotate(decoupleLerp, 1, 0, 0);
                    }

                    if(c > 0) {
                        float spin = (float)c / (float)(cluster - 1);
                        GlStateManager.rotate(360.0F * spin, 0, 1, 0);

                        if(stage.fuselage != null) {
                            GlStateManager.translate(stage.fuselage.part.bottom.radius, 0, 0);
                        } else if(stage.thruster != null) {
                            GlStateManager.translate(stage.thruster.part.top.radius, 0, 0);
                        }
                    }

                    if(stage.thruster != null) {
                        if(hasShroud && stage.fuselage != null) {
                            if(shroudTimer > 0) {
                                float shroudLerp = shroudTimer + interp;
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(0, -shroudLerp, 0);
                                GlStateManager.rotate((float) (shroudLerp * 0.5D), 1F, 0F, 0F);
                            }

                            tex.bindTexture(ResourceManager.universal);
                            buffer.put(new double[] {0, -1, 0, stage.thruster.height});
                            buffer.rewind();
                            GL11.glEnable(GL11.GL_CLIP_PLANE0);
                            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, buffer);
                            stage.fuselage.getShroud().renderAll();
                            GL11.glDisable(GL11.GL_CLIP_PLANE0);

                            if(shroudTimer > 0) {
                                GlStateManager.popMatrix();
                            }
                        }

                        if(!hasShroud || shroudTimer > 0) {
                            tex.bindTexture(stage.thruster.texture);
                            stage.thruster.getModel(isDeployed).renderAll();
                        }
                        GlStateManager.translate(0, stage.thruster.height, 0);
                    }

                    if(stage.fuselage != null) {
                        if(stage.fins != null) {
                            tex.bindTexture(stage.fins.texture);
                            stage.fins.getModel(isDeployed).renderAll();
                        }

                        for(int s = 0; s < stack; s++) {
                            tex.bindTexture(stage.fuselage.texture);
                            stage.fuselage.getModel(isDeployed).renderAll();
                            GlStateManager.translate(0, stage.fuselage.height, 0);
                        }
                    }

                }
                GlStateManager.popMatrix();
            }


            if(stage.thruster != null) GlStateManager.translate(0, stage.thruster.height, 0);
            if(stage.fuselage != null) GlStateManager.translate(0, stage.fuselage.height * stack, 0);

            // Only the bottom-most stage can be deployed
            isDeployed = false;
            decoupleTimer = 0;
            if(hasShroud) shroudTimer = 0; // Only the bottom-most shroud (second stage from bottom) should animate
            hasShroud = true;
        }

        if(rocket.capsule != null) {
            if(entity != null && rocket.capsule.renderer != null) {
                rocket.capsule.renderer.render(tex, entity, interp);
            } else {
                tex.bindTexture(rocket.capsule.texture);
                rocket.capsule.model.renderAll();
            }
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.popMatrix();
    }
}
