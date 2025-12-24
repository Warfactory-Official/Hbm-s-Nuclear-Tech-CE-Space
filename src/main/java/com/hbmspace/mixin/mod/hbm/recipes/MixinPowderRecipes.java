package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.crafting.PowderRecipes;
import com.hbm.items.ModItems;
import com.hbm.main.CraftingManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;

@Mixin(value = PowderRecipes.class, remap = false)
public class MixinPowderRecipes {

    @Inject(method = "register", at = @At("TAIL"))
    private static void addSpaceCrafts(CallbackInfo ci){
        CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_cement, 4), CA.dust(), KEY_SAND, Items.CLAY_BALL, Items.CLAY_BALL); // Alite cement recipe
        //CraftingManager.addShapelessAuto(new ItemStack(Items.DYE, 8, 15), new Object[] { ModItems.ammonium_nitrate, CA.dust() });
        CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_desh_ready, 1), ModItems.powder_desh_mix, ModItems.ingot_mercury, ModItems.ingot_mercury, ANY_COKE.dust());
        //CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_fertilizer, 4), new Object[] { CA.dust(), P_RED.dust(), ModItems.ammonium_nitrate });
        //CraftingManager.addShapelessAuto(new ItemStack(ModItems.powder_fertilizer, 4), new Object[] { ANY_ASH.any(), P_RED.dust(), KNO.dust(), ModItems.ammonium_nitrate});

    }
}
