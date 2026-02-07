package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.machine.ItemRBMKRod;
import com.hbm.items.machine.ItemRBMKRod.EnumBurnFunc;
import com.hbm.items.machine.ItemRBMKRod.EnumDepleteFunc;
import com.hbmspace.enums.EnumAddonRBMKRodTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemRBMKRod.class, remap = false)
public abstract class MixinItemRBMKRod {

    @Shadow public double reactivity;
    @Shadow public double selfRate;
    @Shadow public EnumBurnFunc function;
    @Shadow public EnumDepleteFunc depFunc;

    @Shadow public abstract double reactivityModByEnrichment(double enrichment);

    @Inject(method = "reactivityFunc", at = @At("HEAD"), cancellable = true)
    private void handleSlowLinear(double in, double enrichment, CallbackInfoReturnable<Double> cir) {
        if (this.function == EnumAddonRBMKRodTypes.SLOW_LINEAR) {
            double flux = in * reactivityModByEnrichment(enrichment);
            cir.setReturnValue(Math.sqrt(2 * flux + 30) / 10D * reactivity / 2.5D);
        }
    }

    @Inject(method = "reactivityModByEnrichment", at = @At("HEAD"), cancellable = true)
    private void handleCfSlope(double enrichment, CallbackInfoReturnable<Double> cir) {
        if (this.depFunc == EnumAddonRBMKRodTypes.CF_SLOPE) {
            cir.setReturnValue(enrichment + Math.sin(enrichment * Math.PI) * 1.4D);
        }
    }
}