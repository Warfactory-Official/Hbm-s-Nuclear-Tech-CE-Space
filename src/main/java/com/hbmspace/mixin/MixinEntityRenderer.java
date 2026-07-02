package com.hbmspace.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Shadow public float thirdPersonDistance;

    @ModifyConstant(method = "updateRenderer", constant = @Constant(floatValue = 4.0F))
    private float fixThirdPersonDistancePrev(float constant) {
        return this.thirdPersonDistance;
    }

    @ModifyConstant(method = "orientCamera", constant = @Constant(floatValue = 4.0F))
    private float fixThirdPersonDistanceTarget(float constant) {
        return this.thirdPersonDistance;
    }
}