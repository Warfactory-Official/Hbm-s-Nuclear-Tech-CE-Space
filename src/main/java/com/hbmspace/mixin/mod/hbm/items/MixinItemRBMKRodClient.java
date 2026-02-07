package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.machine.ItemRBMKRod;
import com.hbm.items.machine.ItemRBMKRod.EnumBurnFunc;
import com.hbmspace.enums.EnumAddonRBMKRodTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemRBMKRod.class, remap = false)
public abstract class MixinItemRBMKRodClient {

    @Shadow public double reactivity;
    @Shadow public double selfRate;
    @Shadow public EnumBurnFunc function;
    @Shadow public abstract double reactivityModByEnrichment(double enrichment);

    @Inject(method = "getFuncDescription", at = @At("HEAD"), cancellable = true)
    private void handleSlowLinearDesc(ItemStack stack, CallbackInfoReturnable<String> cir) {
        if (this.function == EnumAddonRBMKRodTypes.SLOW_LINEAR) {
            String formula = "sqrt(2 * %1$s + 30) / 10 * %2$s / 2.5";
            double enrichment = ItemRBMKRod.getEnrichment(stack);

            if (enrichment < 1) {
                enrichment = reactivityModByEnrichment(enrichment);
                String reactStr = TextFormatting.YELLOW + "" + ((int) (this.reactivity * enrichment * 1000D) / 1000D) + TextFormatting.WHITE;
                String enrichPer = TextFormatting.GOLD + " (" + ((int) (enrichment * 1000D) / 10D) + "%)";
                cir.setReturnValue(String.format(formula,
                        selfRate > 0 ? "(x" + TextFormatting.RED + " + " + selfRate + TextFormatting.WHITE + ")" : "x",
                        reactStr).concat(enrichPer));
            } else {
                cir.setReturnValue(String.format(formula,
                        selfRate > 0 ? "(x" + TextFormatting.RED + " + " + selfRate + TextFormatting.WHITE + ")" : "x",
                        reactivity));
            }
        }
    }
}
