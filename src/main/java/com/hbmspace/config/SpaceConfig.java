package com.hbmspace.config;

import com.hbm.config.CommonConfig;
import net.minecraftforge.common.config.Configuration;

public class SpaceConfig {
    public static int dunaoilSpawn = 100;
    public static int moonDimension = 15;
    public static int dunaDimension = 16;
    public static int ikeDimension = 17;
    public static int eveDimension = 18;
    public static int dresDimension = 19;
    public static int mohoDimension = 20;
    public static int minmusDimension = 21;
    public static int laytheDimension = 22;
    public static int orbitDimension = 23;
    public static int tektoDimension = 24;
    public static int orbitBiome = 42;
    public static int minmusBiome = 40;
    public static int minmusBasins = 41;
    public static int moonBiome = 111;
    public static int dunaBiome = 112;
    public static int dunaLowlandsBiome = 113;
    public static int dunaPolarBiome = 114;
    public static int dunaHillsBiome = 115;
    public static int dunaPolarHillsBiome = 116;
    public static int eveBiome = 117;
    public static int eveMountainsBiome = 118;
    public static int eveOceanBiome = 119;
    public static int eveSeismicBiome = 125;
    public static int eveRiverBiome = 110;
    public static int dresBiome = 120;
    public static int dresBasins = 121;
    public static int mohoBiome = 122;
    public static int mohoBasaltBiome = 43;
    public static int laytheBiome = 123;
    public static int laytheOceanBiome = 124;
    public static int laythePolarBiome = 126;
    public static int ikeBiome = 127;
    public static int tektoPolyvinylBiome = 92;
    public static int tektoHalogenHillBiome = 91;
    public static int tektoRiverBiome = 90;
    public static int tektoForestBiome = 89;
    public static int tektoVinylIslandBiome = 88;

    public static int thatmoBiome = 87;
    public static boolean allowNetherPortals = false;

    public static boolean enableVolcanoGen = true;

    public static boolean crashOnBiomeConflict = true;

    public static boolean showOreLocations = true;

    public static int maxProbeDistance = 32_000;
    public static int maxStationDistance = 32_000;

    public static boolean combatPodDespawn = false;

    public SpaceConfig() {
    }

    public static void loadFromConfig(Configuration config) {
        final String CATEGORY_DIM = "17_dims";
        allowNetherPortals = CommonConfig.createConfigBool(config, CATEGORY_DIM, "17.00_allowNetherPortals", "Should Nether portals function on other celestial bodies?", false);

        moonDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.01_moonDimension", "Mun dimension ID", moonDimension);
        dunaDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.02_dunaDimension", "Duna dimension ID", dunaDimension);
        ikeDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.03_ikeDimension", "Ike dimension ID", ikeDimension);
        eveDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.04_eveDimension", "Eve dimension ID", eveDimension);
        dresDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.05_dresDimension", "Dres dimension ID", dresDimension);
        mohoDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.06_mohoDimension", "Moho dimension ID", mohoDimension);
        minmusDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.07_minmusDimension", "Minmus dimension ID", minmusDimension);
        laytheDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.08_laytheDimension", "Laythe dimension ID", laytheDimension);
        orbitDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.09_orbitDimension", "Orbital dimension ID", orbitDimension);
        tektoDimension = CommonConfig.createConfigInt(config, CATEGORY_DIM, "17.10_tektoDimension", "Tekto dimension ID", tektoDimension);

        final String CATEGORY_GENERAL = "01_general";
        maxProbeDistance = CommonConfig.createConfigInt(config, CATEGORY_GENERAL, "1.90_maxProbeDistance", "How far from the center of the dimension can probes generate landing coordinates", maxProbeDistance);
        maxStationDistance = CommonConfig.createConfigInt(config, CATEGORY_GENERAL, "1.93_maxStationDistance", "How far from the center of the dimension can orbital stations be generated", maxStationDistance);
        enableVolcanoGen = CommonConfig.createConfigBool(config, CATEGORY_GENERAL, "1.91_enableVolcanoGen", "Should volcanoes be active when spawning, disabling will prevent natural volcanoes from spewing lava and growing", enableVolcanoGen);
        crashOnBiomeConflict = CommonConfig.createConfigBool(config, CATEGORY_GENERAL, "1.92_crashOnBiomeConflict", "To avoid biome ID collisions, the game will crash if one occurs, and give instructions on how to fix. Only disable this if you know what you're doing!", crashOnBiomeConflict);
        showOreLocations = CommonConfig.createConfigBool(config, CATEGORY_GENERAL, "1.93_showOreLocations", "Should ores indicate which planets they can be found on.", showOreLocations);
        combatPodDespawn = CommonConfig.createConfigBool(config, CATEGORY_GENERAL, "1.94_combatPodDespawn", "Whether combat pods should despawn after a certian amount of time.", combatPodDespawn);

        final String CATEGORY_BIOME = "16_biomes";
        moonBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.02_moonBiome", "Mun Biome ID", moonBiome);
        dunaBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.03_dunaBiome", "Duna Biome ID", dunaBiome);
        dunaLowlandsBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.04_dunaLowlandsBiome", "Duna Lowlands Biome ID", dunaLowlandsBiome);
        dunaPolarBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.05_dunaPolarBiome", "Duna Polar Biome ID", dunaPolarBiome);
        dunaHillsBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.06_dunaHillsBiome", "Duna Hills Biome ID", dunaHillsBiome);
        dunaPolarHillsBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.07_dunaPolarHillsBiome", "Duna Polar Hills Biome ID", dunaPolarHillsBiome);
        eveBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.08_eveBiome", "Eve Biome ID", eveBiome);
        eveMountainsBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.09_eveMountainsBiome", "Eve Mountains Biome ID", eveMountainsBiome);
        eveOceanBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.10_eveOceanBiome", "Eve Ocean Biome ID", eveOceanBiome);
        eveSeismicBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.12_eveSeismicBiome", "Eve Seismic Biome ID", eveSeismicBiome);
        eveRiverBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.24_eveRiverBiome", "Eve River Biome ID", eveRiverBiome);
        ikeBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.13_ikeBiome", "Ike Biome ID", ikeBiome);
        laytheBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.14_laytheBiome", "Laythe Biome ID", laytheBiome);
        laytheOceanBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.15_laytheOceanBiome", "Laythe Ocean Biome ID", laytheOceanBiome);
        laythePolarBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.16_laythePolarBiome", "Laythe Polar Biome ID", laythePolarBiome);
        minmusBasins = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.17_minmusBasinsBiome", "Minmus Basins Biome ID", minmusBasins);
        minmusBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.18_minmusBiome", "Minmus Biome ID", minmusBiome);
        mohoBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.19_mohoBiome", "Moho Biome ID", mohoBiome);
        dresBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.20_dresBiome", "Dres Biome ID", dresBiome);
        dresBasins = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.21_dresBasinsBiome", "Dres Basins Biome ID", dresBasins);
        mohoBasaltBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.22_mohoBasaltBiome", "Moho Basalt Biome ID", mohoBasaltBiome);
        orbitBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.23_orbitBiome", "Space Biome ID", orbitBiome);
        tektoPolyvinylBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.27_tektoPolyvinylBiome", "Tekto Polyvinyl Biome ID", tektoPolyvinylBiome);
        tektoHalogenHillBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.28_tektoHalogenHillBiome", "Tekto Halogen Hills Biome ID", tektoHalogenHillBiome);
        tektoRiverBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.29_tektoRiverBiome", "Tekto River Biome ID", tektoRiverBiome);
        tektoForestBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.30_tektoForestBiome", "Tekto Forest Biome ID", tektoForestBiome);
        tektoVinylIslandBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.31_tektoVinylSandsBiome", "Tekto Vinyl Sands Biome ID", tektoVinylIslandBiome);
        thatmoBiome = CommonConfig.createConfigInt(config, CATEGORY_BIOME, "16.32_thatmoBiome", "Thatmo Biome ID", thatmoBiome);
    }
}
