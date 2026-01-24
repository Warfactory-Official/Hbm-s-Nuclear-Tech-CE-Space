package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.crafting.ArmorRecipes;
import com.hbm.inventory.OreDictManager;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.main.CraftingManager;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;

@Mixin(value = ArmorRecipes.class, remap = false)
public class MixinArmorRecipes {
    @Inject(method = "register", at = @At("TAIL"))
    private static void addSpaceCrafts(CallbackInfo ci){
        // Life support
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.oxy_plss, 1), "AA", "TC", "RR", 'A', AL.plate(), 'T', ModItems.tank_steel, 'C', OreDictManager.DictFrame.fromOne(ModItems.circuit, ItemEnums.EnumCircuitType.BASIC), 'R', RUBBER.ingot());

        // I AM A MAN THAT'S MADE OF MEAT
        // YOU'RE ON THE INTERNET LOOKING AT

        //Feet
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.flippers, 1), "R R", "R R", 'R', RUBBER.ingot());
        CraftingManager.addRecipeAuto(new ItemStack(ModItemsSpace.heavy_boots, 1), "L L", "S S", 'L', Items.LEATHER, 'S', STEEL.ingot());
    }
}
