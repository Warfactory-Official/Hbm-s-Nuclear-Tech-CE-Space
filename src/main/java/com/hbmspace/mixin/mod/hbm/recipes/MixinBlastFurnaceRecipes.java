package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.recipes.BlastFurnaceRecipes;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbmspace.inventory.OreDictManagerSpace.NI;
import static com.hbm.inventory.OreDictManager.STEEL;

@Mixin(value = BlastFurnaceRecipes.class, remap = false)
public abstract class MixinBlastFurnaceRecipes {

    @Shadow
    public static void addRecipe(Object in1, Object in2, ItemStack out) {
    }

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        addRecipe(NI,			STEEL,										new ItemStack(ModItemsSpace.ingot_stainless, 2));
    }
}
