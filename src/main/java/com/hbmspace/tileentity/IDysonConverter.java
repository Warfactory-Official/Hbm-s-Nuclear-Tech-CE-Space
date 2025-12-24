package com.hbmspace.tileentity;

public interface IDysonConverter {

    // Attempt to provide power at a given position on the converter, return false if power can't be received at this location
    public boolean provideEnergy(int x, int y, int z, long energy);

    // Tell Dyson receivers our energy conversion limit
    public long maximumEnergy();

}
