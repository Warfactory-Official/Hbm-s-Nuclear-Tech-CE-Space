package com.hbmspace.blocks;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.OreEnumUtil;
import com.hbm.lib.RefStrings;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.bomb.LaunchPadRocket;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.blocks.machine.*;
import com.hbmspace.util.OreEnumUtilSpace;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModBlocksSpace {
    public static List<Block> ALL_BLOCKS = new ArrayList<>();

    public static final Block moon_rock = new BlockBaseSpace(Material.ROCK, "moon_rock").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F);
    public static final Block tumor = new BlockBaseSpace(Material.CLAY, "tumor").setSoundType(SoundType.SNOW).setCreativeTab(MainRegistry.resourceTab).setHardness(1.0F);
    public static final Block duna_sands = new BlockFallingBaseSpace(Material.SAND, "duna_sands", SoundType.SAND).setCreativeTab(MainRegistry.resourceTab).setHardness(0.5F);
    public static final Block duna_rock = new BlockBaseSpace(Material.ROCK, "duna_rock").setSoundType(SoundType.STONE).setCreativeTab(MainRegistry.resourceTab).setHardness(1.5F);
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
    public static final Block ore_iron = new BlockOre("ore_iron", null, Blocks.IRON_ORE).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_gold = new BlockOre("ore_gold", null, Blocks.GOLD_ORE).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_redstone = new BlockOre("ore_redstone", OreEnumUtilSpace.SpaceOreEnum.REDSTONE, Blocks.REDSTONE_ORE).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_diamond = new BlockOre("ore_diamond", OreEnumUtil.OreEnum.DIAMOND, Blocks.DIAMOND_ORE).setCreativeTab(MainRegistry.blockTab).setHardness(3.0F).setResistance(5.0F);
    public static final Block ore_nickel = new BlockOre("ore_nickel", null, 2).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_copper = new BlockOre("ore_copper", null, 1).setNTMAlt(ModBlocks.ore_copper).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_schrabidium = new BlockOre("ore_schrabidium", null, 3, 300).setNTMAlt(ModBlocks.ore_schrabidium).setHardness(15.0F).setResistance(600.0F).setCreativeTab(MainRegistry.resourceTab);
    public static final Block ore_thorium = new BlockOre("ore_thorium", null, 2).setNTMAlt(ModBlocks.ore_thorium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_titanium = new BlockOre("ore_titanium", null, 2).setNTMAlt(ModBlocks.ore_titanium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_sulfur = new BlockOre("ore_sulfur", OreEnumUtil.OreEnum.SULFUR, 1).setNTMAlt(ModBlocks.ore_sulfur).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_niter = new BlockOre("ore_niter", OreEnumUtil.OreEnum.NITER, 1).setNTMAlt(ModBlocks.ore_niter).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_tungsten = new BlockOre("ore_tungsten", null, 2).setNTMAlt(ModBlocks.ore_tungsten).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_aluminium = new BlockOre("ore_aluminium", null, 1).setNTMAlt(ModBlocks.ore_aluminium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_fluorite = new BlockOre("ore_fluorite", OreEnumUtil.OreEnum.FLUORITE,  1).setNTMAlt(ModBlocks.ore_fluorite).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_lead = new BlockOre("ore_lead", null, 2).setNTMAlt(ModBlocks.ore_lead).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_beryllium = new BlockOre("ore_beryllium", null, 2).setNTMAlt(ModBlocks.ore_beryllium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(15.0F);
    public static final Block ore_rare = new BlockOre("ore_rare", OreEnumUtil.OreEnum.RARE_EARTHS, 2, 12).setNTMAlt(ModBlocks.ore_rare).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_cobalt = new BlockOre("ore_cobalt", OreEnumUtil.OreEnum.COBALT, 3, 15).setNTMAlt(ModBlocks.ore_cobalt).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_cinnabar = new BlockOre("ore_cinnabar", OreEnumUtil.OreEnum.CINNABAR, 1).setNTMAlt(ModBlocks.ore_cinnabar).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_reiium = new BlockOre("ore_reiium", null, 4, 100).setNTMAlt(ModBlocks.ore_reiium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_weidanium = new BlockOre("ore_weidanium", null, 4, 100).setNTMAlt(ModBlocks.ore_weidanium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_australium = new BlockOre("ore_australium", null, 4, 100).setNTMAlt(ModBlocks.ore_australium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_verticium = new BlockOre("ore_verticium", null, 4, 100).setNTMAlt(ModBlocks.ore_verticium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_unobtainium = new BlockOre("ore_unobtainium", null, 4, 100).setNTMAlt(ModBlocks.ore_unobtainium).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_daffergon = new BlockOre("ore_daffergon", null, 4, 100).setNTMAlt(ModBlocks.ore_daffergon).setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_gas = new BlockOre("ore_gas", null, 3).setCreativeTab(MainRegistry.resourceTab).setBlockUnbreakable().setHardness(5.0F).setResistance(10.0F);
    public static final Block ore_gas_empty = new BlockBaseSpace(Material.ROCK, "ore_gas_empty").setCreativeTab(MainRegistry.resourceTab).setHardness(5.0F).setResistance(10.0F);

    public static final Block machine_lpw2 = new MachineLPW2("machine_lpw2").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_htr3 = new MachineHTR3("machine_htr3").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_htrf4 = new MachineHTRF4("machine_htrf4").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_xenon_thruster = new MachineXenonThruster(Material.IRON, "machine_xenon_thruster").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block transporter_rocket = new BlockTransporterRocket(Material.IRON, "transporter_rocket").setHardness(1.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block orbital_station = new BlockOrbitalStation(Material.IRON, "orbital_station").setBlockUnbreakable().setResistance(Float.POSITIVE_INFINITY).setCreativeTab(null);
    public static final Block orbital_station_port = new BlockOrbitalStation(Material.IRON, "orbital_station_port").setHardness(1.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block orbital_station_computer = new BlockOrbitalStationComputer(Material.IRON, "orbital_station_computer").setHardness(1.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_stardar = new MachineStardar(Material.IRON, "machine_stardar").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_drive_processor = new MachineDriveProcessor(Material.IRON, "machine_drive_processor").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.machineTab);
    public static final Block machine_rocket_assembly = new MachineRocketAssembly(Material.IRON, "machine_rocket_assembly").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.missileTab);
    public static final Block launch_pad_rocket = new LaunchPadRocket(Material.IRON, "launch_pad_rocket").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.missileTab);
    public static final Block machine_vacuum_circuit = new MachineVacuumCircuit(Material.IRON, "machine_vacuum_circuit").setHardness(5.0F).setResistance(30.0F).setCreativeTab(MainRegistry.machineTab);

    public static void preInit(){
        for(Block block : ALL_BLOCKS){
            ForgeRegistries.BLOCKS.register(block);
        }
    }
}
