package com.hbmspace.packet;

import com.hbm.util.CompatExternal;
import com.hbmspace.packet.toclient.EntityBufPacket;
import com.hbmspace.packet.toclient.ExtPropSpacePacket;
import com.hbmspace.packet.toclient.TransporterLinkerPacket;
import com.hbmspace.packet.toserver.GuiLayerPacket;
import com.hbmspace.packet.toserver.SatActivatePacket;
import net.minecraftforge.fml.relauncher.Side;

import static com.hbm.packet.PacketDispatcher.wrapper;

public final class PacketRegistry {

    public static void preInit() {
        CompatExternal.registerPacketRegisterListener(i -> {
            wrapper.registerMessage(ExtPropSpacePacket.Handler.class, ExtPropSpacePacket.class, i++, Side.CLIENT);
            wrapper.registerMessage(GuiLayerPacket.Handler.class, GuiLayerPacket.class, i++, Side.SERVER);
            wrapper.registerMessage(TransporterLinkerPacket.Handler.class, TransporterLinkerPacket.class, i++, Side.CLIENT);
            wrapper.registerMessage(EntityBufPacket.Handler.class, EntityBufPacket.class, i++, Side.CLIENT);
            wrapper.registerMessage(SatActivatePacket.Handler.class, SatActivatePacket.class, i++, Side.SERVER);
            return i;
        });
    }
}
