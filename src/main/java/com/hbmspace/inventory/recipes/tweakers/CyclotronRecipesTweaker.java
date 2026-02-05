package com.hbmspace.inventory.recipes.tweakers;


import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.CyclotronRecipes;
import com.hbm.util.Tuple;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.item.ItemStack;

public class CyclotronRecipesTweaker {
    // makeRecipe is fucking PRIVATE lmao
    public static void init() {
        makeSpaceRecipe(new RecipesCommon.ComparableStack(ModBlocks.block_euphemium), new RecipesCommon.ComparableStack(ModBlocksSpace.bf_log), new ItemStack(ModBlocksSpace.eu_log), 0);
    }

    // Th3_Sl1ze: fuck my laziness
    public static void makeSpaceRecipe(RecipesCommon.ComparableStack part, RecipesCommon.AStack in, ItemStack out, int amat) {
        CyclotronRecipes.recipes.put(new Tuple.Pair<>(part, in), new Tuple.Pair<>(out, amat));
    }
}
