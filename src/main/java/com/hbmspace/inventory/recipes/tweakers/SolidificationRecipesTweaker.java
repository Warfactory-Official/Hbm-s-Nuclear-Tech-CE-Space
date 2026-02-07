package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.recipes.SolidificationRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.fluid.Fluids.BLOOD;
import static com.hbm.inventory.fluid.Fluids.CARBONDIOXIDE;
import static com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL;
import static com.hbmspace.inventory.fluid.Fluids.VINYL;

public class SolidificationRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(SolidificationRecipes.class)) return;
        SolidificationRecipes.registerRecipe(BLOOD, 1290, new ItemStack(ModItemsSpace.flesh_wafer, 5));
        SolidificationRecipes.registerRecipe(CARBONDIOXIDE, 1000, ModBlocksSpace.dry_ice);
        SolidificationRecipes.registerRecipe(AQUEOUS_NICKEL, 500, ModItemsSpace.nickel_salts);
        SolidificationRecipes.registerRecipe(VINYL, 1000, ModItems.ingot_pvc);
    }
}
