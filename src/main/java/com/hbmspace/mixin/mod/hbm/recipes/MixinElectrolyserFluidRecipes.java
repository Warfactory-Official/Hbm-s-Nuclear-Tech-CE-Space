package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.ElectrolyserFluidRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;

@Mixin(value = ElectrolyserFluidRecipes.class, remap = false)
public class MixinElectrolyserFluidRecipes {
    @Shadow
    public static HashMap<FluidType, ElectrolyserFluidRecipes.ElectrolysisRecipe> recipes;

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    public void registerSpace(CallbackInfo ci) {
        recipes.put(com.hbmspace.inventory.fluid.Fluids.BRINE, new ElectrolyserFluidRecipes.ElectrolysisRecipe(400, new FluidStack(Fluids.HYDROGEN, 200), new FluidStack(Fluids.OXYGEN, 200),40, new ItemStack(ModItems.powder_sodium, 2)));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL, new ElectrolyserFluidRecipes.ElectrolysisRecipe(300, new FluidStack(Fluids.NONE, 0), new FluidStack(Fluids.NONE, 0),40, new ItemStack(ModItemsSpace.powder_nickel, 2),new ItemStack(ModItems.powder_iron, 2),new ItemStack(ModItems.sulfur, 4)));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.COPPERSULFATE, new ElectrolyserFluidRecipes.ElectrolysisRecipe(200, new FluidStack(Fluids.NONE, 0), new FluidStack(Fluids.OXYGEN, 50),40, new ItemStack(ModItems.powder_copper, 2),new ItemStack(ModItems.sulfur, 2)));
        recipes.put(Fluids.HCL, new ElectrolyserFluidRecipes.ElectrolysisRecipe(1_000, new FluidStack(Fluids.HYDROGEN, 500), new FluidStack(Fluids.CHLORINE, 500), 40));
        recipes.put(com.hbmspace.inventory.fluid.Fluids.LITHCARBONATE, new ElectrolyserFluidRecipes.ElectrolysisRecipe(1000, new FluidStack(Fluids.OXYGEN, 30), new FluidStack(Fluids.CARBONDIOXIDE, 10),40, new ItemStack(ModItems.powder_lithium, 1)));
    }
}
