package com.hbmspace.lib;

import com.hbmspace.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class HBMSpaceSoundHandler {
    public static List<SoundEvent> ALL_SOUNDS = new ArrayList<>();

    public static SoundEvent plssBreathing;
    public static SoundEvent rocketStage;
    public static SoundEvent spinCharge;
    public static SoundEvent spinShot;
    public static SoundEvent fireFlash;
    public static SoundEvent dysonBeam;
    public static SoundEvent stationHum;

    public static void init() {
        plssBreathing = register("player.plss_breathing");
        rocketStage = register("entity.rocketStage");
        spinCharge = register("misc.spincharge");
        spinShot = register("misc.spinshot");
        fireFlash = register("misc.fireflash");
        dysonBeam = register("block.dysonBeam");
        stationHum = register("misc.stationhum");
    }

    public static SoundEvent register(String name) {
        SoundEvent e = new SoundEvent(new ResourceLocation(Tags.MODID, name));
        e.setRegistryName(name);
        ALL_SOUNDS.add(e);
        return e;
    }
}
