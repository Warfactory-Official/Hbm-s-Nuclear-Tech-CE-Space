package com.hbmspace.mixin.mod.hbm;

import com.hbm.items.gear.ArmorFSB;
import com.hbm.util.I18nUtil;
import com.hbmspace.accessors.ICanSealAccessor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ArmorFSB.class, remap = false)
public class MixinArmorFSBClient {

    @Inject(method = "addInformation(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/util/ITooltipFlag;)V", at = @At("HEAD"))
    private void addInformation_canSeal(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn, CallbackInfo ci) {
        if (((ICanSealAccessor) this).getCanSeal()) {
            list.add(TextFormatting.BLUE + I18nUtil.resolveKey("armor.canSeal"));
        }
    }
}
