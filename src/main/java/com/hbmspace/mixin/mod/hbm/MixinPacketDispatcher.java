package com.hbmspace.mixin.mod.hbm;

import com.hbm.main.NetworkHandler;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toserver.SatLaserPacket;
import com.hbmspace.packet.toclient.EntityBufPacket;
import com.hbmspace.packet.toclient.ExtPropSpacePacket;
import com.hbmspace.packet.toclient.TransporterLinkerPacket;
import com.hbmspace.packet.toserver.GuiLayerPacket;
import com.hbmspace.packet.toserver.SatActivatePacket;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketDispatcher.class, remap = false)
public class MixinPacketDispatcher {
    @Shadow
    @Final
    public static NetworkHandler wrapper;

    @Inject(method = "registerPackets", at = @At("TAIL"))
    private static void onRegisterPackets(CallbackInfo ci, @Local int i) {
        wrapper.registerMessage(ExtPropSpacePacket.Handler.class, ExtPropSpacePacket.class, i++, Side.CLIENT);
        wrapper.registerMessage(GuiLayerPacket.Handler.class, GuiLayerPacket.class, i++, Side.SERVER);
        wrapper.registerMessage(TransporterLinkerPacket.Handler.class, TransporterLinkerPacket.class, i++, Side.CLIENT);
        wrapper.registerMessage(EntityBufPacket.Handler.class, EntityBufPacket.class, i++, Side.CLIENT);
        wrapper.registerMessage(SatActivatePacket.Handler.class, SatActivatePacket.class, i++, Side.SERVER);
    }
}
