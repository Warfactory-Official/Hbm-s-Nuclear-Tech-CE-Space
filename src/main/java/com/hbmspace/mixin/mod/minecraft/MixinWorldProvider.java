package com.hbmspace.mixin.mod.minecraft;
import com.hbmspace.handler.INetherAccessor;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldProvider.class)
public abstract class MixinWorldProvider implements INetherAccessor
{
    @Override
    @Accessor("nether")
    public abstract boolean isNether();

    @Override
    @Accessor("nether")
    public abstract void setNether(boolean value);
}
