package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.recipes.CokerRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.fluid.Fluids.*;
import static com.hbm.inventory.fluid.Fluids.GAS_COKER;

// Th3_Sl1ze: let's say I'm not gonna touch this until I remember to change private to public in CokerRecipes..
@Mixin(value = CokerRecipes.class, remap = false)
public class MixinCokerRecipes {

    @Shadow
    private static void registerRecipe(FluidType type, int quantity, ItemStack output, FluidStack byproduct) {
    }

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        registerRecipe(com.hbmspace.inventory.fluid.Fluids.BROMINE, 1_000, new ItemStack(ModItems.powder_bromine, 1), new FluidStack(GAS, 500));
        registerRecipe(com.hbmspace.inventory.fluid.Fluids.SCUTTERBLOOD, 16_000, OreDictManager.DictFrame.fromOne(ModItems.coke, ItemEnums.EnumCokeType.PETROLEUM), new FluidStack(GAS_COKER, 1_600));
    }
}
