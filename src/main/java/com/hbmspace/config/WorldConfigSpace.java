package com.hbmspace.config;

import com.hbm.config.CommonConfig;
import net.minecraftforge.common.config.Configuration;

public class WorldConfigSpace {

    public static int nickelSpawn = 9;
    public static int zincSpawn = 8;

    public static void loadFromConfig(Configuration config) {

        final String CATEGORY_OREGEN = CommonConfig.CATEGORY_ORES;

        nickelSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.24_nickelSpawnrate", "Amount of nickel ore veins per chunk", 12);
        zincSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.25_zincSpawnrate", "Amount of zinc ore veins per chunk", 8);
    }
}
