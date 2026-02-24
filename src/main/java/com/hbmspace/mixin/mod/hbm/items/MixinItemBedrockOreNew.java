package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.ModItems;
import com.hbm.items.special.ItemBedrockOreNew;
import com.hbm.util.EnumUtil;
import com.hbmspace.enums.EnumAddonBedrockOreTypes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemBedrockOreNew.class)
public abstract class MixinItemBedrockOreNew extends Item {

    /**
     * Replaces the logic behind metadata packing to extend the supported range from 16 constants up to 256 constants.
     */
    @Inject(method = "make(Lcom/hbm/items/special/ItemBedrockOreNew$BedrockOreGrade;Lcom/hbm/items/special/ItemBedrockOreNew$BedrockOreType;I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true, remap = false)
    private static void space$make(ItemBedrockOreNew.BedrockOreGrade grade, ItemBedrockOreNew.BedrockOreType type, int amount, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(new ItemStack(ModItems.bedrock_ore, amount, (grade.ordinal() << 8) | type.ordinal()));
    }

    @Inject(method = "getGrade", at = @At("HEAD"), cancellable = true, remap = false)
    private void space$getGrade(int meta, CallbackInfoReturnable<ItemBedrockOreNew.BedrockOreGrade> cir) {
        cir.setReturnValue(EnumUtil.grabEnumSafely(ItemBedrockOreNew.BedrockOreGrade.VALUES, meta >> 8));
    }

    @Inject(method = "getType", at = @At("HEAD"), cancellable = true, remap = false)
    private void space$getType(int meta, CallbackInfoReturnable<ItemBedrockOreNew.BedrockOreType> cir) {
        ItemBedrockOreNew.BedrockOreType[] arr = EnumAddonBedrockOreTypes.ALL_TYPES;
        if (arr == null) arr = ItemBedrockOreNew.BedrockOreType.class.getEnumConstants();
        cir.setReturnValue(EnumUtil.grabEnumSafely(arr, meta & 255));
    }

    @Inject(method = "getSubItems", at = @At("HEAD"), cancellable = true)
    private void space$getSubItems(CreativeTabs tab, NonNullList<ItemStack> items, CallbackInfo ci) {
        if (this.isInCreativeTab(tab)) {
            for (int j = 0; j < ItemBedrockOreNew.BedrockOreType.VALUES.length; j++) {
                ItemBedrockOreNew.BedrockOreType type = ItemBedrockOreNew.BedrockOreType.VALUES[j];
                for (int i = 0; i < ItemBedrockOreNew.BedrockOreGrade.VALUES.length; i++) {
                    ItemBedrockOreNew.BedrockOreGrade grade = ItemBedrockOreNew.BedrockOreGrade.VALUES[i];
                    // Filters out most states to prevent NEI/JEI spam from huge enum sizes
                    if (tab != CreativeTabs.SEARCH && grade != ItemBedrockOreNew.BedrockOreGrade.BASE) continue;
                    items.add(ItemBedrockOreNew.make(grade, type, 1));
                }
            }
        }
        ci.cancel();
    }
}