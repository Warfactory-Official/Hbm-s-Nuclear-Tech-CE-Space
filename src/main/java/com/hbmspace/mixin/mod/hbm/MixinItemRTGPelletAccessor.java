package com.hbmspace.mixin.mod.hbm;

import com.hbm.items.machine.ItemRTGPellet;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
// Th3_Sl1ze: I wanted to do it via accessor but it didn't work... :(
@Mixin(value = ItemRTGPellet.class, remap = false)
public interface MixinItemRTGPelletAccessor {
    @Accessor("doesDecay")
    void setDoesDecay(boolean doesDecay);

    @Accessor("decayItem")
    void setDecayItem(ItemStack decayItem);

    @Accessor("halflife")
    void setHalflife(long halflife);

    @Accessor("lifespan")
    void setLifespan(long lifespan);
}
