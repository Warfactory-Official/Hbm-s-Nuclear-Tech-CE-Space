package com.hbmspace.inventory;

import com.hbm.inventory.OreDictManager;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.items.ItemEnumsSpace;

import static com.hbm.inventory.OreDictManager.DictFrame.fromOne;
import static com.hbmspace.blocks.ModBlocksSpace.*;
import static com.hbmspace.items.ModItemsSpace.*;
import static com.hbm.inventory.OreDictManager.*;

public class OreDictManagerSpace {

    /*
     * STABLE
     */

    /** NICKEL */
    public static final DictFrame NI = new OreDictManager.DictFrame("NickelPure");
    public static final DictFrame NIM = new DictFrame("Nickel"); // Compat with "ferrous metal" so thermal isn't invalidated and neither is our intended progression!
    public static final DictFrame HAFNIUM = new DictFrame("Hafnium");
    public static final DictFrame IRIDIUM = new DictFrame("Iridium");
    public static final DictFrame ZI = new DictFrame("Zinc");
    public static final DictFrame GALLIUM = new DictFrame("Gallium");
    public static final DictFrame GAAS = new DictFrame("GalliumArsenide");
    public static final DictFrame STAINLESS = new DictFrame("StainlessSteel");

    /*
     * DUST AND GEM ORES
     */

    public static final DictFrame CONGLOMERATE  = new DictFrame("Conglomerate");
    public static final DictFrame PENTLANDITE = new DictFrame("Pentlandite");

    public static void registerOres(){

        /*
         * STANDARD OREDICTS WHICH ARE PRESENT IN THE ORIGINAL NTM
         */
        ((IDictFrameAddon) IRON).oreAll(ore_iron);
        ((IDictFrameAddon) GOLD).oreAll(ore_gold);
        ((IDictFrameAddon) REDSTONE).oreAll(ore_redstone);
        ((IDictFrameAddon) LAPIS).oreAll(ore_lapis);
        ((IDictFrameAddon) EMERALD).oreAll(ore_emerald);
        ((IDictFrameAddon) NETHERQUARTZ).oreAll(ore_quartz);
        ((IDictFrameAddon) DIAMOND).oreAll(ore_diamond);
        ((IDictFrameAddon) CU).oreAll(ore_copper);
        ((IDictFrameAddon) LI).oreAll(ore_lithium);
        ((IDictFrameAddon) SA326).oreAll(ore_schrabidium);
        ((IDictFrameAddon) TH232).oreAll(ore_thorium);
        ((IDictFrameAddon) TI).oreAll(ore_titanium);
        ((IDictFrameAddon) S).oreAll(ore_sulfur);
        ((IDictFrameAddon) KNO).oreAll(ore_niter);
        ((IDictFrameAddon) W).oreAll(ore_tungsten);
        ((IDictFrameAddon) AL).oreAll(ore_aluminium);
        ((IDictFrameAddon) F).oreAll(ore_fluorite);
        ((IDictFrameAddon) PB).oreAll(ore_lead);
        ((IDictFrameAddon) BE).oreAll(ore_beryllium);
        ((IDictFrameAddon) RAREEARTH).oreAll(ore_rare);
        ((IDictFrameAddon) CO).oreAll(ore_cobalt);
        ((IDictFrameAddon) CINNABAR).oreAll(ore_cinnabar);
        ((IDictFrameAddon) AUSTRALIUM).oreAll(ore_australium);
        ((IDictFrameAddon) ASBESTOS).oreAll(ore_asbestos);
        ((IDictFrameAddon) U).oreAll(ore_uranium);
        ((IDictFrameAddon) LA).oreAll(ore_lanthanium);
        ((IDictFrameAddon) NB).oreAll(ore_niobium);


        ((IDictFrameAddon) NI.ingot(ingot_nickel).dust(powder_nickel).plate(plate_nickel).block(block_nickel)).oreAll(ore_nickel).nugget(nugget_nickel);
        NIM																	.dust(fromOne(chunk_ore, ItemEnumsSpace.EnumChunkType.PENTLANDITE)); // dust selected for compat reasons
        CONGLOMERATE.ore(fromOne(stone_resource, BlockEnumsSpace.EnumStoneType.CONGLOMERATE));
        ((IDictFrameAddon)ZI			.nugget(nugget_zinc)									.ingot(ingot_zinc)													.dust(powder_zinc))		.oreAll(ore_zinc);
        GALLIUM		.nugget(nugget_gallium)									.ingot(ingot_gallium)												.dust(powder_gallium)		.dustSmall(powder_gallium_tiny);
        GAAS		.nugget(nugget_gaas)									.ingot(ingot_gaas)													.billet(billet_gaas);
        HAFNIUM		.nugget(nugget_hafnium)									.ingot(ingot_hafnium);
        IRIDIUM		.ingot(ingot_iridium);
        STAINLESS															.ingot(ingot_stainless)			 																	.plate(plate_stainless);
        PENTLANDITE	.crystal(fromOne(chunk_ore, ItemEnumsSpace.EnumChunkType.PENTLANDITE));
    }
}
