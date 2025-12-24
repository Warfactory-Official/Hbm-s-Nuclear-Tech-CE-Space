package com.hbmspace.tileentity.machine.oil;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardTransceiver;
import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.IPersistentNBT;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.Tuple;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.recipes.AlkylationRecipes;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
@AutoRegister
public class TileEntityMachineAlkylation extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2, IFluidStandardTransceiver, IPersistentNBT {

    public long power;
    public static final long maxPower = 1_000_000;

    public FluidTankNTM[] tanks;

    public TileEntityMachineAlkylation() {
        super(11, true, true);

        this.tanks = new FluidTankNTM[4];
        this.tanks[0] = new FluidTankNTM(Fluids.CHLOROMETHANE, 8_000);
        this.tanks[1] = new FluidTankNTM(Fluids.NONE, 4_000);
        this.tanks[2] = new FluidTankNTM(Fluids.UNSATURATEDS, 8_000);
        this.tanks[3] = new FluidTankNTM(Fluids.CHLORINE, 8_000);
    }

    @Override
    public String getDefaultName() {
        return "container.alkylation";
    }

    @Override
    public void update() {

        if(!world.isRemote) {
            if(this.world.getTotalWorldTime() % 10 == 0) this.updateConnections();

            if(world.getTotalWorldTime() % 2 == 0) alkylate();

            this.networkPackNT(25);
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(power);
        for (FluidTankNTM tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        for (FluidTankNTM tank : tanks) tank.deserialize(buf);
    }

    private void alkylate() {

        Tuple.Triplet<FluidStack, FluidStack, FluidStack> out = AlkylationRecipes.getOutput(tanks[0].getTankType());
        if(out == null) {
            tanks[2].setTankType(Fluids.NONE);
            tanks[3].setTankType(Fluids.NONE);
            return;
        }

        tanks[1].setTankType(out.getX().type);
        tanks[2].setTankType(out.getY().type);
        tanks[3].setTankType(out.getZ().type);

        if(power < 4_000) return; // 40 kHE/s
        if(tanks[0].getFill() < 100) return;
        if(tanks[1].getFill() < out.getX().fill) return;

        if(tanks[2].getFill() + out.getY().fill > tanks[2].getMaxFill()) return;
        if(tanks[3].getFill() + out.getZ().fill > tanks[3].getMaxFill()) return;

        tanks[0].setFill(tanks[0].getFill() - 100);
        tanks[1].setFill(tanks[1].getFill() - out.getX().fill);
        tanks[2].setFill(tanks[2].getFill() + out.getY().fill);
        tanks[3].setFill(tanks[3].getFill() + out.getZ().fill);

        power -= 4_000;
    }

    private void updateConnections() {
        for(DirPos pos : getConPos()) {
            this.trySubscribe(world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            this.trySubscribe(tanks[0].getTankType(), world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            this.trySubscribe(tanks[1].getTankType(), world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            if(tanks[2].getFill() > 0) this.sendFluid(tanks[2], world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            if(tanks[3].getFill() > 0) this.sendFluid(tanks[3], world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
        }
    }

    public DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.DOWN);

        return new DirPos[] {
                new DirPos(pos.getX() + rot.offsetX * 2, pos.getY(), pos.getZ() + rot.offsetZ * 2, rot),
                new DirPos(pos.getX() + rot.offsetX * 2 + dir.offsetX * 2, pos.getY(), pos.getZ() + rot.offsetZ * 2 + dir.offsetZ * 2, rot),
                new DirPos(pos.getX() + rot.offsetX * 2 - dir.offsetX * 2, pos.getY(), pos.getZ() + rot.offsetZ * 2 - dir.offsetZ * 2, rot),

                new DirPos(pos.getX() + rot.offsetX + dir.offsetX * 3, pos.getY(), pos.getZ() + rot.offsetZ + dir.offsetZ * 3, dir),
                new DirPos(pos.getX() - rot.offsetX + dir.offsetX * 3, pos.getY(), pos.getZ() - rot.offsetZ + dir.offsetZ * 3, dir),

                new DirPos(pos.getX() - rot.offsetX * 2, pos.getY(), pos.getZ() - rot.offsetZ * 2, rot.getOpposite()),
                new DirPos(pos.getX() - rot.offsetX * 2 + dir.offsetX * 2, pos.getY(), pos.getZ() - rot.offsetZ * 2 + dir.offsetZ * 2, rot.getOpposite()),
                new DirPos(pos.getX() - rot.offsetX * 2 - dir.offsetX * 2, pos.getY(), pos.getZ() - rot.offsetZ * 2 - dir.offsetZ * 2, rot.getOpposite()),

                new DirPos(pos.getX() + rot.offsetX - dir.offsetX * 3, pos.getY(), pos.getZ() + rot.offsetZ - dir.offsetZ * 3, dir.getOpposite()),
                new DirPos(pos.getX() - rot.offsetX - dir.offsetX * 3, pos.getY(), pos.getZ() - rot.offsetZ - dir.offsetZ * 3, dir.getOpposite()),
        };
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        power = nbt.getLong("power");
        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(nbt, "t" + i);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("power", power);
        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(nbt, "t" + i);
        return super.writeToNBT(nbt);
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 3,
                    pos.getY(),
                    pos.getZ() - 3,
                    pos.getX() + 3,
                    pos.getY() + 3,
                    pos.getZ() + 3
            );
        }

        return bb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }
    @Override public FluidTankNTM[] getAllTanks() { return tanks; }
    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[] {tanks[2], tanks[3]}; }
    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[] {tanks[0], tanks[1]}; }
    @Override public boolean canConnect(ForgeDirection dir) { return dir != ForgeDirection.UNKNOWN && dir != ForgeDirection.DOWN; }
    @Override public boolean canConnect(FluidType type, ForgeDirection dir) { return dir != ForgeDirection.UNKNOWN && dir != ForgeDirection.DOWN; }

    @Override
    public void writeNBT(NBTTagCompound nbt) {
        if(tanks[0].getFill() == 0 && tanks[1].getFill() == 0 && tanks[2].getFill() == 0 && tanks[3].getFill() == 0) return;
        NBTTagCompound data = new NBTTagCompound();
        for(int i = 0; i < tanks.length; i++) this.tanks[i].writeToNBT(data, "t" + i);
        nbt.setTag(NBT_PERSISTENT_KEY, data);
    }

    @Override
    public void readNBT(NBTTagCompound nbt) {
        NBTTagCompound data = nbt.getCompoundTag(NBT_PERSISTENT_KEY);
        for(int i = 0; i < tanks.length; i++) this.tanks[i].readFromNBT(data, "t" + i);
    }
}
