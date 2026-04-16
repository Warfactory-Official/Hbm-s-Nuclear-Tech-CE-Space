package com.hbmspace.render.entity.mob;

import com.hbm.entity.siege.SiegeTier;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.hbmspace.entity.mob.siege.EntitySiegeUFO;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

@AutoRegister(factory = "FACTORY")
public class RenderSiegeUFO extends Render<EntitySiegeUFO> {

    public static final IRenderFactory<EntitySiegeUFO> FACTORY = RenderSiegeUFO::new;

    public RenderSiegeUFO(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@NotNull EntitySiegeUFO ufo, double x, double y, double z, float f0, float f1) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.25, z);


        this.bindTexture(getEntityTexture(ufo));

        double rot = (ufo.ticksExisted + f1) * 5 % 360D;
        GlStateManager.rotate((float) rot, 0, 1, 0);


        if (!ufo.isEntityAlive()) {
            float tilt = ufo.deathTime + f1;
            GlStateManager.rotate(tilt * 5, 1, 0, 1);
        } else if (ufo.hurtResistantTime > 0) {
            GlStateManager.rotate((float) (Math.sin(System.currentTimeMillis() * 0.01D) * (ufo.hurtResistantTime - f1)), 1, 0, 0);
        }

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableCull();
        ResourceManagerSpace.mini_ufo.renderAll();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySiegeUFO entity) {
        SiegeTier tier = entity.getTier();
        return new ResourceLocation("hbm", "textures/entity/ufo_siege_" + tier.name + ".png");
    }
}
