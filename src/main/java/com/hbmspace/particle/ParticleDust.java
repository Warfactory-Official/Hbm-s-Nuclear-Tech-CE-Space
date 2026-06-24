package com.hbmspace.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleDust extends Particle {

    public ParticleDust(World world, double x, double y, double z, double mX, double mY, double mZ, float scale) {
        super(world, x, y, z, mX, mY, mZ);
        this.setParticleTexture(ParticleGlow.particleFlare);
        this.particleRed = 0.4F;
        this.particleGreen = 0.2F;
        this.particleBlue = 0.1F;
        this.particleScale = scale;
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.particleAge = 1;
        this.particleMaxAge = 50 + world.rand.nextInt(50);
        this.particleAlpha = 0.5F;
        this.canCollide = false;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
