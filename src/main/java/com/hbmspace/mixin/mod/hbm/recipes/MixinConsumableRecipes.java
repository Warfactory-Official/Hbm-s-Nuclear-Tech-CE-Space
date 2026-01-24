package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.crafting.ConsumableRecipes;
import com.hbm.items.ModItems;
import com.hbm.main.CraftingManager;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.util.RecipeUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.ANY_COAL_COKE;

@Mixin(value = ConsumableRecipes.class, remap = false)
public class MixinConsumableRecipes {
    @Inject(method = "register", at = @At("TAIL"))
    private static void addSpaceCrafts(CallbackInfo ci){
        CraftingManager.addShapelessAuto(new ItemStack(ModItemsSpace.flesh_burger), Items.BREAD, ModItemsSpace.grilled_flesh);
        RecipeUtil.replaceShapelessAuto(new ItemStack(ModItems.coffee), CraftingManager.hack, ModItemsSpace.powder_coffee, Items.MILK_BUCKET, Items.POTIONITEM, Items.SUGAR);
        RecipeUtil.replaceRecipeAuto(new ItemStack(ModItems.cladding_rubber, 1), CraftingManager.hack, "RCR", "CDC", "RCR", 'R', ANY_RUBBER.ingot(), 'C', ANY_COAL_COKE.dust(), 'D', ModItems.ducttape);
    }
}
