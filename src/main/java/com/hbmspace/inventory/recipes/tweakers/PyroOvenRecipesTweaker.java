package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.recipes.PyroOvenRecipes;

import java.lang.reflect.Method;

import static com.hbmspace.inventory.fluid.Fluids.POLYTHYLENE;

public class PyroOvenRecipesTweaker {

    public static void init() {
        if(RecipeTweakerManager.isModified(PyroOvenRecipes.class)) return;
        try {
            Method m = PyroOvenRecipes.class.getDeclaredMethod("registerSFAuto", FluidType.class);
            m.setAccessible(true);
            m.invoke(null, POLYTHYLENE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
