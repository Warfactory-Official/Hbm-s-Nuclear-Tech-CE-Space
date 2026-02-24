package com.hbmspace.mixin.mod.hbm.items;

import com.hbm.items.special.ItemBedrockOreNew;
import com.hbm.util.I18nUtil;
import com.hbmspace.enums.EnumAddonBedrockOreTypes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(value = ItemBedrockOreNew.class)
public abstract class MixinItemBedrockOreNewClient extends Item {

    @Shadow(remap = false)
    public abstract ItemBedrockOreNew.BedrockOreType getType(int meta);

    @Shadow(remap = false)
    public abstract ItemBedrockOreNew.BedrockOreGrade getGrade(int meta);

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

    @Inject(method = "getItemStackDisplayName", at = @At("HEAD"), cancellable = true)
    private void space$getItemStackDisplayName(ItemStack stack, CallbackInfoReturnable<String> cir) {
        int meta = stack.getItemDamage();
        ItemBedrockOreNew.BedrockOreType type = getType(meta);

        String typeStr = I18n.format(getUnlocalizedNameInefficiently(stack) + ".type." + type.suffix + ".name");
        String baseName = I18n.format(getUnlocalizedNameInefficiently(stack) + ".grade." + getGrade(meta).name().toLowerCase(Locale.US) + ".name", typeStr);

        String body = EnumAddonBedrockOreTypes.BODY_MAP.get(type);
        if (body != null) {
            String bodyName = I18nUtil.resolveKey("body." + body.toLowerCase(Locale.US));
            cir.setReturnValue(baseName + " (" + bodyName + ")");
        } else {
            cir.setReturnValue(baseName);
        }
    }
}
