package com.hbmspace.items;

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

    public static enum EnumChunkType {
        PENTLANDITE,
        MOONSTONE;

        public static final EnumChunkType[] VALUES = values();
    }
}
