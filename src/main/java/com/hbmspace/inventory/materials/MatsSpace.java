package com.hbmspace.inventory.materials;

import com.hbm.inventory.material.NTMMaterial;

import java.util.ArrayList;
import java.util.List;

import static com.hbm.inventory.material.MaterialShapes.*;
import static com.hbm.inventory.material.Mats.makeAdditive;
import static com.hbm.inventory.material.Mats.makeSmeltable;
import static com.hbm.inventory.material.Mats.makeNonSmeltable;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

public class MatsSpace {

    /* make that >24_000 */
    public static final int _EX = 24_000;

    public static final List<NTMMaterial> SPACE_MATERIALS = new ArrayList<>();

    private static NTMMaterial add(NTMMaterial mat) {
        SPACE_MATERIALS.add(mat);
        return mat;
    }

    public static final NTMMaterial MAT_NICKEL = add(makeSmeltable(2800, NI, 0xE8D1C7, 0x87756E, 0xAE9572).setAutogen(FRAGMENT, NUGGET, DUST, BLOCK).m());
    public static final NTMMaterial MAT_GALLIUM = add(makeSmeltable(3100, GALLIUM, 0x52687F, 0x52687F, 0x52687F).setAutogen(FRAGMENT, NUGGET, DUST, DUSTTINY).m());
    public static final NTMMaterial MAT_ZINC   = add(makeSmeltable(3000, ZI, 0xD7CBDA, 0x7A7277, 0xA79DA8).setAutogen(FRAGMENT, NUGGET, DUST, WIRE).m());
    public static final NTMMaterial MAT_CONGLOMERATE = add(makeAdditive(2993, CONGLOMERATE, 0x797979, 0x797979, 0x797979).m());
    public static final NTMMaterial MAT_IRIDIUM		= add(makeSmeltable(7700,		IRIDIUM,		0xB8D0FF, 0xB8D0FF, 0xB8D0FF).setAutogen(INGOT).m());

    //Space extension alloys
    public static final NTMMaterial MAT_GAAS		= add(makeSmeltable(_EX,	GAAS,		0x6F4A57, 0x6F4A57, 0x6F4A57).setAutogen(NUGGET, BILLET).m());
    public static final NTMMaterial MAT_STAINLESS	= add(makeSmeltable(_EX + 1,	STAINLESS,	0xD8D8D8, 0x474747, 0x4A4A4A).setAutogen(PLATE, WELDEDPLATE, CASTPLATE).m());
    public static final NTMMaterial MAT_RICH_MAGMA	= add(makeSmeltable(_EX + 2,	RICHMAGMA,	0x7F7F7F, 0x353555, 0xFF6212).n());
    public static final NTMMaterial MAT_SEMTEX		= add(makeNonSmeltable(_EX + 3, 		SEMTEX,			0xEDAA28, 0x825D16, 0xF0B090).setAutogen(FRAGMENT).n());
}
