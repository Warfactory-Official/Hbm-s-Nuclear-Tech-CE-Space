package com.hbmspace.tileentity.machine;

import com.hbm.api.tile.IHeatSource;
import com.hbm.blocks.BlockDummyable;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.tileentity.IDysonConverter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityDysonConverterTU extends TileEntityMachineBase implements ITickable, IDysonConverter, IHeatSource {

    public int heatEnergy;

    public TileEntityDysonConverterTU() {
        super(0, false, false);
    }

    @Override
    public String getDefaultName() {
        return "container.machineDysonConverterTU";
    }

    @Override
    public void update() { }

    @Override
    public boolean provideEnergy(int x, int y, int z, long energy) {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        int rx = pos.getX() + dir.offsetX * 6;
        int ry = pos.getY() + 1;
        int rz = pos.getZ() + dir.offsetZ * 6;

        if(x != rx || y != ry || z != rz) return false;

        if(energy > Integer.MAX_VALUE) {
            heatEnergy = Integer.MAX_VALUE;
            return true;
        }
        heatEnergy += energy;
        if(heatEnergy < 0) heatEnergy = Integer.MAX_VALUE; // prevent overflow

        return true;
    }

    @Override
    public long maximumEnergy() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getHeatStored() {
        return heatEnergy;
    }

    @Override
    public void useUpHeat(int heat) {
        heatEnergy = Math.max(0, heatEnergy - heat);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.heatEnergy = nbt.getInteger("heatEnergy");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("heatEnergy", heatEnergy);
        return super.writeToNBT(nbt);
    }

    AxisAlignedBB bb = null;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 6,
                    pos.getY(),
                    pos.getZ() - 6,
                    pos.getX() + 7,
                    pos.getY() + 6,
                    pos.getZ() + 7
            );
        }

        return bb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

}
