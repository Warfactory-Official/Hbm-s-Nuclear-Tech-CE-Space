package com.hbmspace.util;

import com.hbm.particle.ParticleRocketFlame;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SpaceParticleHandler {
    private SpaceParticleHandler() {
    }

    public static boolean test(NBTTagCompound data) {
        String type = data.getString("type");
        return switch (type) {
            case "spinlaunch" -> true;
            // add more labels as needed
            default -> false;
        };
    }

    public static void handle(NBTTagCompound data) {
        World world = Minecraft.getMinecraft().world;
        if (world == null) return;
        String type = data.getString("type");
        double x = data.getDouble("posX");
        double y = data.getDouble("posY");
        double z = data.getDouble("posZ");

        switch (type) {
            case "spinlaunch" -> {
                int count = data.getInteger("count");

                float scale = data.hasKey("scale") ? data.getFloat("scale") : 1F;
                double mX = data.getDouble("moX");
                double mY = data.getDouble("moY");
                double mZ = data.getDouble("moZ");

                int maxAge = data.getInteger("maxAge");

                for(int i = 0; i < count; i++) {
                    double ox = world.rand.nextDouble() * 2 - 1;
                    double oy = world.rand.nextDouble() * 2 - 1;
                    double oz = world.rand.nextDouble() * 2 - 1;
                    double mult = 1.0 - world.rand.nextDouble() * 0.1;

                    ParticleRocketFlame fx = new ParticleRocketFlame(world, x + ox, y + oy, z + oz).setScale(scale);
                    fx.motionX = mX * mult + ox;
                    fx.motionY = mY * mult + oy;
                    fx.motionZ = mZ * mult + oz;
                    if(maxAge > 0) fx.setMaxAge(maxAge + world.rand.nextInt(5));
                    Minecraft.getMinecraft().effectRenderer.addEffect(fx);
                }
            }
            // add more as needed
        }
    }
}
