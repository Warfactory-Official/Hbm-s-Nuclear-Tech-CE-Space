package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbm.tileentity.machine.TileEntityMachineExcavator;
import com.hbmspace.util.BedrockOreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityMachineExcavator.class, remap = false)
public abstract class MixinTileEntityMachineExcavator {

    @Redirect(
            method = "collectBedrock",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/hbm/items/special/ItemBedrockOreBase;setOreAmount(Lnet/minecraft/item/ItemStack;IID)V"
            ),
            require = 1
    )
    private void space$redirectSetOreAmount(ItemStack stack, int x, int z, double mult) {
        World world = ((TileEntity) (Object) this).getWorld();
        BedrockOreUtil.setOreAmount(world, stack, x, z, mult); // yes I've reverted the multiplier, idk why but it gave entirely different multipliers
    }
}
