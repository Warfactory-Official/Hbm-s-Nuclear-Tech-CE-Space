package com.hbmspace.packet.toserver;

import com.hbm.saveddata.satellites.Satellite;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SatActivatePacket implements IMessage {

    //0: Add
    //1: Subtract
    //2: Set

    int freq;

    public SatActivatePacket()
    {

    }

    public SatActivatePacket(int freq)
    {

        this.freq = freq;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        freq = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(freq);
    }

    public static class Handler implements IMessageHandler<SatActivatePacket, IMessage> {

        @Override
        public IMessage onMessage(SatActivatePacket m, MessageContext ctx) {
            EntityPlayer p = ctx.getServerHandler().player;
            //how gross is this...
            // TODO
            Satellite sat = SatelliteSavedData.getData(p.world/*, (int)p.posX, (int)p.posZ*/).getSatFromFreq(m.freq);
            if(sat != null) {
                sat.onClick(p.world, ctx.getServerHandler().player, 0, 0);

            }

            return null;
        }
    }
}
