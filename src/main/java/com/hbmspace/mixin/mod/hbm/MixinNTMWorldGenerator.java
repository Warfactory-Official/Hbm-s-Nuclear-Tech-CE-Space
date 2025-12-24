package com.hbmspace.mixin.mod.hbm;
import com.hbm.world.gen.NTMWorldGenerator;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbm.world.gen.nbt.SpawnCondition;
import com.hbmspace.world.PlanetGen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = NTMWorldGenerator.class, remap = false)
public class MixinNTMWorldGenerator {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/hbm/world/gen/nbt/NBTStructure;registerStructure(ILcom/hbm/world/gen/nbt/SpawnCondition;)V"
            ),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=meteor_dungeon")),
            remap = false,
            require = 1
    )
    private static void hbmspace$registerMeteorDungeonInSpaceDims(int dimensionId, SpawnCondition spawn) {
        NBTStructure.registerStructure(spawn, PlanetGen.getSpaceDimensions());
    }
}
