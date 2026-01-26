package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ArcWelderRecipes;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemArcElectrode;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.OreDictManagerSpace;
import com.hbmspace.inventory.materials.MatsSpace;
import com.hbmspace.items.ItemEnumsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.recipes.ArcWelderRecipes.recipes;
import static com.hbmspace.inventory.OreDictManagerSpace.STAINLESS;

public class ArcWelderRecipesTweaker {

    public static void init() {
        recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModItems.plate_welded, 1, MatsSpace.MAT_STAINLESS.id), 250, 20_000L,
                new RecipesCommon.OreDictStack(STAINLESS.plateCast(), 2)));

        recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModBlocksSpace.machine_xenon_thruster), 200, 50_000L, new FluidStack(Fluids.ARGON, 1_000), new RecipesCommon.OreDictStack(W.plateWelded(), 2), new RecipesCommon.ComparableStack(ModItemsSpace.plate_stainless, 6), new RecipesCommon.ComparableStack(ModItems.arc_electrode, 1, ItemArcElectrode.EnumElectrodeType.GRAPHITE)));

        recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModItemsSpace.rp_fuselage_20_1), 100, 20_000L, new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 1), new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.OreDictStack(TI.shell(), 1))); // 1 welded stainless, 1 titanium shell
        recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModItemsSpace.rp_fuselage_20_3), 150, 30_000L, new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 1), new RecipesCommon.ComparableStack(ModItemsSpace.rp_fuselage_20_1), new RecipesCommon.OreDictStack(TI.shell(), 2))); // 2 weld stain, 3 tit shells
        recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModItemsSpace.rp_fuselage_20_6), 200, 50_000L, new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 2), new RecipesCommon.ComparableStack(ModItemsSpace.rp_fuselage_20_3), new RecipesCommon.OreDictStack(TI.shell(), 3))); // 4 wain, 6 titties
        recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModItemsSpace.rp_fuselage_20_12), 250, 60_000L, new FluidStack(Fluids.OXYGEN, 500), new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 4), new RecipesCommon.ComparableStack(ModItemsSpace.rp_fuselage_20_6), new RecipesCommon.OreDictStack(TI.shell(), 6))); // 8 win, 12 tit

        // space misc
        //recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModItemsSpace.insert_cmb), 600, 50_000L, new FluidStack(Fluids.NEON, 2_000), new RecipesCommon.OreDictStack(CMB.plate(), 2), new RecipesCommon.OreDictStack(U238.ingot())));

        recipes.add(new ArcWelderRecipes.ArcWelderRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS.ordinal()), 250, 25_000L, new RecipesCommon.OreDictStack(AL.plateCast(), 2), new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 2, ItemEnumsSpace.EnumCircuitType.AERO)));
    }
}
