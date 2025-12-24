package com.hbmspace.handler.atmosphere;

import com.hbmspace.dim.trait.CBT_Atmosphere;
import net.minecraftforge.common.IPlantable;

public interface IPlantableBreathing extends IPlantable {
    boolean canBreathe(CBT_Atmosphere atmosphere);
}
