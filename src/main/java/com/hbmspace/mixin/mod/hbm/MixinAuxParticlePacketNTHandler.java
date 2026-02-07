package com.hbmspace.mixin.mod.hbm;

import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.util.I18nUtil;
import com.hbmspace.util.SpaceParticleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AuxParticlePacketNT.Handler.class)
public class MixinAuxParticlePacketNTHandler {

    // mlbv: yes you can inject more precisely or choose to inject effectNT to avoid duplicated work, but this is the
    // most robust approach and makes it impossible for jvmdg to mess with it
    @Inject(method = "Lcom/hbm/packet/toclient/AuxParticlePacketNT$Handler;onMessage(Lcom/hbm/packet/toclient/AuxParticlePacketNT;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;", at = @At("HEAD"))
    private void onMessageFirst(AuxParticlePacketNT m, MessageContext ctx, CallbackInfoReturnable<IMessage> cir) {
        NBTTagCompound nbt = ((MixinAuxParticlePacketNTAccessor) m).getNbt();
        if (nbt != null && SpaceParticleHandler.test(nbt)) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (nbt.hasKey("label", 8)) {
                    nbt.setString("label", I18nUtil.resolveKey(nbt.getString("label")));
                }
                SpaceParticleHandler.handle(nbt);
            });
            cir.cancel();
        }
    }
}
