package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.inventory.recipes.anvil.AnvilSmithingRecipe;
import com.hbm.items.ModItems;
import com.hbmspace.inventory.OreDictManagerSpace;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
// Th3_Sl1ze: behold, some slop for removing the CE-main gunmetal recipe
public class AnvilSmithingRecipeTweaker {

    public static void init() {
        if (!(AnvilRecipes.smithingRecipes instanceof TweakedRecipeList)) {
            List<AnvilSmithingRecipe> oldList = AnvilRecipes.smithingRecipes;
            AnvilRecipes.smithingRecipes = new TweakedRecipeList();
            AnvilRecipes.smithingRecipes.addAll(oldList);
        }

        AnvilRecipes.smithingRecipes.removeIf(recipe -> recipe != null && stacksEqual(recipe.getSimpleOutput(), new ItemStack(ModItems.ingot_gunmetal, 1)));

        AnvilSmithingRecipe newRecipe = new AnvilSmithingRecipe(
                1,
                new ItemStack(ModItems.ingot_gunmetal, 1),
                new RecipesCommon.OreDictStack(OreDictManager.CU.ingot()),
                new RecipesCommon.OreDictStack(OreDictManagerSpace.ZI.ingot())
        );

        ((TweakedRecipeList) AnvilRecipes.smithingRecipes).addTweaked(newRecipe);
    }

    private static boolean stacksEqual(ItemStack a, ItemStack b) {
        return a != null && b != null && !a.isEmpty() && !b.isEmpty()
                && a.getItem() == b.getItem()
                && a.getItemDamage() == b.getItemDamage();
    }

    private static class TweakedRecipeList extends ArrayList<AnvilSmithingRecipe> {
        @Override
        public boolean add(AnvilSmithingRecipe recipe) {
            if (recipe != null && stacksEqual(recipe.getSimpleOutput(), new ItemStack(ModItems.ingot_gunmetal, 1))) {
                return false;
            }
            return super.add(recipe);
        }

        public void addTweaked(AnvilSmithingRecipe recipe) {
            super.add(recipe);
        }
    }
}