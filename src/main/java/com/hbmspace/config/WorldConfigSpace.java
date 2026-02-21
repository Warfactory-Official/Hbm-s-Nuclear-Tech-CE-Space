package com.hbmspace.config;

import com.hbm.config.CommonConfig;
import net.minecraftforge.common.config.Configuration;

public class WorldConfigSpace {

    public static int ironSpawn = 9;

    public static int nickelSpawn = 9;
    public static int zincSpawn = 8;
    public static int mineralSpawn = 5;

    public static int bedrockOilPerDeposit = 100;
    public static int bedrockGasPerDepositMin = 10;
    public static int bedrockGasPerDepositMax = 50;

    // Space oils and ores
    public static int dunaOilSpawn = 100;
    public static int eveGasSpawn = 100;
    public static int laytheOilSpawn = 100;
    public static int munBrineSpawn = 100;
    public static int minmusBrineSpawn = 100;
    public static int ikeBrineSpawn = 100;

    public static int earthOilPerDeposit = 500;
    public static int earthGasPerDepositMin = 100;
    public static int earthGasPerDepositMax = 500;
    public static double earthOilDrainChance = 0.05D;

    public static int dunaOilPerDeposit = 200;
    public static int dunaGasPerDepositMin = 100;
    public static int dunaGasPerDepositMax = 500;
    public static double dunaOilDrainChance = 0.1D;

    public static int laytheOilPerDeposit = 500;
    public static int laytheGasPerDepositMin = 100;
    public static int laytheGasPerDepositMax = 500;
    public static double laytheOilDrainChance = 0.05D;

    public static int tektoOilSpawn = 100;
    public static int tektoOilPerDeposit = 500;
    public static int tektoGasPerDepositMin = 100;
    public static int tektoGasPerDepositMax = 500;
    public static double tektoOilDrainChance = 0.05D;

    public static int tektoBedrockOilSpawn = 200;
    public static int tektoBedrockOilPerDeposit = 100;
    public static int tektoBedrockGasPerDepositMin = 10;
    public static int tektoBedrockGasPerDepositMax = 50;

    public static int eveGasPerDeposit = 500;
    public static int evePetPerDepositMin = 20;
    public static int evePetPerDepositMax = 100;
    public static double eveGasDrainChance = 0.05D;

    public static int munBrinePerDeposit = 300;
    public static double munBrineDrainChance = 0.05D;

    public static int minmusBrinePerDeposit = 300;
    public static double minmusBrineDrainChance = 0.05D;

    public static int ikeBrinePerDeposit = 300;
    public static double ikeBrineDrainChance = 0.05D;

    public static void loadFromConfig(Configuration config) {

        final String CATEGORY_OREGEN = CommonConfig.CATEGORY_ORES;

        nickelSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.24_nickelSpawnrate", "Amount of nickel ore veins per chunk", 12);
        zincSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.25_zincSpawnrate", "Amount of zinc ore veins per chunk", 8);
        mineralSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.26_mineralSpawnrate", "Amount of mineral ore veins per chunk", 5);
        dunaOilSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.27S_oilSpawnRate", "Spawns an oil bubble every nTH chunk (on Duna)", 100);
        laytheOilSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.28S_oilSpawnRate", "Spawns a DS oil bubble every nTH chunk (on Laythe)", 100);
        eveGasSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.29S_gasSpawnRate", "Spawns a natural gas bubble every nTH chunk (on Eve)", 100);
        munBrineSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.30S_brineSpawnRate", "Spawns a brine bubble every nTH chunk (on Mun)", 100);
        minmusBrineSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.31S_brineSpawnRate", "Spawns a brine bubble every nTH chunk (on Minmus)", 100);
        ikeBrineSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.32S_brineSpawnRate", "Spawns a brine bubble every nTH chunk (on Ike)", 100);

        bedrockOilPerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O00_bedrockOilPerDeposit", "Oil extracted per bedrock oil block suck", bedrockOilPerDeposit);
        bedrockGasPerDepositMin = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O01_bedrockGasPerDepositMin", "Minimum natural gas extracted per bedrock oil block suck", bedrockGasPerDepositMin);
        bedrockGasPerDepositMax = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O02_bedrockGasPerDepositMax", "Maximum natural gas extracted per bedrock oil block suck", bedrockGasPerDepositMax);

        earthOilPerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O03_earthOilPerDeposit", "Oil extracted per Earth oil block suck", earthOilPerDeposit);
        earthGasPerDepositMin = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O04_earthGasPerDepositMin", "Minimum natural gas extracted per Earth oil block suck", earthGasPerDepositMin);
        earthGasPerDepositMax = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O05_earthGasPerDepositMax", "Maximum natural gas extracted per Earth oil block suck", earthGasPerDepositMax);
        earthOilDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O06_earthOilDrainChance", "Chance for an Earth oil block to become empty on suck", earthOilDrainChance);

        dunaOilPerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O07_dunaOilPerDeposit", "Oil extracted per Duna oil block suck", dunaOilPerDeposit);
        dunaGasPerDepositMin = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O08_dunaGasPerDepositMin", "Minimum natural gas extracted per Duna oil block suck", dunaGasPerDepositMin);
        dunaGasPerDepositMax = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O09_dunaGasPerDepositMax", "Maximum natural gas extracted per Duna oil block suck", dunaGasPerDepositMax);
        dunaOilDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O10_dunaOilDrainChance", "Chance for a Duna oil block to become empty on suck", dunaOilDrainChance);

        laytheOilPerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O11_laytheOilPerDeposit", "Desulfurized Oil extracted per Laythe oil block suck", laytheOilPerDeposit);
        laytheGasPerDepositMin = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O12_laytheGasPerDepositMin", "Minimum natural gas extracted per Laythe oil block suck", laytheGasPerDepositMin);
        laytheGasPerDepositMax = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O13_laytheGasPerDepositMax", "Maximum natural gas extracted per Laythe oil block suck", laytheGasPerDepositMax);
        laytheOilDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O14_laytheOilDrainChance", "Chance for a Laythe oil block to become empty on suck", laytheOilDrainChance);

        eveGasPerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O15_eveGasPerDeposit", "Natural Gas extracted per Eve gas block suck", eveGasPerDeposit);
        evePetPerDepositMin = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O16_evePetPerDepositMin", "Minimum petroleum gas extracted per Eve oil block suck", evePetPerDepositMin);
        evePetPerDepositMax = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O17_evePetPerDepositMax", "Maximum petroleum gas extracted per Eve oil block suck", evePetPerDepositMax);
        eveGasDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O18_eveGasDrainChance", "Chance for an Eve gas block to become empty on suck", eveGasDrainChance);

        munBrinePerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O19_munBrinePerDeposit", "Brine extracted per Mun brine block suck", munBrinePerDeposit);
        munBrineDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O20_munBrineDrainChance", "Chance for an Mun brine block to become empty on suck", munBrineDrainChance);

        minmusBrinePerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O21_minmusBrinePerDeposit", "Brine extracted per Minmus brine block suck", minmusBrinePerDeposit);
        minmusBrineDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O22_minmusBrineDrainChance", "Chance for an Minmus brine block to become empty on suck", minmusBrineDrainChance);

        ikeBrinePerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O23_ikeBrinePerDeposit", "Brine extracted per Ike brine block suck", ikeBrinePerDeposit);
        ikeBrineDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O24_ikeBrineDrainChance", "Chance for an Ike brine block to become empty on suck", ikeBrineDrainChance);

        tektoOilSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.33S_tektoOilSpawnRate", "Spawns a Tekto oil bubble every nTH chunk (on Tekto)", tektoOilSpawn);
        tektoOilPerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O25_tektoOilPerDeposit", "Oil extracted per Tekto oil block suck", tektoOilPerDeposit);
        tektoGasPerDepositMin = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O26_tektoGasPerDepositMin", "Minimum natural gas extracted per Tekto oil block suck", tektoGasPerDepositMin);
        tektoGasPerDepositMax = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O27_tektoGasPerDepositMax", "Maximum natural gas extracted per Tekto oil block suck", tektoGasPerDepositMax);
        tektoOilDrainChance = CommonConfig.createConfigDouble(config, CATEGORY_OREGEN, "2.O28_tektoOilDrainChance", "Chance for a Tekto oil block to become empty on suck", tektoOilDrainChance);


        tektoBedrockOilSpawn = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.34_tektoBedrockOilSpawnRate", "Spawns a Tekto bedrock oil bubble every nTH chunk (on Tekto)", tektoBedrockOilSpawn);
        tektoBedrockOilPerDeposit = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O35_tektoBedrockOilPerDeposit", "Oil extracted per bedrock oil block suck", tektoBedrockOilPerDeposit);
        tektoBedrockGasPerDepositMin = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O36_tektoBedrockGasPerDepositMin", "Minimum natural gas extracted per bedrock oil block suck", tektoBedrockGasPerDepositMin);
        tektoBedrockGasPerDepositMax = CommonConfig.createConfigInt(config, CATEGORY_OREGEN, "2.O37_tektoBedrockGasPerDepositMax", "Maximum natural gas extracted per bedrock oil block suck", tektoBedrockGasPerDepositMax);
    }
}
