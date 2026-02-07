package com.hbmspace.enums;

import com.hbm.items.ModItems;
import com.hbm.items.special.ItemWasteShort;

public class EnumAddonWasteTypes {

    public static ItemWasteShort.WasteClass AMERICIUM241;   //funny fission fragments + cm242 / am242 / more curium / pu239
    public static ItemWasteShort.WasteClass BERKELIUM247;  //funny fission fragments + curium / californium / americium
    public static ItemWasteShort.WasteClass CURIUM244;	    //californium / lots rare curium isotopes
    public static ItemWasteShort.WasteClass CURIUM245;        //more californium

    private static final Class<?>[] PARAM_TYPES = new Class<?>[]{
            String.class, int.class, int.class
    };

    public static void init() {
        AMERICIUM241 = addWasteType("Americium-241", 750, 1000);
        BERKELIUM247 = addWasteType("Berkelium-247", 1000, 1000);
        CURIUM244 = addWasteType("Curium-244", 1000, 1000);
        CURIUM245 = addWasteType("Curium-245", 1000, 1000);

        EnumAddonTypes.updateStaticValuesField(ItemWasteShort.WasteClass.class, "VALUES");
        EnumAddonTypes.updateInstanceField(ItemWasteShort.WasteClass.class,
                ItemWasteShort.class, "theEnum",
                ModItems.nuclear_waste_short, ModItems.nuclear_waste_short_depleted);
    }

    private static ItemWasteShort.WasteClass addWasteType(String name, int liquid, int gas) {
        return EnumAddonTypes.addEnum(ItemWasteShort.WasteClass.class, name, PARAM_TYPES, name, liquid, gas);
    }
}
