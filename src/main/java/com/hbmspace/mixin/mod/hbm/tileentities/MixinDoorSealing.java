package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbm.interfaces.IDoor;
import com.hbm.tileentity.TileEntityDoorGeneric;
import com.hbm.tileentity.machine.TileEntityBlastDoor;
import com.hbm.tileentity.machine.TileEntitySiloHatch;
import com.hbm.tileentity.machine.TileEntitySlidingBlastDoor;
import com.hbm.tileentity.machine.TileEntityVaultDoor;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TileEntityDoorGeneric.class, TileEntityBlastDoor.class, TileEntityVaultDoor.class, TileEntitySlidingBlastDoor.class, TileEntitySiloHatch.class})
public abstract class MixinDoorSealing {

    @Unique
    private byte space$sealState = -1;

    @Inject(method = "update", at = @At("RETURN"))
    private void space$onUpdate(CallbackInfo ci) {
        TileEntity tile = (TileEntity) (Object) this;
        World world = tile.getWorld();
        if(world == null || world.isRemote) return;

        byte sealed = (byte) (((IDoor) (Object) this).getState() == IDoor.DoorState.CLOSED ? 1 : 0);
        if(space$sealState == sealed) return;

        boolean initialized = space$sealState != -1;
        space$sealState = sealed;

        if(initialized) ChunkAtmosphereManager.proxy.onDoorStateChanged(world, tile.getPos(), sealed == 1);
    }
}
