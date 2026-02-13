package com.hbmspace.blocks;

import com.hbm.blocks.BlockBase;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.OreEnumUtil;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.lib.ModDamageSource;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.bomb.LaunchPadRocket;
import com.hbmspace.blocks.fluid.GenericFluidBlockSpace;
import com.hbmspace.blocks.generic.*;
import com.hbmspace.blocks.machine.*;
import com.hbmspace.blocks.machine.rbmk.RBMKBurner;
import com.hbmspace.util.OreEnumUtilSpace;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.hbmspace.blocks.fluid.ModFluidsSpace.ccl_fluid;

public class ModBlocksSpace {
    public static List<Block> ALL_BLOCKS = new ArrayList<>();

    public static final Block moon_rock = new BlockBaseSpace(Material.ROCK, "moon_rock").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F);
    public static final Block tumor = new BlockBaseSpace(Material.CLAY, "tumor").setSoundType(SoundType.SNOW).setCreativeTab(MainRegistry.resourceTab).setHardness(1.0F);
    public static final Block duna_sands = new BlockFallingBaseSpace(Material.SAND, "duna_sands", SoundType.SAND).setCreativeTab(MainRegistry.resourceTab).setHardness(0.5F);
    public static final Block duna_cobble = new BlockBakeBaseSpace(Material.ROCK, "duna_cobble").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F);
    public static final Block duna_rock = new BlockBaseDrop(Material.ROCK, "duna_rock", ModBlocksSpace.duna_cobble).setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F);
    public static final Block dry_ice = new BlockBaseSpace(Material.ICE,"dry_ice").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(0.5F);
    public static final Block ferric_clay = new BlockBaseSpace(Material.CLAY, "ferric_clay").setSoundType(SoundType.GROUND).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F);
    public static final Block eve_silt = new BlockFallingBaseSpace(Material.SAND, "eve_silt", SoundType.SAND).setCreativeTab(MainRegistry.resourceTab).setHardness(0.5F);
    public static final Block eve_rock = new BlockBaseSpace(Material.ROCK, "eve_rock").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F);
    public static final Block laythe_silt = new BlockFallingBaseSpace(Material.SAND, "laythe_silt", SoundType.SAND).setCreativeTab(MainRegistry.resourceTab).setHardness(0.5F);
    public static final Block ike_regolith = new BlockBaseSpace(Material.ROCK, "ike_regolith").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block ike_stone = new BlockBaseSpace(Material.ROCK, "ike_stone").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block dres_rock = new BlockBaseSpace(Material.ROCK, "dres_rock").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block moho_regolith = new BlockBaseSpace(Material.ROCK, "moho_regolith").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block moho_stone = new BlockBaseSpace(Material.ROCK, "moho_stone").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block minmus_regolith = new BlockBaseSpace(Material.ROCK, "minmus_regolith").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block minmus_stone = new BlockBaseSpace(Material.ROCK, "minmus_stone").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block minmus_smooth = new BlockBaseSpace(Material.ROCK, "minmus_smooth").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F).setResistance(10.0F);
    public static final Block block_nickel = new BlockBakeBaseSpace(Material.IRON, "block_nickel").setSoundType(SoundType.METAL).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block block_osmiridium = new BlockBakeBaseSpace(Material.IRON, "block_osmiridium").setCreativeTab(MainRegistry.blockTab).setSoundType(SoundType.METAL).setHardness(5.0F).setResistance(10.0F);
    public static final Block deco_stainless = new BlockBaseSpace(Material.IRON, "deco_stainless").setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_iron = new BlockOre("ore_iron", null, Blocks.IRON_ORE).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_gold = new BlockOre("ore_gold", null, Blocks.GOLD_ORE).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_redstone = new BlockOre("ore_redstone", OreEnumUtilSpace.SpaceOreEnum.REDSTONE, Blocks.REDSTONE_ORE, 2).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_lapis = new BlockOre("ore_lapis", OreEnumUtilSpace.SpaceOreEnum.LAPIS, Blocks.LAPIS_ORE, 2).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_emerald = new BlockOre("ore_emerald", OreEnumUtilSpace.SpaceOreEnum.EMERALD, Blocks.EMERALD_ORE, 2).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_quartz = new BlockOre("ore_quartz", null, Blocks.QUARTZ_ORE).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_diamond = new BlockOre("ore_diamond", OreEnumUtil.OreEnum.DIAMOND, Blocks.DIAMOND_ORE, 2).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_nickel = new BlockOre("ore_nickel", null, 2).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_mineral = new BlockOre("ore_mineral", null, 2).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_copper = new BlockOre("ore_copper", null, 1).setNTMAlt(ModBlocks.ore_copper).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_lithium = new BlockOre("ore_lithium", null, 0).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_shale = new BlockOre("ore_shale", null, 2).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_fire = new BlockOre("ore_fire", OreEnumUtilSpace.SpaceOreEnum.FIRE, 2).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_glowstone = new BlockOre("ore_glowstone", OreEnumUtilSpace.SpaceOreEnum.GLOWSTONE, 0).setLightLevel(4F/15F).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_schrabidium = new BlockOre("ore_schrabidium", null, 3, 300).setCreativeTab(MainRegistry.resourceTab);
    public static final Block ore_thorium = new BlockOre("ore_thorium", null, 2).setNTMAlt(ModBlocks.ore_thorium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_titanium = new BlockOre("ore_titanium", null, 2).setNTMAlt(ModBlocks.ore_titanium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_sulfur = new BlockOre("ore_sulfur", OreEnumUtil.OreEnum.SULFUR, 1).setNTMAlt(ModBlocks.ore_sulfur).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_niter = new BlockOre("ore_niter", OreEnumUtil.OreEnum.NITER, 1).setNTMAlt(ModBlocks.ore_niter).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_tungsten = new BlockOre("ore_tungsten", null, 2).setNTMAlt(ModBlocks.ore_tungsten).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_aluminium = new BlockOre("ore_aluminium", null, 1).setNTMAlt(ModBlocks.ore_aluminium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_fluorite = new BlockOre("ore_fluorite", OreEnumUtil.OreEnum.FLUORITE,  1).setNTMAlt(ModBlocks.ore_fluorite).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_zinc = new BlockOre("ore_zinc", null,  2).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_lead = new BlockOre("ore_lead", null, 2).setNTMAlt(ModBlocks.ore_lead).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_beryllium = new BlockOre("ore_beryllium", null, 2).setNTMAlt(ModBlocks.ore_beryllium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(15.0F);
    public static final Block ore_rare = new BlockOre("ore_rare", OreEnumUtil.OreEnum.RARE_EARTHS, 2, 12).setNTMAlt(ModBlocks.ore_rare).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_cobalt = new BlockOre("ore_cobalt", OreEnumUtil.OreEnum.COBALT, 3, 15).setNTMAlt(ModBlocks.ore_cobalt).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_cinnabar = new BlockOre("ore_cinnabar", OreEnumUtil.OreEnum.CINNABAR, 1).setNTMAlt(ModBlocks.ore_cinnabar).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_australium = new BlockOre("ore_australium", null, 4, 100).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_niobium = new BlockOre("ore_niobium", null, 2).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_iodine = new BlockOre("ore_iodine", null, 2).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_lanthanium = new BlockOre("ore_lanthanium", null, 2).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_oil_empty = new BlockOre("ore_oil_empty", null, 3).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_oil = new BlockOreFluid("ore_oil", ore_oil_empty, BlockOreFluid.ReserveType.OIL).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_bedrock_oil = new BlockOreFluid("ore_bedrock_oil", null, BlockOreFluid.ReserveType.OIL).setCreativeTab(MainRegistry.blockTab).setBlockUnbreakable().setResistance(1_000_000);
    public static final Block ore_gas_empty = new BlockOre("ore_gas_empty", null, 3).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_gas = new BlockOreFluid("ore_gas", ore_gas_empty, BlockOreFluid.ReserveType.GAS).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_brine_empty = new BlockOre("ore_brine_empty", null, 3).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_brine = new BlockOreFluid("ore_brine", ore_brine_empty, BlockOreFluid.ReserveType.BRINE).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_tekto_empty = new BlockOre("ore_tekto_empty", null, 3).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_tekto = new BlockOreFluid("ore_tekto", ore_tekto_empty, BlockOreFluid.ReserveType.OIL).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block cluster_iron = new BlockCluster("cluster_iron", OreEnumUtil.OreEnum.CLUSTER_IRON).setNTMAlt(ModBlocks.cluster_iron).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(35.0F);
    public static final Block cluster_titanium = new BlockCluster("cluster_titanium", OreEnumUtil.OreEnum.CLUSTER_TITANIUM).setNTMAlt(ModBlocks.cluster_titanium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(35.0F);
    public static final Block cluster_aluminium = new BlockCluster("cluster_aluminium", OreEnumUtil.OreEnum.CLUSTER_ALUMINIUM).setNTMAlt(ModBlocks.cluster_aluminium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(35.0F);
    public static final Block cluster_copper = new BlockCluster("cluster_copper", OreEnumUtil.OreEnum.CLUSTER_COPPER).setNTMAlt(ModBlocks.cluster_copper).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(35.0F);
    public static final Block ore_uranium = new BlockOreOutgas(true, 20, true, "ore_uranium").setNTMAlt(ModBlocks.ore_uranium).setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.resourceTab);
    public static final Block ore_asbestos = new BlockOreOutgas(true, 5, true, "ore_asbestos").setNTMAlt(ModBlocks.ore_asbestos).setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.resourceTab);
    public static final Block ore_plutonium = new BlockOreOutgas(true, 5, true, "ore_plutonium").setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_palladium = new BlockOre("ore_palladium", null, 2).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_arsenic = new BlockOre("ore_arsenic", null, 2).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_cadmium = new BlockOre("ore_cadmium", null, 2).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_silicon = new BlockOre("ore_silicon", null, 2).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);

    public static final Block stone_resource = new BlockEnumMetaSpace<>(Material.ROCK, SoundType.STONE, "stone_resource", BlockEnumsSpace.EnumStoneType.VALUES, true, true).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);

    public static final Block vinyl_log = new BlockLogNT("vinyl_log", "vinyl_log_side", "vinyl_log_top").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(0.5F).setResistance(2.5F);
    public static final Block pvc_log = new BlockLogNT("pvc_log", "pvc_log_side", "pvc_log_top").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(0.5F).setResistance(2.5F);
    public static final Block vinyl_planks = new BlockBakeBaseSpace(Material.WOOD, "vinyl_planks").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(1.0F).setResistance(1.0F);
    public static final Block pvc_planks = new BlockBakeBaseSpace(Material.WOOD, "pvc_planks").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(1.0F).setResistance(1.0F);

    public static final Block lattice_log = new BlockLogNT("lattice_log", "rad_log_side", "rad_log_top").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(0.5F).setResistance(2.5F);
    public static final Block bf_log = new BlockLogNT("bf_log", "bf_log_side", "bf_log_top").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(0.5F).setResistance(2.5F);
    public static final Block primed_log = new BlockLogNT("primed_log", "primed_log_side", "primed_log_top").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(0.5F).setResistance(2.5F);
    public static final Block eu_log = new BlockLogNT("eu_log", "eu_log_side", "eu_log_top").setSoundType(SoundType.WOOD).setCreativeTab(MainRegistry.blockTab).setHardness(0.5F).setResistance(2.5F);

    public static final Block flesh_block = new MeltedFlesh(Material.SNOW, "flesh_block", "flesh_block").setSoundType(SoundType.GROUND).setCreativeTab(MainRegistry.blockTab).setHardness(0.1F).setLightOpacity(0);
    public static final Block charred_flesh_block = new MeltedFlesh(Material.SNOW, "charred_flesh_block", "charred_flesh_block").setSoundType(SoundType.GROUND).setCreativeTab(MainRegistry.blockTab).setHardness(0.1F).setLightOpacity(0);
    public static final Block carbonized_flesh_block = new MeltedFlesh(Material.SNOW, "carbonized_flesh_block", "carbonized_flesh_block").setSoundType(SoundType.GROUND).setCreativeTab(MainRegistry.blockTab).setHardness(0.1F).setLightOpacity(0);

    public static final Block machine_lpw2 = new MachineLPW2("machine_lpw2").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_htr3 = new MachineHTR3("machine_htr3").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_htrf4 = new MachineHTRF4("machine_htrf4").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_htrf4neo = new MachineHTRFNeo("machine_htrf4neo").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_xenon_thruster = new MachineXenonThruster(Material.IRON, "machine_xenon_thruster").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block transporter_rocket = new BlockTransporterRocket(Material.IRON, "transporter_rocket").setHardness(1.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block orbital_station = new BlockOrbitalStation(Material.IRON, "orbital_station").setBlockUnbreakable().setResistance(Float.POSITIVE_INFINITY).setCreativeTab(null);
    public static final Block orbital_station_port = new BlockOrbitalStation(Material.IRON, "orbital_station_port").setHardness(1.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block orbital_station_launcher = new BlockOrbitalStationLauncher(Material.IRON, "orbital_station_launcher").setHardness(1.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block orbital_station_computer = new BlockOrbitalStationComputer(Material.IRON, "orbital_station_computer").setHardness(1.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_stardar = new MachineStardar(Material.IRON, "machine_stardar").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_drive_processor = new MachineDriveProcessor(Material.IRON, "machine_drive_processor").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_rocket_assembly = new MachineRocketAssembly(Material.IRON, "machine_rocket_assembly").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.missileTab);
    public static final Block launch_pad_rocket = new LaunchPadRocket(Material.IRON, "launch_pad_rocket").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.missileTab);
    public static final Block machine_vacuum_circuit = new MachineVacuumCircuit(Material.IRON, "machine_vacuum_circuit").setHardness(5.0F).setResistance(30.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_alkylation = new MachineAlkylation(Material.IRON, "machine_alkylation").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_solar = new MachineSolar(Material.IRON, "machine_solar").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block air_vent = new BlockAirPump(Material.IRON, "air_vent").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block air_scrubber = new BlockAirScrubber(Material.IRON, "air_scrubber").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    //public static final Block algae_film = new BlockAlgaeFilm(Material.IRON, "algae_film").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block hydrobay = new MachineHydroponic(Material.IRON, "hydrobay").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_cryo_distill = new MachineCryoDistill(Material.IRON, "machine_cryo_distill").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_milk_reformer = new MachineMilkReformer(Material.IRON, "machine_milk_reformer").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_magma = new MachineMagma("machine_magma").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_radiator = new MachineRadiator(Material.IRON, "machine_radiator").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_dish_controller = new MachineDishControl(Material.IRON, "dish_control").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_discharger = new MachineDischarger(Material.IRON, "machine_discharger").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_atmo_tower = new AtmoTower(Material.IRON, "machine_atmo_tower").setHardness(10.0F).setResistance(20.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_atmo_vent = new BlockAtmosphericCompressor(Material.IRON, "machine_atmo_vent").setHardness(10.0F).setResistance(20.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block gas_dock = new MachineGasDock(Material.IRON, "gas_dock").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.missileTab);
    public static final Block rbmk_burner = new RBMKBurner("rbmk_burner", "rbmk_burner").setCreativeTab(MainRegistry.machineTab);

    public static final Block dyson_launcher = new MachineDysonLauncher(Material.IRON, "dyson_launcher").setHardness(10.0F).setResistance(20.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block dyson_receiver = new MachineDysonReceiver(Material.IRON, "dyson_receiver").setHardness(10.0F).setResistance(20.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block dyson_converter_tu = new MachineDysonConverterTU(Material.IRON, "dyson_converter_tu").setHardness(10.0F).setResistance(20.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block dyson_converter_he = new MachineDysonConverterHE(Material.IRON, "dyson_converter_he").setHardness(10.0F).setResistance(20.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block dyson_converter_anatmogenesis = new MachineDysonConverterAnatmogenesis(Material.IRON, "dyson_converter_anatmogenesis").setHardness(10.0F).setResistance(20.0F).setCreativeTab(MainRegistry.machineTab);

    public static final Block crop_strawberry = new BlockCrop(Blocks.FARMLAND, (atmosphere) -> atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 0.1) || atmosphere.hasFluid(Fluids.OXYGEN, 0.1), true, "crop_strawberry", "strawberry").setSoundType(SoundType.PLANT).setHardness(0.0F);
    public static final Block crop_mint = new BlockCrop(Blocks.FARMLAND, (atmosphere) -> atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 0.1) || atmosphere.hasFluid(Fluids.OXYGEN, 0.1), true, "crop_mint", "mint").setSoundType(SoundType.PLANT).setHardness(0.0F);
    public static final Block crop_coffee = new BlockCrop(Blocks.FARMLAND, (atmosphere) -> atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 0.1) || atmosphere.hasFluid(Fluids.OXYGEN, 0.1), true, "crop_coffee", "coffee").setSoundType(SoundType.PLANT).setHardness(0.0F);
    public static final Block crop_tea = new BlockCrop(Blocks.FARMLAND, (atmosphere) -> atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 0.1) || atmosphere.hasFluid(Fluids.OXYGEN, 0.1), true, "crop_tea", "tea").setSoundType(SoundType.PLANT).setHardness(0.0F);
    public static final Block crop_paraffin = new BlockCrop(ModBlocksSpace.rubber_farmland, (atmosphere) -> atmosphere.hasFluid(com.hbmspace.inventory.fluid.Fluids.TEKTOAIR, 0.1) || atmosphere.hasFluid(Fluids.CHLORINE, 0.1), false, "crop_paraffin", "paraffin").setSoundType(SoundType.PLANT).setHardness(0.0F);
    public static final Block rubber_farmland = new BlockRubberFarm("rubber_farmland").setSoundType(SoundType.GROUND).setCreativeTab(MainRegistry.blockTab).setHardness(1.0F).setResistance(1.0F);
    public static final Block rubber_silt = new BlockBakeBaseSpace(Material.SAND, "rubber_silt").setSoundType(SoundType.GROUND).setCreativeTab(MainRegistry.blockTab).setHardness(1.0F).setResistance(1.0F);
    public static final Block ccl_block = new GenericFluidBlockSpace(ccl_fluid, Material.WATER, "ccl_block").setDamage(ModDamageSource.lead, 1F).setResistance(500.0F);
    public static final Block dummy_beam = new BlockDummyableBeam(Material.IRON, "dummy_beam").setHardness(10.0F).setResistance(20.0F);

    public static final Block furnace = new BlockFurnaceSpace(false).setHardness(3.5F).setSoundType(SoundType.STONE);
    public static final Block lit_furnace = new BlockFurnaceSpace(true).setHardness(3.5F).setSoundType(SoundType.STONE).setLightLevel(0.875F);

    public static void preInit(){
        for(Block block : ALL_BLOCKS){
            ForgeRegistries.BLOCKS.register(block);
        }
    }
}
