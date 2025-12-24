package com.hbmspace.mixin.mod.hbm;

import com.hbm.blocks.BlockEnums;
import com.hbm.blocks.ModBlocks;
import com.hbmspace.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbm.main.MainRegistry;
import com.hbm.world.feature.OreCave;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.world.feature.OreCaveSpace;
import com.hbmspace.world.feature.OreLayer3DSpace;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MainRegistry.class, remap = false)
public class MixinMainRegistry {
    @Unique
    private boolean spc$hem, spc$baux, spc$mal, spc$sulf, spc$asb;

    @Inject(method = "postInit", at = @At("HEAD"))
    private void addon$disableOriginal(FMLPostInitializationEvent event, CallbackInfo ci) {
        spc$hem = WorldConfig.enableHematite;
        spc$baux = WorldConfig.enableBauxite;
        spc$mal = WorldConfig.enableMalachite;
        spc$sulf = WorldConfig.enableSulfurCave;
        spc$asb = WorldConfig.enableAsbestosCave;

        WorldConfig.enableHematite = false;
        WorldConfig.enableBauxite = false;
        WorldConfig.enableMalachite = false;
        WorldConfig.enableSulfurCave = false;
        WorldConfig.enableAsbestosCave = false;
    }

    @Inject(method = "postInit", at = @At("TAIL"))
    private void addon$injectSpaceLayers(FMLPostInitializationEvent event, CallbackInfo ci) {
        WorldConfig.enableHematite = spc$hem;
        WorldConfig.enableBauxite = spc$baux;
        WorldConfig.enableMalachite = spc$mal;
        WorldConfig.enableSulfurCave = spc$sulf;
        WorldConfig.enableAsbestosCave = spc$asb;

        // Global caves + layers
        // Sulfur caves can't be defined globally due to vacuums evaporating fluids
        if(WorldConfig.enableHematite) new OreLayer3DSpace(ModBlocks.stone_resource, BlockEnums.EnumStoneType.HEMATITE.ordinal()).setGlobal(true).setScaleH(0.04D).setScaleV(0.25D).setThreshold(230);
        if(WorldConfig.enableBauxite) new OreLayer3DSpace(ModBlocks.stone_resource, BlockEnums.EnumStoneType.BAUXITE.ordinal()).setGlobal(true).setScaleH(0.03D).setScaleV(0.15D).setThreshold(300);
        if(WorldConfig.enableMalachite) new OreLayer3DSpace(ModBlocks.stone_resource, BlockEnums.EnumStoneType.MALACHITE.ordinal()).setGlobal(true).setScaleH(0.1D).setScaleV(0.15D).setThreshold(275);


        // Earth caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20).withFluid(ModBlocks.sulfuric_acid_block);
        if(WorldConfig.enableAsbestosCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.ASBESTOS.ordinal()).setThreshold(1.75D).setRangeMult(20).setYLevel(25).setMaxRange(20);

        // Moon caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setDimension(SpaceConfig.moonDimension).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20);
        new OreLayer3DSpace(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal()).setDimension(SpaceConfig.moonDimension).setScaleH(0.04D).setScaleV(0.25D).setThreshold(220);

        // Duna caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setDimension(SpaceConfig.dunaDimension).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20);

        // Ike caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setDimension(SpaceConfig.ikeDimension).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20);
        new OreLayer3DSpace(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal()).setDimension(SpaceConfig.ikeDimension).setScaleH(0.04D).setScaleV(0.25D).setThreshold(220);

        // Eve caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setDimension(SpaceConfig.eveDimension).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20).withFluid(ModBlocks.sulfuric_acid_block);
        new OreCaveSpace(ModBlocks.basalt, 0).setDimension(SpaceConfig.eveDimension).setThreshold(0.15D).setRangeMult(40).setYLevel(54).setMaxRange(24).setBlockOverride(ModBlocksSpace.eve_silt).setIgnoreWater(true).setStalagmites(false);

        // Moho caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setDimension(SpaceConfig.mohoDimension).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20);

        // Minmus caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setDimension(SpaceConfig.minmusDimension).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20);
        new OreLayer3DSpace(ModBlocksSpace.minmus_regolith, 0).setDimension(SpaceConfig.minmusDimension).setScaleH(0.06D).setScaleV(0.25D).setThreshold(220);
        new OreLayer3DSpace(ModBlocksSpace.minmus_smooth, 0).setDimension(SpaceConfig.minmusDimension).setScaleH(0.05D).setScaleV(0.15D).setThreshold(280);
        new OreLayer3DSpace(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal()).setDimension(SpaceConfig.minmusDimension).setScaleH(0.04D).setScaleV(0.25D).setThreshold(220);

        // Laythe caves + layers
        if(WorldConfig.enableSulfurCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.SULFUR.ordinal()).setDimension(SpaceConfig.laytheDimension).setThreshold(1.5D).setRangeMult(20).setYLevel(30).setMaxRange(20).withFluid(ModBlocks.sulfuric_acid_block);
        if(WorldConfig.enableAsbestosCave) new OreCave(ModBlocks.stone_resource, BlockEnums.EnumStoneType.ASBESTOS.ordinal()).setDimension(SpaceConfig.laytheDimension).setThreshold(1.75D).setRangeMult(20).setYLevel(25).setMaxRange(20);
        new OreCave(ModBlocksSpace.tumor).setDimension(SpaceConfig.laytheDimension).setThreshold(0.3D).setRangeMult(20).setYLevel(25).setMaxRange(70);
        new OreCaveSpace(ModBlocksSpace.laythe_silt, 6).setDimension(SpaceConfig.laytheDimension).setThreshold(0.25D).setRangeMult(80).setYLevel(54).setMaxRange(24).setBlockOverride(ModBlocksSpace.laythe_silt).setIgnoreWater(true).setStalagmites(false);
        new OreCaveSpace(ModBlocksSpace.laythe_silt, 3).setDimension(SpaceConfig.laytheDimension).setThreshold(0.2D).setRangeMult(60).setYLevel(58).setMaxRange(14).setBlockOverride(ModBlocksSpace.laythe_silt).setIgnoreWater(true).setStalagmites(false);
    }
}
