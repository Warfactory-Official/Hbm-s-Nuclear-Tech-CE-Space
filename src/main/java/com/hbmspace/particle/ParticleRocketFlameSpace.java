package com.hbmspace.particle;

import com.hbm.particle.ParticleRocketFlame;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRocketFlameSpace extends ParticleRocketFlame {

    protected double pressure = 1;

    public ParticleRocketFlameSpace(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    public ParticleRocketFlameSpace setScale(float scale) {
        this.particleScale = scale;
        return this;
    }
}
