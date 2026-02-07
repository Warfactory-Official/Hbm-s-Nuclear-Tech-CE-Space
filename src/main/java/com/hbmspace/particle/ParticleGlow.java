package com.hbmspace.particle;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class ParticleGlow extends Particle {

    private final float peakScale;
    public static TextureAtlasSprite particleFlare;

    public ParticleGlow(World world, double x, double y, double z, double mX, double mY, double mZ, float scale) {
        super(world, x, y, z, mX, mY, mZ);
        this.setParticleTexture(particleFlare);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleScale = peakScale = scale;
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.particleAge = 1;
        this.particleMaxAge = 50 + world.rand.nextInt(50);
        this.particleAlpha = 0.8F;
        this.canCollide = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        float t = (float) particleAge / (float) particleMaxAge;
        t = Math.abs(0.5F - (t - 0.5F)) * 2;

        particleScale = peakScale * (float) Math.sin(t * Math.PI / 2);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        return 15728880;
    }
}