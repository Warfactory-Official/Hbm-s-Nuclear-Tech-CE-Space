package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyProviderMK2;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityLoadedBase;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.orbit.WorldProviderOrbit;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityMachineSolarPanel extends TileEntityLoadedBase implements ITickable, IEnergyProviderMK2 {

    private long power;
    private long maxpwr = 1_000;

    @Override
    public void update() {

        if(!world.isRemote) {

            // Sun power ranges from 1-4
            int sun = world.getLightFor(EnumSkyBlock.SKY, pos) - world.getSkylightSubtracted() - 11;

            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                tryProvide(world, pos.getX() + dir.offsetX, pos.getY() + dir.offsetY, pos.getZ() + dir.offsetZ, dir);
            }

            if (sun <= 0 || !world.canSeeSky(pos.up())) {
                return;
            }

            power += getOutput(sun);

            if(power > getMaxPower())
                power = getMaxPower();
        }
    }

    // Balanced around 100he/t on Earth
    public long getOutput(int sun) {
        float sunPower = world.provider instanceof WorldProviderOrbit
                ? ((WorldProviderOrbit)world.provider).getSunPower()
                : CelestialBody.getBody(world).getSunPower();
        return MathHelper.ceil(sun * 25 * sunPower);
    }

    @Override
    public long getPower() {
        return power;
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return maxpwr; //temp
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.power = nbt.getLong("power");
        this.maxpwr = nbt.getLong("maxpwr");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("power", power);
        nbt.setLong("maxpwr", maxpwr);
        return super.writeToNBT(nbt);
    }
}
