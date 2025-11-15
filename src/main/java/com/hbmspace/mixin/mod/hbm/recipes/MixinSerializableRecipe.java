package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbmspace.inventory.recipes.VacuumCircuitRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SerializableRecipe.class, remap = false)
public class MixinSerializableRecipe {

    @Shadow public static List<SerializableRecipe> recipeHandlers;

    @Inject(method = "registerAllHandlers", at = @At("TAIL"))
    private static void registerSpaceHandlers(CallbackInfo ci) {
        recipeHandlers.add(new VacuumCircuitRecipes());
    }
}
