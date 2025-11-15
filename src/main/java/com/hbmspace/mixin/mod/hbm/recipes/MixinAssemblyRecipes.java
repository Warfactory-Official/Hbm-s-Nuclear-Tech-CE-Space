package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ItemEnumsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

@Mixin(AssemblyMachineRecipes.class)
public class MixinAssemblyRecipes {

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    private void registerSpace(CallbackInfo ci) {
        AssemblyMachineRecipes instance = (AssemblyMachineRecipes) (Object) this;
        String autoPlate = "autoswitch.plates";
        instance.register(new GenericRecipe("ass.platenickel").setup(60, 100).outputItems(new ItemStack(ModItemsSpace.plate_nickel, 1)).inputItems(new RecipesCommon.OreDictStack(NI.ingot())).setPools(GenericRecipes.POOL_PREFIX_ALT + "plates").setGroup(autoPlate, instance));

        instance.register(new GenericRecipe("ass.stardar").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_stardar, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.ComparableStack(ModItems.sat_head_radar),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 16),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC)));
        instance.register(new GenericRecipe("ass.driveprocessor").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_drive_processor, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 2),
                        new RecipesCommon.OreDictStack(CU.wireFine(), 4),
                        new RecipesCommon.OreDictStack(IRON.dust(), 3),
                        new RecipesCommon.ComparableStack(ModItems.crt_display, 2),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.BASIC),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ANALOG)));
        instance.register(new GenericRecipe("ass.vacuumcircuit").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_vacuum_circuit, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 2),
                        new RecipesCommon.OreDictStack(W.wireFine(), 4),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.VACUUM_TUBE)));

        instance.register(new GenericRecipe("ass.launchpadrocket").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.launch_pad_rocket, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 12),
                        new RecipesCommon.OreDictStack(AL.pipe(), 24),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 64),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 64),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 16),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.ADVANCED)));

        instance.register(new GenericRecipe("ass.rocketassembly").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_rocket_assembly, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 8),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 12),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 16),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC)));

        // stations
        instance.register(new GenericRecipe("ass.orbitalstationport").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_port, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(TI.plateWelded(), 6),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 8),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 4)));
        instance.register(new GenericRecipe("ass.rpstationcore20").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_station_core_20, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModBlocksSpace.orbital_station_port, 1), // we're basically sending up a port
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4), // wrapped in a fairing
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS))); // with a computer to navigate
        instance.register(new GenericRecipe("ass.rppod20").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_pod_20, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.shell(), 4),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 8),
                        new RecipesCommon.OreDictStack(FIBER.ingot(), 4),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 2),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 4)));
        instance.register(new GenericRecipe("ass.orbitalstationcomputer").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.orbital_station_computer, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(AL.plateCast(), 4),
                        new RecipesCommon.OreDictStack(STAINLESS.plate(), 4),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 2),
                        new RecipesCommon.OreDictStack(KEY_CLEARGLASS, 1),
                        new RecipesCommon.OreDictStack(QUARTZ.dust(), 4), // has a liquid crystal display
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 4, ItemEnumsSpace.EnumCircuitType.MOLYCHIP),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS)));

        instance.register(new GenericRecipe("ass.rp_fuselage_20_12").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_12, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 16), new RecipesCommon.OreDictStack(TI.shell(), 12), new RecipesCommon.OreDictStack(AL.plateWelded(), 8)));
        instance.register(new GenericRecipe("ass.rp_fuselage_20_6").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_6, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 8), new RecipesCommon.OreDictStack(TI.shell(), 6), new RecipesCommon.OreDictStack(AL.plateWelded(), 4)));
        instance.register(new GenericRecipe("ass.rp_fuselage_20_3").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_3, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 4), new RecipesCommon.OreDictStack(TI.shell(), 3), new RecipesCommon.OreDictStack(AL.plateWelded(), 2)));
        instance.register(new GenericRecipe("ass.rp_fuselage_20_1").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_1, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 2), new RecipesCommon.OreDictStack(TI.shell(), 1), new RecipesCommon.OreDictStack(AL.plateWelded(), 1)));
        instance.register(new GenericRecipe("ass.rp_fuselage_20_12_hydrazine").setup(400, 100).outputItems(new ItemStack(ModItemsSpace.rp_fuselage_20_12_hydrazine, 1))
                .inputItems(new RecipesCommon.ComparableStack(ModItems.seg_20, 2), new RecipesCommon.OreDictStack(TI.shell(), 12), new RecipesCommon.OreDictStack(AL.plateWelded(), 16), new RecipesCommon.OreDictStack(POLYMER.ingot(), 8)));

        instance.register(new GenericRecipe("ass.rp_capsule_20").setup(600, 100).outputItems(new ItemStack(ModItemsSpace.rp_capsule_20, 1))
                .inputItems(
                        new RecipesCommon.ComparableStack(ModItems.rocket_fuel, 8),
                        new RecipesCommon.ComparableStack(ModItems.thruster_small, 4),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS),
                        new RecipesCommon.OreDictStack(ANY_RUBBER.ingot(), 16),
                        new RecipesCommon.OreDictStack(AL.shell(), 4),
                        new RecipesCommon.OreDictStack(FIBER.ingot(), 12)));
        instance.register(new GenericRecipe("ass.rp_legs_20").setup(200, 100).outputItems(new ItemStack(ModItemsSpace.rp_legs_20, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 4),
                        new RecipesCommon.OreDictStack(AL.plate(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor, 4)));

        // thrusters
        instance.register(new GenericRecipe("ass.lpw2").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_lpw2, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 16),
                        new RecipesCommon.OreDictStack(STEEL.bolt(), 32),
                        new RecipesCommon.OreDictStack(TI.shell(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_desh, 2),
                        new RecipesCommon.ComparableStack(ModItems.coil_advanced_alloy, 8),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(CU.pipe(), 4)));
        instance.register(new GenericRecipe("ass.htr3").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htr3, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(ANY_RESISTANTALLOY.plateWelded(), 4),
                        new RecipesCommon.OreDictStack(DURA.bolt(), 16),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 1),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(DURA.pipe(), 8)));
        instance.register(new GenericRecipe("ass.htrf4").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_htrf4, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(BIGMT.plateCast(), 8),
                        new RecipesCommon.OreDictStack(DURA.bolt(), 16),
                        new RecipesCommon.OreDictStack(W.plateWelded(), 8),
                        new RecipesCommon.ComparableStack(ModItems.motor_bismuth, 1),
                        new RecipesCommon.OreDictStack(ANY_HARDPLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.BISMOID),
                        new RecipesCommon.ComparableStack(ModBlocks.fusion_conductor, 24)));
    }
}


