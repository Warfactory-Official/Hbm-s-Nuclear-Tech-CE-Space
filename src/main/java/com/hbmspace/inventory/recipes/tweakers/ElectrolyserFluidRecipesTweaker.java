package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ElectrolyserFluidRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.recipes.ElectrolyserFluidRecipes.recipes;

public class ElectrolyserFluidRecipesTweaker {

    public static void init() {
        recipes.put(com.hbmspace.inventory.fluid.Fluids.BRINE, new ElectrolyserFluidRecipes.ElectrolysisRecipe(400, new FluidStack(Fluids.HYDROGEN, 200), new FluidStack(Fluids.OXYGEN, 200), 40, new ItemStack(ModItems.powder_sodium, 2)));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL, new ElectrolyserFluidRecipes.ElectrolysisRecipe(300, new FluidStack(Fluids.NONE, 0), new FluidStack(Fluids.NONE, 0), 40, new ItemStack(ModItemsSpace.powder_nickel, 2), new ItemStack(ModItems.powder_iron, 2), new ItemStack(ModItems.sulfur, 4)));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.COPPERSULFATE, new ElectrolyserFluidRecipes.ElectrolysisRecipe(200, new FluidStack(Fluids.NONE, 0), new FluidStack(Fluids.OXYGEN, 50), 40, new ItemStack(ModItems.powder_copper, 2), new ItemStack(ModItems.sulfur, 2)));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.HCL, new ElectrolyserFluidRecipes.ElectrolysisRecipe(1_000, new FluidStack(Fluids.HYDROGEN, 500), new FluidStack(Fluids.CHLORINE, 500), 40));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.LITHCARBONATE, new ElectrolyserFluidRecipes.ElectrolysisRecipe(1000, new FluidStack(Fluids.OXYGEN, 30), new FluidStack(Fluids.CARBONDIOXIDE, 10), 40, new ItemStack(ModItems.powder_lithium, 1)));
    }
}
