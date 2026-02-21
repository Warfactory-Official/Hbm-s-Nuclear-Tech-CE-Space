package com.hbmspace.enums;

import com.hbm.inventory.material.NTMMaterial;
import com.hbm.items.special.ItemBedrockOreNew;
import com.hbm.items.special.ItemBedrockOreNew.BedrockOreOutput;
import com.hbm.items.special.ItemBedrockOreNew.BedrockOreType;

import java.util.*;

import static com.hbm.inventory.material.Mats.*;
import static com.hbm.inventory.material.Mats.MAT_GHIORSIUM;
import static com.hbmspace.inventory.materials.MatsSpace.*;

public class EnumAddonBedrockOreTypes {

    public static final Map<BedrockOreType, String> BODY_MAP = new HashMap<>();
    // Add a field to store all types (both deleted base ones + new ones) to properly resolve them by ordinal
    public static BedrockOreType[] ALL_TYPES;

    private static final Class<?>[] PARAM_TYPES = new Class<?>[] {
            int.class, int.class, String.class,
            BedrockOreOutput.class, BedrockOreOutput.class,
            BedrockOreOutput.class, BedrockOreOutput.class, BedrockOreOutput.class,
            BedrockOreOutput.class, BedrockOreOutput.class, BedrockOreOutput.class,
            BedrockOreOutput.class, BedrockOreOutput.class, BedrockOreOutput.class
    };

    public static void init() {
        register(
                "KERBIN",
                T("light",      o(MAT_IRON, 18),            o(MAT_COPPER, 9),           o(MAT_CRYOLITE, 6),         o(MAT_SODIUM, 3)),
                T("heavy",      o(MAT_TUNGSTEN, 18),        o(MAT_TUNGSTEN, 9),         o(MAT_TUNGSTEN, 6),         o(MAT_ZINC, 3)),
                T("nonmetal",   o(MAT_COAL, 18),            o(MAT_LIGNITE, 9),          o(MAT_SULFUR, 6),           o(MAT_KNO, 3)),
                T("crystal",    o(MAT_REDSTONE, 18),        o(MAT_ASBESTOS, 9),         o(MAT_DIAMOND, 6),          o(MAT_EMERALD, 3))
        );

        register(
                "MUN",
                T("light",      o(MAT_LITHIUM, 18),         o(MAT_IRON, 9),             o(MAT_SODIUM, 6),           o(MAT_CHLOROCALCITE, 3)),
                T("heavy",      o(MAT_LEAD, 18),            o(MAT_ZINC, 9),             o(MAT_GOLD, 6),             o(MAT_BISMUTH, 3)),
                T("rare",       o(MAT_COBALT, 18),          o(MAT_RAREEARTH, 9),        o(MAT_NEODYMIUM, 6),        o(MAT_STRONTIUM, 3)),
                T("nonmetal",   o(MAT_SULFUR, 18),          o(MAT_FLUORITE, 9),         o(MAT_KNO, 6),              o(MAT_SILICON, 3)),
                T("crystal",    o(MAT_QUARTZ, 18),          o(MAT_SODALITE, 9),         o(MAT_EMERALD, 6),          o(MAT_CINNABAR, 3))
        );

        register(
                "MINMUS",
                T("light",      o(MAT_COPPER, 18),          o(MAT_TITANIUM, 9),         o(MAT_CHLOROCALCITE, 6),    o(MAT_COPPER, 3)),
                T("heavy",      o(MAT_LEAD, 18),            o(MAT_GOLD, 9),             o(MAT_TUNGSTEN, 6),         o(MAT_BISMUTH, 3)),
                T("rare",       o(MAT_ZIRCONIUM, 18),       o(MAT_BORON, 9),            o(MAT_COBALT, 6),           o(MAT_STRONTIUM, 3)),
                T("nonmetal",   o(MAT_SULFUR, 18),          o(MAT_KNO, 9),              o(MAT_FLUORITE, 6),         o(MAT_SILICON, 3)),
                T("crystal",    o(MAT_EMERALD, 18),         o(MAT_SODALITE, 9),         o(MAT_DIAMOND, 6),          o(MAT_EMERALD, 3))
        );

        register(
                "DUNA",
                T("light",      o(MAT_IRON, 18),            o(MAT_NICKEL, 9),           o(MAT_TITANIUM, 6),         o(MAT_CHLOROCALCITE, 3)),
                T("heavy",      o(MAT_BERYLLIUM, 18),       o(MAT_TUNGSTEN, 9),         o(MAT_ZINC, 6),             o(MAT_BISMUTH, 3)),
                T("rare",       o(MAT_RAREEARTH, 18),       o(MAT_BORON, 9),            o(MAT_ZIRCONIUM, 6),        o(MAT_STRONTIUM, 3)),
                T("actinide",   o(MAT_THORIUM, 18),         o(MAT_RADIUM, 9),           o(MAT_POLONIUM, 6),         o(MAT_U233, 3)),
                T("nonmetal",   o(MAT_FLUORITE, 18),        o(MAT_SULFUR, 9),           o(MAT_SILICON, 6),          o(MAT_PHOSPHORUS, 3)),
                T("crystal",    o(MAT_REDSTONE, 18),        o(MAT_CINNABAR, 9),         o(MAT_DIAMOND, 6),          o(MAT_MOLYSITE, 3))
        );

        register(
                "MOHO",
                T("light",      o(MAT_TITANIUM, 18),        o(MAT_CHLOROCALCITE, 9),    o(MAT_NICKEL, 6),           o(MAT_LITHIUM, 3)),
                T("heavy",      o(MAT_GOLD, 18),            o(MAT_ZINC, 9),             o(MAT_LEAD, 6),             o(MAT_BISMUTH, 3)),
                T("rare",       o(MAT_NEODYMIUM, 18),       o(MAT_ZIRCONIUM, 9),        o(MAT_BROMINE, 6),          o(MAT_STRONTIUM, 3)),
                T("actinide",   o(MAT_AUSTRALIUM, 18),      o(MAT_AUSTRALIUM, 9),       o(MAT_TASMANITE, 6),        o(MAT_AYERITE, 3)),
                T("nonmetal",   o(MAT_GLOWSTONE, 18),       o(MAT_PHOSPHORUS, 9),       o(MAT_SULFUR, 6),           o(MAT_PHOSPHORUS_W, 3)),
                T("crystal",    o(MAT_CINNABAR, 18),        o(MAT_REDSTONE, 9),         o(MAT_QUARTZ, 6),           o(MAT_MOLYSITE, 3))
        );

        register(
                "DRES",
                T("light",      o(MAT_NICKEL, 18),          o(MAT_TITANIUM, 9),         o(MAT_CADMIUM, 6),          o(MAT_GALLIUM, 3)),
                T("heavy",      o(MAT_ZINC, 18),            o(MAT_GOLD, 9),             o(MAT_BISMUTH, 6),          o(MAT_ARSENIC, 3)),
                T("rare",       o(MAT_TANTALIUM, 18),       o(MAT_LANTHANIUM, 9),       o(MAT_NIOBIUM, 6),          o(MAT_STRONTIUM, 3)),
                T("actinide",   o(MAT_URANIUM, 18),         o(MAT_RADIUM, 9),           o(MAT_TECHNETIUM, 6),       o(MAT_U238, 3)),
                T("nonmetal",   o(MAT_SILICON, 18),         o(MAT_SILICON, 9),          o(MAT_FLUORITE, 6),         o(MAT_FLUORITE, 3)),
                T("crystal",    o(MAT_DIAMOND, 18),         o(MAT_BORAX, 9),            o(MAT_MOLYSITE, 6),         o(MAT_MOLYSITE, 3))
        );

        register(
                "EVE",
                T("light",      o(MAT_SODIUM, 18),          o(MAT_CHLOROCALCITE, 9),    o(MAT_IRON, 6),             o(MAT_CO60, 3)),
                T("heavy",      o(MAT_TUNGSTEN, 18),        o(MAT_LEAD, 9),             o(MAT_ARSENIC, 6),          o(MAT_PB209, 3)),
                T("rare",       o(MAT_NIOBIUM, 18),         o(MAT_STRONTIUM, 9),        o(MAT_IODINE, 6),           o(MAT_AU198, 3)),
                T("actinide",   o(MAT_PLUTONIUM, 18),       o(MAT_POLONIUM, 9),         o(MAT_NEPTUNIUM, 6),        o(MAT_PU239, 3)),
                T("schrabidic", o(MAT_SCHRABIDIUM, 18),     o(MAT_SOLINIUM, 9),         o(MAT_GHIORSIUM, 6),        o(MAT_SCHRABIDIUM, 3)),
                T("crystal",    o(MAT_SODALITE, 18),        o(MAT_MOLYSITE, 9),         o(MAT_DIAMOND, 6),          o(MAT_BORAX, 3))
        );

        register(
                "IKE",
                T("light",      o(MAT_COPPER, 18),          o(MAT_BAUXITE, 9),          o(MAT_NICKEL, 6),           o(MAT_SODIUM, 3)),
                T("heavy",      o(MAT_LEAD, 18),            o(MAT_ZINC, 9),             o(MAT_GOLD, 6),             o(MAT_ARSENIC, 3)),
                T("rare",       o(MAT_BORON, 18),           o(MAT_NEODYMIUM, 9),        o(MAT_STRONTIUM, 6),        o(MAT_LANTHANIUM, 3)),
                T("hazard",     o(MAT_URANIUM, 18),         o(MAT_U238, 9),             o(MAT_PLUTONIUM, 6),        o(MAT_TECHNETIUM, 3))
        );

        register(
                "LAYTHE",
                T("light",      o(MAT_ALUMINIUM, 18),       o(MAT_TITANIUM, 9),         o(MAT_GALLIUM, 6),          o(MAT_HAFNIUM, 3)),
                T("heavy",      o(MAT_BERYLLIUM, 18),       o(MAT_TUNGSTEN, 9),         o(MAT_LEAD, 6),             o(MAT_ARSENIC, 3)),
                T("rare",       o(MAT_RAREEARTH, 18),       o(MAT_NEODYMIUM, 9),        o(MAT_STRONTIUM, 6),        o(MAT_NIOBIUM, 3)),
                T("actinide",   o(MAT_URANIUM, 18),         o(MAT_THORIUM, 9),          o(MAT_POLONIUM, 6),         o(MAT_U235, 3)),
                T("nonmetal",   o(MAT_CHLOROCALCITE, 18),   o(MAT_COAL, 9),             o(MAT_FLUORITE, 6),         o(MAT_SILICON, 3)),
                T("crystal",    o(MAT_ASBESTOS, 18),        o(MAT_SODALITE, 9),         o(MAT_DIAMOND, 6),          o(MAT_SODALITE, 3))
        );

        register(
                "TEKTO",
                T("light",      o(MAT_TITANIUM, 18),        o(MAT_COPPER, 9),           o(MAT_NICKEL, 6),           o(MAT_LITHIUM, 3)),
                T("heavy",      o(MAT_BERYLLIUM, 18),       o(MAT_LEAD, 9),             o(MAT_ZINC, 6),             o(MAT_ZINC, 3)),
                T("rare",       o(MAT_BORON, 18),           o(MAT_RAREEARTH, 9),        o(MAT_TANTALIUM, 6),        o(MAT_BISMUTH, 3)),
                T("actinide",   o(MAT_URANIUM, 18),         o(MAT_NEPTUNIUM, 9),        o(MAT_RADIUM, 6),           o(MAT_TECHNETIUM, 3)),
                T("crystal",    o(MAT_EMERALD, 18),         o(MAT_SILICON, 9),          o(MAT_MOLYSITE, 6),         o(MAT_BORAX, 3)),
                T("plastic",    o(MAT_POLYMER, 18),         o(MAT_RUBBER, 9),           o(MAT_SEMTEX, 6),           o(MAT_PVC, 3))
        );

        ALL_TYPES = BedrockOreType.class.getEnumConstants();
        List<BedrockOreType> spaceTypes = new ArrayList<>();

        for (BedrockOreType type : ALL_TYPES) {
            if (BODY_MAP.containsKey(type)) {
                spaceTypes.add(type);
            }
        }

        // Overrides the standard VALUES array with only the Space ones.
        // SubItems, Model Generation, Sprites, and Recipes will automatically ignore the base types!
        EnumAddonTypes.setStaticField(BedrockOreType.class, "VALUES", spaceTypes.toArray(new BedrockOreType[0]));
    }

    private static BedrockOreOutput o(NTMMaterial mat, int amount) {
        return ItemBedrockOreNew.o(mat, amount);
    }

    private static void register(String bodyName, TypeData... types) {
        for (TypeData data : types) {
            String enumName = bodyName + "_" + data.suffix.toUpperCase(Locale.US);
            int light = getAverageColor(false, data.primary, data.byproductAcid, data.byproductSolvent, data.byproductRad);
            int dark = getAverageColor(true, data.primary, data.byproductAcid, data.byproductSolvent, data.byproductRad);

            BedrockOreType registeredType = EnumAddonTypes.addEnum(
                    BedrockOreType.class, enumName, PARAM_TYPES,
                    light, dark, data.suffix,
                    data.primary, data.primary,
                    data.byproductAcid, data.byproductAcid, data.byproductAcid,
                    data.byproductSolvent, data.byproductSolvent, data.byproductSolvent,
                    data.byproductRad, data.byproductRad, data.byproductRad
            );

            BODY_MAP.put(registeredType, bodyName);
        }
    }

    private static int getAverageColor(boolean dark, BedrockOreOutput... outputs) {
        int r = 0, g = 0, b = 0;
        int count = 0;

        for (BedrockOreOutput output : outputs) {
            if (output == null || output.mat == null) continue;
            int color = dark ? output.mat.solidColorDark : output.mat.solidColorLight;
            r += (color >> 16) & 255;
            g += (color >> 8) & 255;
            b += color & 255;
            count++;
        }

        if (count == 0) return 0xFFFFFF;

        r /= count;
        g /= count;
        b /= count;

        return (r << 16) | (g << 8) | b;
    }

    private static class TypeData {
        String suffix;
        BedrockOreOutput primary;
        BedrockOreOutput byproductAcid;
        BedrockOreOutput byproductSolvent;
        BedrockOreOutput byproductRad;

        TypeData(String suffix, BedrockOreOutput primary, BedrockOreOutput byproductAcid, BedrockOreOutput byproductSolvent, BedrockOreOutput byproductRad) {
            this.suffix = suffix;
            this.primary = primary;
            this.byproductAcid = byproductAcid;
            this.byproductSolvent = byproductSolvent;
            this.byproductRad = byproductRad;
        }
    }

    private static TypeData T(String suffix, BedrockOreOutput primary, BedrockOreOutput byproductAcid, BedrockOreOutput byproductSolvent, BedrockOreOutput byproductRad) {
        return new TypeData(suffix, primary, byproductAcid, byproductSolvent, byproductRad);
    }
}