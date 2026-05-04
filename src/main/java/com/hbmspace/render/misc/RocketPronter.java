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
            buffer = GLAllocation.createDirectByteBuffer(8 * 4).asDoubleBuffer();

        for(RocketStruct.RocketStage stage : rocket.stages) {
            RocketPart fuselagePart = stage.fuselage != null ? RocketPart.getPart(stage.fuselage) : null;
            RocketPart finsPart     = stage.fins     != null ? RocketPart.getPart(stage.fins)     : null;
            RocketPart thrusterPart = stage.thruster != null ? RocketPart.getPart(stage.thruster) : null;

            int stack   = stage.getStack();
            int cluster = stage.getCluster();

            if(isDeployed && thrusterPart != null && finsPart != null && finsPart.height > thrusterPart.height) {
                GlStateManager.translate(0, finsPart.height - thrusterPart.height, 0);
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

                        if(fuselagePart != null) {
                            GlStateManager.translate(fuselagePart.part.bottom.radius, 0, 0);
                        } else if(thrusterPart != null) {
                            GlStateManager.translate(thrusterPart.part.top.radius, 0, 0);
                        }
                    }

                    if(thrusterPart != null) {
                        if(hasShroud && fuselagePart != null && fuselagePart.getShroud() != null) {
                            if(shroudTimer > 0) {
                                float shroudLerp = shroudTimer + interp;
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(0, -shroudLerp, 0);
                                GlStateManager.rotate((float) (shroudLerp * 0.5D), 1F, 0F, 0F);
                            }

                            tex.bindTexture(ResourceManager.universal);
                            buffer.put(new double[]{0, -1, 0, thrusterPart.height});
                            buffer.rewind();
                            GL11.glEnable(GL11.GL_CLIP_PLANE0);
                            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, buffer);
                            fuselagePart.getShroud().renderAll();
                            GL11.glDisable(GL11.GL_CLIP_PLANE0);

                            if(shroudTimer > 0) {
                                GlStateManager.popMatrix();
                            }
                        }

                        if(!hasShroud || shroudTimer > 0) {
                            tex.bindTexture(thrusterPart.texture);
                            if(thrusterPart.getModel(isDeployed) != null) {
                                thrusterPart.getModel(isDeployed).renderAll();
                            }
                        }
                        GlStateManager.translate(0, thrusterPart.height, 0);
                    }

                    if(fuselagePart != null) {
                        if(finsPart != null && finsPart.getModel(isDeployed) != null) {
                            tex.bindTexture(finsPart.texture);
                            finsPart.getModel(isDeployed).renderAll();
                        }

                        for(int s = 0; s < stack; s++) {
                            tex.bindTexture(fuselagePart.texture);
                            if(fuselagePart.getModel(isDeployed) != null) {
                                fuselagePart.getModel(isDeployed).renderAll();
                            }
                            GlStateManager.translate(0, fuselagePart.height, 0);
                        }
                    }

                }
                GlStateManager.popMatrix();
            }

            if(thrusterPart != null) GlStateManager.translate(0, thrusterPart.height, 0);
            if(fuselagePart != null) GlStateManager.translate(0, fuselagePart.height * stack, 0);

            isDeployed = false;
            decoupleTimer = 0;
            if(hasShroud) shroudTimer = 0;
            hasShroud = true;
        }

        if(rocket.capsule != null) {
            RocketPart capsulePart = RocketPart.getPart(rocket.capsule);
            if(capsulePart != null) {
                if(entity != null && capsulePart.renderer != null) {
                    capsulePart.renderer.render(tex, entity, interp);
                } else if(capsulePart.model != null) {
                    tex.bindTexture(capsulePart.texture);
                    capsulePart.model.renderAll();
                }
            }
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.popMatrix();
    }
}
