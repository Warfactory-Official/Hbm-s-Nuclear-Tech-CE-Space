package com.hbmspace.mixin.mod.optifine;

import com.hbmspace.config.SpaceConfig;
import com.hbmspace.world.PlanetGen;
import net.minecraft.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "net.optifine.shaders.Shaders", remap = false)
public class MixinShaders {

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/DimensionType;func_186068_a()I", remap = true))
    private static int spaceDimensionsUseOverworldPrograms(DimensionType type) {
        int id = type.getId();
        if(SpaceConfig.kerbinSkyboxEverywhere) for(int space : PlanetGen.getSpaceDimensions()) if(space == id) return 0;
        return id;
    }
}