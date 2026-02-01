package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.OutgasserRecipes;
import com.hbm.items.ModItems;
import com.hbm.util.Tuple;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.BI;
import static com.hbm.inventory.OreDictManager.CO;
import static com.hbm.inventory.recipes.OutgasserRecipes.recipes;

public class OutgasserRecipesTweaker {

    public static void init() {
        if(RecipeTweakerManager.isModified(OutgasserRecipes.class)) return;
        /* cobalt to cobalt-60 */
        recipes.put(new RecipesCommon.OreDictStack(CO.ingot()),		new Tuple.Pair<>(new ItemStack(ModItems.ingot_co60), null));
        recipes.put(new RecipesCommon.OreDictStack(CO.nugget()),		new Tuple.Pair<>(new ItemStack(ModItems.nugget_co60), null));
        recipes.put(new RecipesCommon.OreDictStack(CO.dust()),		new Tuple.Pair<>(new ItemStack(ModItems.powder_co60), null));

        /* bismuth to polonium */
        recipes.put(new RecipesCommon.OreDictStack(BI.ingot()),		new Tuple.Pair<>(new ItemStack(ModItems.ingot_polonium), null));
        recipes.put(new RecipesCommon.OreDictStack(BI.nugget()),		new Tuple.Pair<>(new ItemStack(ModItems.nugget_polonium), null));
        recipes.put(new RecipesCommon.OreDictStack(BI.dust()),		new Tuple.Pair<>(new ItemStack(ModItems.powder_polonium), null));
    }
}
