package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.recipes.CrucibleRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.inventory.materials.MatsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = CrucibleRecipes.class, remap = false)
public class MixinCrucibleRecipes {
    @Shadow
    public static List<CrucibleRecipes.CrucibleRecipe> recipes;

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        int n = MaterialShapes.NUGGET.q(1);
        int i = MaterialShapes.INGOT.q(1);
        recipes.add(new CrucibleRecipes.CrucibleRecipe(18, "crucible.hsss", 12, new ItemStack(ModItems.ingot_dura_steel))
                .inputs(new Mats.MaterialStack(MatsSpace.MAT_STAINLESS, n * 5), new Mats.MaterialStack(Mats.MAT_TUNGSTEN, n * 3), new Mats.MaterialStack(Mats.MAT_COBALT, n))
                .outputs(new Mats.MaterialStack(Mats.MAT_DURA, i * 2)));

        recipes.add(new CrucibleRecipes.CrucibleRecipe(19, "crucible.arse", 9, new ItemStack(ModItemsSpace.ingot_gaas))
                .inputs(new Mats.MaterialStack(MatsSpace.MAT_GALLIUM, n * 6), new Mats.MaterialStack(Mats.MAT_ARSENIC, n * 3 ))
                .outputs(new Mats.MaterialStack(MatsSpace.MAT_GAAS, i)));

        recipes.add(new CrucibleRecipes.CrucibleRecipe(20, "crucible.stainless", 2, new ItemStack(ModItemsSpace.ingot_stainless))
                .inputs(new Mats.MaterialStack(Mats.MAT_STEEL, n), new Mats.MaterialStack(MatsSpace.MAT_NICKEL, n))
                .outputs(new Mats.MaterialStack(MatsSpace.MAT_STAINLESS, n * 2)));
    }
}
