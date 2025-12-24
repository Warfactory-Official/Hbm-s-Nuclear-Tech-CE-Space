package com.hbmspace.packet.toclient;

import com.hbm.main.MainRegistry;
import com.hbm.packet.threading.PrecompiledPacket;
import com.hbm.tileentity.IBufPacketReceiver;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EntityBufPacket extends PrecompiledPacket {

    int entityId;
    IBufPacketReceiver rec;
    ByteBuf buf;

    public EntityBufPacket() { }

    public EntityBufPacket(int entityId, IBufPacketReceiver rec) {
        this.entityId = entityId;
        this.rec = rec;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.buf = buf;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        this.rec.serialize(buf);
    }

    public static class Handler implements IMessageHandler<EntityBufPacket, IMessage> {

        @Override
        public IMessage onMessage(EntityBufPacket m, MessageContext ctx) {

            if(Minecraft.getMinecraft().world == null)
                return null;

            Entity entity = Minecraft.getMinecraft().world.getEntityByID(m.entityId);

            if(entity instanceof IBufPacketReceiver) {
                try {
                    ((IBufPacketReceiver) entity).deserialize(m.buf);
                } catch(Exception e) { // just in case gamma fucked up
                    MainRegistry.logger.warn("An EntityByteBuf packet failed to be read and has thrown an error. This normally means that there was a buffer underflow and more data was read than was actually in the packet.");
                    MainRegistry.logger.warn("Entity: {}", entity.getCommandSenderEntity().getName());
                    MainRegistry.logger.warn(e.getMessage());
                } finally {
                    m.buf.release();
                }
            }

            return null;
        }
    }
}
