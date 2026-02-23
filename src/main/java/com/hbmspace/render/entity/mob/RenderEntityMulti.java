package com.hbmspace.render.entity.mob;

import java.util.Locale;

import com.hbmspace.entity.mob.*;

import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.render.model.ModelDepthSquid;
import com.hbmspace.render.model.ModelScrapFish;
import com.hbmspace.render.model.ModelScuttlecrab;
import com.hbmspace.render.model.ModelSifterEel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

@AutoRegister(entity = EntityScuttlecrab.class, factory = "FACTORY_1")
@AutoRegister(entity = EntityDepthSquid.class, factory = "FACTORY_2")
@AutoRegister(entity = EntityScrapFish.class, factory = "FACTORY_3")
@AutoRegister(entity = EntitySifterEel.class, factory = "FACTORY_4")
public class RenderEntityMulti extends RenderLiving {

    public static final IRenderFactory<EntityScuttlecrab> FACTORY_1 = (RenderManager man) -> new RenderEntityMulti(man, new ModelScuttlecrab(), EntityScuttlecrab.Scuttlecrab.class);
    public static final IRenderFactory<EntityDepthSquid> FACTORY_2 = (RenderManager man) -> new RenderEntityMulti(man, new ModelDepthSquid(), EntityDepthSquid.DepthSquid.class);
    public static final IRenderFactory<EntityScrapFish> FACTORY_3 = (RenderManager man) -> new RenderEntityMulti(man, new ModelScrapFish(), EntityScrapFish.ScrapFish.class);
    public static final IRenderFactory<EntitySifterEel> FACTORY_4 = (RenderManager man) -> new RenderEntityMulti(man, new ModelSifterEel(), EntitySifterEel.SifterEel.class);

    private final ResourceLocation[] textures;

    @SuppressWarnings("rawtypes")
    public RenderEntityMulti(RenderManager man, ModelBase model, Class<? extends Enum> theEnum) {
        super(man, model, 0.5F);

        Enum[] order = theEnum.getEnumConstants();
        textures = new ResourceLocation[order.length];
        for(int i = 0; i < order.length; i++) {
            textures[i] = new ResourceLocation("hbm", "textures/entity/" + theEnum.getSimpleName().toLowerCase(Locale.US) + "_" + order[i].name().toLowerCase(Locale.US) + ".png");
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected ResourceLocation getEntityTexture(@NotNull Entity entity) {
        Enum entityEnum = ((IEntityEnumMulti) entity).getEnum();
        return textures[entityEnum.ordinal()];
    }

}
