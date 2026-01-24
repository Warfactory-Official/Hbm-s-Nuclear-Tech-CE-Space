package com.hbmspace.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

import static com.hbm.main.CraftingManager.addRecipeAuto;
import static com.hbm.main.CraftingManager.addShapelessAuto;

public class RecipeUtil {

    public static void removeAllByOutput(ItemStack out, RegistryEvent.Register<IRecipe> hack) {
        if (hack == null) return;

        final IForgeRegistry<IRecipe> reg = hack.getRegistry();
        if (!(reg instanceof IForgeRegistryModifiable<IRecipe> mod)) return;

        final List<ResourceLocation> toRemove = new ArrayList<>();

        for (IRecipe r : reg.getValuesCollection()) {
            if (r == null) continue;

            ItemStack ro = r.getRecipeOutput();
            if (ro.isEmpty()) continue;

            if (ItemStack.areItemsEqual(ro, out) && ItemStack.areItemStackTagsEqual(ro, out)) {
                ResourceLocation name = r.getRegistryName();
                if (name != null) toRemove.add(name);
            }
        }

        for (ResourceLocation rl : toRemove) {
            mod.remove(rl);
        }
    }

    public static void replaceRecipeAuto(ItemStack output, RegistryEvent.Register<IRecipe> hack, Object... args) {
        removeAllByOutput(output, hack);
        addRecipeAuto(output, args);
    }

    public static void replaceShapelessAuto(ItemStack output, RegistryEvent.Register<IRecipe> hack, Object... args) {
        removeAllByOutput(output, hack);
        addShapelessAuto(output, args);
    }
}
