package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.recipes.GasCentrifugeRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GasCentrifugeRecipes.PseudoFluidType.class, remap = false)
public abstract class MixinGasCentrifugeRecipes_PseudoFluidType {

    @Invoker("<init>")
    private static GasCentrifugeRecipes.PseudoFluidType hbm$ctor(String name, int fluidConsumed, int fluidProduced,
                                                                 GasCentrifugeRecipes.PseudoFluidType outputFluid, boolean isHighSpeed, ItemStack[] output) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void hbm$addMinsolPseudoFluids(CallbackInfo ci) {
        hbm$ctor("MINSOLSEP", 500, 0, GasCentrifugeRecipes.PseudoFluidType.NONE, false,
                new ItemStack[] { new ItemStack(ModItemsSpace.crystal_cleaned, 1) });

        hbm$ctor("MINSOL", 500, 500,
                GasCentrifugeRecipes.PseudoFluidType.types.get("MINSOLSEP"),
                false, new ItemStack[] { new ItemStack(ModItems.powder_iron, 1) });
    }
}
