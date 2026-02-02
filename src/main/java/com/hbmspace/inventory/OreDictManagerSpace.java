package com.hbmspace.inventory;

import com.hbm.inventory.OreDictManager;
import com.hbm.items.ItemEnums;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.hazard.HazardRegistrySpace;
import com.hbmspace.items.enums.ItemEnumsSpace;

import static com.hbm.inventory.OreDictManager.DictFrame.fromAll;
import static com.hbm.inventory.OreDictManager.DictFrame.fromOne;
import static com.hbm.inventory.material.MaterialShapes.DUST;
import static com.hbmspace.blocks.ModBlocksSpace.*;
import static com.hbmspace.items.ModItemsSpace.*;
import static com.hbm.inventory.OreDictManager.*;

public class OreDictManagerSpace {

    /*
     * STABLE
     */

    public static final String KEY_STONE = "stone";

    public static final DictGroup ANY_COAL_COKE = new DictGroup("AnyCoalCoke", ANY_COKE, COAL);

    /** NICKEL */
    public static final DictFrame NI = new OreDictManager.DictFrame("NickelPure");
    public static final DictFrame NIM = new DictFrame("Nickel"); // Compat with "ferrous metal" so thermal isn't invalidated and neither is our intended progression!
    public static final DictFrame HAFNIUM = new DictFrame("Hafnium");
    public static final DictFrame IRIDIUM = new DictFrame("Iridium");
    public static final DictFrame ZI = new DictFrame("Zinc");
    public static final DictFrame GALLIUM = new DictFrame("Gallium");
    public static final DictFrame GAAS = new DictFrame("GalliumArsenide");
    public static final DictFrame STAINLESS = new DictFrame("StainlessSteel");

    public static final DictFrame BK247 = new DictFrame ("Berkelium247", "Bk247");
    public static final DictFrame CF251 = new DictFrame ("Californium251", "Cf251");
    public static final DictFrame CF252 = new DictFrame ("Californium252", "Cf252");
    public static final DictFrame ES253 = new DictFrame ("Einsteinium253", "Es253");
    public static final DictFrame ES255 = new DictFrame ("Einsteinium255", "Es255");
    public static final DictFrame CM242 = new DictFrame ("Curium242", "Cm242");
    public static final DictFrame CM243 = new DictFrame ("Curium243", "Cm243");
    public static final DictFrame CM244 = new DictFrame ("Curium244", "Cm244");
    public static final DictFrame CM245 = new DictFrame ("Curium245", "Cm245");
    public static final DictFrame CM246 = new DictFrame ("Curium246", "Cm246");
    public static final DictFrame CM247 = new DictFrame ("Curium247", "Cm247");
    public static final DictFrame CMRG = new DictFrame ("CuriumRG");
    public static final DictFrame AMF = new DictFrame("AmericiumFuel");
    public static final DictFrame CMF = new DictFrame ("CuriumFuel");

    public static final DictFrame RICHMAGMA = new DictFrame("RichMagma");
    public static final DictFrame SEMTEX = new DictFrame("Semtex");

    public static final DictFrame CN989 = new DictFrame("Chinesium989", "Cn989");

    /*
     * DUST AND GEM ORES
     */

    public static final DictFrame CONGLOMERATE  = new DictFrame("Conglomerate");
    public static final DictFrame PENTLANDITE = new DictFrame("Pentlandite");

    public static void registerGroups() {
        ANY_COAL_COKE.addPrefix(DUST, true);
    }
    public static void registerOres() {

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
        ((IDictFrameAddon) I).oreAll(ore_iodine);

        // TODO: neutron type for hazard registry
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
        COALCOKE.dust(fromOne(powder_coke, ItemEnums.EnumCokeType.COAL));
        PETCOKE.dust(fromOne(powder_coke, ItemEnums.EnumCokeType.PETROLEUM));
        LIGCOKE.dust(fromOne(powder_coke, ItemEnums.EnumCokeType.LIGNITE));
        ANY_COKE.dust(fromAll(powder_coke, ItemEnums.EnumCokeType.class));
        CM242	.rad(HazardRegistrySpace.cm242)							.nugget(nugget_cm242)		.billet(billet_cm242)		.ingot(ingot_cm242);
        CM243	.rad(HazardRegistrySpace.cm243)/*.neutron(HazardRegistry.cm243/80f)*/							.nugget(nugget_cm243)		.billet(billet_cm243)		.ingot(ingot_cm243);
        CM244	.rad(HazardRegistrySpace.cm244)/*.neutron(HazardRegistry.cm244/50f)*/						.nugget(nugget_cm244)		.billet(billet_cm244)		.ingot(ingot_cm244);
        CM245	.rad(HazardRegistrySpace.cm245)/*.neutron(HazardRegistry.cm245/100f)*/							.nugget(nugget_cm245)		.billet(billet_cm245)		.ingot(ingot_cm245);
        CM246	.rad(HazardRegistrySpace.cm246)/*.neutron(HazardRegistry.cm246/50f)*/							.nugget(nugget_cm246)		.billet(billet_cm246)		.ingot(ingot_cm246);
        CM247	.rad(HazardRegistrySpace.cm247)							.nugget(nugget_cm247)		.billet(billet_cm247)		.ingot(ingot_cm247);
        CMRG    .rad(HazardRegistrySpace.cmrg)/*.neutron(HazardRegistrySpace.cmrg/60f)*/                             .nugget(nugget_cm_mix)         .billet(billet_cm_mix)		.ingot(ingot_cm_mix);
        CMF    .rad(HazardRegistrySpace.cmf)/*.neutron(HazardRegistry.cmf/120f)*/                             .nugget(nugget_cm_fuel)         .billet(billet_cm_fuel)		.ingot(ingot_cm_fuel);
        BK247   .rad(HazardRegistrySpace.bk247)							.nugget(nugget_bk247)		.billet(billet_bk247)		.ingot(ingot_bk247);
        CF251   .rad(HazardRegistrySpace.cf251).hot(2)										.nugget(nugget_cf251)				.billet(billet_cf251)    	.ingot(ingot_cf251);
        CF252	.rad(HazardRegistrySpace.cf252)/*.neutron(HazardRegistry.cf252/5f).hot(3)*/						.nugget(nugget_cf252)								.billet(billet_cf252)		.ingot(ingot_cf252);
        ES253	.rad(HazardRegistrySpace.es253)/*.neutron(HazardRegistry.es253/40).hot(4)*/                          .nugget(nugget_es253)	   	.billet(billet_es253)       .ingot(ingot_es253);
        ES255	.rad(HazardRegistrySpace.es255)																					.ingot(ingot_es255);
        CN989   .rad(HazardRegistrySpace.cn989)  .hot(4F)                .nugget(nugget_cn989)       .billet(billet_cn989)       .ingot(ingot_cn989)         .dust(powder_cn989)         .plate(plate_cn989);
    }
}
