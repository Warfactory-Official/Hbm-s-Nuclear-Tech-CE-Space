package com.hbmspace.hazard;

import com.hbm.hazard.HazardData;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbmspace.items.ModItemsSpace;

public class HazardRegistrySpace {
    public static final float cm242 = 9.3F; //fertile but probably unused
    public static final float cm243 = 5.6F; //fissile
    public static final float cm244 = 2.0F; //fertile
    public static final float cm245 = 0.8F; //fissile
    public static final float cm246 = 2.5F; //fertile
    public static final float cm247 = 0.2F; //fissile
    public static final float cmrg = 6.0F; //reactor-grade curium
    public static final float cmf = 2.2F; //curium fuel
    public static final float bk247 = 10.5F;
    public static final float cf251 = 14.3F;
    public static final float cf252 = 15.3F;
    public static final float es253 = 18.3F;
    public static final float es255 = 19.3F;
    public static final float cn989 = 89.0F;

    public static void registerItemHazards() {
        HazardSystem.register(ModItemsSpace.powder_wd2004, new HazardData().addEntry(HazardRegistry.DIGAMMA, 1F));
        HazardSystem.register(ModItemsSpace.powder_wd2004_tiny, new HazardData().addEntry(HazardRegistry.DIGAMMA, 0.05F));
    }
}
