package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.recipes.CentrifugeRecipes;
import com.hbm.inventory.recipes.loader.SerializableRecipe;

public class RecipeTweakerManager {

    public static void initRecipeTweakers() {
        AnvilRecipeTweaker.init();
        ArcWelderRecipesTweaker.init();
        AssemblyRecipesTweaker.init();
        BlastFurnaceRecipesTweaker.init();
        CentrifugeRecipesTweaker.init();
        ChemicalPlantRecipesTweaker.init();
        CrackingRecipesTweaker.init();
        CrucibleRecipesTweaker.init();
        CrystallizerRecipesTweaker.init();
        CyclotronRecipesTweaker.init();
        ElectrolyserFluidRecipesTweaker.init();
        FractionRecipesTweaker.init();
        HadronRecipesTweaker.init();
        HydrotreatingRecipesTweaker.init();
        LiquefactionRecipesTweaker.init();
        MagicRecipesTweaker.init();
        MixerRecipesTweaker.init();
        OutgasserRecipesTweaker.init();
        PUREXRecipesTweaker.init();
        PressRecipesTweaker.init();
        PyroOvenRecipesTweaker.init();
        RefineryRecipesTweaker.init();
        ReformingRecipesTweaker.init();
        RotaryFurnaceRecipesTweaker.init();
        SILEXRecipesTweaker.init();
        ShredderRecipesTweaker.init();
        SolderingRecipesTweaker.init();
        SolidificationRecipesTweaker.init();
    }

    public static boolean isModified(Class<? extends SerializableRecipe> recipeClass) {
        for (SerializableRecipe handler : SerializableRecipe.recipeHandlers) {
            if (recipeClass.isInstance(handler)) {
                return handler.modified;
            }
        }
        return false;
    }
}
