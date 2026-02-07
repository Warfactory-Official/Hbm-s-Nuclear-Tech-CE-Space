package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.config.GeneralConfig;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.SolderingRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.recipes.SolderingRecipes.*;

public class SolderingRecipesTweaker {

    private static void rebuildIngredientCaches() {
        toppings.clear();
        pcb.clear();
        solder.clear();

        for (SolderingRecipes.SolderingRecipe r : recipes) {
            toppings.addAll(Arrays.asList(r.toppings));
            pcb.addAll(Arrays.asList(r.pcb));
            solder.addAll(Arrays.asList(r.solder));
        }
    }

    public static void init() {
        if (RecipeTweakerManager.isModified(SolderingRecipes.class)) return;
        boolean lbsm = GeneralConfig.enableLBSM && GeneralConfig.enableLBSMSimpleCrafting;
        boolean no528 = !GeneralConfig.enable528;

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO.ordinal()), 300, 1_000,
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 3, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(RUBBER.ingot(), 4)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.OreDictStack(PB.wireFine(), 4)}
        ));

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItemsSpace.hard_drive, 1), 200, 250,
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 16, ItemEnums.EnumCircuitType.PCB)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.OreDictStack(MINGRADE.wireFine(), 4)}
        ));

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.BISMOID.ordinal()), 400, 10_000,
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.POLYTHYLENE, 1_000),
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.CHIP_BISMOID),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, lbsm ? 1 : 4, ItemEnumsSpace.EnumCircuitType.GASCHIP),
                        new RecipesCommon.ComparableStack(ModItems.circuit, lbsm ? 2 : 8, ItemEnums.EnumCircuitType.CAPACITOR)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 6, ItemEnums.EnumCircuitType.PCB)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.OreDictStack(PB.wireFine(), 12)}
        ));

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED.ordinal()), 300, 1_000,
                new FluidStack(com.hbmspace.inventory.fluid.Fluids.POLYTHYLENE, 250),
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, lbsm ? 1 : 2, ItemEnumsSpace.EnumCircuitType.GASCHIP),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CAPACITOR)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.PCB)},
                new RecipesCommon.AStack[]{
                        new RecipesCommon.OreDictStack(PB.wireFine(), 8)}
        ));

        if (!no528) return;

        final int targetMeta = ItemEnums.EnumCircuitType.CONTROLLER_ADVANCED.ordinal();

        for (int i = recipes.size() - 1; i >= 0; i--) {
            SolderingRecipes.SolderingRecipe r = recipes.get(i);
            if (r != null && r.output != null
                    && r.output.getItem() == ModItems.circuit
                    && r.output.getMetadata() == targetMeta) {
                recipes.remove(i);
            }
        }

        recipes.add(new SolderingRecipes.SolderingRecipe(
                new ItemStack(ModItems.circuit, 1, targetMeta),
                600, 25_000,
                new FluidStack(Fluids.PERFLUOROMETHYL, 4_000),
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, lbsm ? 8 : 16, ItemEnums.EnumCircuitType.CHIP_BISMOID),
                        new RecipesCommon.ComparableStack(ModItems.circuit, lbsm ? 16 : 48, ItemEnums.EnumCircuitType.CAPACITOR_TANTALIUM),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, lbsm ? 8 : 32, ItemEnumsSpace.EnumCircuitType.CAPACITOR_LANTHANIUM)
                },
                new RecipesCommon.AStack[]{
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.CONTROLLER_CHASSIS),
                        new RecipesCommon.ComparableStack(ModItems.upgrade_speed_3)
                },
                new RecipesCommon.AStack[]{
                        new RecipesCommon.OreDictStack(PB.wireFine(), 24)
                }
        ));

        rebuildIngredientCaches();
    }
}
