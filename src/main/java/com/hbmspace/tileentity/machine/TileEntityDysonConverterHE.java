package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyProviderMK2;
import com.hbm.blocks.BlockDummyable;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.tileentity.IDysonConverter;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityDysonConverterHE extends TileEntityMachineBase implements ITickable, IDysonConverter, IEnergyProviderMK2 {

    public long power;

    public boolean isConverting;
    private int cooldown;

    public TileEntityDysonConverterHE() {
        super(0, false, true);
    }

    @Override
    public String getDefaultName() {
        return "container.machineDysonConverterHE";
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset).getOpposite();
            ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

            DirPos output = new DirPos(pos.getX() + dir.offsetX * 5, pos.getY(), pos.getZ() + dir.offsetZ * 5, dir);
            tryProvide(world, output.getPos().getX(), output.getPos().getY(), output.getPos().getZ(), output.getDir());

            isConverting = power > 0;

            if(isConverting && world.getTotalWorldTime() % 2 == 0) {
                NBTTagCompound dPart = new NBTTagCompound();
                dPart.setString("type", world.getTotalWorldTime() % 10 == 0 ? "tau" : "hadron");
                dPart.setByte("count", (byte) 1);
                PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(dPart, pos.getX() + 0.5 + dir.offsetX * 4 + rot.offsetX, pos.getY() + 2.25, pos.getZ() + 0.5 + dir.offsetZ * 4 + rot.offsetZ), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
                PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(dPart, pos.getX() + 0.5 + dir.offsetX * 4 - rot.offsetX, pos.getY() + 2.25, pos.getZ() + 0.5 + dir.offsetZ * 4 - rot.offsetZ), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
                PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(dPart, pos.getX() + 0.5 + dir.offsetX * 3 + rot.offsetX, pos.getY() + 2.75, pos.getZ() + 0.5 + dir.offsetZ * 3 + rot.offsetZ), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
                PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(dPart, pos.getX() + 0.5 + dir.offsetX * 3 - rot.offsetX, pos.getY() + 2.75, pos.getZ() + 0.5 + dir.offsetZ * 3 - rot.offsetZ), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));

                cooldown++;
                if(cooldown > 10) {
                    // To prevent this machine acting like an endgame battery, but still be able to transmit every drop of power
                    // this machine will clear its buffers (almost) immediately after transmitting power
                    power = 0;
                    cooldown = 0;
                }
            }

            networkPackNT(250);
        }
    }

    @Override
    public boolean provideEnergy(int x, int y, int z, long energy) {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        int rx = pos.getX() + dir.offsetX * 4;
        int ry = pos.getY() + 1;
        int rz = pos.getZ() + dir.offsetZ * 4;

        if(x != rx || y != ry || z != rz) return false;

        power = energy;
        cooldown = 0;

        return true;
    }

    @Override
    public long maximumEnergy() {
        return Long.MAX_VALUE;
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(isConverting);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        isConverting = buf.readBoolean();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return super.writeToNBT(nbt);
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
        return power;
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

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
