package com.hbmspace.tileentity.machine;

import com.hbmspace.api.tile.IPropulsion;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.List;
@AutoRegister
public class TileEntityStationPropulsionCreative extends TileEntity implements ITickable, IPropulsion {

    private boolean hasRegistered = false;

    @Override
    public void update() {
        if(!CelestialBody.inOrbit(world)) return;

        if(!world.isRemote) {
            if(!hasRegistered) {
                registerPropulsion();
                hasRegistered = true;
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if(hasRegistered) {
            unregisterPropulsion();
            hasRegistered = false;
        }
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();

        if(hasRegistered) {
            unregisterPropulsion();
            hasRegistered = false;
        }
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public boolean canPerformBurn(int shipMass, double deltaV) {
        return true;
    }

    @Override
    public void addErrors(List<String> errors) { }

    @Override
    public float getThrust() {
        return 10_000_000;
    }

    @Override
    public int startBurn() {
        return 20;
    }

    @Override
    public int endBurn() {
        return 20;
    }

}
