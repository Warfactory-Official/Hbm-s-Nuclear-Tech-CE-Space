package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.OreDictStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemArcElectrode;
import com.hbm.items.machine.ItemBatteryPack;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ItemEnumsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

@Mixin(AssemblyMachineRecipes.class)
public abstract class MixinAssemblyRecipes extends GenericRecipes<GenericRecipe> {

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    private void registerSpace(CallbackInfo ci) {
        String autoPlate = "autoswitch.plates";
        register(new GenericRecipe("ass.platenickel").setup(60, 100).outputItems(new ItemStack(ModItemsSpace.plate_nickel, 1)).inputItems(new RecipesCommon.OreDictStack(NI.ingot())).setPools(GenericRecipes.POOL_PREFIX_ALT + "plates").setGroup(autoPlate, this));
        register(new GenericRecipe("ass.platestainless").setup(60, 100).outputItems(new ItemStack(ModItemsSpace.plate_stainless, 1)).inputItems(new RecipesCommon.OreDictStack(STAINLESS.ingot())).setPools(GenericRecipes.POOL_PREFIX_ALT + "plates").setGroup(autoPlate, this));

        /// DELETING SOME DEFAULT SHIT ///

        removeRecipeByName("ass.mpf10kero");
        removeRecipeByName("ass.mpf10kerolong");
        removeRecipeByName("ass.mpf10solid");
        removeRecipeByName("ass.mpf10solidlong");
        removeRecipeByName("ass.mpf10xenon");
        removeRecipeByName("ass.mpf1015kero");
        removeRecipeByName("ass.mpf1015solid");
        removeRecipeByName("ass.mpf1015hydro");
        removeRecipeByName("ass.mpf1015bf");
        removeRecipeByName("ass.mpf15kero");
        removeRecipeByName("ass.mpf15solid");
        removeRecipeByName("ass.mpf15hydro");
        removeRecipeByName("ass.mpf1520kero");
        removeRecipeByName("ass.mpf1520solid");
        removeRecipeByName("ass.satellitelunarminer");

        /// BENT FORK ///

        // bombs
        /*register(new GenericRecipe("ass.nukeantimatter").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.nuke_antimatter, 1))
                .inputItems(
                        //new ComparableStack(ModItems.hull_big_steel, 3),
                        new OreDictStack(STAINLESS.plate(), 16),
                        new OreDictStack(MINGRADE.wireFine(), 32),
                        new ComparableStack(ModItemsSpace.ingot_hafnium, 2),
                        new ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ANALOG),
                        new ComparableStack(ModItemsSpace.billet_gaas, 1),
                        new ComparableStack(ModItems.magnetron, 4)));*/

        // dyson
        register(new GenericRecipe("ass.dysonlauncher").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_launcher, 1))
                .inputItems(
                        new OreDictStack(OSMIRIDIUM.plateWelded(), 4),
                        new OreDictStack(STAINLESS.plate(), 64),
                        new ComparableStack(ModBlocks.steel_scaffold, 64),
                        new ComparableStack(ModBlocks.steel_scaffold, 64),
                        new ComparableStack(ModItemsSpace.turbine_syngas, 8),
                        new ComparableStack(ModBlocks.machine_transformer_dnt, 2),
                        new ComparableStack(ModItems.plate_dineutronium, 8),
                        new ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.CONTROLLER_QUANTUM)));
        register(new GenericRecipe("ass.dysonreceiver").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_receiver, 1))
                .inputItems(
                        new OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new OreDictStack(W.plateWelded(), 4),
                        new ComparableStack(ModBlocks.steel_scaffold, 32),
                        new ComparableStack(ModItems.crystal_xen),
                        new ComparableStack(ModBlocks.hadron_coil_alloy, 16),
                        new ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.QUANTUM),
                        new OreDictStack(BSCCO.wireDense(), 64),
                        new OreDictStack(BSCCO.wireDense(), 64)));
        register(new GenericRecipe("ass.dysonconverterhe").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_he, 1))
                .inputItems(
                        new OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new OreDictStack(ALLOY.wireDense(), 64),
                        new OreDictStack(GOLD.wireDense(), 16),
                        new ComparableStack(ModBlocks.machine_transformer_dnt, 4),
                        new ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.BISMOID)));
        register(new GenericRecipe("ass.dysonconvertertu").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_tu, 1))
                .inputItems(
                        new OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new OreDictStack(W.plateWelded(), 8),
                        new ComparableStack(ModBlocks.machine_transformer_dnt, 4),
                        new ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.BISMOID),
                        new OreDictStack(STEEL.pipe(), 12)));
        register(new GenericRecipe("ass.dysonconverteranatmo").setup(6_000, 100).outputItems(new ItemStack(ModBlocksSpace.dyson_converter_anatmogenesis, 1))
                .inputItems(
                        new OreDictStack(OSMIRIDIUM.plateWelded(), 2),
                        new ComparableStack(ModItemsSpace.turbine_syngas, 16),
                        new OreDictStack(W.plateWelded(), 8),
                        new ComparableStack(ModBlocks.machine_transformer_dnt, 4),
                        new ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.BISMOID)));

        register(new GenericRecipe("ass.dysonmember").setup(100, 100).outputItems(new ItemStack(ModItemsSpace.swarm_member, 1))
                .inputItems(
                        new OreDictStack(W.plateWelded(), 1),
                        new OreDictStack(ANY_HARDPLASTIC.ingot(), 2),
                        new ComparableStack(ModItemsSpace.beryllium_mirror, 1),
                        new OreDictStack(GOLD.wireDense(), 2),
                        new OreDictStack(ALLOY.wireFine(), 32),
                        new OreDictStack(STAINLESS.plate(), 4),
                        new ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CAPACITOR_BOARD),
                        new ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.HFCHIP)));

        register(new GenericRecipe("ass.satdysonrelay").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.sat_dyson_relay, 1))
                .inputItems(
                        new OreDictStack(BIGMT.plate(), 24),
                        new ComparableStack(ModItems.motor_bismuth, 2),
                        new ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.ADVANCED),
                        new ComparableStack(ModItems.fluid_barrel_full, 1, Fluids.KEROSENE.getID()),
                        new ComparableStack(ModItems.thruster_small, 1),
                        new OreDictStack(BSCCO.wireDense(), 64),
                        new ComparableStack(ModBlocks.machine_transformer_dnt, 1)));

        // machines
        register(new GenericRecipe("ass.magma").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_magma, 1))
                .inputItems(
                        new ComparableStack(ModBlocks.steel_scaffold, 8),
                        new OreDictStack(W.plateWelded(), 2),
                        new OreDictStack(STEEL.plate(), 32),
                        new ComparableStack(ModItems.drill_titanium, 1),
                        new ComparableStack(ModItems.motor_bismuth),
                        new ComparableStack(ModItemsSpace.circuit, 8, ItemEnumsSpace.EnumCircuitType.GASCHIP)));
        register(new GenericRecipe("ass.hydrobay").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.hydrobay, 1))
                .inputItems(
                        new OreDictStack(STAINLESS.plate(), 16),
                        new OreDictStack(Fluids.WATER.getDict(16_000)),
                        new OreDictStack(STEEL.pipe(), 6),
                        new OreDictStack(KEY_CLEARGLASS, 8),
                        new ComparableStack(Blocks.DIRT, 8),
                        new OreDictStack(ANY_PLASTIC.ingot(), 2)));
        register(new GenericRecipe("ass.radiator").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_radiator, 1))
                .inputItems(
                        new OreDictStack(AL.plateCast(), 6),
                        new OreDictStack(STAINLESS.plate(), 6),
                        new OreDictStack(CU.pipe(), 4),
                        new ComparableStack(ModItems.thermo_element, 3)));
        register(new GenericRecipe("ass.milkreformer").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_milk_reformer, 1))
                .inputItems(
                        new OreDictStack(STEEL.plateCast(), 14),
                        new OreDictStack(STEEL.ingot(), 2),
                        new ComparableStack(ModItems.motor, 2),
                        new OreDictStack(STEEL.pipe(), 8)));
        /*register(new GenericRecipe("ass.algaefilm").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.algae_film, 1))
                .inputItems(
                        new OreDictStack(AL.plate(), 8),
                        new ComparableStack(ModItemsSpace.saltleaf, 16),
                        new ComparableStack(ModBlocks.fan, 1),
                        new ComparableStack(ModBlocks.steel_beam, 4),
                        new ComparableStack(ModBlocks.fence_metal, 2)));*/
        register(new GenericRecipe("ass.airscrubber").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.air_scrubber, 1))
                .inputItems(
                        new OreDictStack(STAINLESS.plate(), 6),
                        new OreDictStack(CA.dust(), 4),
                        new OreDictStack(LI.dust(), 12),
                        new ComparableStack(ModItems.motor, 1),
                        new ComparableStack(ModItems.blades_titanium, 1),
                        new ComparableStack(ModItems.blades_titanium, 1)));
        register(new GenericRecipe("ass.alkylation").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_alkylation))
                .inputItems(
                        new OreDictStack(ANY_CONCRETE.any(), 12),
                        new OreDictStack(STAINLESS.plate(), 12),
                        new OreDictStack(STEEL.shell(), 6),
                        new ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.CAPACITOR),
                        new ComparableStack(ModItems.catalyst_clay, 12),
                        new ComparableStack(ModItems.coil_tungsten, 4)));
        register(new GenericRecipe("ass.cryodistil").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_cryo_distill))
                .inputItems(
                        new OreDictStack(STEEL.plateCast(), 2),
                        new OreDictStack(ANY_CONCRETE.any(), 4),
                        new OreDictStack(STAINLESS.plate(), 12),
                        new OreDictStack(ANY_PLASTIC.ingot(), 4),
                        new ComparableStack(ModItems.battery_pack, 1, ItemBatteryPack.EnumBatteryPack.BATTERY_REDSTONE),
                        new ComparableStack(ModItems.coil_copper, 4)));
        register(new GenericRecipe("ass.transporterrocket").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.transporter_rocket, 2))
                .inputItems(
                        new OreDictStack(STEEL.plateCast(), 2),
                        new OreDictStack(TI.plateWelded(), 4),
                        new ComparableStack(ModBlocks.crate_iron, 2),
                        new ComparableStack(ModItems.thruster_small, 1),
                        new ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO)));
        register(new GenericRecipe("ass.gasdock").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.gas_dock, 1))
                .inputItems(
                        new OreDictStack(STEEL.plateWelded(), 5),
                        new OreDictStack(ANY_RUBBER.ingot(), 4),
                        new ComparableStack(ModItems.thruster_small, 1),
                        new ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS)));
        register(new GenericRecipe("ass.stardar").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_stardar, 1))
                .inputItems(
                        new ComparableStack(ModItems.motor, 4),
                        new ComparableStack(ModItems.sat_head_radar),
                        new OreDictStack(ANY_CONCRETE.any(), 16),
                        new ComparableStack(ModBlocks.steel_scaffold, 8),
                        new ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC)));
        register(new GenericRecipe("ass.driveprocessor").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_drive_processor, 1))
                .inputItems(
                        new OreDictStack(ANY_RUBBER.ingot(), 2),
                        new OreDictStack(CU.wireFine(), 4),
                        new OreDictStack(IRON.dust(), 3),
                        new ComparableStack(ModItems.crt_display, 2),
                        new ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.BASIC)));
        register(new GenericRecipe("ass.vacuumcircuit").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_vacuum_circuit, 1))
                .inputItems(
                        new OreDictStack(STEEL.plateWelded(), 2),
                        new OreDictStack(W.wireFine(), 4),
                        new ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED)));
        register(new GenericRecipe("ass.solarpanel").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_solar, 1))
                .inputItems(
                        new OreDictStack(STAINLESS.plate(), 4),
                        new ComparableStack(ModItems.photo_panel, 4),
                        new OreDictStack(ANY_PLASTIC.ingot(), 2),
                        new OreDictStack(MINGRADE.wireFine(), 8)));
        register(new GenericRecipe("ass.launchpadrocket").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.launch_pad_rocket, 1))
                .inputItems(
                        new OreDictStack(STEEL.plateWelded(), 12),
                        new OreDictStack(AL.pipe(), 24),
                        new OreDictStack(ANY_CONCRETE.any(), 64),
                        new OreDictStack(ANY_CONCRETE.any(), 64),
                        new OreDictStack(ANY_PLASTIC.ingot(), 16),
                        new ComparableStack(ModBlocks.steel_scaffold, 64),
                        new ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.AERO)));
        register(new GenericRecipe("ass.rocketassembly").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_rocket_assembly, 1))
                .inputItems(
                        new OreDictStack(STEEL.plateCast(), 8),
                        new OreDictStack(STEEL.pipe(), 12),
                        new OreDictStack(ANY_CONCRETE.any(), 16),
                        new OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new ComparableStack(ModBlocks.steel_scaffold, 64),
                        new ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC)));
        /*register(new GenericRecipe("ass.orrery").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orrery, 1))
                .inputItems(new OreDictStack(KEY_ANYGLASS, 16), new ComparableStack(ModItems.circuit, 12, ItemEnums.EnumCircuitType.ADVANCED))
                .inputFluids(new FluidStack(Fluids.TRITIUM, 2_000)));*/

        // stations
        register(new GenericRecipe("ass.orbitalstationport").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_port, 1))
                .inputItems(
                        new OreDictStack(TI.plateWelded(), 6),
                        new ComparableStack(ModItems.motor, 4),
                        new OreDictStack(KEY_CLEARGLASS, 8),
                        new OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC),
                        new OreDictStack(STAINLESS.plate(), 4)));
        register(new GenericRecipe("ass.orbitalstationlauncher").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_launcher, 1))
                .inputItems(
                        new OreDictStack(TI.plateWelded(), 6),
                        new ComparableStack(ModItems.motor, 4),
                        new OreDictStack(KEY_CLEARGLASS, 8),
                        new OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED),
                        new OreDictStack(STAINLESS.plate(), 4)));
        register(new GenericRecipe("ass.rpstationcore20").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_station_core_20, 1))
                .inputItems(
                        new ComparableStack(ModBlocksSpace.orbital_station_port, 1), // we're basically sending up a port
                        new OreDictStack(AL.plateCast(), 4), // wrapped in a fairing
                        new ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS))); // with a computer to navigate
        register(new GenericRecipe("ass.rppod20").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_pod_20, 1))
                .inputItems(
                        new OreDictStack(AL.shell(), 4),
                        new OreDictStack(STAINLESS.plate(), 8),
                        new OreDictStack(FIBER.ingot(), 4),
                        new OreDictStack(ANY_PLASTIC.ingot(), 2),
                        new ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS),
                        new ComparableStack(ModItems.thruster_small, 4)));
        register(new GenericRecipe("ass.orbitalstationcomputer").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_computer, 1))
                .inputItems(
                        new OreDictStack(AL.plateCast(), 4),
                        new OreDictStack(STAINLESS.plate(), 4),
                        new OreDictStack(ANY_HARDPLASTIC.ingot(), 2),
                        new OreDictStack(KEY_CLEARGLASS, 1),
                        new OreDictStack(QUARTZ.dust(), 4), // has a liquid crystal display
                        new ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.MOLYCHIP),
                        new ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.AERO)));

        // thrusters
        register(new GenericRecipe("ass.lpw2").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_lpw2, 1))
                .inputItems(
                        new OreDictStack(STEEL.plateWelded(), 16),
                        new OreDictStack(STEEL.bolt(), 32),
                        new OreDictStack(TI.shell(), 8),
                        new ComparableStack(ModItems.motor_desh, 2),
                        new ComparableStack(ModItems.coil_advanced_alloy, 8),
                        new OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED),
                        new OreDictStack(CU.pipe(), 4)));
        register(new GenericRecipe("ass.htr3").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htr3, 1))
                .inputItems(
                        new OreDictStack(ANY_RESISTANTALLOY.plateWelded(), 4),
                        new OreDictStack(DURA.bolt(), 16),
                        new OreDictStack(W.plateWelded(), 8),
                        new ComparableStack(ModItems.motor_bismuth, 1),
                        new OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new ComparableStack(ModItemsSpace.circuit, 2, ItemEnumsSpace.EnumCircuitType.AERO),
                        new OreDictStack(DURA.pipe(), 8)));
        register(new GenericRecipe("ass.htrf4").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htrf4, 1))
                .inputItems(
                        new OreDictStack(BIGMT.plateCast(), 8),
                        new OreDictStack(DURA.bolt(), 16),
                        new OreDictStack(W.plateWelded(), 8),
                        new ComparableStack(ModItems.motor_bismuth, 1),
                        new OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.BISMOID),
                        new ComparableStack(ModBlocks.hadron_coil_alloy, 24)));

        // rocket parts
        register(new GenericRecipe("ass.mp_thruster_10_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 4), new OreDictStack(STEEL.plate(), 4)));
        register(new GenericRecipe("ass.mp_thruster_10_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 1), new ComparableStack(ModItems.coil_tungsten, 1), new OreDictStack(DURA.ingot(), 4), new OreDictStack(STEEL.plate(), 4)));
        register(new GenericRecipe("ass.mp_thruster_10_xenon").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_10_xenon, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 1), new OreDictStack(STEEL.plate(), 4), new OreDictStack(STEEL.pipe(), 12), new ComparableStack(ModItems.arc_electrode, 4, ItemArcElectrode.EnumElectrodeType.LANTHANIUM)));
        register(new GenericRecipe("ass.mp_thruster_15_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 8), new OreDictStack(STEEL.plate(), 6), new OreDictStack(DESH.ingot(), 4)));
        register(new GenericRecipe("ass.mp_thruster_15_kerosene_dual").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_kerosene_dual, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 4), new OreDictStack(STEEL.plate(), 6), new OreDictStack(DESH.ingot(), 1)));
        register(new GenericRecipe("ass.mp_thruster_15_kerosene_triple").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_kerosene_triple, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 6), new OreDictStack(STEEL.plate(), 6), new OreDictStack(DESH.ingot(), 2)));
        register(new GenericRecipe("ass.mp_thruster_15_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new OreDictStack(STEEL.plate(), 6), new OreDictStack(DURA.ingot(), 6), new ComparableStack(ModItems.coil_tungsten, 3)));
        register(new GenericRecipe("ass.mp_thruster_15_solid_hexdecuple").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_solid_hexdecuple, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new OreDictStack(STEEL.plate(), 6), new OreDictStack(DURA.ingot(), 12), new ComparableStack(ModItems.coil_tungsten, 6)));
        register(new GenericRecipe("ass.mp_thruster_15_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_hydrogen, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 8), new OreDictStack(STEEL.plate(), 6), new OreDictStack(BIGMT.ingot(), 4)));
        register(new GenericRecipe("ass.mp_thruster_15_hydrogen_dual").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_hydrogen_dual, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 4), new OreDictStack(STEEL.plate(), 6), new OreDictStack(BIGMT.ingot(), 1)));
        register(new GenericRecipe("ass.mp_thruster_15_balefire_short").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_balefire_short, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModItems.plate_polymer, 8), new ComparableStack(ModBlocks.pwr_fuelrod, 1), new OreDictStack(DESH.ingot(), 8), new OreDictStack(BIGMT.plate(), 12), new OreDictStack(CU.plateCast(), 2), new ComparableStack(ModItems.ingot_uranium_fuel, 4)));
        register(new GenericRecipe("ass.mp_thruster_15_balefire").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_balefire, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModItems.plate_polymer, 16), new ComparableStack(ModBlocks.pwr_fuelrod, 2), new OreDictStack(DESH.ingot(), 16), new OreDictStack(BIGMT.plate(), 24), new OreDictStack(CU.plateCast(), 4), new ComparableStack(ModItems.ingot_uranium_fuel, 8)));
        register(new GenericRecipe("ass.mp_thruster_15_balefire_large").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_15_balefire_large, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModItems.plate_polymer, 16), new ComparableStack(ModBlocks.pwr_fuelrod, 2), new OreDictStack(DESH.ingot(), 24), new OreDictStack(BIGMT.plate(), 32), new OreDictStack(CU.plateCast(), 4), new ComparableStack(ModItems.ingot_uranium_fuel, 8)));
        register(new GenericRecipe("ass.mp_thruster_20_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 8), new OreDictStack(STEEL.plate(), 6), new OreDictStack(DESH.ingot(), 2)));
        register(new GenericRecipe("ass.mp_thruster_20_kerosene_dual").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_kerosene_dual, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 12), new OreDictStack(STEEL.plate(), 8), new OreDictStack(DESH.ingot(), 4)));
        register(new GenericRecipe("ass.mp_thruster_20_kerosene_triple").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_kerosene_triple, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 16), new OreDictStack(STEEL.plate(), 12), new OreDictStack(DESH.ingot(), 6)));
        register(new GenericRecipe("ass.mp_thruster_20_methalox").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_methalox, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 8), new OreDictStack(STEEL.plate(), 6), new OreDictStack(DESH.ingot(), 2)));
        register(new GenericRecipe("ass.mp_thruster_20_methalox_dual").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_methalox_dual, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 12), new OreDictStack(STEEL.plate(), 8), new OreDictStack(DESH.ingot(), 4)));
        register(new GenericRecipe("ass.mp_thruster_20_methalox_triple").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_methalox_triple, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 16), new OreDictStack(STEEL.plate(), 12), new OreDictStack(DESH.ingot(), 6)));
        register(new GenericRecipe("ass.mp_thruster_20_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_hydrogen, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 8), new OreDictStack(STEEL.plate(), 6), new OreDictStack(BIGMT.ingot(), 2)));
        register(new GenericRecipe("ass.mp_thruster_20_hydrogen_dual").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_hydrogen_dual, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 12), new OreDictStack(STEEL.plate(), 8), new OreDictStack(BIGMT.ingot(), 4)));
        register(new GenericRecipe("ass.mp_thruster_20_hydrogen_triple").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.mp_thruster_20_hydrogen_triple, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new OreDictStack(STEEL.pipe(), 1), new OreDictStack(W.ingot(), 16), new OreDictStack(STEEL.plate(), 12), new OreDictStack(BIGMT.ingot(), 6)));
        register(new GenericRecipe("ass.mp_thruster_20_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new ComparableStack(ModItems.coil_tungsten, 8), new OreDictStack(DURA.ingot(), 16), new OreDictStack(STEEL.plate(), 1)));
        register(new GenericRecipe("ass.mp_thruster_20_solid_multi").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_solid_multi, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new ComparableStack(ModItems.coil_tungsten, 12), new OreDictStack(DURA.ingot(), 18), new OreDictStack(STEEL.plate(), 1)));
        register(new GenericRecipe("ass.mp_thruster_20_solid_multier").setup(400, 100).outputItems(new ItemStack(ModItems.mp_thruster_20_solid_multier, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 1), new ComparableStack(ModItems.coil_tungsten, 16), new OreDictStack(DURA.ingot(), 20), new OreDictStack(STEEL.plate(), 1)));
        register(new GenericRecipe("ass.mp_fuselage_10_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 2), new ComparableStack(ModBlocks.steel_scaffold, 3), new OreDictStack(TI.plate(), 12), new OreDictStack(STEEL.plate(), 3)));
        register(new GenericRecipe("ass.mp_fuselage_10_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 2), new ComparableStack(ModBlocks.steel_scaffold, 3), new OreDictStack(TI.plate(), 12), new OreDictStack(AL.plate(), 3)));
        register(new GenericRecipe("ass.mp_fuselage_10_xenon").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_xenon, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 2), new ComparableStack(ModBlocks.steel_scaffold, 3), new OreDictStack(TI.plate(), 12), new OreDictStack(CU.plateCast(), 3)));
        register(new GenericRecipe("ass.mp_fuselage_10_long_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_long_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 2), new ComparableStack(ModBlocks.steel_scaffold, 6), new OreDictStack(TI.plate(), 24), new OreDictStack(STEEL.plate(), 6)));
        register(new GenericRecipe("ass.mp_fuselage_10_long_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_long_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 2), new ComparableStack(ModBlocks.steel_scaffold, 6), new OreDictStack(TI.plate(), 24), new OreDictStack(AL.plate(), 6)));
        register(new GenericRecipe("ass.mp_fuselage_10_15_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 1), new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModBlocks.steel_scaffold, 9), new OreDictStack(TI.plate(), 36), new OreDictStack(STEEL.plate(), 9)));
        register(new GenericRecipe("ass.mp_fuselage_10_15_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 1), new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModBlocks.steel_scaffold, 9), new OreDictStack(TI.plate(), 36), new OreDictStack(AL.plate(), 9)));
        register(new GenericRecipe("ass.mp_fuselage_10_15_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_hydrogen, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 1), new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModBlocks.steel_scaffold, 9), new OreDictStack(TI.plate(), 36), new OreDictStack(IRON.plate(), 9)));
        register(new GenericRecipe("ass.mp_fuselage_10_15_balefire").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_10_15_balefire, 1))
                .inputItems(new ComparableStack(ModItems.seg_10, 1), new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModBlocks.steel_scaffold, 9), new OreDictStack(TI.plate(), 36), new OreDictStack(BIGMT.plate(), 9)));
        register(new GenericRecipe("ass.mp_fuselage_15_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 2), new ComparableStack(ModBlocks.steel_scaffold, 12), new OreDictStack(TI.plate(), 48), new OreDictStack(STEEL.plate(), 1)));
        register(new GenericRecipe("ass.mp_fuselage_15_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 2), new ComparableStack(ModBlocks.steel_scaffold, 12), new OreDictStack(TI.plate(), 48), new OreDictStack(AL.plate(), 1)));
        register(new GenericRecipe("ass.mp_fuselage_15_hydrogen").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_hydrogen, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 2), new ComparableStack(ModBlocks.steel_scaffold, 12), new OreDictStack(TI.plate(), 48), new OreDictStack(IRON.plate(), 1)));
        register(new GenericRecipe("ass.mp_fuselage_15_20_kerosene").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_20_kerosene, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModItems.seg_20, 1), new ComparableStack(ModBlocks.steel_scaffold, 16), new OreDictStack(TI.plate(), 64), new OreDictStack(STEEL.plate(), 1)));
        register(new GenericRecipe("ass.mp_fuselage_15_20_solid").setup(400, 100).outputItems(new ItemStack(ModItems.mp_fuselage_15_20_solid, 1))
                .inputItems(new ComparableStack(ModItems.seg_15, 1), new ComparableStack(ModItems.seg_20, 1), new ComparableStack(ModBlocks.steel_scaffold, 16), new OreDictStack(TI.plate(), 64), new OreDictStack(AL.plate(), 1)));
        register(new GenericRecipe("ass.rp_fuselage_20_12").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_12, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 2), new ComparableStack(ModBlocks.steel_scaffold, 16), new OreDictStack(TI.shell(), 12), new OreDictStack(AL.plateWelded(), 8)));
        register(new GenericRecipe("ass.rp_fuselage_20_6").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_6, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 2), new ComparableStack(ModBlocks.steel_scaffold, 8), new OreDictStack(TI.shell(), 6), new OreDictStack(AL.plateWelded(), 4)));
        register(new GenericRecipe("ass.rp_fuselage_20_3").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_3, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 2), new ComparableStack(ModBlocks.steel_scaffold, 4), new OreDictStack(TI.shell(), 3), new OreDictStack(AL.plateWelded(), 2)));
        register(new GenericRecipe("ass.rp_fuselage_20_1").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_1, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 2), new ComparableStack(ModBlocks.steel_scaffold, 2), new OreDictStack(TI.shell(), 1), new OreDictStack(AL.plateWelded(), 1)));
        register(new GenericRecipe("ass.rp_fuselage_20_12_hydrazine").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_12_hydrazine, 1))
                .inputItems(new ComparableStack(ModItems.seg_20, 2), new OreDictStack(TI.shell(), 12), new OreDictStack(AL.plateWelded(), 16), new OreDictStack(POLYMER.ingot(), 8)));

        register(new GenericRecipe("ass.rp_capsule_20").setup(600, 100).outputItems(new ItemStack(ModItemsSpace.rp_capsule_20, 1))
                .inputItems(
                        new ComparableStack(ModItems.rocket_fuel, 8),
                        new ComparableStack(ModItems.thruster_small, 4),
                        new ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO),
                        new OreDictStack(ANY_RUBBER.ingot(), 16),
                        new OreDictStack(AL.shell(), 4),
                        new OreDictStack(FIBER.ingot(), 12)));
        register(new GenericRecipe("ass.rp_legs_20").setup(200, 100).outputItems(new ItemStack(ModItemsSpace.rp_legs_20, 1))
                .inputItems(
                        new OreDictStack(STEEL.pipe(), 4),
                        new OreDictStack(AL.plate(), 8),
                        new ComparableStack(ModItems.motor, 4)));


        /// UNBEND ///
    }
}


