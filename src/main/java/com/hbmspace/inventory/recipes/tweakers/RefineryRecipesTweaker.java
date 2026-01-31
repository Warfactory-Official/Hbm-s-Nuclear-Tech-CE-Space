package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.recipes.RefineryRecipes;
import com.hbm.util.Tuple;
import com.hbmspace.inventory.fluid.Fluids;

import java.lang.reflect.Field;
import java.util.Map;

public class RefineryRecipesTweaker {

    public static final int vac_frac_heavy = 40;
    public static final int vac_frac_reform = 25;
    public static final int vac_frac_light = 20;
    public static final int vac_frac_sour = 15;

    public static void init() {
        try {
            Field recipesField = RefineryRecipes.class.getDeclaredField("vacuum");
            recipesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<FluidType, Tuple.Quartet<FluidStack, FluidStack, FluidStack, FluidStack>> recipes =
                    (Map<FluidType, Tuple.Quartet<FluidStack, FluidStack, FluidStack, FluidStack>>) recipesField.get(null);
            recipes.put(Fluids.TCRUDE, new Tuple.Quartet<>(
                    new FluidStack(Fluids.HALOLIGHT,		vac_frac_heavy),
                    new FluidStack(Fluids.CHLOROMETHANE,	vac_frac_reform),
                    new FluidStack(Fluids.HCL,				vac_frac_light),
                    new FluidStack(Fluids.HGAS,				vac_frac_sour)
            ));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
