package com.hbmspace.packet.toclient;

import com.hbmspace.items.tool.ItemTransporterLinker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransporterLinkerPacket implements IMessage {

    PacketBuffer buffer;

    public TransporterLinkerPacket() {

    }

    public TransporterLinkerPacket(List<ItemTransporterLinker.TransporterInfo> transporters) {
        buffer = new PacketBuffer(Unpooled.buffer());

        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList transporterList = new NBTTagList();
        for(ItemTransporterLinker.TransporterInfo info : transporters) {
            NBTTagCompound tag = new NBTTagCompound();
            info.writeToNBT(tag);
            transporterList.appendTag(tag);
        }
        nbt.setTag("a", transporterList);

        buffer.writeCompoundTag(nbt);

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buffer == null) {
            buffer = new PacketBuffer(Unpooled.buffer());
        }
        buffer.writeBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (buffer == null) {
            buffer = new PacketBuffer(Unpooled.buffer());
        }
        buf.writeBytes(buffer);
    }

    public static class Handler implements IMessageHandler<TransporterLinkerPacket, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(TransporterLinkerPacket m, MessageContext ctx) {

            Minecraft.getMinecraft();

            try {
                NBTTagCompound nbt = m.buffer.readCompoundTag();
                ItemTransporterLinker.currentTransporters = new ArrayList<>();

                if(nbt != null) {
                    NBTTagList transporterList = nbt.getTagList("a", Constants.NBT.TAG_COMPOUND);
                    for (int i = 0; i < transporterList.tagCount(); i++) {
                        NBTTagCompound tag = transporterList.getCompoundTagAt(i);
                        ItemTransporterLinker.TransporterInfo info = ItemTransporterLinker.TransporterInfo.readFromNBT(tag);
                        ItemTransporterLinker.currentTransporters.add(info);
                    }
                }

            } catch (Exception x) {
            }
            return null;
        }
    }
}
