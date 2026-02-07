package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.PressRecipes;
import com.hbm.items.machine.ItemStamp;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;

import static com.hbm.inventory.recipes.PressRecipes.makeRecipe;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

public class PressRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(PressRecipes.class)) return;
        makeRecipe(ItemStamp.StampType.PLATE, new RecipesCommon.OreDictStack(NI.ingot()), ModItemsSpace.plate_nickel);
        makeRecipe(ItemStamp.StampType.PLATE, new RecipesCommon.OreDictStack(STAINLESS.ingot()), ModItemsSpace.plate_stainless);
        makeRecipe(ItemStamp.StampType.CIRCUIT, new RecipesCommon.OreDictStack(GAAS.billet()), OreDictManager.DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.GAAS));
    }
}
