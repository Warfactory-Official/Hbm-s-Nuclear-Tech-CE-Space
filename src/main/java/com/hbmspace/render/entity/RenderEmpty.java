package com.hbmspace.render.entity;

import com.hbmspace.entity.effect.EntityDepress;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

@AutoRegister(entity = EntityDepress.class, factory = "FACTORY")
public class RenderEmpty extends Render<Entity> {

    public static final IRenderFactory<Entity> FACTORY = RenderEmpty::new;

    protected RenderEmpty(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@NotNull Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {}

    @Override
    public void doRenderShadowAndFire(@NotNull Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {}

    @Override
    protected ResourceLocation getEntityTexture(@NotNull Entity entity) {
        return null;
    }

}
