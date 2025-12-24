package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.config.GeneralConfig;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.SolderingRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.items.ItemEnumsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.hbm.inventory.OreDictManager.*;

@Mixin(value = SolderingRecipes.class, remap = false)
public class MixinSolderingRecipes {

    @Shadow
    public static List<SolderingRecipes.SolderingRecipe> recipes;

    @Shadow public static HashSet<RecipesCommon.AStack> toppings;
    @Shadow public static HashSet<RecipesCommon.AStack> pcb;
    @Shadow public static HashSet<RecipesCommon.AStack> solder;

    @Unique
    private static void hbm$rebuildIngredientCaches() {
        toppings.clear();
        pcb.clear();
        solder.clear();

        for (SolderingRecipes.SolderingRecipe r : recipes) {
            toppings.addAll(Arrays.asList(r.toppings));
            pcb.addAll(Arrays.asList(r.pcb));
            solder.addAll(Arrays.asList(r.solder));
        }
    }

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {

        boolean lbsm = GeneralConfig.enableLBSM && GeneralConfig.enableLBSMSimpleCrafting;
        boolean no528 = !GeneralConfig.enable528;

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO.ordinal()), 300, 1_000,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 3, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(RUBBER.ingot(), 4)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(PB.wireFine(), 4)}
        ));

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItemsSpace.hard_drive, 1), 200, 250,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 16, ItemEnums.EnumCircuitType.PCB)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(MINGRADE.wireFine(), 4)}
        ));

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.BISMOID.ordinal()), 400, 10_000,
                new FluidStack(Fluids.POLYTHYLENE, 1_000),
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.CHIP_BISMOID),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, lbsm ? 1 : 4, ItemEnumsSpace.EnumCircuitType.GASCHIP),
                        new RecipesCommon.ComparableStack(ModItems.circuit, lbsm ? 2 : 8, ItemEnums.EnumCircuitType.CAPACITOR)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 6, ItemEnums.EnumCircuitType.PCB)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(PB.wireFine(), 12)}
        ));

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED.ordinal()), 300, 1_000,
                new FluidStack(Fluids.POLYTHYLENE, 250),
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, lbsm ? 1 : 2, ItemEnumsSpace.EnumCircuitType.GASCHIP),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CAPACITOR)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.PCB)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(PB.wireFine(), 8)}
        ));
    }

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private void hbm$replaceControllerAdvancedRecipe(CallbackInfo ci) {
        boolean lbsm = GeneralConfig.enableLBSM && GeneralConfig.enableLBSMSimpleCrafting;
        boolean no528 = !GeneralConfig.enable528;
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
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, lbsm ? 8 : 16, ItemEnums.EnumCircuitType.CHIP_BISMOID),
                        new RecipesCommon.ComparableStack(ModItems.circuit, lbsm ? 16 : 48, ItemEnums.EnumCircuitType.CAPACITOR_TANTALIUM),
                        new RecipesCommon.ComparableStack(ModItemsSpace.circuit, lbsm ? 8 : 32, ItemEnumsSpace.EnumCircuitType.CAPACITOR_LANTHANIUM)
                },
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.CONTROLLER_CHASSIS),
                        new RecipesCommon.ComparableStack(ModItems.upgrade_speed_3)
                },
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(PB.wireFine(), 24)
                }
        ));

        hbm$rebuildIngredientCaches();
    }
}
