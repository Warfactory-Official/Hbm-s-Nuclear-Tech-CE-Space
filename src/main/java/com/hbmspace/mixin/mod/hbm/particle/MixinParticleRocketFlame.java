package com.hbmspace.mixin.mod.hbm.particle;

import com.hbm.particle.ParticleRocketFlame;
import com.hbmspace.main.SpaceMain;
import com.hbmspace.particle.IParticleRocketFlame;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = ParticleRocketFlame.class)
public abstract class MixinParticleRocketFlame extends Particle implements IParticleRocketFlame {

    @Shadow(remap = false) private int age;
    @Shadow(remap = false) private int maxAge;
    @Shadow(remap = false) private int randSeed;

    @Unique private float customRed;
    @Unique private float customGreen;
    @Unique private float customBlue;
    @Unique private double pressure = 1.0;

    protected MixinParticleRocketFlame(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    @Override
    public ParticleRocketFlame setCustomColor(float red, float green, float blue) {
        this.customRed = red;
        this.customGreen = green;
        this.customBlue = blue;
        return (ParticleRocketFlame) (Object) this;
    }

    @Override
    public ParticleRocketFlame setAtmosphericPressure(double pressure) {
        this.pressure = pressure;

        if (pressure < 0.08) {
            double factor = (0.08 - pressure) * 20.0;
            this.motionX += (this.rand.nextDouble() - 0.5) * factor;
            this.motionY += (this.rand.nextDouble() - 0.5) * factor;
            this.motionZ += (this.rand.nextDouble() - 0.5) * factor;
        }

        if (pressure < 0.05) {
            this.maxAge /= 4;
        } else if (pressure < 0.2) {
            this.maxAge /= 2;
        }

        return (ParticleRocketFlame) (Object) this;
    }

    @Inject(method = "renderParticle", at = @At("HEAD"), cancellable = true)
    private void renderParticleAddon(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (this.age == 0) {
            ci.cancel();
            return;
        }

        Random urandom = new Random(this.randSeed);

        for (int i = 0; i < 10; i++) {
            float add = urandom.nextFloat() * 0.3F;
            float dark = 1.0F - Math.min(((float) (this.age) / (this.maxAge * 0.25F)), 1.0F);

            this.particleRed = MathHelper.clamp((this.customRed != 0 ? this.customRed : 1.0F) * dark + add, 0.0F, 1.0F);
            this.particleGreen = MathHelper.clamp((this.customGreen != 0 ? this.customGreen : 0.6F) * dark + add, 0.0F, 1.0F);
            this.particleBlue = MathHelper.clamp((this.customBlue != 0 ? this.customBlue : 0.0F) * dark + add, 0.0F, 1.0F);

            this.particleAlpha = MathHelper.clamp((float) Math.pow(1.0 - Math.min(((float) (this.age) / (float) (this.maxAge)), 1.0), 0.5), 0.0F, 1.0F);

            int j = this.getBrightnessForRender(partialTicks);
            int k = j >> 16 & 65535;
            int l = j & 65535;

            float spread = (float) Math.pow(((float) (this.age) / (float) this.maxAge) * 4.0F, 1.5) + 1.0F;
            spread *= this.particleScale;

            float scale = (urandom.nextFloat() * 0.5F + 0.1F + ((float) (this.age) / (float) this.maxAge) * 2.0F) * this.particleScale;
            float pX = (float) ((this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - Particle.interpPosX) + (urandom.nextGaussian() - 1.0) * 0.2F * spread);
            float pY = (float) ((this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - Particle.interpPosY) + (urandom.nextGaussian() - 1.0) * 0.5F * spread);
            float pZ = (float) ((this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - Particle.interpPosZ) + (urandom.nextGaussian() - 1.0) * 0.2F * spread);

            buffer.pos(pX - rotationX * scale - rotationXY * scale, pY - rotationZ * scale, pZ - rotationYZ * scale - rotationXZ * scale)
                    .tex(this.particleTexture.getMaxU(), this.particleTexture.getMaxV())
                    .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha * 0.75F)
                    .lightmap(k, l).endVertex();

            buffer.pos(pX - rotationX * scale + rotationXY * scale, pY + rotationZ * scale, pZ - rotationYZ * scale + rotationXZ * scale)
                    .tex(this.particleTexture.getMaxU(), this.particleTexture.getMinV())
                    .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha * 0.75F)
                    .lightmap(k, l).endVertex();

            buffer.pos(pX + rotationX * scale + rotationXY * scale, pY + rotationZ * scale, pZ + rotationYZ * scale + rotationXZ * scale)
                    .tex(this.particleTexture.getMinU(), this.particleTexture.getMinV())
                    .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha * 0.75F)
                    .lightmap(k, l).endVertex();

            buffer.pos(pX + rotationX * scale - rotationXY * scale, pY - rotationZ * scale, pZ + rotationYZ * scale - rotationXZ * scale)
                    .tex(this.particleTexture.getMinU(), this.particleTexture.getMaxV())
                    .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha * 0.75F)
                    .lightmap(k, l).endVertex();
        }

        ci.cancel();
    }
}
