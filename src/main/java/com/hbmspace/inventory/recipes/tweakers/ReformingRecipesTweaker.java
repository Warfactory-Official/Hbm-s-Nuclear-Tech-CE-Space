package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ReformingRecipes;
import com.hbm.util.Tuple;

import static com.hbm.inventory.recipes.ReformingRecipes.recipes;

public class ReformingRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(ReformingRecipes.class)) return;

        recipes.put(com.hbmspace.inventory.fluid.Fluids.HALOLIGHT, new Tuple.Triplet<>(
                new FluidStack(Fluids.UNSATURATEDS, 50),
                new FluidStack(Fluids.REFORMGAS, 35),
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 10)
        ));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.HGAS, new Tuple.Triplet<>(
                new FluidStack(Fluids.AROMATICS, 60),
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROMETHANE, 25),
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 10)
        ));
    }
}
