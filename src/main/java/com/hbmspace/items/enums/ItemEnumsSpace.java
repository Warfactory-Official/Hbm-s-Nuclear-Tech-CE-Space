package com.hbmspace.items.enums;

public class ItemEnumsSpace {

    public enum EnumCircuitType {
        GAAS,
        AERO,
        AVIONICS,
        CAPACITOR_LANTHANIUM,
        PROCESST1,
        PROCESST2,
        PROCESST3,
        GASCHIP,
        HFCHIP,
        MOLYCHIP;

        public static final EnumCircuitType[] VALUES = values();
    }

    public enum EnumExpensiveType {
        STAINLESS_BEAM,
        AVIONICS,
        ASTRO;

        public static final EnumExpensiveType[] VALUES = values();
    }

    public static enum EnumChunkType {
        PENTLANDITE,
        MOONSTONE;

        public static final EnumChunkType[] VALUES = values();
    }
}
