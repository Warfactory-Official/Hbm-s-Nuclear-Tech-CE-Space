package com.hbmspace.mixin.mod.hbm;

import com.hbm.blocks.BlockEnums;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.WorldConfig;
import com.hbm.main.MainRegistry;
import com.hbmspace.world.feature.OreLayer3DSpace;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MainRegistry.class, remap = false)
public class MixinMainRegistry {
    private boolean hem, baux, mal;

    @Inject(method = "postInit", at = @At("HEAD"))
    private void addon$disableOriginal(FMLPostInitializationEvent event, CallbackInfo ci) {
        hem = WorldConfig.enableHematite;
        baux = WorldConfig.enableBauxite;
        mal = WorldConfig.enableMalachite;

        WorldConfig.enableHematite = false;
        WorldConfig.enableBauxite = false;
        WorldConfig.enableMalachite = false;
    }

    @Inject(method = "postInit", at = @At("TAIL"))
    private void addon$injectSpaceLayers(FMLPostInitializationEvent event, CallbackInfo ci) {
        WorldConfig.enableHematite = hem;
        WorldConfig.enableBauxite = baux;
        WorldConfig.enableMalachite = mal;

        if(WorldConfig.enableHematite) new OreLayer3DSpace(ModBlocks.stone_resource, BlockEnums.EnumStoneType.HEMATITE.ordinal()).setGlobal(true).setScaleH(0.04D).setScaleV(0.25D).setThreshold(230);
        if(WorldConfig.enableBauxite) new OreLayer3DSpace(ModBlocks.stone_resource, BlockEnums.EnumStoneType.BAUXITE.ordinal()).setGlobal(true).setScaleH(0.03D).setScaleV(0.15D).setThreshold(300);
        if(WorldConfig.enableMalachite) new OreLayer3DSpace(ModBlocks.stone_resource, BlockEnums.EnumStoneType.MALACHITE.ordinal()).setGlobal(true).setScaleH(0.1D).setScaleV(0.15D).setThreshold(275);
    }
}
