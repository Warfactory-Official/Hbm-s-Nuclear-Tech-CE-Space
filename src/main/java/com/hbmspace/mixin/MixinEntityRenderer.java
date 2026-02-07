package com.hbmspace.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Shadow public float thirdPersonDistance;

    // 1. Prevent updateRenderer from forcibly resetting 'prev' distance to 4.0F.
    // Instead, make it update 'prev' to the current 'thirdPersonDistance' (e.g., 12.0F set by your event).
    @ModifyConstant(method = "updateRenderer", constant = @Constant(floatValue = 4.0F))
    private float fixThirdPersonDistancePrev(float constant) {
        return this.thirdPersonDistance;
    }

    // 2. Make orientCamera use the actual 'thirdPersonDistance' as the target distance 
    // instead of the hardcoded 4.0F.
    @ModifyConstant(method = "orientCamera", constant = @Constant(floatValue = 4.0F))
    private float fixThirdPersonDistanceTarget(float constant) {
        return this.thirdPersonDistance;
    }
}