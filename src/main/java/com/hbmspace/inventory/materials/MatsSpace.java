package com.hbmspace.inventory.materials;

import com.hbm.inventory.material.NTMMaterial;

import java.util.ArrayList;
import java.util.List;

import static com.hbm.inventory.material.MaterialShapes.*;
import static com.hbm.inventory.material.Mats.makeAdditive;
import static com.hbm.inventory.material.Mats.makeSmeltable;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

public class MatsSpace {

    public static final List<NTMMaterial> SPACE_MATERIALS = new ArrayList<>();

    private static NTMMaterial add(NTMMaterial mat) {
        SPACE_MATERIALS.add(mat);
        return mat;
    }

    public static final NTMMaterial MAT_NICKEL = add(makeSmeltable(2800, NI, 0xE8D1C7, 0x87756E, 0xAE9572).setAutogen(FRAGMENT, NUGGET, DUST, BLOCK).m());
    public static final NTMMaterial MAT_GALLIUM = add(makeSmeltable(3100, GALLIUM, 0x52687F, 0x52687F, 0x52687F).setAutogen(FRAGMENT, NUGGET, DUST, DUSTTINY).m());
    public static final NTMMaterial MAT_ZINC   = add(makeSmeltable(3000, ZI, 0xD7CBDA, 0x7A7277, 0xA79DA8).setAutogen(FRAGMENT, NUGGET, DUST, WIRE).m());
    public static final NTMMaterial MAT_CONGLOMERATE = add(makeAdditive(2993, CONGLOMERATE, 0x797979, 0x797979, 0x797979).m());
}
