package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.FractionRecipes;
import com.hbm.util.Tuple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = FractionRecipes.class, remap = false)
public class MixinFractionRecipes {

    @Shadow
    public static Map<FluidType, Tuple.Pair<FluidStack, FluidStack>> fractions;

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        fractions.put(com.hbmspace.inventory.fluid.Fluids.CONGLOMERA,		new Tuple.Pair(new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE,					25),		new FluidStack(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL,		75)));
        fractions.put(com.hbmspace.inventory.fluid.Fluids.HGAS,			new Tuple.Pair(new FluidStack(Fluids.CHLOROMETHANE,				25),		new FluidStack(Fluids.CHLORINE,		85)));
        fractions.put(com.hbmspace.inventory.fluid.Fluids.HALOLIGHT,		new Tuple.Pair(new FluidStack(Fluids.PHOSGENE,				35),		new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROETHANE,		75)));
    }
}
