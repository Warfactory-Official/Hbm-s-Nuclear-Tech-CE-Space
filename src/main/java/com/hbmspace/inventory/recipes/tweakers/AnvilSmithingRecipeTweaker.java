package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.inventory.recipes.anvil.AnvilSmithingRecipe;
import com.hbm.items.ModItems;
import com.hbmspace.inventory.OreDictManagerSpace;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class AnvilSmithingRecipeTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(AnvilRecipes.class)) return;

        Iterator<AnvilSmithingRecipe> iterator = AnvilRecipes.smithingRecipes.iterator();
        while (iterator.hasNext()) {
            AnvilSmithingRecipe recipe = iterator.next();
            if (recipe != null && stacksEqual(recipe.getSimpleOutput(), new ItemStack(ModItems.ingot_gunmetal, 1))) {
                iterator.remove();
                break;
            }
        }

        AnvilRecipes.smithingRecipes.add(new AnvilSmithingRecipe(
                1,
                new ItemStack(ModItems.ingot_gunmetal, 1),
                new RecipesCommon.OreDictStack(OreDictManager.CU.ingot()),
                new RecipesCommon.OreDictStack(OreDictManagerSpace.ZI.ingot())
        ));
    }

    private static boolean stacksEqual(ItemStack a, ItemStack b) {
        return a != null && b != null && !a.isEmpty() && !b.isEmpty()
                && a.getItem() == b.getItem()
                && a.getMetadata() == b.getMetadata();
    }
}
