package com.hbmspace.inventory.recipes.tweakers;


import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.recipes.CyclotronRecipes.makeRecipe;

public class CyclotronRecipesTweaker {
    public static void init() {
        makeRecipe(new RecipesCommon.ComparableStack(ModBlocks.block_euphemium), new RecipesCommon.ComparableStack(ModBlocksSpace.bf_log), new ItemStack(ModBlocksSpace.eu_log), 0);
    }
}
