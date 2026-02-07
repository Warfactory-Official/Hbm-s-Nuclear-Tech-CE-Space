package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.HydrotreatingRecipes;
import com.hbm.util.Tuple;

import static com.hbm.inventory.recipes.HydrotreatingRecipes.recipes;

public class HydrotreatingRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(HydrotreatingRecipes.class)) return;
        recipes.put(com.hbmspace.inventory.fluid.Fluids.CCL, new Tuple.Triplet<>(
                new FluidStack(Fluids.HYDROGEN, 10, 1),
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROMETHANE, 80),
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 20)
        ));

        recipes.put(com.hbmspace.inventory.fluid.Fluids.CBENZ, new Tuple.Triplet<>(
                new FluidStack(Fluids.HYDROGEN, 10, 1),
                new FluidStack(Fluids.XYLENE, 50),
                new FluidStack(Fluids.CHLORINE, 40)
        ));

        recipes.put(com.hbmspace.inventory.fluid.Fluids.CHLOROETHANE, new Tuple.Triplet<>(
                new FluidStack(Fluids.HYDROGEN, 10, 1),
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.VINYL, 50),
                new FluidStack(Fluids.CHLORINE, 20)
        ));
    }
}
