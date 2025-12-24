package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.GasCentrifugeRecipes;
import com.hbm.items.ModItems;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = GasCentrifugeRecipes.class, remap = false)
public abstract class MixinGasCentrifugeRecipes {

    @Shadow
    private static Map<FluidStack, Object[]> gasCent;
    @Shadow public static HashMap<FluidType, GasCentrifugeRecipes.PseudoFluidType> fluidConversions;

    @Inject(method = "register", at = @At("TAIL"))
    private static void hbm$registerMinsol(CallbackInfo ci) {
        GasCentrifugeRecipes.PseudoFluidType minsol = GasCentrifugeRecipes.PseudoFluidType.types.get("MINSOL");
        if (minsol != null) {
            fluidConversions.put(Fluids.MINSOL, minsol);
        }

        gasCent.put(new FluidStack(1000, Fluids.MINSOL),
                new Object[] { new ItemStack[] { new ItemStack(ModItems.powder_iron, 1) }, false, 2 });
    }
}
