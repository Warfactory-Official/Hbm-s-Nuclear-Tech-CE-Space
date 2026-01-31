package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ReformingRecipes;
import com.hbm.util.Tuple;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ReformingRecipesTweaker {

    public static void init() {
        try {
            Field recipesField = ReformingRecipes.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            HashMap<FluidType, Tuple.Triplet<FluidStack, FluidStack, FluidStack>> recipes =
                    (HashMap<FluidType, Tuple.Triplet<FluidStack, FluidStack, FluidStack>>) recipesField.get(null);
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
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
