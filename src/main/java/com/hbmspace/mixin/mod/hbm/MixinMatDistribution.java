package com.hbmspace.mixin.mod.hbm;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.material.MatDistribution;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.OreDictManagerSpace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.material.MaterialShapes.*;
import static com.hbm.inventory.material.Mats.*;
import static com.hbmspace.inventory.materials.MatsSpace.*;

@Mixin(MatDistribution.class)
public class MixinMatDistribution {

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    private void registerSpace(CallbackInfo ci) {
        MatDistribution.registerOre(OreDictManagerSpace.ZI.ore(), MAT_ZINC, INGOT.q(2), MAT_STONE, QUART.q(1));
        MatDistribution.registerOre(OreDictManagerSpace.CONGLOMERATE.ore(), MAT_CONGLOMERATE, INGOT.q(4));
        MatDistribution.registerEntry(OreDictManager.DictFrame.fromOne(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CALCIUM), MAT_FLUX, DUST.q(12));
        MatDistribution.registerEntry(ModItems.ring_starmetal, MAT_STAR, INGOT.q(4));
    }
}
