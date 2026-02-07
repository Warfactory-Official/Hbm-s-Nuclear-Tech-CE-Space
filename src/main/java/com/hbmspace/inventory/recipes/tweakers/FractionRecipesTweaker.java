package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.FractionRecipes;
import com.hbm.util.Tuple;

import static com.hbm.inventory.recipes.FractionRecipes.fractions;

public class FractionRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(FractionRecipes.class)) return;
        fractions.put(com.hbmspace.inventory.fluid.Fluids.CONGLOMERA, new Tuple.Pair<>(new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 25), new FluidStack(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL, 75)));
        fractions.put(com.hbmspace.inventory.fluid.Fluids.HGAS, new Tuple.Pair<>(new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROMETHANE, 25), new FluidStack(Fluids.CHLORINE, 85)));
        fractions.put(com.hbmspace.inventory.fluid.Fluids.HALOLIGHT, new Tuple.Pair<>(new FluidStack(Fluids.PHOSGENE, 35), new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROETHANE, 75)));
    }
}
