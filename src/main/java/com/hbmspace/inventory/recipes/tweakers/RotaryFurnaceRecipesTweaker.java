package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.recipes.RotaryFurnaceRecipes;
import com.hbmspace.inventory.OreDictManagerSpace;

import java.util.ListIterator;

public class RotaryFurnaceRecipesTweaker {

    public static void init() {
        final String cu = OreDictManager.CU.ingot();
        final String al = OreDictManager.AL.ingot();

        for (ListIterator<RotaryFurnaceRecipes.RotaryFurnaceRecipe> it = RotaryFurnaceRecipes.recipes.listIterator(); it.hasNext(); ) {
            RotaryFurnaceRecipes.RotaryFurnaceRecipe r = it.next();

            if (r.output == null || r.output.material != Mats.MAT_GUNMETAL) continue;
            if (r.fluid != null) continue;
            if (r.ingredients == null || r.ingredients.length != 2) continue;

            boolean matchCu = false;
            boolean matchAl = false;

            for (RecipesCommon.AStack stack : r.ingredients) {
                if (stack instanceof RecipesCommon.OreDictStack ods) {
                    if (ods.name.equals(cu) && ods.stacksize == 3) matchCu = true;
                    if (ods.name.equals(al) && ods.stacksize == 1) matchAl = true;
                }
            }

            if (matchCu && matchAl) {
                it.set(new RotaryFurnaceRecipes.RotaryFurnaceRecipe(
                        new Mats.MaterialStack(Mats.MAT_GUNMETAL, 4), 200, 100,
                        new RecipesCommon.OreDictStack(OreDictManager.CU.ingot(), 3),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.ZI.ingot(), 1)
                ));
                break;
            }
        }
    }
}
