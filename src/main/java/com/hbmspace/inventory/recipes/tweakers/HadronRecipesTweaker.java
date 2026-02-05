package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.recipes.HadronRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

public class HadronRecipesTweaker {
    public static void init() {
        HadronRecipes.getRecipes().add(new HadronRecipes.HadronRecipe(
                new ItemStack(ModBlocksSpace.eu_log, 5),
                new ItemStack(ModItems.particle_digamma),
                1000000,
                new ItemStack(ModItemsSpace.powder_wd2004_tiny),
                new ItemStack(ModItems.fallout),
                false
        ));
    }
}
