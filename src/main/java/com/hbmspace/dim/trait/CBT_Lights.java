package com.hbmspace.dim.trait;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class CBT_Lights extends CelestialBodyTrait{

    public int lights;
    public boolean isCivilized;

    public CBT_Lights() {}

    public CBT_Lights(int light) {
        this.lights = light;
    }

    public int getIntensity() {
        if(lights > 10000) return 3;
        if(lights > 2000) return 2;
        if(lights > 400) return 1;
        return 0;
    }

    public void addLight(Block block, int x, int y, int z) {
        lights += block.getLightValue(block.getDefaultState());
    }

    public void removeLight(Block block, int x, int y, int z) {
        lights -= block.getLightValue(block.getDefaultState());
        if(lights < 0) lights = 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("lights", lights);
        nbt.setBoolean("isCiv", isCivilized);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        lights = nbt.getInteger("lights");
        isCivilized = nbt.getBoolean("isCiv");
    }

    @Override
    public void writeToBytes(ByteBuf buf) {
        buf.writeInt(lights);
        buf.writeBoolean(isCivilized);
    }

    @Override
    public void readFromBytes(ByteBuf buf) {
        lights = buf.readInt();
        isCivilized = buf.readBoolean();
    }

}
