package com.hbmspace.mixin.mod.hbm.jei;

import com.hbm.handler.jei.OreSlopperHandler;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.items.special.ItemBedrockOreNew;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.util.BedrockOreUtil;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(OreSlopperHandler.class)
public class MixinOreSlopperHandler {

    @Inject(method = "getSlopperRecipes", at = @At("HEAD"), cancellable = true, remap = false)
    private static void space$getSlopperRecipes(CallbackInfoReturnable<HashMap<Object, Object>> cir) {
        HashMap<Object, Object> recipes = new HashMap<>();

        for (SolarSystem.Body body : SolarSystem.Body.values()) {
            if (body == SolarSystem.Body.ORBIT) continue;

            List<ItemBedrockOreNew.BedrockOreType> types = BedrockOreUtil.getTypesForBody(body);
            if (types.isEmpty()) continue;

            List<ItemStack> outputs = new ArrayList<>();
            for (ItemBedrockOreNew.BedrockOreType type : types) {
                outputs.add(ItemBedrockOreNew.make(ItemBedrockOreNew.BedrockOreGrade.BASE, type));
            }
            outputs.add(ItemFluidIcon.make(Fluids.SLOP, 1000));

            recipes.put(
                    new ItemStack[] {ItemFluidIcon.make(Fluids.WATER, 1000), new ItemStack(ModItems.bedrock_ore_base, 1, body.ordinal())},
                    outputs.toArray(new ItemStack[0])
            );
        }

        cir.setReturnValue(recipes);
    }
}