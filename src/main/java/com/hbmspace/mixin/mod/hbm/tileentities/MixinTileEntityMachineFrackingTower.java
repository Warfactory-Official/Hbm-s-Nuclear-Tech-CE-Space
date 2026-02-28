package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbmspace.dim.SolarSystem;
import com.hbm.tileentity.machine.oil.TileEntityMachineFrackingTower;
import com.hbm.tileentity.machine.oil.TileEntityOilDrillBase;
import com.hbmspace.world.feature.OilSpotSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityMachineFrackingTower.class, remap = false)
public abstract class MixinTileEntityMachineFrackingTower extends TileEntityOilDrillBase {

    @Shadow protected static int destructionRange;

    @Inject(
            method = "onSuck",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/hbm/world/feature/OilSpot;generateOilSpot(Lnet/minecraft/world/World;IIIIZ)V",
                    remap = false
            ),
            cancellable = true
    )
    private void hbmspace$onSuckTekto(BlockPos pos, CallbackInfo ci) {
        IBlockState state = this.world.getBlockState(pos);
        if (state.getBlock().getMetaFromState(state) == SolarSystem.Body.TEKTO.ordinal()) {
            OilSpotSpace.generateCrack(this.world, this.pos.getX(), this.pos.getZ(), destructionRange, 10);
            ci.cancel();
        }
    }
}