package com.hbmspace.mixin.mod.hbm;

import com.hbm.main.MainRegistry;
import com.hbm.packet.PermaSyncHandler;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystemWorldSavedData;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.dim.trait.CelestialBodyTrait;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashMap;

/**
 * Mixins using ordinals are brittle and must be updated when PermaSyncHandler changes.
 *
 * @author mlbv
 */
@Mixin(value = PermaSyncHandler.class, remap = false)
public class MixinPermaSyncHandler {

    @Inject(method = "writePacket", at = @At(value = "FIELD", target = "Lcom/hbm/saveddata/satellites/SatelliteSavedData;sats:Lit/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap;", shift = At.Shift.BEFORE))
    private static void beforeSatelliteWrite(ByteBuf buf, World world, EntityPlayerMP player, CallbackInfo ci) {
        /// CBT ///
        if(world.getTotalWorldTime() % 5 == 1) { // update a little less frequently to not blast the players with large packets
            buf.writeBoolean(true);

            SolarSystemWorldSavedData solarSystemData = SolarSystemWorldSavedData.get(world);
            Collection<CelestialBody> allBodies = CelestialBody.getAllBodies();
            buf.writeInt(allBodies.size());

            for(CelestialBody body : allBodies) {
                ByteBufUtils.writeUTF8String(buf, body.name);

                HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait> traits = solarSystemData.getTraits(body.name);
                if(traits != null) {
                    buf.writeBoolean(true);

                    int writeCount = 0;
                    for(int i = 0; i < CelestialBodyTrait.traitList.size(); i++) {
                        if(traits.containsKey(CelestialBodyTrait.traitList.get(i))) {
                            writeCount++;
                        }
                    }
                    buf.writeInt(writeCount);

                    for(int i = 0; i < CelestialBodyTrait.traitList.size(); i++) {
                        Class<? extends CelestialBodyTrait> traitClass = CelestialBodyTrait.traitList.get(i);
                        CelestialBodyTrait trait = traits.get(traitClass);

                        if(trait != null) {
                            buf.writeInt(i); // ID of the trait, in order registered
                            trait.writeToBytes(buf);
                        }
                    }
                } else {
                    buf.writeBoolean(false);
                }
            }
        } else {
            buf.writeBoolean(false);
        }
        /// CBT ///
    }

    @Inject(method = "writePacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayerMP;getRidingEntity()Lnet/minecraft/entity/Entity;", shift = At.Shift.BEFORE, ordinal = 0))
    private static void beforeDesyncFixWrite(ByteBuf buf, World world, EntityPlayerMP player, CallbackInfo ci) {
        /// TIME OF DAY ///
        if(world.provider instanceof WorldProviderCelestial celestial && world.provider.getDimension() != 0) {
            buf.writeBoolean(true);
            celestial.serialize(buf);
        } else {
            buf.writeBoolean(false);
        }
        /// TIME OF DAY ///
    }

    @Inject(method = "readPacket", at = @At(value = "INVOKE", target = "Lio/netty/buffer/ByteBuf;readInt()I", ordinal = 1, shift = At.Shift.BEFORE), cancellable = true)
    private static void beforeSatelliteRead(ByteBuf buf, World world, EntityPlayer player, CallbackInfo ci) {
        /// CBT ///
        if(buf.readBoolean()) {
            try {
                HashMap<String, HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait>> traitMap = new HashMap<>();

                int bodyCount = buf.readInt();
                for(int bodyIdx = 0; bodyIdx < bodyCount; bodyIdx++) {
                    String bodyName = ByteBufUtils.readUTF8String(buf);

                    if(buf.readBoolean()) {
                        HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait> traits = new HashMap<>();

                        int cbtSize = buf.readInt();
                        for(int i = 0; i < cbtSize; i++) {
                            int traitId = buf.readInt();
                            if(traitId >= 0 && traitId < CelestialBodyTrait.traitList.size()) {
                                CelestialBodyTrait trait = CelestialBodyTrait.traitList.get(traitId).newInstance();
                                trait.readFromBytes(buf);
                                traits.put(trait.getClass(), trait);
                            }
                        }

                        traitMap.put(bodyName, traits);
                    }
                }

                SolarSystemWorldSavedData.updateClientTraits(traitMap);
            } catch (Exception ex) {
                // If any exception occurs, stop parsing any more bytes, they'll be unaligned
                // We'll unset the client trait set to prevent any issues

                MainRegistry.logger.catching(ex);
                SolarSystemWorldSavedData.updateClientTraits(null);

                ci.cancel();
            }
        }
        /// CBT ///
    }

    @Inject(method = "readPacket", at = @At(value = "INVOKE", target = "Lcom/hbm/saveddata/satellites/SatelliteSavedData;setClientSats(Lit/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap;)V", shift = At.Shift.AFTER))
    private static void afterSatelliteRead(ByteBuf buf, World world, EntityPlayer player, CallbackInfo ci) {
        /// TIME OF DAY ///
        boolean hasTimeData = buf.readBoolean();
        if(hasTimeData) {
            if(world.provider instanceof WorldProviderCelestial) {
                ((WorldProviderCelestial) world.provider).deserialize(buf);
            } else {
                buf.readLong();
            }
        }
        /// TIME OF DAY ///
    }
}
