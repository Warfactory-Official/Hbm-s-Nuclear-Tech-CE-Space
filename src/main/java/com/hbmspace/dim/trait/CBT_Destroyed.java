package com.hbmspace.dim.trait;

import com.hbmspace.dim.CelestialBody;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class CBT_Destroyed extends CelestialBodyTrait {

    public float destProgress;

    public CBT_Destroyed() {}

    public CBT_Destroyed(float destProgress) {
        this.destProgress = destProgress;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setFloat("interp", destProgress);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        destProgress = nbt.getFloat("interp");
    }

    @Override
    public void writeToBytes(ByteBuf buf) {
        //buf.writeFloat(interp);
    }

    @Override
    public void readFromBytes(ByteBuf buf) {
        //interp = buf.readFloat();
    }

    @Override
    public void update(boolean isRemote, CelestialBody body) {
        if(isRemote) {
            destProgress = Math.min(201.0f, destProgress + 0.0025f * (201.0f - destProgress) * 0.15f);
            if (destProgress >= 200) {
                destProgress = 0;
            }
        }
    }

}
