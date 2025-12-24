package com.hbmspace.tileentity.machine;

import com.hbm.api.fluid.IFluidStandardTransceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.dim.orbit.WorldProviderOrbit;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityAlgaeFilm extends TileEntityMachineBase implements ITickable, IFluidStandardTransceiver {

    public FluidTankNTM[] tanks;
    public boolean canOperate;

    public TileEntityAlgaeFilm() {
        super(0, true, false);
        tanks = new FluidTankNTM[2];
        tanks[0] = new FluidTankNTM(Fluids.CARBONDIOXIDE, 8_000);
        tanks[1] = new FluidTankNTM(Fluids.OXYGEN, 8_000);
    }

    @Override
    public String getDefaultName() {
        return "container.algaeFilm";
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            canOperate = world.provider instanceof WorldProviderOrbit;

            if(canOperate && world.rand.nextBoolean()) {
                if(tanks[0].getFill() > 0 && tanks[1].getFill() < tanks[1].getMaxFill()) {
                    tanks[0].setFill(tanks[0].getFill() - 1);
                    tanks[1].setFill(tanks[1].getFill() + 1);
                }
            }

            ForgeDirection d = ForgeDirection.getOrientation(this.getBlockMetadata()).getRotation(ForgeDirection.UP);
            ForgeDirection[] dirs = new ForgeDirection[] { d, d.getOpposite() };

            for(ForgeDirection dir : dirs) {
                trySubscribe(tanks[0].getTankType(), world, pos.getX() + dir.offsetX, pos.getY() + dir.offsetY, pos.getZ() + dir.offsetZ, dir);
                sendFluid(tanks[1], world, pos.getX() + dir.offsetX, pos.getY() + dir.offsetY, pos.getZ() + dir.offsetZ, dir);
            }

            networkPackNT(20);
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(canOperate);
        for(FluidTankNTM tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        canOperate = buf.readBoolean();
        for(FluidTankNTM tank : tanks) tank.deserialize(buf);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(nbt, "t" + i);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(nbt, "t" + i);
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return tanks;
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return new FluidTankNTM[] { tanks[1] };
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return new FluidTankNTM[] { tanks[0] };
    }

}
