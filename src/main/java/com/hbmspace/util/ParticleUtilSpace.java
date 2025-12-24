package com.hbmspace.util;

import com.hbm.handler.threading.PacketThreading;
import com.hbm.main.MainRegistry;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ParticleUtilSpace {

    public static void spawnFlare(World world, double x, double y, double z, double mX, double mY, double mZ, float scale) {

        NBTTagCompound data = new NBTTagCompound();
        data.setString("type", "flare");
        data.setDouble("mX", mX);
        data.setDouble("mY", mY);
        data.setDouble("mZ", mZ);
        data.setFloat("scale", scale);

        if(world.isRemote) {
            data.setDouble("posX", x);
            data.setDouble("posY", y);
            data.setDouble("posZ", z);
            // TODO
            //MainRegistry.proxy.effectNT(data);
        } else {
            //PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(data, x, y, z), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 150));
        }
    }
}
