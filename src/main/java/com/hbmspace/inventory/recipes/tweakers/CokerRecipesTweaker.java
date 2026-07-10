package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.recipes.CokerRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.fluid.Fluids.*;
import static com.hbm.inventory.fluid.Fluids.GAS_COKER;
import static com.hbmspace.inventory.fluid.Fluids.BROMINE;
import static com.hbmspace.inventory.fluid.Fluids.SCUTTERBLOOD;

public class CokerRecipesTweaker {

    public static void init(){
        CokerRecipes.registerRecipe(BROMINE, 1_000, new ItemStack(ModItems.powder_bromine, 1), new FluidStack(GAS, 500));
        CokerRecipes.registerRecipe(SCUTTERBLOOD, 16_000, OreDictManager.DictFrame.fromOne(ModItems.coke, ItemEnums.EnumCokeType.PETROLEUM), new FluidStack(GAS_COKER, 1_600));
    }
}
