package com.hbmspace.mixin.mod.hbm.block;

import com.hbm.blocks.generic.BlockPedestal;
import com.hbmspace.dim.WorldProviderCelestial;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockPedestal.class, remap = false)
public class MixinBlockPedestal {

    @Redirect(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getCelestialAngle(F)F", ordinal = 2))
    private float spork_redirectNewMoonAngle1(World world, float partialTicks) {
        if(world.provider instanceof WorldProviderCelestial) {
            float realAngle = world.getCelestialAngle(partialTicks);
            return (realAngle > 0.15F && realAngle < 0.85F) ? 0.0F : 0.5F;
        }
        return world.getCelestialAngle(partialTicks);
    }

    @Redirect(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getCelestialAngle(F)F", ordinal = 3))
    private float spork_redirectNewMoonAngle2(World world, float partialTicks) {
        if(world.provider instanceof WorldProviderCelestial) {
            float realAngle = world.getCelestialAngle(partialTicks);
            return (realAngle > 0.15F && realAngle < 0.85F) ? 0.0F : 0.5F;
        }
        return world.getCelestialAngle(partialTicks);
    }

    @Redirect(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getMoonPhase(J)I", ordinal = 1))
    private int spork_redirectNewMoonPhase(WorldProvider provider, long worldTime) {
        if(provider instanceof WorldProviderCelestial) {
            return ((WorldProviderCelestial) provider).isEclipse() ? 4 : 0;
        }
        return provider.getMoonPhase(worldTime);
    }
}
