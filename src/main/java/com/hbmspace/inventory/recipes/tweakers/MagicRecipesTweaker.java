package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.MagicRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.recipes.MagicRecipes.recipes;

public class MagicRecipesTweaker {

    public static void init() {
        recipes.add(new MagicRecipes.MagicRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.bobblehead), 1, 21),
                new RecipesCommon.ComparableStack(ModItemsSpace.ingot_gwenium),
                new RecipesCommon.ComparableStack(ModItems.ingot_dineutronium),
                new RecipesCommon.ComparableStack(ModItems.ingot_dineutronium)));
    }
}
