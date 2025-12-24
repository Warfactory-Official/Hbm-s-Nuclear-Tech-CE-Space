package com.hbmspace.saveddata.satellites;

import com.hbm.render.loader.IModelCustom;
import com.hbm.saveddata.satellites.Satellite;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SatelliteWar extends Satellite {

    //time to clean up this shit and make it PROPER.

    public SatelliteWar() {

    }

    public long lastOp;
    public float interp;
    public int cooldown;

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("lastOp", lastOp);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        lastOp = nbt.getLong("lastOp");
    }

    public void onClick(World world, int x, int z) {

    }

    public void fire() {


    }

    public void setTarget(CelestialBody body) {

    }

    public void fireAtTarget(CelestialBody body) {

    }

    public void playsound() {
        Minecraft.getMinecraft().player.playSound(HBMSpaceSoundHandler.fireFlash, 10F, 1F);
    }

    @Override
    public float[] getColor() {
        return new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
    }

    public float getInterp() {
        return interp;
    }

    public int magSize() {
        return 0;
    }

    public void serialize(ByteBuf buf) {
        buf.writeFloat(interp);

    }


    public void deserialize(ByteBuf buf) {
        this.interp = buf.readFloat();
    }

    public IModelCustom getModel() {
        return null;
    }

}
