package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardReceiver;
import com.hbm.api.fluid.IFluidStandardSender;
import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.orbit.WorldProviderOrbit;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class TileEntityAtmosphericCompressor extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2, IFluidStandardSender {

    int consumption = 200;
    public float rot;
    public float prevRot;
    private float rotSpeed;
    public long power = 0;
    public FluidTankNTM tank;
    public List<IFluidStandardReceiver> list = new ArrayList<>();

    public TileEntityAtmosphericCompressor() {
        super(0, true, true);
        tank = new FluidTankNTM(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 50000);
    }

    @Override
    public String getDefaultName() {
        return "container.air";
    }

    @Override
    public void update() {

        if(!world.isRemote) {

            this.updateConnections();

            // Extractors will not work indoors (or in space oops)
            CBT_Atmosphere atmosphere = !(world.provider instanceof WorldProviderOrbit) && !ChunkAtmosphereManager.proxy.hasAtmosphere(world, pos.getX(), pos.getY(), pos.getZ())
                    ? CelestialBody.getTrait(world, CBT_Atmosphere.class)
                    : null;

            if(atmosphere != null && atmosphere.getPressure() > 0.0D) {
                // If the atmosphere doesn't contain the fluid we're sucking up, pick a new one
                if(!atmosphere.hasFluid(tank.getTankType())) {
                    tank.setTankType(atmosphere.getMainFluid());
                }
            } else {
                tank.setTankType(Fluids.NONE);
            }

            if(hasPower() && tank.getTankType() != Fluids.NONE && tank.getFill() + 100 <= tank.getMaxFill()) {
                tank.setFill(tank.getFill() + 100);
                power -= this.getMaxPower() / 100;

                CelestialBody.capture(world, tank.getTankType(), 100);
            }

            markDirty();

            this.networkPackNT(50);
        } else {
            float maxSpeed = 30F;

            if(hasPower()) {
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

    public void cycleGas() {
        CBT_Atmosphere atmosphere = !ChunkAtmosphereManager.proxy.hasAtmosphere(world, pos.getX(), pos.getY(), pos.getZ())
                ? CelestialBody.getTrait(world, CBT_Atmosphere.class)
                : null;

        if(atmosphere == null) return;

        FluidType currentFluid = tank.getTankType();

        for(int i = 0; i < atmosphere.fluids.size(); i++) {
            if(atmosphere.fluids.get(i).fluid == currentFluid) {
                int targetIndex = i + 1;
                if(targetIndex >= atmosphere.fluids.size()) targetIndex = 0;

                tank.setTankType(atmosphere.fluids.get(targetIndex).fluid);
                break;
            }
        }
    }

    protected void updateConnections() {
        for(DirPos pos : getConPos()) {
            trySubscribe(world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            sendFluid(tank, world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
        }
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

    public boolean hasPower() {
        return power >= this.getMaxPower() / 100;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.power = nbt.getLong("power");
        tank.readFromNBT(nbt, "water");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("power", power);
        tank.writeToNBT(nbt, "water");
        return super.writeToNBT(nbt);
    }

    @Override
    public void setPower(long i) {
        power = i;
    }

    @Override
    public long getPower() {
        return power;
    }

    @Override
    public long getMaxPower() {
        return 1000000;
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return new FluidTankNTM[] { tank };
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return new FluidTankNTM[] { tank };
    }

    private DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.DOWN);

        return new DirPos[] {
                new DirPos(this.pos.getX() - dir.offsetX * 2, this.pos.getY(), this.pos.getZ() - dir.offsetZ * 2, dir.getOpposite()),
                new DirPos(this.pos.getX() - dir.offsetX * 2 + rot.offsetX, this.pos.getY(), this.pos.getZ() - dir.offsetZ * 2 + rot.offsetZ, dir.getOpposite()),

                new DirPos(this.pos.getX() + dir.offsetX, this.pos.getY(), this.pos.getZ() + dir.offsetZ, dir),
                new DirPos(this.pos.getX() + dir.offsetX + rot.offsetX, this.pos.getY(), this.pos.getZ() + dir.offsetZ  + rot.offsetZ, dir),

                new DirPos(this.pos.getX() - rot.offsetX, this.pos.getY(), this.pos.getZ() - rot.offsetZ, rot.getOpposite()),
                new DirPos(this.pos.getX() - dir.offsetX - rot.offsetX, this.pos.getY(), this.pos.getZ() - dir.offsetZ - rot.offsetZ, rot.getOpposite()),

                new DirPos(this.pos.getX() + rot.offsetX * 2, this.pos.getY(), this.pos.getZ() + rot.offsetZ * 2, rot),
                new DirPos(this.pos.getX() - dir.offsetX + rot.offsetX * 2, this.pos.getY(), this.pos.getZ() - dir.offsetZ + rot.offsetZ * 2, rot),
        };
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 1,
                    pos.getY(),
                    pos.getZ() - 1,
                    pos.getX() + 2,
                    pos.getY() + 10,
                    pos.getZ() + 2
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
