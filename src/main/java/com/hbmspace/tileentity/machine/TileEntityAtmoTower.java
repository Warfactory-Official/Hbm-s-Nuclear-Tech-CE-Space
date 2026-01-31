package com.hbmspace.tileentity.machine;

import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.tileentity.machine.TileEntityDeuteriumTower;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.fluid.Fluids;

@AutoRegister
public class TileEntityAtmoTower extends TileEntityDeuteriumTower {

    public TileEntityAtmoTower() {
        super();
        tanks[0] = new FluidTankNTM(Fluids.EARTHAIR, 50000);
        tanks[1] = new FluidTankNTM(Fluids.NITROGEN, 5000);
    }

    @Override
    public long getMaxPower() {
        return 1000000;
    }

}
