package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.recipes.PyroOvenRecipes;

import static com.hbmspace.inventory.fluid.Fluids.POLYTHYLENE;

public class PyroOvenRecipesTweaker {

    public static void init() {
        PyroOvenRecipes.registerSFAuto(POLYTHYLENE);
    }
}
