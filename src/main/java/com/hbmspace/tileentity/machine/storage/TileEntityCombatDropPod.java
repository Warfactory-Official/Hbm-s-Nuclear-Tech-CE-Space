package com.hbmspace.tileentity.machine.storage;

import com.hbm.lib.HBMSoundHandler;
import com.hbm.tileentity.IBufPacketReceiver;

import com.hbmspace.config.SpaceConfig;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;
@AutoRegister
public class TileEntityCombatDropPod extends TileEntity implements ITickable, IBufPacketReceiver {

    public NBTTagCompound entityType;
    public int amount;
    public int color;
    public int delay = 40;

    public double hatchopen;
    public double hatchopen2;
    public double prevHatchopen;
    public double prevHatchopen2;

    public TileEntityCombatDropPod() {

    }

    public void setPayload(NBTTagCompound entityType, int amount, int color) {
        this.entityType = entityType;
        this.amount = amount;
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void update() {
        prevHatchopen = hatchopen;
        prevHatchopen2 = hatchopen2;

        if(delay > 0) {
            delay--;
            return;
        }

        if(delay == 0) {
            hatchopen += (90 - hatchopen) * 0.2;
            hatchopen2 += (-90 - hatchopen2) * 0.2;
        }

        if(entityType != null && amount > 0 && !world.isRemote) {

            for(int i = 0; i < amount; i++) {

                Entity entity = EntityList.createEntityFromNBT(entityType, world);

                if(entity != null) {
                    entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);

                    world.spawnEntity(entity);
                }
            }

            amount = 0;
            this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), HBMSoundHandler.hatch_open, SoundCategory.BLOCKS, 10.0F, 0.9F);
            return;
        }

        if(SpaceConfig.combatPodDespawn) {
            if(this.world.getTotalWorldTime() % 1000 == 0) {
                world.setBlockToAir(pos);
            }
        }
    }

    //we serious?? like actually??
    //i hate working on this game so much sometimes
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new SPacketUpdateTileEntity(this.pos, 1, nbt);
    }

    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 3,
                    pos.getY(),
                    pos.getZ() - 3,
                    pos.getX() + 4,
                    pos.getY() + 8,
                    pos.getZ() + 4
            );
        }

        return bb;
    }


    @Override
    public void deserialize(ByteBuf buf) {
        this.color = buf.readInt();
        this.hatchopen = buf.readDouble();
        this.hatchopen2 = buf.readDouble();
    }

    @Override
    public void networkPackNT(int range) {
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeInt(color);
        buf.writeDouble(hatchopen);
        buf.writeDouble(hatchopen2);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        if(entityType != null) {
            nbt.setTag("EntityType", entityType);
        }

        nbt.setInteger("amount", amount);
        nbt.setInteger("color", color);
        nbt.setInteger("delay", delay);
        nbt.setDouble("hatchopen", hatchopen);
        nbt.setDouble("hatchopen2", hatchopen2);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        entityType = nbt.getCompoundTag("EntityType");
        amount = nbt.getInteger("amount");
        color = nbt.getInteger("color");
        delay = nbt.getInteger("delay");
        hatchopen = nbt.getDouble("hatchopen");
        hatchopen2 = nbt.getDouble("hatchopen2");
    }

}

