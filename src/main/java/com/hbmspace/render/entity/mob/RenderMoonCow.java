package com.hbmspace.render.entity.mob;

import com.hbmspace.entity.mob.EntityMoonCow;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.render.model.ModelMoonCow;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;
@AutoRegister(entity = EntityMoonCow.class, factory = "FACTORY")
public class RenderMoonCow extends RenderLiving<EntityMoonCow> {
    public static final IRenderFactory<EntityMoonCow> FACTORY = RenderMoonCow::new;

    public static final ResourceLocation texture = new ResourceLocation("hbm", "textures/entity/moon_cow.png");

    public RenderMoonCow(RenderManager man) {
        super(man, new ModelMoonCow(), 0.7F);
    }

    protected ResourceLocation getEntityTexture(@NotNull EntityMoonCow entity) {
        return texture;
    }

}
