package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.BreederRecipes;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

public class BreederRecipesTweaker {

    public static void init() {
        BreederRecipes.addRecipe(new ComparableStack(new ItemStack(ModBlocksSpace.lattice_log)), new ItemStack(ModItemsSpace.woodemium_briquette), 4000);
    }
}
