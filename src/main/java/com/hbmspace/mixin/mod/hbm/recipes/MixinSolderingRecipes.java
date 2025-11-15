package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.SolderingRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.items.ItemEnumsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.recipes.SolderingRecipes.recipes;

@Mixin(value = SolderingRecipes.class, remap = false)
public class MixinSolderingRecipes {

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AVIONICS.ordinal()), 300, 1_000,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 3, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ADVANCED),
                        new RecipesCommon.OreDictStack(RUBBER.ingot(), 4)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(PB.wireFine(), 4)}
        ));
        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.AERO.ordinal()), 300, 1_000,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 3, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.BASIC),
                        new RecipesCommon.ComparableStack(ModItemsSpace.nugget_hafnium, 1)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(PB.wireFine(), 4)} // temp
        ));

        recipes.add(new SolderingRecipes.SolderingRecipe(new ItemStack(ModItemsSpace.hard_drive, 1), 200, 250,
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 2, ItemEnums.EnumCircuitType.CHIP)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.ComparableStack(ModItems.circuit, 16, ItemEnums.EnumCircuitType.PCB)},
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(MINGRADE.wireFine(), 4)}
        ));


    }
}
