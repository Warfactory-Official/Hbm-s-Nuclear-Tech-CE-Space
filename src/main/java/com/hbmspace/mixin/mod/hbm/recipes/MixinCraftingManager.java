package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.main.CraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = CraftingManager.class, remap = false)
public class MixinCraftingManager {

    @Inject(method = "getRecipeName", at = @At("HEAD"), cancellable = true)
    private static void useActiveModDomain(ItemStack output, CallbackInfoReturnable<ResourceLocation> cir) {
        // Th3_Sl1ze: hopefully that will save me of 200+ random shitwarns
        ModContainer active = Loader.instance().activeModContainer();
        if (active == null) return;
        String domain = active.getModId().toLowerCase();
        if (domain.equals("hbm")) return;

        String path = Objects.requireNonNull(output.getItem().getRegistryName()).getPath();
        ResourceLocation loc = new ResourceLocation(domain, path);
        int i = 0;
        while (ForgeRegistries.RECIPES.containsKey(loc)) {
            i++;
            loc = new ResourceLocation(domain, path + "_" + i);
        }
        cir.setReturnValue(loc);
    }
}
