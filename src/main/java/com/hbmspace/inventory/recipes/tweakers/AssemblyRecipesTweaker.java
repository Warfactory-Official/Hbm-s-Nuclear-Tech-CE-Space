package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemArcElectrode;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.OreDictManagerSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.NI;
import static com.hbmspace.inventory.OreDictManagerSpace.STAINLESS;

public class AssemblyRecipesTweaker {

    public static void init() {
        String autoPlate = "autoswitch.plates";
        AssemblyMachineRecipes recs = AssemblyMachineRecipes.INSTANCE;
        recs.register(new GenericRecipe("ass.platenickel").setup(60, 100).outputItems(new ItemStack(ModItemsSpace.plate_nickel, 1)).inputItems(new RecipesCommon.OreDictStack(NI.ingot())).setPools(GenericRecipes.POOL_PREFIX_ALT + "plates").setGroup(autoPlate, recs));
        recs.register(new GenericRecipe("ass.platestainless").setup(60, 100).outputItems(new ItemStack(ModItemsSpace.plate_stainless, 1)).inputItems(new RecipesCommon.OreDictStack(STAINLESS.ingot())).setPools(GenericRecipes.POOL_PREFIX_ALT + "plates").setGroup(autoPlate, recs));

        /// DELETING SOME DEFAULT SHIT ///

        recs.removeRecipeByName("ass.mpf10kero");
        recs.removeRecipeByName("ass.mpf10kerolong");
        recs.removeRecipeByName("ass.mpf10solid");
        recs.removeRecipeByName("ass.mpf10solidlong");
        recs.removeRecipeByName("ass.mpf10xenon");
        recs.removeRecipeByName("ass.mpf1015kero");
        recs.removeRecipeByName("ass.mpf1015solid");
        recs.removeRecipeByName("ass.mpf1015hydro");
        recs.removeRecipeByName("ass.mpf1015bf");
        recs.removeRecipeByName("ass.mpf15kero");
        recs.removeRecipeByName("ass.mpf15solid");
        recs.removeRecipeByName("ass.mpf15hydro");
        recs.removeRecipeByName("ass.mpf1520kero");
        recs.removeRecipeByName("ass.mpf1520solid");
        recs.removeRecipeByName("ass.satellitelunarminer");
        recs.removeRecipeByName("ass.entanglementkit");
        recs.removeRecipeByName("ass.electrolyzer");

        // expensive space
        recs.register(new GenericRecipe("ass.exstainlessbeam").setup(600, 20_000).outputItems(new ItemStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM.ordinal()))
                .inputItems(new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 8), new RecipesCommon.OreDictStack(AL.plateCast(), 8), new RecipesCommon.OreDictStack(TI.plateCast(), 4), new RecipesCommon.OreDictStack(STEEL.bolt(), 32))
                .inputFluids(new FluidStack(com.hbmspace.inventory.fluid.Fluids.KRYPTON, 1_000)));
        recs.register(new GenericRecipe("ass.exstainlessbeamalt").setup(600, 20_000).outputItems(new ItemStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM.ordinal()))
                .inputItems(new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 8), new RecipesCommon.OreDictStack(AL.plateCast(), 8), new RecipesCommon.OreDictStack(TI.plateCast(), 4), new RecipesCommon.OreDictStack(STEEL.bolt(), 32))
                .inputFluids(new FluidStack(com.hbmspace.inventory.fluid.Fluids.ARGON, 1_000)));
        recs.register(new GenericRecipe("ass.exavionics").setup(1_600, 25_000).outputItems(new ItemStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.AVIONICS.ordinal()))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.item_expensive, 2, ItemEnums.EnumExpensiveType.CIRCUIT), new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 16, ItemEnumsSpace.EnumCircuitType.CAPACITOR_LANTHANIUM), new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.AVIONICS), new RecipesCommon.ComparableStack(ModItems.part_generic, 32, ItemEnums.EnumPartType.GLASS_POLARIZED))
                .inputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 2_000)));
        recs.register(new GenericRecipe("ass.exastro").setup(2_000, 100_000).outputItems(new ItemStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.ASTRO.ordinal()))
                .inputItems(new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 2, ItemEnumsSpace.EnumExpensiveType.AVIONICS), new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.CONTROLLER_ADVANCED), new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 32, ItemEnumsSpace.EnumCircuitType.MOLYCHIP), new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 16, ItemEnumsSpace.EnumCircuitType.GASCHIP))
                .inputFluids(new FluidStack(GeneralConfig.enable528 ? Fluids.XENON : com.hbmspace.inventory.fluid.Fluids.ARGON, 1_000)));

        // spuh-ace chips (will do 528 precass later)
        recs.register(new GenericRecipe("ass.gasChip").setup(50, 250).outputItems(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.GASCHIP))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.plate_polymer), new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.GAAS), new RecipesCommon.OreDictStack(GOLD.wireFine())));
        recs.register(new GenericRecipe("ass.hfChip").setup(100, 2_000).outputItems(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.HFCHIP))
                .inputItems(new RecipesCommon.ComparableStack(ModItemsSpace.nugget_hafnium), new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.GAAS), new RecipesCommon.OreDictStack(GOLD.wireFine(), 2)));
        recs.register(new GenericRecipe("ass.molyChip").setup(100, 1_500).outputItems(DictFrame.fromOne(ModItemsSpace.circuit, ItemEnumsSpace.EnumCircuitType.MOLYCHIP))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.powder_molysite), new RecipesCommon.OreDictStack(MINGRADE.billet(), 1), new RecipesCommon.OreDictStack(GOLD.wireFine())));

        recs.register(new GenericRecipe("ass.entanglementkit").setup(200, 100).outputItems(new ItemStack(ModItems.entanglement_kit, 1))
                .inputItems(new RecipesCommon.OreDictStack(DURA.plateCast(), 4), new RecipesCommon.OreDictStack(CU.plate(), 24), new RecipesCommon.OreDictStack(GOLD.wireDense(), 16))
                .inputFluids(new FluidStack(com.hbmspace.inventory.fluid.Fluids.KRYPTON, 8_000)));

        /*recs.register(new GenericRecipe("ass.cargodoor").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.cargo_door, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModBlocks.steel_beam, 32), new RecipesCommon.OreDictStack(STEEL.plate(), 4), new RecipesCommon.OreDictStack(DURA.bolt(), 4), new RecipesCommon.ComparableStack(ModItems.motor, 2), new RecipesCommon.OreDictStack(KEY_GRAY, 1)));*/

        recs.register(new GenericRecipe("ass.electrolyzer").setup(200, 100).outputItems(new ItemStack(ModBlocks.machine_electrolyser, 1))
                .inputItems(new RecipesCommon.OreDictStack(STEEL.plateCast(), 8), new RecipesCommon.OreDictStack(CU.plate(), 16), new RecipesCommon.OreDictStack(TI.shell(), 3), new RecipesCommon.OreDictStack(RUBBER.ingot(), 8), new RecipesCommon.ComparableStack(ModItems.ingot_firebrick, 16), new RecipesCommon.ComparableStack(ModItems.coil_copper, 16), new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.BASIC))
                .inputItemsEx(new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.HEAVY_FRAME), new RecipesCommon.OreDictStack(TI.shell(), 3), new RecipesCommon.OreDictStack(RUBBER.ingot(), 8), new RecipesCommon.ComparableStack(ModItems.ingot_firebrick, 16), new RecipesCommon.ComparableStack(ModItems.coil_copper, 16), new RecipesCommon.ComparableStack(ModItems.circuit, 24, ItemEnums.EnumCircuitType.ANALOG)));

        /// BENT FORK ///

        // bombs
        /*recs.register(new GenericRecipe("ass.nukeantimatter").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.nuke_antimatter, 1))
                .inputItems(
                        //new ComparableStack(ModItems.hull_big_steel, 3),
                        new OreDictStack(STAINLESS.plate(), 16),
                        new OreDictStack(MINGRADE.wireFine(), 32),
                        new ComparableStack(ModItemsSpace.ingot_hafnium, 2),
                        new ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ANALOG),
                        new ComparableStack(ModItemsSpace.billet_gaas, 1),
                        new ComparableStack(ModItems.magnetron, 4)));
				.setPools528(GenericRecipes.POOL_PREFIX_528 + "controller"));*/

        recs.register(new GenericRecipe("ass.dysonmember").setup(100, 100).outputItems(new ItemStack(ModItemsSpace.swarm_member, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(W.plateWelded(), 1),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItemsSpace.beryllium_mirror, 1),
                        new RecipesCommon.OreDictStack(GOLD.wireDense(), 2),
                        new RecipesCommon.OreDictStack(GOLD.wireFine(), 32),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 4),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CAPACITOR_BOARD),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.HFCHIP)));

        recs.register(new GenericRecipe("ass.satdysonrelay").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.sat_dyson_relay, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(BIGMT.plate(), 24),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 2),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.ComparableStack(ModItems.fluid_barrel_full, 1, Fluids.KEROSENE.getID()),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 1),
                        new RecipesCommon.OreDictStack(BSCCO.wireDense(), 64),
                        new RecipesCommon.ComparableStack(ModBlocks.machine_transformer_dnt, 1)));

        // machines
        recs.register(new GenericRecipe("ass.magma").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_magma, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 8),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 2),
                        new RecipesCommon.OreDictStack(STEEL.plate(), 32),
                        new RecipesCommon.ComparableStack(ModItems.drill_titanium, 1),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnumsSpace.EnumCircuitType.GASCHIP))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 6, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 4),
                        new RecipesCommon.ComparableStack(ModItems.drill_titanium, 1),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 4),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 24, ItemEnumsSpace.EnumCircuitType.GASCHIP))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "gaschip"));

        recs.register(new GenericRecipe("ass.hydrobay").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.hydrobay, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 16),
                        new RecipesCommon.OreDictStack(Fluids.WATER.getDict(16_000)),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 6),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.ComparableStack(Blocks.DIRT, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 2))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 2, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM),
                        new RecipesCommon.OreDictStack(Fluids.WATER.getDict(16_000)),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 6),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.ComparableStack(Blocks.DIRT, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 4))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "stainless"));

        recs.register(new GenericRecipe("ass.radiator").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_radiator, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.plateCast(), 6),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 6),
                        new RecipesCommon.OreDictStack(CU.pipe(), 4),
                        new RecipesCommon.ComparableStack(ModItems.thermo_element, 3)));

        recs.register(new GenericRecipe("ass.milkreformer").setup(600, 100).outputItems(new ItemStack(ModBlocksSpace.machine_milk_reformer, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 14),
                        new RecipesCommon.OreDictStack(STEEL.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItems.motor, 2),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 8))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.STEEL_PLATING),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 8)));

        recs.register(new GenericRecipe("ass.algaefilm").setup(100, 100).outputItems(new ItemStack(ModBlocksSpace.algae_film, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.plate(), 8),
                        new RecipesCommon.ComparableStack(ModItemsSpace.saltleaf, 16),
                        new RecipesCommon.ComparableStack(ModBlocks.fan, 1),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_beam, 4),
                        new RecipesCommon.ComparableStack(ModBlocks.fence_metal, 2))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM),
                        new RecipesCommon.ComparableStack(ModItemsSpace.saltleaf, 24),
                        new RecipesCommon.ComparableStack(ModBlocks.fence_metal, 4)));

        recs.register(new GenericRecipe("ass.airscrubber").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.air_scrubber, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 6),
                        new RecipesCommon.OreDictStack(CA.dust(), 4),
                        new RecipesCommon.OreDictStack(LI.dust(), 12),
                        new RecipesCommon.ComparableStack(ModItems.motor, 1),
                        new RecipesCommon.ComparableStack(ModItems.turbine_titanium, 1))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM),
                        new RecipesCommon.OreDictStack(CA.dust(), 8),
                        new RecipesCommon.OreDictStack(LI.dust(), 16),
                        new RecipesCommon.ComparableStack(ModItems.motor, 1),
                        new RecipesCommon.ComparableStack(ModItems.turbine_titanium, 1))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "stainless"));

        recs.register(new GenericRecipe("ass.alkylation").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_alkylation))
                .inputItems(
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 12),
                        new RecipesCommon.OreDictStack(STEEL.shell(), 6),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.CAPACITOR),
                        new RecipesCommon.ComparableStack(ModItems.catalyst_clay, 12),
                        new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 8))
                .inputItemsEx(
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 4),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.STEEL_PLATING),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 8, ItemEnums.EnumCircuitType.CAPACITOR),
                        new RecipesCommon.ComparableStack(ModItems.catalyst_clay, 12),
                        new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 8))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "stainless"));

        recs.register(new GenericRecipe("ass.cryodistil").setup(600, 100).outputItems(new ItemStack(ModBlocksSpace.machine_cryo_distill))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 2),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 12),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 4),
                        new RecipesCommon.OreDictStack(NB.ingot(), 4))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 2, ItemEnums.EnumExpensiveType.HEAVY_FRAME),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 4),
                        new RecipesCommon.OreDictStack(NB.ingot(), 4))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "stainless"));
        recs.register(new GenericRecipe("ass.transporterrocket").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.transporter_rocket, 2))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 2),
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 4),
                        new RecipesCommon.ComparableStack(ModBlocks.crate_iron, 2),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 1),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 2, ItemEnums.EnumExpensiveType.STEEL_PLATING),
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 4),
                        new RecipesCommon.ComparableStack(ModBlocks.crate_iron, 2),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 1),
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.AVIONICS)));

        recs.register(new GenericRecipe("ass.gasdock").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.gas_dock, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 5),
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 4),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 1),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 2, ItemEnums.EnumExpensiveType.STEEL_PLATING),
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 4),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 1),
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.AVIONICS)));
        recs.register(new GenericRecipe("ass.stardar").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.machine_stardar, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.ComparableStack(ModItems.sat_head_radar),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 16),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.ComparableStack(ModItems.sat_head_radar),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 2, ItemEnums.EnumExpensiveType.HEAVY_FRAME),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.CIRCUIT))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "aluminium"));

        recs.register(new GenericRecipe("ass.driveprocessor").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_drive_processor, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 2),
                        new RecipesCommon.OreDictStack(CU.wireFine(), 8),
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 2),
                        new RecipesCommon.ComparableStack(ModItems.crt_display, 2),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 2, ItemEnums.EnumCircuitType.BASIC))
                .inputItemsEx(
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.STEEL_PLATING),
                        new RecipesCommon.ComparableStack(ModItems.crt_display, 2),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 1, ItemEnums.EnumExpensiveType.CIRCUIT))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "aluminium"));

        recs.register(new GenericRecipe("ass.vacuumcircuit").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.machine_vacuum_circuit, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 4),
                        new RecipesCommon.OreDictStack(W.wireFine(), 16),
                        new RecipesCommon.ComparableStack(ModItems.turbine_titanium, 1),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED)));

        recs.register(new GenericRecipe("ass.solarpanel").setup(100, 100).outputItems(new ItemStack(ModBlocksSpace.machine_solar, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 4),
                        new RecipesCommon.ComparableStack(ModItems.photo_panel, 4),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 2),
                        new RecipesCommon.OreDictStack(MINGRADE.wireFine(), 8)));

        recs.register(new GenericRecipe("ass.launchpadrocket").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.launch_pad_rocket, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 12),
                        new RecipesCommon.OreDictStack(AL.pipe(), 24),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 64),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 64),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 16),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.AERO))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "aluminium"));

        recs.register(new GenericRecipe("ass.rocketassembly").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.machine_rocket_assembly, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 8),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 12),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 16),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "aluminium"));

        recs.register(new GenericRecipe("ass.orrery").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orrery, 1))
                .inputItems(new RecipesCommon.OreDictStack(KEY_ANYGLASS, 16), new RecipesCommon.ComparableStack(ModItems.circuit, 12, ItemEnums.EnumCircuitType.ADVANCED))
                .inputFluids(new FluidStack(Fluids.TRITIUM, 2_000)));

        // stations
        recs.register(new GenericRecipe("ass.orbitalstationport").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_port, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 6),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 4))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 8, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 2, ItemEnums.EnumExpensiveType.CIRCUIT))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "stainless"));

        recs.register(new GenericRecipe("ass.orbitalstationlauncher").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_launcher, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 6),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 4))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 8, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.ADVANCED))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "stainless"));

        recs.register(new GenericRecipe("ass.rpstationcore20").setup(200, 100).outputItems(new ItemStack(ModItemsSpace.rp_station_core_20, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModBlocksSpace.orbital_station_port, 1), // we're basically sending up a port
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4), // wrapped in a fairing
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS)) // with a computer to navigate
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModBlocksSpace.orbital_station_port, 1),
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4),
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 1, ItemEnumsSpace.EnumExpensiveType.AVIONICS)));

        recs.register(new GenericRecipe("ass.rppod20").setup(200, 100).outputItems(new ItemStack(ModItemsSpace.rp_pod_20, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.shell(), 4),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 8),
                        new RecipesCommon.OreDictStack(FIBER.ingot(), 4),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 4)));

        recs.register(new GenericRecipe("ass.orbitalstationcomputer").setup(200, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_computer, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plate(), 4),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 2),
                        new RecipesCommon.OreDictStack(QUARTZ.dust(), 4), // has a liquid crystal display
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.MOLYCHIP),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.AERO))
                .inputItemsEx(
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateCast(), 4),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 2, ItemEnums.EnumExpensiveType.PLASTIC),
                        new RecipesCommon.OreDictStack(QUARTZ.dust(), 16),
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 2, ItemEnumsSpace.EnumExpensiveType.ASTRO))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "stainless"));

        // thrusters
        recs.register(new GenericRecipe("ass.lpw2").setup(600, 100).outputItems(new ItemStack(ModBlocksSpace.machine_lpw2, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(STEEL.bolt(), 32),
                        new RecipesCommon.OreDictStack(TI.shell(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_desh, 2),
                        new RecipesCommon.ComparableStack(ModItems.coil_gold, 8),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(CU.pipe(), 4))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 8, ItemEnums.EnumExpensiveType.HEAVY_FRAME),
                        new RecipesCommon.OreDictStack(TI.shell(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_desh, 4),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.CIRCUIT)));

        recs.register(new GenericRecipe("ass.htr3").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htr3, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_RESISTANTALLOY.plateWelded(), 4),
                        new RecipesCommon.OreDictStack(DURA.bolt(), 16),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 1),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 2, ItemEnumsSpace.EnumCircuitType.AERO),
                        new RecipesCommon.OreDictStack(DURA.pipe(), 8))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 8, ItemEnums.EnumExpensiveType.LEAD_PLATING),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 4),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 4, ItemEnums.EnumExpensiveType.CIRCUIT)));

        recs.register(new GenericRecipe("ass.htrf4neo").setup(1_200, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htrf4neo, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_RESISTANTALLOY.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(CU.plateWelded(), 64),
                        new RecipesCommon.OreDictStack(SBD.wireDense(), 64),
                        new RecipesCommon.OreDictStack(OreDictManagerSpace.STAINLESS.plateWelded(), 16),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.BISMOID),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.GASCHIP))
                .inputItemsEx(
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 16, ItemEnums.EnumExpensiveType.FERRO_PLATING),
                        new RecipesCommon.ComparableStack(ModItems.item_expensive, 16, ItemEnums.EnumExpensiveType.PLASTIC),
                        new RecipesCommon.ComparableStack(ModItemsSpace.item_expensive, 16, ItemEnumsSpace.EnumExpensiveType.STAINLESS_BEAM),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 64, ItemEnumsSpace.EnumCircuitType.GASCHIP))
                .setPools528(GenericRecipes.POOL_PREFIX_528 + "gaschip"));

        // rocket parts
        recs.register(new GenericRecipe("ass.mp_thruster_10_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 4), new RecipesCommon.OreDictStack(STEEL.plate(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_10_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 1), new RecipesCommon.OreDictStack(DURA.ingot(), 4), new RecipesCommon.OreDictStack(STEEL.plate(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_10_xenon").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_xenon, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.OreDictStack(STEEL.plate(), 4), new RecipesCommon.OreDictStack(STEEL.pipe(), 12), new RecipesCommon.ComparableStack(ModItems.arc_electrode, 1, ItemArcElectrode.EnumElectrodeType.LANTHANIUM)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 8), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(DESH.ingot(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_kerosene_dual").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_kerosene_dual, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 4), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(DESH.ingot(), 1)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_kerosene_triple").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_kerosene_triple, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 6), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(DESH.ingot(), 2)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(DURA.ingot(), 6), new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 3)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_solid_hexdecuple").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_solid_hexdecuple, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(DURA.ingot(), 12), new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 6)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_hydrogen, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 8), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(BIGMT.ingot(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_hydrogen_dual").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_hydrogen_dual, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 4), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(BIGMT.ingot(), 1)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_balefire_short").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_balefire_short, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModItems.plate_polymer, 8), new RecipesCommon.ComparableStack(ModBlocks.pwr_fuelrod, 1), new RecipesCommon.OreDictStack(DESH.ingot(), 8), new RecipesCommon.OreDictStack(BIGMT.plate(), 12), new RecipesCommon.OreDictStack(CU.plateCast(), 2), new RecipesCommon.ComparableStack(ModItems.ingot_uranium_fuel, 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_balefire").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_balefire, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModItems.plate_polymer, 16), new RecipesCommon.ComparableStack(ModBlocks.pwr_fuelrod, 2), new RecipesCommon.OreDictStack(DESH.ingot(), 16), new RecipesCommon.OreDictStack(BIGMT.plate(), 24), new RecipesCommon.OreDictStack(CU.plateCast(), 4), new RecipesCommon.ComparableStack(ModItems.ingot_uranium_fuel, 8)));
        recs.register(new GenericRecipe("ass.mp_thruster_15_balefire_large").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_balefire_large, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModItems.plate_polymer, 16), new RecipesCommon.ComparableStack(ModBlocks.pwr_fuelrod, 2), new RecipesCommon.OreDictStack(DESH.ingot(), 24), new RecipesCommon.OreDictStack(BIGMT.plate(), 32), new RecipesCommon.OreDictStack(CU.plateCast(), 4), new RecipesCommon.ComparableStack(ModItems.ingot_uranium_fuel, 8)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 8), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(DESH.ingot(), 2)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_kerosene_dual").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_kerosene_dual, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 12), new RecipesCommon.OreDictStack(STEEL.plate(), 8), new RecipesCommon.OreDictStack(DESH.ingot(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_kerosene_triple").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_kerosene_triple, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 16), new RecipesCommon.OreDictStack(STEEL.plate(), 12), new RecipesCommon.OreDictStack(DESH.ingot(), 6)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_methalox").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_methalox, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 8), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(DESH.ingot(), 2)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_methalox_dual").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_methalox_dual, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 12), new RecipesCommon.OreDictStack(STEEL.plate(), 8), new RecipesCommon.OreDictStack(DESH.ingot(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_methalox_triple").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_methalox_triple, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 16), new RecipesCommon.OreDictStack(STEEL.plate(), 12), new RecipesCommon.OreDictStack(DESH.ingot(), 6)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_hydrogen, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 8), new RecipesCommon.OreDictStack(STEEL.plate(), 6), new RecipesCommon.OreDictStack(BIGMT.ingot(), 2)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_hydrogen_dual").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_hydrogen_dual, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 12), new RecipesCommon.OreDictStack(STEEL.plate(), 8), new RecipesCommon.OreDictStack(BIGMT.ingot(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_hydrogen_triple").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_hydrogen_triple, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 16), new RecipesCommon.OreDictStack(STEEL.plate(), 12), new RecipesCommon.OreDictStack(BIGMT.ingot(), 6)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 8), new RecipesCommon.OreDictStack(DURA.ingot(), 16), new RecipesCommon.OreDictStack(STEEL.plate(), 1)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_solid_multi").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_solid_multi, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 12), new RecipesCommon.OreDictStack(DURA.ingot(), 18), new RecipesCommon.OreDictStack(STEEL.plate(), 1)));
        recs.register(new GenericRecipe("ass.mp_thruster_20_solid_multier").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_solid_multier, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 16), new RecipesCommon.OreDictStack(DURA.ingot(), 20), new RecipesCommon.OreDictStack(STEEL.plate(), 1)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 3), new RecipesCommon.OreDictStack(TI.plate(), 12), new RecipesCommon.OreDictStack(STEEL.plate(), 3)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 3), new RecipesCommon.OreDictStack(TI.plate(), 12), new RecipesCommon.OreDictStack(AL.plate(), 3)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_xenon").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_xenon, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 3), new RecipesCommon.OreDictStack(TI.plate(), 12), new RecipesCommon.OreDictStack(CU.plateCast(), 3)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_long_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_long_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 6), new RecipesCommon.OreDictStack(TI.plate(), 24), new RecipesCommon.OreDictStack(STEEL.plate(), 6)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_long_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_long_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 6), new RecipesCommon.OreDictStack(TI.plate(), 24), new RecipesCommon.OreDictStack(AL.plate(), 6)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_15_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 9), new RecipesCommon.OreDictStack(TI.plate(), 36), new RecipesCommon.OreDictStack(STEEL.plate(), 9)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_15_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 9), new RecipesCommon.OreDictStack(TI.plate(), 36), new RecipesCommon.OreDictStack(AL.plate(), 9)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_15_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_hydrogen, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 9), new RecipesCommon.OreDictStack(TI.plate(), 36), new RecipesCommon.OreDictStack(IRON.plate(), 9)));
        recs.register(new GenericRecipe("ass.mp_fuselage_10_15_balefire").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_balefire, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 9), new RecipesCommon.OreDictStack(TI.plate(), 36), new RecipesCommon.OreDictStack(BIGMT.plate(), 9)));
        recs.register(new GenericRecipe("ass.mp_fuselage_15_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 12), new RecipesCommon.OreDictStack(TI.plate(), 48), new RecipesCommon.OreDictStack(STEEL.plate(), 1)));
        recs.register(new GenericRecipe("ass.mp_fuselage_15_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 12), new RecipesCommon.OreDictStack(TI.plate(), 48), new RecipesCommon.OreDictStack(AL.plate(), 1)));
        recs.register(new GenericRecipe("ass.mp_fuselage_15_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_hydrogen, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 12), new RecipesCommon.OreDictStack(TI.plate(), 48), new RecipesCommon.OreDictStack(IRON.plate(), 1)));
        recs.register(new GenericRecipe("ass.mp_fuselage_15_20_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_20_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 16), new RecipesCommon.OreDictStack(TI.plate(), 64), new RecipesCommon.OreDictStack(STEEL.plate(), 1)));
        recs.register(new GenericRecipe("ass.mp_fuselage_15_20_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_20_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_15, 1), new RecipesCommon.ComparableStack(ModItems.seg_20, 1), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 16), new RecipesCommon.OreDictStack(TI.plate(), 64), new RecipesCommon.OreDictStack(AL.plate(), 1)));
        recs.register(new GenericRecipe("ass.rp_fuselage_20_12").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_12, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 16), new RecipesCommon.OreDictStack(TI.shell(), 12), new RecipesCommon.OreDictStack(AL.plateWelded(), 8)));
        recs.register(new GenericRecipe("ass.rp_fuselage_20_6").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_6, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 8), new RecipesCommon.OreDictStack(TI.shell(), 6), new RecipesCommon.OreDictStack(AL.plateWelded(), 4)));
        recs.register(new GenericRecipe("ass.rp_fuselage_20_3").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_3, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 4), new RecipesCommon.OreDictStack(TI.shell(), 3), new RecipesCommon.OreDictStack(AL.plateWelded(), 2)));
        recs.register(new GenericRecipe("ass.rp_fuselage_20_1").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_1, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 2), new RecipesCommon.OreDictStack(TI.shell(), 1), new RecipesCommon.OreDictStack(AL.plateWelded(), 1)));
        recs.register(new GenericRecipe("ass.rp_fuselage_20_12_hydrazine").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_12_hydrazine, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.OreDictStack(TI.shell(), 12), new RecipesCommon.OreDictStack(AL.plateWelded(), 16), new RecipesCommon.OreDictStack(POLYMER.ingot(), 8)));

        recs.register(new GenericRecipe("ass.rp_capsule_20").setup(200, 100).outputItems(new ItemStack(ModItemsSpace.rp_capsule_20, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModItems.rocket_fuel, 8),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 4),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO),
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 16),
                        new RecipesCommon.OreDictStack(AL.shell(), 4),
                        new RecipesCommon.OreDictStack(FIBER.ingot(), 12)));

        recs.register(new GenericRecipe("ass.rp_legs_20").setup(100, 100).outputItems(new ItemStack(ModItemsSpace.rp_legs_20, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 4),
                        new RecipesCommon.OreDictStack(AL.plate(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4)));

        // astrosmurf
        recs.register(new GenericRecipe("ass.astroturf").setup(100, 100).outputItems(new ItemStack(ModBlocksSpace.astroturf, 16))
                .inputItems(
                        new RecipesCommon.ComparableStack(Blocks.DIRT, 16),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 1)));


        /// UNBEND ///
    }
}
