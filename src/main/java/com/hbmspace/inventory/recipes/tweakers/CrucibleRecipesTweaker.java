package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.recipes.CrucibleRecipe;
import com.hbm.inventory.recipes.CrucibleRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.inventory.materials.MatsSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.main.SpaceMain;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class CrucibleRecipesTweaker {

    public static void init() {
        int n = MaterialShapes.NUGGET.q(1);
        int i = MaterialShapes.INGOT.q(1);
        CrucibleRecipes recs = CrucibleRecipes.INSTANCE;

        recs.register(new CrucibleRecipe("crucible.hsss").setup(12, new ItemStack(ModItems.ingot_dura_steel))
                .inputs(new Mats.MaterialStack(MatsSpace.MAT_STAINLESS, n * 5), new Mats.MaterialStack(Mats.MAT_TUNGSTEN, n * 3), new Mats.MaterialStack(Mats.MAT_COBALT, n))
                .outputs(new Mats.MaterialStack(Mats.MAT_DURA, i * 2)));

        recs.register(new CrucibleRecipe("crucible.arse").setup(9, new ItemStack(ModItemsSpace.ingot_gaas))
                .inputs(new Mats.MaterialStack(MatsSpace.MAT_GALLIUM, n * 6), new Mats.MaterialStack(Mats.MAT_ARSENIC, n * 3))
                .outputs(new Mats.MaterialStack(MatsSpace.MAT_GAAS, i)));

        recs.register(new CrucibleRecipe("crucible.stainless").setup(2, new ItemStack(ModItemsSpace.ingot_stainless))
                .inputs(new Mats.MaterialStack(Mats.MAT_STEEL, n), new Mats.MaterialStack(MatsSpace.MAT_NICKEL, n))
                .outputs(new Mats.MaterialStack(MatsSpace.MAT_STAINLESS, n * 2)));
    }
}
