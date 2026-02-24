package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.IDynamicModels;
import com.hbm.items.special.ItemBedrockOreBase;
import com.hbmspace.dim.SolarSystem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBedrockOreBase.class)
public abstract class MixinItemBedrockOreBase extends Item implements IDynamicModels {

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void space$onInit(String s, CallbackInfo ci) {
        this.setHasSubtypes(true);
        IDynamicModels.INSTANCES.add(this);
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for(SolarSystem.Body body : SolarSystem.Body.values()) {
                if(body == SolarSystem.Body.ORBIT) continue;
                items.add(new ItemStack(this, 1, body.ordinal()));
            }
        }
    }
}
