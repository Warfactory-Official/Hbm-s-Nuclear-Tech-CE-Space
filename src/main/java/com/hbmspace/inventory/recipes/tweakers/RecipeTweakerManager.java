package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.api.recipe.IRecipeRegisterListener;
import com.hbm.inventory.recipes.loader.SerializableRecipe;

public class RecipeTweakerManager {

    public static void initRecipeTweakers() {
        SerializableRecipe.additionalListeners.add(recipeHandlerName -> {
            switch (recipeHandlerName) {
                case "AnvilRecipes": AnvilRecipeTweaker.init(); break;
                case "AnvilSmithingRecipes": AnvilSmithingRecipeTweaker.init(); break;
                case "ArcWelderRecipes": ArcWelderRecipesTweaker.init(); break;
                case "AssemblerRecipes":
                case "AssemblyMachineRecipes": AssemblyRecipesTweaker.init(); break;
                case "BlastFurnaceRecipes": BlastFurnaceRecipesTweaker.init(); break;
                case "CentrifugeRecipes": CentrifugeRecipesTweaker.init(); break;
                case "ChemicalPlantRecipes": ChemicalPlantRecipesTweaker.init(); break;
                case "CrackingRecipes": CrackingRecipesTweaker.init(); break;
                case "CrucibleRecipes": CrucibleRecipesTweaker.init(); break;
                case "CrystallizerRecipes": CrystallizerRecipesTweaker.init(); break;
                case "CyclotronRecipes": CyclotronRecipesTweaker.init(); break;
                case "ElectrolyserFluidRecipes": ElectrolyserFluidRecipesTweaker.init(); break;
                case "FractionRecipes": FractionRecipesTweaker.init(); break;
                case "HadronRecipes": HadronRecipesTweaker.init(); break;
                case "HydrotreatingRecipes": HydrotreatingRecipesTweaker.init(); break;
                case "LiquefactionRecipes": LiquefactionRecipesTweaker.init(); break;
                case "MagicRecipes": MagicRecipesTweaker.init(); break;
                case "MixerRecipes": MixerRecipesTweaker.init(); break;
                case "OutgasserRecipes": OutgasserRecipesTweaker.init(); break;
                case "PUREXRecipes": PUREXRecipesTweaker.init(); break;
                case "PressRecipes": PressRecipesTweaker.init(); break;
                case "PyroOvenRecipes": PyroOvenRecipesTweaker.init(); break;
                case "RefineryRecipes": RefineryRecipesTweaker.init(); break;
                case "ReformingRecipes": ReformingRecipesTweaker.init(); break;
                case "RotaryFurnaceRecipes": RotaryFurnaceRecipesTweaker.init(); break;
                case "SILEXRecipes": SILEXRecipesTweaker.init(); break;
                case "ShredderRecipes": ShredderRecipesTweaker.init(); break;
                case "SolderingRecipes": SolderingRecipesTweaker.init(); break;
                case "SolidificationRecipes": SolidificationRecipesTweaker.init(); break;
            }
        });
    }
}
