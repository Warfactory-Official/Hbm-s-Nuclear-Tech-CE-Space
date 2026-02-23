package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.CrackingRecipes;
import com.hbm.util.Tuple;

import static com.hbm.inventory.recipes.CrackingRecipes.cracking;

public class CrackingRecipesTweaker {

    //cracking in percent
    public static final int oil_crack_oil = 80;
    public static final int oil_crack_petro = 20;
    public static final int diesel_crack_kero = 40;
    public static final int diesel_crack_petro = 30;

    public static void init() {
        cracking.put(Fluids.OIL_DS, new Tuple.Pair<>(new FluidStack(Fluids.CRACKOIL_DS, oil_crack_oil), new FluidStack(Fluids.PETROLEUM, oil_crack_petro)));
        cracking.put(Fluids.DIESEL_REFORM, new Tuple.Pair<>(new FluidStack(Fluids.KEROSENE_REFORM, diesel_crack_kero), new FluidStack(Fluids.PETROLEUM, diesel_crack_petro)));
        cracking.put(Fluids.DIESEL_CRACK_REFORM, new Tuple.Pair<>(new FluidStack(Fluids.KEROSENE_REFORM, diesel_crack_kero), new FluidStack(Fluids.PETROLEUM, diesel_crack_petro)));
        cracking.put(com.hbmspace.inventory.fluid.Fluids.CHLOROETHANE, new Tuple.Pair<>(new FluidStack(com.hbmspace.inventory.fluid.Fluids.POLYTHYLENE, 50), new FluidStack(Fluids.CHLORINE, 30)));
        cracking.put(com.hbmspace.inventory.fluid.Fluids.ELBOWGREASE, new Tuple.Pair<>(new FluidStack(Fluids.MERCURY, 70), new FluidStack(Fluids.SOURGAS, 50)));
        cracking.put(com.hbmspace.inventory.fluid.Fluids.CBENZ, new Tuple.Pair<>(new FluidStack(Fluids.AROMATICS, 50), new FluidStack(Fluids.CHLORINE, 30)));
    }
}
