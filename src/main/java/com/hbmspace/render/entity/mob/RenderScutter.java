package com.hbmspace.render.entity.mob;

import com.hbmspace.entity.mob.EntityScutterfish;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.render.model.ModelScutter;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

@AutoRegister(entity = EntityScutterfish.class, factory = "FACTORY")
public class RenderScutter extends RenderLiving<EntityScutterfish> {

    public static final IRenderFactory<EntityScutterfish> FACTORY = RenderScutter::new;

    public static final ResourceLocation texture = new ResourceLocation("hbm", "textures/entity/scutterfish.png");

    public RenderScutter(RenderManager man) {
        super(man, new ModelScutter(), 0.3F);
    }

    protected ResourceLocation getEntityTexture(@NotNull EntityScutterfish entity) {
        return texture;
    }
}
