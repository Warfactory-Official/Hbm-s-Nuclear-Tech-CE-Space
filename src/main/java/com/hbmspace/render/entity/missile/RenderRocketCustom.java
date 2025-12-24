package com.hbmspace.render.entity.missile;

import com.hbmspace.dim.CelestialBody;
import com.hbmspace.entity.missile.EntityRideableRocket;
import com.hbmspace.handler.RocketStruct;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.render.misc.RocketPronter;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

@AutoRegister(factory = "FACTORY")
public class RenderRocketCustom extends Render<EntityRideableRocket> {

    public static final IRenderFactory<EntityRideableRocket> FACTORY = RenderRocketCustom::new;

    protected RenderRocketCustom(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityRideableRocket entity, double x, double y, double z, float f, float interp) {
        RocketStruct rocket = entity.getRocket();

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * interp - 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * interp, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * interp - 90.0F, 0.0F, -1.0F, 0.0F);

            RocketPronter.prontRocket(rocket, entity, Minecraft.getMinecraft().getTextureManager(), !CelestialBody.inOrbit(entity.world), entity.decoupleTimer, entity.shroudTimer, interp);

        }
        GlStateManager.popMatrix();
    }
    @Override
    protected ResourceLocation getEntityTexture(@NotNull EntityRideableRocket entity) {
        return ResourceManagerSpace.universal;
    }
}
