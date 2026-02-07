package com.hbmspace.enums;

import com.hbm.items.machine.ItemRBMKRod.EnumBurnFunc;
import com.hbm.items.machine.ItemRBMKRod.EnumDepleteFunc;

public final class EnumAddonRBMKRodTypes {

    public static EnumBurnFunc SLOW_LINEAR;
    public static EnumDepleteFunc CF_SLOPE;

    public static void init() {
        SLOW_LINEAR = EnumAddonTypes.addEnum(
                EnumBurnFunc.class, "SLOW_LINEAR",
                new Class<?>[]{ String.class },
                "trait.rbmx.flux.slow_linear"
        );

        CF_SLOPE = EnumAddonTypes.addEnum(
                EnumDepleteFunc.class, "CF_SLOPE",
                new Class<?>[0]
        );
    }
}