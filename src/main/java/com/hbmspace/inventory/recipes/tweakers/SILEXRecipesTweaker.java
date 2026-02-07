package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.SILEXRecipes;
import com.hbm.items.ModItems;
import com.hbm.items.special.ItemWasteShort;
import com.hbm.util.WeightedRandomObject;
import com.hbmspace.enums.EnumAddonWasteTypes;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.recipes.SILEXRecipes.recipes;

public class SILEXRecipesTweaker {

    public static void init() {
        // TODO add another crystal + stop forgetting to do things + implement space changes(!)

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.ingot_cm_mix), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm244), 3)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm245), 6)
        );

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.ingot_cm_mix), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new WeightedRandomObject(new ItemStack(ModItemsSpace.nugget_cm244), 3))
                .addOut(new WeightedRandomObject(new ItemStack(ModItemsSpace.nugget_cm245), 6))
        );


        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short, 1, EnumAddonWasteTypes.AMERICIUM241.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm_mix), 40)
                .addOut(new ItemStack(ModItems.nugget_pu239), 10)
                .addOut(new ItemStack(ModItems.powder_cs137_tiny), 5)
                .addOut(new ItemStack(ModItems.powder_i131_tiny), 5)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 10)
                .addOut(new ItemStack(ModItems.nugget_am242), 30)
        );
        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short_depleted, 1, EnumAddonWasteTypes.AMERICIUM241.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm_mix), 50)
                .addOut(new ItemStack(ModItems.nugget_pu239), 20)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 20)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm242), 10)
        );
        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short, 1, ItemWasteShort.WasteClass.AMERICIUM242.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm_mix), 70)
                .addOut(new ItemStack(ModItems.nugget_pu239), 10)
                .addOut(new ItemStack(ModItems.powder_cs137_tiny), 5)
                .addOut(new ItemStack(ModItems.powder_i131_tiny), 5)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 10)
        );
        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short_depleted, 1, ItemWasteShort.WasteClass.AMERICIUM242.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm_mix), 50)
                .addOut(new ItemStack(ModItems.nugget_pu239), 20)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 10)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm242), 20)
        );
        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short, 1, EnumAddonWasteTypes.BERKELIUM247.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf251), 40)
                .addOut(new ItemStack(ModItems.nugget_am_mix), 10)
                .addOut(new ItemStack(ModItems.powder_cs137_tiny), 5)
                .addOut(new ItemStack(ModItems.powder_sr90_tiny), 5)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 10)
        );

        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short_depleted, 1, EnumAddonWasteTypes.BERKELIUM247.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm_mix), 50)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf251), 20)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 10)
                .addOut(new ItemStack(ModItems.nugget_am_mix), 20)
        );

        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short, 1, EnumAddonWasteTypes.CURIUM244.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm245), 30)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm246), 15)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm247), 10)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf251), 25)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf252), 20)
                .addOut(new ItemStack(ModItemsSpace.nugget_es253), 10)
        );
        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short_depleted, 1, EnumAddonWasteTypes.CURIUM244.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm_mix), 40) //from short-lived californium isotope decay
                .addOut(new ItemStack(ModItemsSpace.nugget_cm246), 5)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm247), 15)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 10)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf251), 20)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf252), 10)
        );
        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short, 1, EnumAddonWasteTypes.CURIUM245.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm246), 15)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm247), 10)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf251), 35)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf252), 30)
                .addOut(new ItemStack(ModItemsSpace.nugget_es253), 10)
        );
        recipes.put(new RecipesCommon.ComparableStack(ModItems.nuclear_waste_short_depleted, 1, EnumAddonWasteTypes.CURIUM245.ordinal()), new SILEXRecipes.SILEXRecipe(900, 100, 2)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm_mix), 10) //from short-lived californium isotope decay
                .addOut(new ItemStack(ModItemsSpace.nugget_cm246), 5)
                .addOut(new ItemStack(ModItemsSpace.nugget_cm247), 15)
                .addOut(new ItemStack(ModItems.nuclear_waste_tiny), 10)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf252), 15)
                .addOut(new ItemStack(ModItemsSpace.nugget_cf251), 25)
        );
    }
}
