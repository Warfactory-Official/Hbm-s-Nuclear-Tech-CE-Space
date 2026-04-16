package com.hbmspace.render.entity.mob;

import com.hbm.entity.siege.SiegeTier;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.RenderMiscEffects;
import com.hbm.util.RenderUtil;
import com.hbmspace.entity.mob.siege.EntitySiegeCraft;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.render.util.BeamPronterSpace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@AutoRegister(factory = "FACTORY")
public class RenderSiegeCraft extends Render<EntitySiegeCraft> {

    public static final IRenderFactory<EntitySiegeCraft> FACTORY = RenderSiegeCraft::new;

    public RenderSiegeCraft(RenderManager man) {
        super(man);
    }

    @Override
    public void doRender(@NotNull EntitySiegeCraft ufo, double x, double y, double z, float f0, float f1) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.5, z);
        GlStateManager.pushMatrix();

        this.bindTexture(getEntityTexture(ufo));

        double rot = (ufo.ticksExisted + f1) * 5 % 360D;
        GlStateManager.rotate((float) rot, 0, 1, 0);


        if (!ufo.isEntityAlive()) {
            float tilt = ufo.deathTime + f1;
            GlStateManager.rotate(tilt * 5, 1, 0, 1);
        }

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableCull();
        ResourceManagerSpace.siege_ufo.renderPart("UFO");
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL11.GL_FLAT);

        GlStateManager.disableTexture2D();
        RenderUtil.pushAttrib(GL11.GL_LIGHTING_BIT);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        float health = ufo.getHealth() / ufo.getMaxHealth();
        GlStateManager.color(1F - health, health, 0F);
        ResourceManagerSpace.siege_ufo.renderPart("Coils");
        GlStateManager.enableLighting();
        RenderUtil.popAttrib();
        GlStateManager.enableTexture2D();

        if (ufo.getBeam()) {
            bindTexture(new ResourceLocation("hbm", "textures/misc/glintBF.png"));
            RenderMiscEffects.renderClassicGlint(ufo.world, f1, ResourceManagerSpace.siege_ufo, "UFO", 0.5F, 1.0F, 1.0F, 5, 1F);
        }

        GlStateManager.color(1F, 1F, 1F);

        Random rand = new Random(ufo.ticksExisted / 4);

        GlStateManager.pushMatrix();
        for (int i = 0; i < 8; i++) {
            GlStateManager.rotate(45F, 0, 1, 0);
            if (rand.nextInt(5) == 0 || ufo.getBeam()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(4, 0, 0);
                BeamPronterSpace.prontBeam(new Vec3d(-1.125, 0, 2.875), BeamPronter.EnumWaveType.RANDOM, BeamPronter.EnumBeamType.LINE, 0x80d0ff, 0xffffff, (int) (System.currentTimeMillis() % 1000) / 50, 15, 0.125F, 1, 0, 256);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        if (ufo.getBeam()) {
            GlStateManager.pushMatrix();
            Vec3d delta = ufo.getLockon().add(-ufo.posX, -ufo.posY, -ufo.posZ);
            double length = delta.length();
            double scale = 0.1D;
            BeamPronterSpace.prontBeam(delta, BeamPronter.EnumWaveType.RANDOM, BeamPronter.EnumBeamType.SOLID, 0x101020, 0x101020, ufo.ticksExisted / 6, (int) (length / 2 + 1), (float) scale, 4, 0.25F, 256);
            BeamPronterSpace.prontBeam(delta, BeamPronter.EnumWaveType.RANDOM, BeamPronter.EnumBeamType.SOLID, 0x202060, 0x202060, ufo.ticksExisted / 2, (int) (length / 2 + 1), (float) scale * 7F, 2, 0.0625F, 256);
            BeamPronterSpace.prontBeam(delta, BeamPronter.EnumWaveType.RANDOM, BeamPronter.EnumBeamType.SOLID, 0x202060, 0x202060, ufo.ticksExisted / 4, (int) (length / 2 + 1), (float) scale * 7F, 2, 0.0625F, 256);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected @Nullable ResourceLocation getEntityTexture(EntitySiegeCraft entity) {
        SiegeTier tier = entity.getTier();
        return new ResourceLocation("hbm", "textures/entity/siege_craft_" + tier.name + ".png");
    }
}
