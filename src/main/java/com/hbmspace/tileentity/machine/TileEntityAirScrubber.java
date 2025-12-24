package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardSender;
import com.hbm.handler.ThreeInts;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.handler.atmosphere.AtmosphereBlob;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister
public class TileEntityAirScrubber extends TileEntityMachineBase implements ITickable, IFluidStandardSender, IEnergyReceiverMK2 {

    private TileEntityAirPump pump;
    public FluidTankNTM tank;

    private long power;

    public float rot;
    public float prevRot;
    private float rotSpeed;

    public TileEntityAirScrubber() {
        super(0, true, true);
        tank = new FluidTankNTM(Fluids.CARBONDIOXIDE, 16_000);
    }

    @Override
    public String getDefaultName() {
        return "container.airScrubber";
    }

    @Override
    public void update() {
        if(!world.isRemote) {

            if(canOperate()) {
                // Fetch a new pump to scrub CO2 from
                if(world.getTotalWorldTime() % 5 == 0 && (pump == null || pump.getFluidPressure() == 0 || !pump.registerScrubber(this))) {
                    pump = null;

                    List<AtmosphereBlob> blobs = ChunkAtmosphereManager.proxy.getBlobs(world, pos.getX(), pos.getY(), pos.getZ());

                    for(AtmosphereBlob blob : blobs) {
                        if(blob != null) {
                            ThreeInts pos = blob.getRootPosition();
                            TileEntity te = world.getTileEntity(new BlockPos(pos.x, pos.y, pos.z));
                            if(te instanceof TileEntityAirPump) {
                                pump = (TileEntityAirPump) te;
                                if(!pump.registerScrubber(this)) {
                                    pump = null;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                trySubscribe(world, pos.getX() + dir.offsetX, pos.getY() + dir.offsetY, pos.getZ() + dir.offsetZ, dir);
                if(tank.getFill() > 0) sendFluid(tank, world, pos.getX() + dir.offsetX, pos.getY() + dir.offsetY, pos.getZ() + dir.offsetZ, dir);
            }

            networkPackNT(20);
        } else {
            float maxSpeed = 30F;

            if(canOperate()) {
                rotSpeed += 0.2F;
                if(rotSpeed > maxSpeed) rotSpeed = maxSpeed;
            } else {
                rotSpeed -= 0.1F;
                if(rotSpeed < 0) rotSpeed = 0;
            }

            prevRot = rot;

            rot += rotSpeed;

            if(rot >= 360) {
                rot -= 360;
                prevRot -= 360;
            }
        }
    }

    public boolean canOperate() {
        return power > 200;
    }

    public int scrub(int amount) {
        if(!canOperate()) return 0;
        int add = Math.min(tank.getMaxFill() - tank.getFill(), amount);
        tank.setFill(tank.getFill() + add);
        power -= add * 10L;
        return add;
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(power);
        tank.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        power = buf.readLong();
        tank.deserialize(buf);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("power", power);
        tank.writeToNBT(nbt, "t");
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        power = nbt.getLong("power");
        tank.readFromNBT(nbt, "t");
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return new FluidTankNTM[] { tank };
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return new FluidTankNTM[] { tank };
    }

    @Override
    public long getPower() {
        return power;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return 10000;
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    pos.getX() + 1,
                    pos.getY() + 2,
                    pos.getZ() + 1
            );
        }

        return bb;
    }

}
