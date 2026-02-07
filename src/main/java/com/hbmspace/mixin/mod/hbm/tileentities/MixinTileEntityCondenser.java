package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.saveddata.TomSaveData;
import com.hbm.tileentity.TileEntityLoadedBase;
import com.hbm.tileentity.machine.TileEntityCondenser;
import com.hbmspace.api.tile.IVacuumOptimised;
import net.minecraft.world.EnumSkyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityCondenser.class, remap = false)
public abstract class MixinTileEntityCondenser extends TileEntityLoadedBase implements IVacuumOptimised {

    @Unique
    public boolean vacuumOptimised = false;

    @Override
    public void setVacuumOptimised(boolean optimized) {
        this.vacuumOptimised = optimized;
    }

    @Override
    public boolean isVacuumOptimised() {
        return this.vacuumOptimised;
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lcom/hbm/inventory/fluid/tank/FluidTankNTM;setFill(I)V", ordinal = 1))
    public void onUpdateFill(FluidTankNTM tank, int amount) {
        int convert = amount - tank.getFill();

        int light = this.world.getLightFor(EnumSkyBlock.SKY, this.pos);

        boolean shouldEvaporate = TomSaveData.forWorld(world).fire > 1e-5 && light > 7;

        if (!shouldEvaporate && !vacuumOptimised) {
            CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
            if (CelestialBody.inOrbit(world) || atmosphere == null || atmosphere.getPressure() < 0.01) {
                shouldEvaporate = true;
            }
        }

        if (shouldEvaporate) {
            tank.setFill(tank.getFill() - convert);
        } else {
            tank.setFill(amount);
        }
    }
}