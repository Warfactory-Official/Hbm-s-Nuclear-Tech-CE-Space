package com.hbmspace.mixin.mod.hbm;

import com.hbm.items.ModItems;
import com.hbm.items.gear.ArmorFSB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(ModItems.class)
public abstract class MixinModItems {
    // Th3_Sl1ze: apparently, injecting in cloneStats didn't work as it injects later than items are initialized
    // I thought of re-cloning the stats but why the fuck would I need to copy ALL the stats again just for canSeal if I can set the canSeal manually?
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void applySealed(CallbackInfo ci) {
        try {
            Method m = ArmorFSB.class.getMethod("setSealed", boolean.class);
            m.invoke(ModItems.t51_helmet, true);
            m.invoke(ModItems.t51_plate, true);
            m.invoke(ModItems.t51_legs, true);
            m.invoke(ModItems.t51_boots, true);
            m.invoke(ModItems.steamsuit_helmet, true);
            m.invoke(ModItems.steamsuit_plate, true);
            m.invoke(ModItems.steamsuit_legs, true);
            m.invoke(ModItems.steamsuit_boots, true);
            m.invoke(ModItems.envsuit_helmet, true);
            m.invoke(ModItems.envsuit_plate, true);
            m.invoke(ModItems.envsuit_legs, true);
            m.invoke(ModItems.envsuit_boots, true);
            m.invoke(ModItems.dieselsuit_helmet, true);
            m.invoke(ModItems.dieselsuit_plate, true);
            m.invoke(ModItems.dieselsuit_legs, true);
            m.invoke(ModItems.dieselsuit_boots, true);
            m.invoke(ModItems.trenchmaster_helmet, true);
            m.invoke(ModItems.trenchmaster_plate, true);
            m.invoke(ModItems.trenchmaster_legs, true);
            m.invoke(ModItems.trenchmaster_boots, true);
            m.invoke(ModItems.dns_helmet, true);
            m.invoke(ModItems.dns_plate, true);
            m.invoke(ModItems.dns_legs, true);
            m.invoke(ModItems.dns_boots, true);
            m.invoke(ModItems.fau_helmet, true);
            m.invoke(ModItems.fau_plate, true);
            m.invoke(ModItems.fau_legs, true);
            m.invoke(ModItems.fau_boots, true);
            m.invoke(ModItems.rpa_helmet, true);
            m.invoke(ModItems.rpa_plate, true);
            m.invoke(ModItems.rpa_legs, true);
            m.invoke(ModItems.rpa_boots, true);
            m.invoke(ModItems.ajr_helmet, true);
            m.invoke(ModItems.ajr_plate, true);
            m.invoke(ModItems.ajr_legs, true);
            m.invoke(ModItems.ajr_boots, true);
            m.invoke(ModItems.ajro_helmet, true);
            m.invoke(ModItems.ajro_plate, true);
            m.invoke(ModItems.ajro_legs, true);
            m.invoke(ModItems.ajro_boots, true);
            m.invoke(ModItems.hev_helmet, true);
            m.invoke(ModItems.hev_plate, true);
            m.invoke(ModItems.hev_legs, true);
            m.invoke(ModItems.hev_boots, true);
        } catch (Throwable ignored) {
        }
    }
}
