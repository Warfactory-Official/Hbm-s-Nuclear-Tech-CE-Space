package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.recipes.BlastFurnaceRecipes;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.STEEL;
import static com.hbm.inventory.recipes.BlastFurnaceRecipes.addRecipe;
import static com.hbmspace.inventory.OreDictManagerSpace.NI;

public class BlastFurnaceRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(BlastFurnaceRecipes.class)) return;
        addRecipe(NI, STEEL, new ItemStack(ModItemsSpace.ingot_stainless, 2));
    }
}
