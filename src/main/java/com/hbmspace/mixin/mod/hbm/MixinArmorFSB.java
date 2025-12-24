package com.hbmspace.mixin.mod.hbm;

import com.hbm.items.gear.ArmorFSB;
import com.hbmspace.accessors.ICanSealAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ArmorFSB.class, remap = false)
public abstract class MixinArmorFSB implements ICanSealAccessor {

    @Unique
    private boolean canSeal;

    @Unique
    public ArmorFSB setSealed(boolean canSeal) {
        this.canSeal = canSeal;
        return (ArmorFSB) (Object) this;
    }

    @Unique
    @Override
    public boolean getCanSeal() {
        return this.canSeal;
    }
}
