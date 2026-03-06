package com.hbmspace.mixin.mod.hbm;

import com.hbm.main.MainRegistry;
import com.hbm.packet.PermaSyncHandler;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystemWorldSavedData;
import com.hbmspace.dim.WorldProviderCelestial;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.dim.trait.CelestialBodyTrait;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Mixins using ordinals are brittle and must be updated when PermaSyncHandler changes.
 *
 * @author mlbv
 */
@Mixin(value = PermaSyncHandler.class, remap = false)
public class MixinPermaSyncHandler {

    @Inject(method = "writePacket", at = @At("TAIL"))
    private static void afterWritePacket(ByteBuf buf, World world, EntityPlayerMP player, CallbackInfo ci) {
        /// CBT ///
        if(world.getTotalWorldTime() % 5 == 1) { // update a little less frequently to not blast the players with large packets
            buf.writeBoolean(true);

            SolarSystemWorldSavedData solarSystemData = SolarSystemWorldSavedData.get(world);
            for(CelestialBody body : CelestialBody.getAllBodies()) {
                HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait> traits = solarSystemData.getTraits(body.name);
                if(traits != null) {
                    buf.writeBoolean(true); // Has traits marker (since we can have an empty list)
                    buf.writeInt(traits.size());

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

            // long ass line award
            List<OrbitalStation> stations = solarSystemData.getStations().values().stream()
                    .filter(station -> station.hasStation && station.orbiting.dimensionId == player.dimension)
                    .toList();

            buf.writeInt(stations.size());
            for(OrbitalStation station : stations) {
                buf.writeInt(station.dX);
                buf.writeInt(station.dZ);
            }
        } else {
            buf.writeBoolean(false);
        }
        /// CBT ///

        /// TIME OF DAY ///
        if(world.provider instanceof WorldProviderCelestial celestial && world.provider.getDimension() != 0) {
            buf.writeBoolean(true);
            celestial.serialize(buf);
        } else {
            buf.writeBoolean(false);
        }
        /// TIME OF DAY ///
    }

    @Inject(method = "readPacket", at = @At("TAIL"))
    private static void afterReadPacket(ByteBuf buf, World world, EntityPlayer player, CallbackInfo ci) {
        /// CBT ///
        if(buf.readBoolean()) {
            try {
                HashMap<String, HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait>> traitMap = SolarSystemWorldSavedData.clientTraits;

                if(traitMap == null) {
                    traitMap = new HashMap<>();
                    SolarSystemWorldSavedData.updateClientTraits(traitMap);
                }

                for(CelestialBody body : CelestialBody.getAllBodies()) {
                    if(buf.readBoolean()) {
                        HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait> traits = traitMap.computeIfAbsent(body.name, _ -> new HashMap<>());

                        List<Class<? extends CelestialBodyTrait>> sentTraits = new ArrayList<>();

                        int cbtSize = buf.readInt();
                        for(int i = 0; i < cbtSize; i++) {
                            Class<? extends CelestialBodyTrait> clazz = CelestialBodyTrait.traitList.get(buf.readInt());
                            sentTraits.add(clazz);

                            CelestialBodyTrait trait = traits.getOrDefault(clazz, clazz.getDeclaredConstructor().newInstance());
                            trait.readFromBytes(buf);

                            traits.put(trait.getClass(), trait);
                        }

                        traits.keySet().removeIf(traitClass -> !sentTraits.contains(traitClass));
                    } else {
                        traitMap.remove(body.name);
                    }
                }

                OrbitalStation.orbitingStations.clear();
                int count = buf.readInt();
                for(int i = 0; i < count; i++) {
                    OrbitalStation.orbitingStations.add(new OrbitalStation(null, buf.readInt(), buf.readInt()));
                }
            } catch (Exception ex) {
                MainRegistry.logger.catching(ex);
                SolarSystemWorldSavedData.updateClientTraits(null);
                return;
            }
        }
        /// CBT ///

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
