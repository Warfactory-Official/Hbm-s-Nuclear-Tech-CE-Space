package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemArcElectrode;
import com.hbm.items.machine.ItemBatteryPack;
import com.hbmspace.blocks.ModBlocksSpace;
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
        if (recs.modified) {
            return;
        }
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
                        new ComparableStack(ModItems.magnetron, 4)));*/

        // dyson
        recs.register(new GenericRecipe("ass.dysonlauncher").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_launcher, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 4),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 64),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItemsSpace.turbine_syngas, 8),
                        new RecipesCommon.ComparableStack(ModBlocks.machine_transformer_dnt, 2),
                        new RecipesCommon.ComparableStack(ModItems.plate_dineutronium, 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.CONTROLLER_QUANTUM)));
        recs.register(new GenericRecipe("ass.dysonreceiver").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_receiver, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 4),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 32),
                        new RecipesCommon.ComparableStack(ModItems.crystal_xen),
                        new RecipesCommon.ComparableStack(ModBlocks.hadron_coil_alloy, 16),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.QUANTUM),
                        new RecipesCommon.OreDictStack(BSCCO.wireDense(), 64),
                        new RecipesCommon.OreDictStack(BSCCO.wireDense(), 64)));
        recs.register(new GenericRecipe("ass.dysonconverterhe").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_he, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new RecipesCommon.OreDictStack(ALLOY.wireDense(), 64),
                        new RecipesCommon.OreDictStack(GOLD.wireDense(), 16),
                        new RecipesCommon.ComparableStack(ModBlocks.machine_transformer_dnt, 4),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.BISMOID)));
        recs.register(new GenericRecipe("ass.dysonconvertertu").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_tu, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModBlocks.machine_transformer_dnt, 4),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.BISMOID),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 12)));
        recs.register(new GenericRecipe("ass.dysonconverteranatmo").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_anatmogenesis, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new RecipesCommon.ComparableStack(ModItemsSpace.turbine_syngas, 16),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModBlocks.machine_transformer_dnt, 4),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.BISMOID)));

        recs.register(new GenericRecipe("ass.dysonmember").setup(100, 100).outputItems(new ItemStack(ModItemsSpace.swarm_member, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(W.plateWelded(), 1),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItemsSpace.beryllium_mirror, 1),
                        new RecipesCommon.OreDictStack(GOLD.wireDense(), 2),
                        new RecipesCommon.OreDictStack(ALLOY.wireFine(), 32),
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
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 8, ItemEnumsSpace.EnumCircuitType.GASCHIP)));
        recs.register(new GenericRecipe("ass.hydrobay").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.hydrobay, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 16),
                        new RecipesCommon.OreDictStack(Fluids.WATER.getDict(16_000)),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 6),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.ComparableStack(Blocks.DIRT, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 2)));
        recs.register(new GenericRecipe("ass.radiator").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_radiator, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.plateCast(), 6),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 6),
                        new RecipesCommon.OreDictStack(CU.pipe(), 4),
                        new RecipesCommon.ComparableStack(ModItems.thermo_element, 3)));
        recs.register(new GenericRecipe("ass.milkreformer").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_milk_reformer, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 14),
                        new RecipesCommon.OreDictStack(STEEL.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItems.motor, 2),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 8)));
        /*recs.register(new GenericRecipe("ass.algaefilm").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.algae_film, 1))
                .inputItems(
                        new OreDictStack(AL.plate(), 8),
                        new ComparableStack(ModItemsSpace.saltleaf, 16),
                        new ComparableStack(ModBlocks.fan, 1),
                        new ComparableStack(ModBlocks.steel_beam, 4),
                        new ComparableStack(ModBlocks.fence_metal, 2)));*/
        recs.register(new GenericRecipe("ass.airscrubber").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.air_scrubber, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 6),
                        new RecipesCommon.OreDictStack(CA.dust(), 4),
                        new RecipesCommon.OreDictStack(LI.dust(), 12),
                        new RecipesCommon.ComparableStack(ModItems.motor, 1),
                        new RecipesCommon.ComparableStack(ModItems.blades_titanium, 1),
                        new RecipesCommon.ComparableStack(ModItems.blades_titanium, 1)));
        recs.register(new GenericRecipe("ass.alkylation").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_alkylation))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 12),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 12),
                        new RecipesCommon.OreDictStack(STEEL.shell(), 6),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.CAPACITOR),
                        new RecipesCommon.ComparableStack(ModItems.catalyst_clay, 12),
                        new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 4)));
        recs.register(new GenericRecipe("ass.cryodistil").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_cryo_distill))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 2),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 4),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 12),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 4),
                        new RecipesCommon.ComparableStack(ModItems.battery_pack, 1, ItemBatteryPack.EnumBatteryPack.BATTERY_REDSTONE),
                        new RecipesCommon.ComparableStack(ModItems.coil_copper, 4)));
        recs.register(new GenericRecipe("ass.transporterrocket").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.transporter_rocket, 2))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 2),
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 4),
                        new RecipesCommon.ComparableStack(ModBlocks.crate_iron, 2),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 1),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO)));
        recs.register(new GenericRecipe("ass.gasdock").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.gas_dock, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 5),
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 4),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 1),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS)));
        recs.register(new GenericRecipe("ass.stardar").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_stardar, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.ComparableStack(ModItems.sat_head_radar),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 16),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC)));
        recs.register(new GenericRecipe("ass.driveprocessor").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_drive_processor, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 2),
                        new RecipesCommon.OreDictStack(CU.wireFine(), 4),
                        new RecipesCommon.OreDictStack(IRON.dust(), 3),
                        new RecipesCommon.ComparableStack(ModItems.crt_display, 2),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.BASIC)));
        recs.register(new GenericRecipe("ass.vacuumcircuit").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_vacuum_circuit, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 2),
                        new RecipesCommon.OreDictStack(W.wireFine(), 4),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED)));
        recs.register(new GenericRecipe("ass.solarpanel").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_solar, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 4),
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
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.AERO)));
        recs.register(new GenericRecipe("ass.rocketassembly").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_rocket_assembly, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 8),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 12),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 16),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC)));
        /*recs.register(new GenericRecipe("ass.orrery").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orrery, 1))
                .inputItems(new OreDictStack(KEY_ANYGLASS, 16), new ComparableStack(ModItems.circuit, 12, ItemEnums.EnumCircuitType.ADVANCED))
                .inputFluids(new FluidStack(Fluids.TRITIUM, 2_000)));*/

        // stations
        recs.register(new GenericRecipe("ass.orbitalstationport").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_port, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 6),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 4)));
        recs.register(new GenericRecipe("ass.orbitalstationlauncher").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_launcher, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 6),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 4)));
        recs.register(new GenericRecipe("ass.rpstationcore20").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_station_core_20, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModBlocksSpace.orbital_station_port, 1), // we're basically sending up a port
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4), // wrapped in a fairing
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS))); // with a computer to navigate
        recs.register(new GenericRecipe("ass.rppod20").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_pod_20, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.shell(), 4),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 8),
                        new RecipesCommon.OreDictStack(FIBER.ingot(), 4),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 4)));
        recs.register(new GenericRecipe("ass.orbitalstationcomputer").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_computer, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 4),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 2),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 1),
                        new RecipesCommon.OreDictStack(QUARTZ.dust(), 4), // has a liquid crystal display
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.MOLYCHIP),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.AERO)));

        // thrusters
        recs.register(new GenericRecipe("ass.lpw2").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_lpw2, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(STEEL.bolt(), 32),
                        new RecipesCommon.OreDictStack(TI.shell(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_desh, 2),
                        new RecipesCommon.ComparableStack(ModItems.coil_advanced_alloy, 8),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(CU.pipe(), 4)));
        recs.register(new GenericRecipe("ass.htr3").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htr3, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_RESISTANTALLOY.plateWelded(), 4),
                        new RecipesCommon.OreDictStack(DURA.bolt(), 16),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 1),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 2, ItemEnumsSpace.EnumCircuitType.AERO),
                        new RecipesCommon.OreDictStack(DURA.pipe(), 8)));
        recs.register(new GenericRecipe("ass.htrf4").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htrf4, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(BIGMT.plateCast(), 8),
                        new RecipesCommon.OreDictStack(DURA.bolt(), 16),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 1),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.BISMOID),
                        new RecipesCommon.ComparableStack(ModBlocks.hadron_coil_alloy, 24)));

        // rocket parts
        recs.register(new GenericRecipe("ass.mp_thruster_10_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_kerosene, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.OreDictStack(STEEL.pipe(), 1), new RecipesCommon.OreDictStack(W.ingot(), 4), new RecipesCommon.OreDictStack(STEEL.plate(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_10_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_solid, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.ComparableStack(ModItems.coil_tungsten, 1), new RecipesCommon.OreDictStack(DURA.ingot(), 4), new RecipesCommon.OreDictStack(STEEL.plate(), 4)));
        recs.register(new GenericRecipe("ass.mp_thruster_10_xenon").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_xenon, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_10, 1), new RecipesCommon.OreDictStack(STEEL.plate(), 4), new RecipesCommon.OreDictStack(STEEL.pipe(), 12), new RecipesCommon.ComparableStack(ModItems.arc_electrode, 4, ItemArcElectrode.EnumElectrodeType.LANTHANIUM)));
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

        recs.register(new GenericRecipe("ass.rp_capsule_20").setup(600, 100).outputItems(new ItemStack(ModItemsSpace.rp_capsule_20, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModItems.rocket_fuel, 8),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 4),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO),
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 16),
                        new RecipesCommon.OreDictStack(AL.shell(), 4),
                        new RecipesCommon.OreDictStack(FIBER.ingot(), 12)));
        recs.register(new GenericRecipe("ass.rp_legs_20").setup(200, 100).outputItems(new ItemStack(ModItemsSpace.rp_legs_20, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 4),
                        new RecipesCommon.OreDictStack(AL.plate(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4)));


        /// UNBEND ///
    }
}
