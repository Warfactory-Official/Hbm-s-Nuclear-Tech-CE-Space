package com.hbmspace.mixin.mod.hbm;

import com.hbm.packet.toclient.AuxParticlePacketNT;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AuxParticlePacketNT.class)
public interface MixinAuxParticlePacketNTAccessor {
    //technically AT can work but the changed won't be reflected in the decompiled bytecode
    //it works at runtime but not at compiled time. Compiled time AT only works for Minecraft source, at applyJST
    //i believe? technically you can use add AT then use a plain GETFIELD in bytecode; but it's just unnecessary
    //and an @Accessor is just cleaner
    //btw MethodHandleHelper also works, but it has classloading side effect
    @Accessor(value = "nbt", remap = false)
    NBTTagCompound getNbt();
}
