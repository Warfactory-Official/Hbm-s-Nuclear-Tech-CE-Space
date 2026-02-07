package com.hbmspace.mixin.mod.hbm;

import com.hbm.handler.pollution.PollutionHandler;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.*;
import com.hbm.render.misc.EnumSymbol;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.hbmspace.inventory.fluid.Fluids.*;

import java.util.List;

@Mixin(value = Fluids.class, remap = false)
public abstract class MixinFluids {

    @Shadow
    @Final
    protected static List<FluidType> metaOrder;

    @Unique
    private static FluidType space$createFixed(String name, int color, int p, int f, int r, EnumSymbol symbol, int id) {
        FluidType fluid = new FluidType(name, color, p, f, r, symbol, name.toLowerCase(java.util.Locale.US), 0xFFFFFF, id, null);
        fluid.renderWithTint = false; // since otherwise I won't be able to render corresponding textures for tanks

        return fluid;
    }

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private static void hbmextra$registerExtraFluids(CallbackInfo ci) {
        if (Fluids.fromName("EARTHAIR") != Fluids.NONE) return;

        int idCounter = 4000;

        EARTHAIR = space$createFixed("EARTHAIR", 0xD1CEEE, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.GASEOUS);
        CCL = space$createFixed("CCL", 0x0C3B2F, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.LIQUID, new FT_Corrosive(10));
        CHLOROETHANE = space$createFixed("CHLOROETHANE", 0xBBA9A0, 2, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.GASEOUS);
        CBENZ = space$createFixed("CBENZ", 0x91C6BB, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(Fluids.LIQUID);
        VINYL = space$createFixed("VINYL", 0xA2A2A2, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID);
        AQUEOUS_COPPER = space$createFixed("AQUEOUS_COPPER", 0x4CC2A2, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        AQUEOUS_NICKEL = space$createFixed("AQUEOUS_NICKEL", 0xDACEBA, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID);
        COPPERSULFATE = space$createFixed("COPPERSULFATE", 0x55E5CF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        BRINE = space$createFixed("BRINE", 0xD1A73E, 3, 3, 3, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        CONGLOMERA = space$createFixed("CONGLOMERA", 0x364D47, 0, 0, 2, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        HGAS = space$createFixed("HGAS", 0x999368, 0, 0, 0, EnumSymbol.ACID, idCounter++).addTraits(GASEOUS, new FT_Corrosive(120));
        HALOLIGHT = space$createFixed("HALOLIGHT", 0xB6F9CF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID);
        LITHCARBONATE = space$createFixed("LITHCARBONATE", 0xD1CEBE, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(GASEOUS);
        TCRUDE = space$createFixed("TCRUDE", 0x051914, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID);
        ELBOWGREASE = space$createFixed("ELBOWGREASE",  0xCBC433, 1, 3, 0, EnumSymbol.NONE, idCounter++).addContainers(new Fluids.CD_Canister(0xCBC433)).addTraits(new FT_Flammable(600_000), LIQUID);
        NMASSTETRANOL = space$createFixed("NMASSTETRANOL",0xF1DB0F, 1, 3, 0, EnumSymbol.NONE, idCounter++).addContainers(new Fluids.CD_Canister(0xF1DB0F)).addTraits(new FT_Flammable(1_000_000), LIQUID, new FT_Corrosive(70), new FT_Poison(true, 0), new FT_VentRadiation(0.01F));
        NMASS = space$createFixed("NMASS",        0x53A9F4, 1, 2, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, new FT_Corrosive(10), new FT_Poison(true, 0), new FT_VentRadiation(0.04F));
        SCUTTERBLOOD = space$createFixed("SCUTTERBLOOD", 0x6C166C, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS, DELICIOUS);
        HTCO4 = space$createFixed("HTCO4",        0x675454, 1, 3, 0, EnumSymbol.RADIATION, idCounter++).addTraits(LIQUID, new FT_Corrosive(10), new FT_VentRadiation(0.5F));
        NEON = space$createFixed("NEON",         0xF1F600, 0, 0, 0, EnumSymbol.CROYGENIC, idCounter++).addTraits(GASEOUS);
        ARGON = space$createFixed("ARGON",        0xFD70D0, 0, 0, 0, EnumSymbol.CROYGENIC, idCounter++).addTraits(GASEOUS);
        KRYPTON = space$createFixed("KRYPTON",      0x9AC6E6, 0, 0, 0, EnumSymbol.CROYGENIC, idCounter++).addTraits(GASEOUS);
        COFFEE = space$createFixed("COFFEE",       0x57493D, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);
        TEA = space$createFixed("TEA",          0x76523C, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);
        HONEY = space$createFixed("HONEY",        0xD99A02, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);
        OLIVEOIL = space$createFixed("OLIVEOIL",     0xA9B990, 0, 0, 0, EnumSymbol.NONE, idCounter++).setFFNameOverride("olive_oil").addTraits(DELICIOUS, LIQUID);
        FLUORINE = space$createFixed("FLUORINE",     0xC5C539, 4, 4, 4, EnumSymbol.OXIDIZER, idCounter++).addTraits(GASEOUS, new FT_Corrosive(40), new FT_Poison(true, 1)).addTraits(new FT_Flammable(10_000));
        DUNAAIR = space$createFixed("DUNAAIR",      0xD4704E, 3, 0, 0, EnumSymbol.ASPHYXIANT, idCounter++).addTraits(GASEOUS, new FT_Polluting().release(PollutionHandler.PollutionType.POISON, POISON_MINOR));
        TEKTOAIR = space$createFixed("TEKTOAIR",     0x245F46, 4, 2, 0, EnumSymbol.OXIDIZER, idCounter++).addTraits(GASEOUS, new FT_Poison(true, 1)).addTraits(new FT_Flammable(30_000));
        JOOLGAS = space$createFixed("JOOLGAS",      0x829F82, 0, 0, 0, EnumSymbol.ASPHYXIANT, idCounter++).addTraits(GASEOUS);
        SARNUSGAS = space$createFixed("SARNUSGAS",    0xE47D5C, 0, 0, 0, EnumSymbol.ASPHYXIANT, idCounter++).addTraits(GASEOUS);
        UGAS = space$createFixed("UGAS",         0x718C9A, 0, 0, 0, EnumSymbol.ASPHYXIANT, idCounter++).addTraits(GASEOUS);
        NGAS = space$createFixed("NGAS",         0x8A668A, 0, 0, 0, EnumSymbol.ASPHYXIANT, idCounter++).addTraits(GASEOUS);
        MILK = space$createFixed("MILK",         0xCFCFCF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);//F5DEE4
        SMILK = space$createFixed("SMILK",        0xF5DEE4, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);
        EVEAIR = space$createFixed("EVEAIR",       0xDCABF8, 4, 0, 0, EnumSymbol.OXIDIZER, idCounter++).addTraits(GASEOUS, new FT_Corrosive(25), new FT_Poison(true, 1));
        KMnO4 = space$createFixed("KMnO4",        0x560046, 4, 0, 0, EnumSymbol.ACID, idCounter++).addTraits(LIQUID, new FT_Corrosive(15), new FT_Poison(true, 1));
        CHLOROMETHANE = space$createFixed("CHLOROMETHANE",0xD3CF9E, 2, 4, 0, EnumSymbol.NONE, idCounter++).addTraits(GASEOUS, new FT_Corrosive(15)).addTraits(new FT_Flammable(50_000));
        METHANOL = space$createFixed("METHANOL",     0x88739F, 3, 4, 0, EnumSymbol.NONE, idCounter++).addTraits(GASEOUS).addTraits(new FT_Flammable(400_000)).addTraits(new FT_Combustible(FT_Combustible.FuelGrade.HIGH, 600_000), LIQUID);    //ethanol but more etha per nol
        BROMINE = space$createFixed("BROMINE",      0xAF2214, 2, 0, 1, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS, new FT_Corrosive(10));
        METHYLENE = space$createFixed("METHYLENE",    0xBBA9A0, 2, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(GASEOUS);
        POLYTHYLENE = space$createFixed("POLYTHYLENE",  0x35302E, 1, 2, 0, EnumSymbol.NONE, idCounter++).addTraits(LIQUID).addTraits(new FT_Flammable(50_000));
        HCL = space$createFixed("HCL",          0x00D452, 3, 0, 3, EnumSymbol.ACID, idCounter++).setFFNameOverride("hydrochloric_acid").addTraits(new FT_Corrosive(30), LIQUID);
        AMMONIA = space$createFixed("AMMONIA",      0x00A0F7, 2, 0, 1, EnumSymbol.ASPHYXIANT, idCounter++).addTraits(new FT_Poison(true, 4), GASEOUS);
        BLOODGAS = space$createFixed("BLOODGAS",     0x591000, 3, 1, 1, EnumSymbol.NONE, idCounter++).addContainers(new Fluids.CD_Canister(0x591000)).addTraits(new FT_Flammable(1_000_000), new FT_Combustible(FT_Combustible.FuelGrade.AERO, 2_500_000)).addTraits(LIQUID);
        MINSOL = space$createFixed("MINSOL",       0xFADF6A, 3, 0, 3, EnumSymbol.ACID, idCounter++).addTraits(new FT_Corrosive(10), LIQUID);
        NITROGEN = space$createFixed("NITROGEN",     0xB3C6D2, 1, 0, 0, EnumSymbol.CROYGENIC, idCounter++).setTemp(-90).addTraits(LIQUID, EVAP);
        EMILK = space$createFixed("EMILK",        0xCFCFCF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);//F5DEE4
        CMILK = space$createFixed("CMILK",        0xCFCFCF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);//F5DEE4
        CREAM = space$createFixed("CREAM",        0xCFCFCF, 0, 0, 0, EnumSymbol.NONE, idCounter++).addTraits(DELICIOUS, LIQUID);//F5DEE4
        DICYANOACETYLENE = space$createFixed("DICYANOACETYLENE",             0x675A9F, 1, 2, 1, EnumSymbol.NONE, idCounter++).addTraits(new FT_Flammable(4_000_000), GASEOUS);
        MORKITE = space$createFixed("MORKITE",      0x333C42, 3, 3, 3, EnumSymbol.NONE, idCounter++).addTraits(new FT_Flammable(60), LIQUID, VISCOUS);
        MORKINE = space$createFixed("MORKINE",      0x796089, 3, 3, 3, EnumSymbol.NONE, idCounter++).addTraits(new FT_Flammable(200), LIQUID, VISCOUS);
        MSLURRY = space$createFixed("MSLURRY",      0x364D47, 0, 0, 2, EnumSymbol.NONE, idCounter++).addTraits(LIQUID, VISCOUS);
        SUPERHEATED_HYDROGEN = space$createFixed("SUPERHEATED_HYDROGEN", 0xE39393, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(2200).addTraits(GASEOUS, NOCON, NOID, new FT_Rocket(900, 700_000));
        URANIUM_BROMIDE = space$createFixed("URANIUM_BROMIDE",              0xD1CEBE, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(200).addTraits(LIQUID, VISCOUS, new FT_Corrosive(65), new FT_VentRadiation(0.1F));
        PLUTONIUM_BROMIDE = space$createFixed("PLUTONIUM_BROMIDE",    0x4C4C4C, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(200).addTraits(LIQUID, VISCOUS, new FT_Corrosive(65), new FT_VentRadiation(0.1F));
        SCHRABIDIUM_BROMIDE = space$createFixed("SCHRABIDIUM_BROMIDE",  0x006B6B, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(200).addTraits(LIQUID, VISCOUS, new FT_Corrosive(65), new FT_VentRadiation(0.1F));
        THORIUM_BROMIDE = space$createFixed("THORIUM_BROMIDE",      0x7A5542, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(200).addTraits(LIQUID, VISCOUS, new FT_Corrosive(65), new FT_VentRadiation(0.1F));
        GASEOUS_URANIUM_BROMIDE = space$createFixed("GASEOUS_URANIUM_BROMIDE",           0xD1CEBE, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(2500).addTraits(GASEOUS, NOCON, NOID, new FT_Rocket(1500, 700_000));
        GASEOUS_PLUTONIUM_BROMIDE = space$createFixed("GASEOUS_PLUTONIUM_BROMIDE",     0x4C4C4C, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(2600).addTraits(GASEOUS, NOCON, NOID, new FT_Rocket(2000, 700_000));
        GASEOUS_SCHRABIDIUM_BROMIDE = space$createFixed("GASEOUS_SCHRABIDIUM_BROMIDE",   0x006B6B, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(3000).addTraits(GASEOUS, NOCON, NOID, new FT_Rocket(3000, 700_000));
        GASEOUS_THORIUM_BROMIDE = space$createFixed("GASEOUS_THORIUM_BROMIDE",           0x7A5542, 0, 0, 0, EnumSymbol.NONE, idCounter++).setTemp(2300).addTraits(GASEOUS, NOCON, NOID, new FT_Rocket(1300, 700_000));
        GASEOUS_HYDROGEN = space$createFixed("GASEOUS_HYDROGEN",     0x4286f4, 3, 4, 0, EnumSymbol.NONE, idCounter++).addTraits(new FT_Flammable(5_000), new FT_Combustible(FT_Combustible.FuelGrade.HIGH, 10_000), GASEOUS, new FT_Rocket(380, 700_000));
        GAS_WATZ = space$createFixed("GAS_WATZ",     0x86653E, 4, 0, 3, EnumSymbol.ACID, idCounter++).setTemp(2500).addTraits(GASEOUS, NOCON, NOID, new FT_Polluting().release(PollutionHandler.PollutionType.POISON, POISON_EXTREME), new FT_Rocket(1200, 700_000));
        
        metaOrder.add(EARTHAIR);
        metaOrder.add(CCL);
        metaOrder.add(CHLOROETHANE);
        metaOrder.add(CBENZ);
        metaOrder.add(VINYL);
        metaOrder.add(AQUEOUS_COPPER);
        metaOrder.add(AQUEOUS_NICKEL);
        metaOrder.add(COPPERSULFATE);
        metaOrder.add(BRINE);
        metaOrder.add(CONGLOMERA);
        metaOrder.add(HGAS);
        metaOrder.add(HALOLIGHT);
        metaOrder.add(LITHCARBONATE);
        metaOrder.add(TCRUDE);
        metaOrder.add(ELBOWGREASE);
        metaOrder.add(NMASSTETRANOL);
        metaOrder.add(NMASS);
        metaOrder.add(SCUTTERBLOOD);
        metaOrder.add(HTCO4);
        metaOrder.add(NEON);
        metaOrder.add(ARGON);
        metaOrder.add(KRYPTON);
        metaOrder.add(COFFEE);
        metaOrder.add(TEA);
        metaOrder.add(HONEY);
        metaOrder.add(OLIVEOIL);
        metaOrder.add(FLUORINE);
        metaOrder.add(DUNAAIR);
        metaOrder.add(TEKTOAIR);
        metaOrder.add(JOOLGAS);
        metaOrder.add(SARNUSGAS);
        metaOrder.add(UGAS);
        metaOrder.add(NGAS);
        metaOrder.add(MILK);
        metaOrder.add(SMILK);
        metaOrder.add(EVEAIR);
        metaOrder.add(KMnO4);
        metaOrder.add(CHLOROMETHANE);
        metaOrder.add(METHANOL);
        metaOrder.add(BROMINE);
        metaOrder.add(METHYLENE);
        metaOrder.add(POLYTHYLENE);
        metaOrder.add(HCL);
        metaOrder.add(AMMONIA);
        metaOrder.add(BLOODGAS);
        metaOrder.add(MINSOL);
        metaOrder.add(NITROGEN);
        metaOrder.add(EMILK);
        metaOrder.add(CMILK);
        metaOrder.add(CREAM);
        metaOrder.add(DICYANOACETYLENE);
        metaOrder.add(MORKITE);
        metaOrder.add(MORKINE);
        metaOrder.add(MSLURRY);
        metaOrder.add(SUPERHEATED_HYDROGEN);
        metaOrder.add(URANIUM_BROMIDE);
        metaOrder.add(PLUTONIUM_BROMIDE);
        metaOrder.add(SCHRABIDIUM_BROMIDE);
        metaOrder.add(THORIUM_BROMIDE);
        metaOrder.add(GASEOUS_URANIUM_BROMIDE);
        metaOrder.add(GASEOUS_PLUTONIUM_BROMIDE);
        metaOrder.add(GASEOUS_SCHRABIDIUM_BROMIDE);
        metaOrder.add(GASEOUS_THORIUM_BROMIDE);
        metaOrder.add(GASEOUS_HYDROGEN);
        metaOrder.add(GAS_WATZ);
    }
}
