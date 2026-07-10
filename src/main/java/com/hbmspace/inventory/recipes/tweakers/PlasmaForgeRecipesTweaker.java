package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.PlasmaForgeRecipe;
import com.hbm.inventory.recipes.PlasmaForgeRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.materials.MatsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.BSCCO;
import static com.hbm.inventory.OreDictManager.CMB;
import static com.hbm.inventory.OreDictManager.DNT;
import static com.hbm.inventory.OreDictManager.OSMIRIDIUM;
import static com.hbm.inventory.OreDictManager.STAR;
import static com.hbmspace.inventory.OreDictManagerSpace.STAINLESS;

public class PlasmaForgeRecipesTweaker {

    public static void init() {
        String autoPlate = "autoswitch.weldPlates";
        PlasmaForgeRecipes recs = PlasmaForgeRecipes.INSTANCE;

        recs.register((PlasmaForgeRecipe) new PlasmaForgeRecipe("plsm.weldstainless").setInputEnergy(500_000).setup(125, 20_000L)
                .outputItems(new ItemStack(ModItems.plate_welded, 1, MatsSpace.MAT_STAINLESS.id))
                .inputItems(new RecipesCommon.OreDictStack(STAINLESS.plateCast(), 2)).setGroup(autoPlate, recs));

        // Dyson
        recs.register((PlasmaForgeRecipe) new PlasmaForgeRecipe("plsm.dysonlauncher").setInputEnergy(50_000_000).setup(1_200, 10_000_000).outputItems(new ItemStack(ModBlocksSpace.dyson_launcher))
                .inputFluids(new FluidStack(Fluids.STELLAR_FLUX, 8_000))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(CMB.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(STAINLESS.plateWelded(), 16),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.CONTROLLER_ADVANCED)));

        recs.register((PlasmaForgeRecipe) new PlasmaForgeRecipe("plsm.dysonreceiver").setInputEnergy(50_000_000).setup(1_200, 10_000_000).outputItems(new ItemStack(ModBlocksSpace.dyson_receiver))
                .inputFluids(new FluidStack(Fluids.STELLAR_FLUX, 8_000))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(CMB.plateWelded(), 16),
                        new RecipesCommon.ComparableStack(ModItems.crystal_xen),
                        new RecipesCommon.OreDictStack(BSCCO.wireDense(), 64),
                        new RecipesCommon.OreDictStack(BSCCO.wireDense(), 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 16, ItemEnums.EnumCircuitType.CONTROLLER_QUANTUM)));

        recs.register((PlasmaForgeRecipe) new PlasmaForgeRecipe("plsm.dysonconverterhe").setInputEnergy(50_000_000).setup(1_200, 10_000_000).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_he))
                .inputFluids(new FluidStack(Fluids.STELLAR_FLUX, 8_000))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(CMB.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(STAR.plateCast(), 16),
                        new RecipesCommon.OreDictStack(BSCCO.wireDense(), 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.CONTROLLER_QUANTUM)));

        recs.register((PlasmaForgeRecipe) new PlasmaForgeRecipe("plsm.dysonconvertertu").setInputEnergy(50_000_000).setup(1_200, 10_000_000).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_tu))
                .inputFluids(new FluidStack(Fluids.STELLAR_FLUX, 8_000))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(CMB.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(STAR.plateCast(), 16),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.CONTROLLER_ADVANCED)));

        recs.register((PlasmaForgeRecipe) new PlasmaForgeRecipe("plsm.dysonconverteranatmo").setInputEnergy(50_000_000).setup(1_200, 10_000_000).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_anatmogenesis))
                .inputFluids(new FluidStack(Fluids.STELLAR_FLUX, 16_000))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(CMB.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(DNT.wireDense(), 16),
                        new RecipesCommon.ComparableStack(ModItems.singularity_spark),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 16, ItemEnums.EnumCircuitType.CONTROLLER_QUANTUM)));
    }
}
