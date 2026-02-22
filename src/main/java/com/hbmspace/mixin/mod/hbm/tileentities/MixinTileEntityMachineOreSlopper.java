package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbm.items.special.ItemBedrockOreNew;
import com.hbm.tileentity.machine.TileEntityMachineOreSlopper;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.enums.EnumAddonBedrockOreTypes;
import com.hbmspace.util.BedrockOreUtil;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityMachineOreSlopper.class)
public class MixinTileEntityMachineOreSlopper {

    @Shadow(remap = false) public double[] ores;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void space$onInit(CallbackInfo ci) {
        ItemBedrockOreNew.BedrockOreType[] allTypes = EnumAddonBedrockOreTypes.ALL_TYPES;
        if (allTypes == null) allTypes = ItemBedrockOreNew.BedrockOreType.class.getEnumConstants();
        if (this.ores.length < allTypes.length) {
            double[] newOres = new double[allTypes.length];
            System.arraycopy(this.ores, 0, newOres, 0, this.ores.length);
            this.ores = newOres;
        }
    }

    @Redirect(
            method = "update",
            at = @At(value = "FIELD", target = "Lcom/hbm/items/special/ItemBedrockOreNew$BedrockOreType;VALUES:[Lcom/hbm/items/special/ItemBedrockOreNew$BedrockOreType;", remap = false)
    )
    private ItemBedrockOreNew.BedrockOreType[] space$redirectBedrockOreTypeValues() {
        ItemBedrockOreNew.BedrockOreType[] allTypes = EnumAddonBedrockOreTypes.ALL_TYPES;
        return allTypes != null ? allTypes : ItemBedrockOreNew.BedrockOreType.class.getEnumConstants();
    }

    @Redirect(
            method = "update",
            at = @At(value = "INVOKE", target = "Lcom/hbm/items/special/ItemBedrockOreBase;getOreAmount(Lnet/minecraft/item/ItemStack;Lcom/hbm/items/special/ItemBedrockOreNew$BedrockOreType;)D", remap = false)
    )
    private double space$redirectGetOreAmount(ItemStack stack, ItemBedrockOreNew.BedrockOreType type) {
        SolarSystem.Body body = BedrockOreUtil.getOreBody(stack);
        if (BedrockOreUtil.getTypesForBody(body).contains(type)) {
            return BedrockOreUtil.getOreAmount(stack, type);
        }
        return 0.0D;
    }
}