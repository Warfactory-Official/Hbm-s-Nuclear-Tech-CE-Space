package com.hbmspace.inventory.fluid;

import com.google.common.collect.HashBiMap;
import com.hbm.handler.pollution.PollutionHandler;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.trait.*;

// Th3_Sl1ze: did I need this? Probably not. But I'd love to be able to properly extract fluidtypes I add
public class Fluids {

    // In case these traits will be useful..
    public static final HashBiMap<String, FluidType> renameMapping = HashBiMap.create();
    public static final FluidTraitSimple.FT_Liquid LIQUID = new FluidTraitSimple.FT_Liquid();
    public static final FluidTraitSimple.FT_Viscous VISCOUS = new FluidTraitSimple.FT_Viscous();
    public static final FluidTraitSimple.FT_Gaseous_ART EVAP = new FluidTraitSimple.FT_Gaseous_ART();
    public static final FluidTraitSimple.FT_Gaseous GASEOUS = new FluidTraitSimple.FT_Gaseous();
    public static final FluidTraitSimple.FT_Plasma PLASMA = new FluidTraitSimple.FT_Plasma();
    public static final FluidTraitSimple.FT_Amat ANTI = new FluidTraitSimple.FT_Amat();
    public static final FluidTraitSimple.FT_LeadContainer LEADCON = new FluidTraitSimple.FT_LeadContainer();
    public static final FluidTraitSimple.FT_NoContainer NOCON = new FluidTraitSimple.FT_NoContainer();
    public static final FluidTraitSimple.FT_NoID NOID = new FluidTraitSimple.FT_NoID();
    public static final FluidTraitSimple.FT_Delicious DELICIOUS = new FluidTraitSimple.FT_Delicious();
    public static final FluidTraitSimple.FT_Unsiphonable UNSIPHONABLE = new FluidTraitSimple.FT_Unsiphonable();
    /* Burns 4x dirtier than regular fuel */
    public static final float SOOT_UNREFINED_OIL = PollutionHandler.SOOT_PER_SECOND * 0.1F;
    /* Original baseline, used for most fuels */
    public static final float SOOT_REFINED_OIL = PollutionHandler.SOOT_PER_SECOND * 0.025F;
    /* Gasses burn very cleanly */
    public static final float SOOT_GAS = PollutionHandler.SOOT_PER_SECOND * 0.005F;
    /* Original baseline for leaded fuels */
    public static final float LEAD_FUEL = PollutionHandler.HEAVY_METAL_PER_SECOND * 0.025F;
    /* Poison stat for most petrochemicals */
    public static final float POISON_OIL = PollutionHandler.POISON_PER_SECOND * 0.0025F;
    /* Poison stat for horrible chemicals like red mud or phosgene */
    public static final float POISON_EXTREME = PollutionHandler.POISON_PER_SECOND * 0.025F;
    /* Poison stat for mostly inert things like carbon dioxide */
    public static final float POISON_MINOR = PollutionHandler.POISON_PER_SECOND * 0.001F;
    public static final FT_Polluting P_OIL = new FT_Polluting().burn(PollutionHandler.PollutionType.SOOT, SOOT_UNREFINED_OIL).release(PollutionHandler.PollutionType.POISON, POISON_OIL);
    public static final FT_Polluting P_FUEL = new FT_Polluting().burn(PollutionHandler.PollutionType.SOOT, SOOT_REFINED_OIL).release(PollutionHandler.PollutionType.POISON, POISON_OIL);
    public static final FT_Polluting P_FUEL_LEADED = new FT_Polluting().burn(PollutionHandler.PollutionType.SOOT, SOOT_REFINED_OIL).burn(PollutionHandler.PollutionType.HEAVYMETAL, LEAD_FUEL).release(PollutionHandler.PollutionType.POISON, POISON_OIL).release(PollutionHandler.PollutionType.HEAVYMETAL, LEAD_FUEL * 0.1F);
    public static final FT_Polluting P_GAS = new FT_Polluting().burn(PollutionHandler.PollutionType.SOOT, SOOT_GAS);
    public static final FT_Polluting P_LIQUID_GAS = new FT_Polluting().burn(PollutionHandler.PollutionType.SOOT, SOOT_GAS * 2F);
    public static FluidType EARTHAIR;			//I can't believe it's not compressed air
    public static FluidType BRINE;
    public static FluidType CCL;
    public static FluidType CHLOROETHANE;
    public static FluidType CBENZ;
    public static FluidType VINYL;
    public static FluidType AQUEOUS_COPPER;
    public static FluidType AQUEOUS_NICKEL;
    public static FluidType COPPERSULFATE;
    public static FluidType CONGLOMERA;
    public static FluidType HGAS;
    public static FluidType HALOLIGHT;
    public static FluidType LITHCARBONATE;
    public static FluidType TCRUDE;
    public static FluidType ELBOWGREASE;
    public static FluidType NMASSTETRANOL; //stronger, not suitable for FTL due to its Carbon-Chain content
    public static FluidType NMASS; //weaker, much more suitable for FTL
    public static FluidType SCUTTERBLOOD;
    public static FluidType HTCO4;//we
    public static FluidType NEON;
    public static FluidType ARGON;
    public static FluidType KRYPTON;
    public static FluidType COFFEE;
    public static FluidType TEA;
    public static FluidType HONEY;
    public static FluidType OLIVEOIL;
    public static FluidType FLUORINE; //why not
    public static FluidType DUNAAIR; //yields mostly carbon dioxide with a touch of N2
    public static FluidType TEKTOAIR; // makes methane, and some hydrocarbons too. literally free... //can be distilled for methane, chlorine, aromatics, or can be cracked for chlorine, unsats, and possibly methanol
    public static FluidType JOOLGAS;
    public static FluidType SARNUSGAS;
    public static FluidType UGAS; //urlum
    public static FluidType NGAS;//neidon
    public static FluidType MILK;
    public static FluidType SMILK;
    public static FluidType EVEAIR; // when cryogenically distillated, can yield stuff like mercury, that one chemical pu suggested involving something purple i forgot, and possibly iodine
    public static FluidType KMnO4;
    public static FluidType CHLOROMETHANE; // halogenated natural gas, used in alkylation
    public static FluidType METHANOL; //syngas + methane, or + natgas? or just from cracking natgas? requires an OH hydroxyl group (it's an alcohOL)
    public static FluidType BROMINE; // Aklyl Bromide, cokes into bromide powder and natural gas
    public static FluidType METHYLENE;
    public static FluidType POLYTHYLENE; //this is so that you wont need to go through microcrafting hell on circuits //idea is that rubber solution makes these casts that can then be imprinted in the assembly machine without needing to go through the resources to make the circuits one by one, it would be gated behind oil though.
    public static FluidType HCL;
    public static FluidType AMMONIA;
    public static FluidType BLOODGAS;
    public static FluidType MINSOL;
    public static FluidType NITROGEN;
    public static FluidType EMILK; //ghostycore
    public static FluidType CMILK;
    public static FluidType CREAM;
    public static FluidType DICYANOACETYLENE;//DICYANOACETYLENE
    public static FluidType MORKITE;
    public static FluidType MORKINE; //gaseous morkite
    public static FluidType MSLURRY; // Morkite slurry, similar to MINSOL
    public static FluidType SUPERHEATED_HYDROGEN;
    public static FluidType URANIUM_BROMIDE;
    public static FluidType PLUTONIUM_BROMIDE;
    public static FluidType SCHRABIDIUM_BROMIDE;
    public static FluidType THORIUM_BROMIDE;
    public static FluidType GASEOUS_URANIUM_BROMIDE;
    public static FluidType GASEOUS_PLUTONIUM_BROMIDE;
    public static FluidType GASEOUS_SCHRABIDIUM_BROMIDE;
    public static FluidType GASEOUS_THORIUM_BROMIDE;
    public static FluidType GASEOUS_HYDROGEN;
    public static FluidType GAS_WATZ;

}
