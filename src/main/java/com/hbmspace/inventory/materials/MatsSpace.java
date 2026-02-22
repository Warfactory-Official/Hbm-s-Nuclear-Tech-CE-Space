package com.hbmspace.inventory.materials;

import com.hbm.inventory.material.NTMMaterial;

import java.util.ArrayList;
import java.util.List;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.OreDictManager.AUSTRALIUM;
import static com.hbm.inventory.material.MaterialShapes.*;
import static com.hbm.inventory.material.Mats.*;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

public class MatsSpace {

    static {
        MAT_POLYMER.setAutogen(FRAGMENT, STOCK, GRIP);
        MAT_RUBBER.setAutogen(FRAGMENT, DUST, PIPE, GRIP);
        MAT_PVC.setAutogen(FRAGMENT, DUST, STOCK, GRIP);
    }
    /* make that >24_000 */
    public static final int _EX = 24_000;

    public static final List<NTMMaterial> SPACE_MATERIALS = new ArrayList<>();

    private static NTMMaterial add(NTMMaterial mat) {
        SPACE_MATERIALS.add(mat);
        return mat;
    }

    public static final NTMMaterial MAT_GLOWSTONE		= add(makeNonSmeltable(_VS + 05,		GLOWSTONE,			0xFFFF00, 0x535300, 0xFFFF00).setAutogen(FRAGMENT).n());

    public static final NTMMaterial MAT_QUARTZ			= add(makeNonSmeltable(1402,		NETHERQUARTZ,	0xF7F5F2, 0x6F5D5A, 0xF7F5F2).setAutogen(FRAGMENT).n());
    public static final NTMMaterial MAT_PHOSPHORUS_W	= add(makeNonSmeltable(1501,	P_WHITE,			0xF5F5ED, 0xC4BD9A, 0xC4BD9A).setAutogen(FRAGMENT, DUST).n());
    public static final NTMMaterial MAT_NICKEL = add(makeSmeltable(2800, NI, 0xE8D1C7, 0x87756E, 0xAE9572).setAutogen(FRAGMENT, NUGGET, DUST, BLOCK).m());
    public static final NTMMaterial MAT_GALLIUM = add(makeSmeltable(3100, GALLIUM, 0x52687F, 0x52687F, 0x52687F).setAutogen(FRAGMENT, NUGGET, DUST, DUSTTINY).m());
    public static final NTMMaterial MAT_ZINC   = add(makeSmeltable(3000, ZI, 0xD7CBDA, 0x7A7277, 0xA79DA8).setAutogen(FRAGMENT, NUGGET, DUST, WIRE).m());
    public static final NTMMaterial MAT_BROMINE			= add(makeNonSmeltable(3500,		BR,				0xFF642B, 0x720000, 0xFF642B).setAutogen(FRAGMENT).m());
    public static final NTMMaterial MAT_IODINE			= add(makeNonSmeltable(5300,		I,				0x7A8796, 0x3F3049, 0x7A8796).setAutogen(FRAGMENT).m());
    public static final NTMMaterial MAT_HAFNIUM			= add(makeSmeltable(7200,		HAFNIUM,		0xFFF8C7, 0x2E1600, 0xFFF8C7).setAutogen(FRAGMENT, DUST).m());
    public static final NTMMaterial MAT_CONGLOMERATE = add(makeAdditive(2993, CONGLOMERATE, 0x797979, 0x797979, 0x797979).m());
    public static final NTMMaterial MAT_IRIDIUM		= add(makeSmeltable(7700,		IRIDIUM,		0xB8D0FF, 0xB8D0FF, 0xB8D0FF).setAutogen(INGOT).m());

    public static final NTMMaterial MAT_AUSTRALIUM	= add(makeSmeltable(13800,		AUSTRALIUM,	0xFFFF00, 0x935B00, 0xFFFF00).setAutogen(FRAGMENT).m());
    public static final NTMMaterial MAT_TASMANITE	= add(makeSmeltable(13895,		TASMANITE,	0xFFFF00, 0x935B00, 0xFFFF00).setAutogen(FRAGMENT).m());
    public static final NTMMaterial MAT_AYERITE		= add(makeSmeltable(13851,		AYERITE,	0xFFFF00, 0x935B00, 0xFFFF00).setAutogen(FRAGMENT).m());

    //Space extension alloys
    public static final NTMMaterial MAT_GAAS		= add(makeSmeltable(_EX,	GAAS,		0x6F4A57, 0x6F4A57, 0x6F4A57).setAutogen(NUGGET, BILLET).m());
    public static final NTMMaterial MAT_STAINLESS	= add(makeSmeltable(_EX + 1,	STAINLESS,	0xD8D8D8, 0x474747, 0x4A4A4A).setAutogen(PLATE, WELDEDPLATE, CASTPLATE).m());
    public static final NTMMaterial MAT_RICH_MAGMA	= add(makeSmeltable(_EX + 2,	RICHMAGMA,	0x7F7F7F, 0x353555, 0xFF6212).n());
    public static final NTMMaterial MAT_SEMTEX		= add(makeNonSmeltable(_EX + 3, 		SEMTEX,			0xEDAA28, 0x825D16, 0xF0B090).setAutogen(FRAGMENT).n());

    public static void forceInit() {
    }
}
