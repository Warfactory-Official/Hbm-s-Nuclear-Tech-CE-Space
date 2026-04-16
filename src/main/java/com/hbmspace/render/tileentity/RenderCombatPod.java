package com.hbmspace.render.tileentity;

import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.storage.TileEntityCombatDropPod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.Random;
@AutoRegister
public class RenderCombatPod extends TileEntitySpecialRenderer<TileEntityCombatDropPod> {
    public static Random rand = new Random();

    @Override
    public void render(TileEntityCombatDropPod pod, double x, double y, double z, float i, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.enableLighting();

        GlStateManager.translate(0.0F, -0.25F, 0.0F);
        rand.setSeed(getIdentity(pod.getPos()));
        double yaw = rand.nextDouble() * 360;
        double pitch = rand.nextDouble() * 8;
        double roll = rand.nextDouble() * 6;

        GlStateManager.rotate((float) yaw, 0, 1, 0);
        GlStateManager.rotate((float) pitch, 1, 0, 0);
        GlStateManager.rotate((float) roll, 0, 0, 1);

        int color = pod.color;
        double open = pod.prevHatchopen + (pod.hatchopen - pod.prevHatchopen) * i;
        double open2 = pod.prevHatchopen2 + (pod.hatchopen2 - pod.prevHatchopen2) * i;

        switch (color) {
            case 1:
                bindTexture(ResourceManagerSpace.combat_pod_skin_red);
                break;
            case 2:
                bindTexture(ResourceManagerSpace.combat_pod_skin_yellow);
                break;
            default:
                bindTexture(ResourceManagerSpace.combat_pod_skin_white);
                break;
        }

        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, open * 0.013, open * 0.01);
        GlStateManager.rotate((float) open, 1, 0, 0);
        ResourceManagerSpace.combat_pod.renderPart("hatch1");

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -open2 * 0.013, -open * 0.01);
        GlStateManager.rotate((float) open2, 1, 0, 0);

        ResourceManagerSpace.combat_pod.renderPart("hatch2");
        GlStateManager.popMatrix();
        ResourceManagerSpace.combat_pod.renderPart("bomb");

        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.popMatrix();
    }

    public static int getIdentity(BlockPos pos) {
        return (pos.getY() + pos.getZ() * 27644437) * 27644437 + pos.getX();
    }

}
