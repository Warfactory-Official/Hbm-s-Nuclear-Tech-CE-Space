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
        if (SpaceMain.checkNTMVersion("[2.4.0.0,)")) {
            CrucibleModernHandler.init();
        } else {
            CrucibleLegacyHandler.init();
        }
    }
    // Th3_Sl1ze: I probably should make a NTMCompatHandler or something instead of leaving static classes here..
    private static class CrucibleModernHandler {
        private static void init() {
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

    private static class CrucibleLegacyHandler {
        private static void init() {
            try {
                int n = MaterialShapes.NUGGET.q(1);
                int i = MaterialShapes.INGOT.q(1);

                Class<?> crucibleRecipesClass = Class.forName("com.hbm.inventory.recipes.CrucibleRecipes");
                Class<?> recipeClass = Class.forName("com.hbm.inventory.recipes.CrucibleRecipes$CrucibleRecipe");

                Field recipesField = crucibleRecipesClass.getField("recipes");
                List<Object> recipesList = (List<Object>) recipesField.get(null);

                Constructor<?> constr = recipeClass.getConstructor(int.class, String.class, int.class, ItemStack.class);
                Method inputsMethod = recipeClass.getMethod("inputs", Mats.MaterialStack[].class);
                Method outputsMethod = recipeClass.getMethod("outputs", Mats.MaterialStack[].class);

                Object rec1 = constr.newInstance(18, "crucible.hsss", 12, new ItemStack(ModItems.ingot_dura_steel));
                inputsMethod.invoke(rec1, (Object) new Mats.MaterialStack[]{new Mats.MaterialStack(MatsSpace.MAT_STAINLESS, n * 5), new Mats.MaterialStack(Mats.MAT_TUNGSTEN, n * 3), new Mats.MaterialStack(Mats.MAT_COBALT, n)});
                outputsMethod.invoke(rec1, (Object) new Mats.MaterialStack[]{new Mats.MaterialStack(Mats.MAT_DURA, i * 2)});
                recipesList.add(rec1);

                Object rec2 = constr.newInstance(19, "crucible.arse", 9, new ItemStack(ModItemsSpace.ingot_gaas));
                inputsMethod.invoke(rec2, (Object) new Mats.MaterialStack[]{new Mats.MaterialStack(MatsSpace.MAT_GALLIUM, n * 6), new Mats.MaterialStack(Mats.MAT_ARSENIC, n * 3)});
                outputsMethod.invoke(rec2, (Object) new Mats.MaterialStack[]{new Mats.MaterialStack(MatsSpace.MAT_GAAS, i)});
                recipesList.add(rec2);

                Object rec3 = constr.newInstance(20, "crucible.stainless", 2, new ItemStack(ModItemsSpace.ingot_stainless));
                inputsMethod.invoke(rec3, (Object) new Mats.MaterialStack[]{new Mats.MaterialStack(Mats.MAT_STEEL, n), new Mats.MaterialStack(MatsSpace.MAT_NICKEL, n)});
                outputsMethod.invoke(rec3, (Object) new Mats.MaterialStack[]{new Mats.MaterialStack(MatsSpace.MAT_STAINLESS, n * 2)});
                recipesList.add(rec3);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
