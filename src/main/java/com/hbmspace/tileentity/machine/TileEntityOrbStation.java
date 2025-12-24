package com.hbmspace.tileentity.machine;

import com.hbm.tileentity.TileEntityMachineBase;

public class TileEntityOrbStation extends TileEntityMachineBase {

    public TileEntityOrbStation(int sc, boolean fluid, boolean energy) {
        super(sc, fluid, energy);
    }

    @Override
    public String getDefaultName() {
        return "";
    }
}
