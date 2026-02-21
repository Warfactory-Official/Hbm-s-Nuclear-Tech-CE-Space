package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.ModItems;
import com.hbm.items.special.ItemBedrockOreNew;
import com.hbm.util.EnumUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.enums.EnumAddonBedrockOreTypes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

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

    @SideOnly(Side.CLIENT)
    @Inject(method = "registerModels", at = @At("HEAD"), cancellable = true, remap = false)
    private void space$registerModels(CallbackInfo ci) {
        for (int i = 0; i < ItemBedrockOreNew.BedrockOreGrade.VALUES.length; i++) {
            ItemBedrockOreNew.BedrockOreGrade grade = ItemBedrockOreNew.BedrockOreGrade.VALUES[i];
            for (int j = 0; j < ItemBedrockOreNew.BedrockOreType.VALUES.length; j++) {
                ItemBedrockOreNew.BedrockOreType type = ItemBedrockOreNew.BedrockOreType.VALUES[j];
                String placeholderName = "hbm:items/bedrock_ore_" + grade.prefix + "_" + type.suffix + "-" + (i * ItemBedrockOreNew.BedrockOreType.VALUES.length + j);
                ModelLoader.setCustomModelResourceLocation((ItemBedrockOreNew) (Object) this, (grade.ordinal() << 8) | type.ordinal(), new ModelResourceLocation(placeholderName, "inventory"));
            }
        }
        ci.cancel();
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

    @SideOnly(Side.CLIENT)
    @Inject(method = "getItemStackDisplayName", at = @At("HEAD"), cancellable = true)
    private void space$getItemStackDisplayName(ItemStack stack, CallbackInfoReturnable<String> cir) {
        int meta = stack.getItemDamage();
        ItemBedrockOreNew self = (ItemBedrockOreNew) (Object) this;
        ItemBedrockOreNew.BedrockOreType type = self.getType(meta);

        String typeStr = I18n.format(self.getUnlocalizedNameInefficiently(stack) + ".type." + type.suffix + ".name");
        String baseName = I18n.format(self.getUnlocalizedNameInefficiently(stack) + ".grade." + self.getGrade(meta).name().toLowerCase(Locale.US) + ".name", typeStr);

        String body = EnumAddonBedrockOreTypes.BODY_MAP.get(type);
        if (body != null) {
            String bodyName = I18nUtil.resolveKey("body." + body.toLowerCase(Locale.US));
            cir.setReturnValue(baseName + " (" + bodyName + ")");
        } else {
            cir.setReturnValue(baseName);
        }
    }
}