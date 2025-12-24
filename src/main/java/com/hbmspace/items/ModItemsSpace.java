package com.hbmspace.items;

import com.hbm.blocks.ICustomBlockItem;
import com.hbm.items.machine.ItemSatellite;
import com.hbm.items.special.ItemConsumable;
import com.hbm.items.weapon.ItemMissile;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.armor.ItemModOxy;
import com.hbmspace.items.food.ItemLemonSpace;
import com.hbmspace.items.machine.ItemSatelliteSpace;
import com.hbmspace.items.special.ItemConsumableSpace;
import com.hbmspace.items.special.ItemMineralOre;
import com.hbmspace.items.tool.ItemTransporterLinker;
import com.hbmspace.items.weapon.ItemCustomMissilePart;
import com.hbmspace.items.weapon.ItemCustomRocket;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModItemsSpace {

    public static final List<Item> ALL_ITEMS = new ArrayList<>();
    public static final Item rocket_custom = new ItemCustomRocket("rocket_custom").setMaxStackSize(1).setCreativeTab(null);
    public static final Item oxy_plss = new ItemModOxy("oxy_plss", 16000, 10, 1).setCreativeTab(MainRegistry.consumableTab).setMaxStackSize(1);
    public static final Item hard_drive = new ItemBakedSpace("hard_drive", "votv_e").setMaxStackSize(64).setCreativeTab(MainRegistry.partsTab);
    public static final ItemEnumMultiSpace full_drive = (ItemEnumMultiSpace) new ItemVOTVdrive("hard_drive_full").setCreativeTab(MainRegistry.controlTab);
    public static final Item circuit = new ItemEnumMultiSpace("circuit", ItemEnumsSpace.EnumCircuitType.class, true, true).setCreativeTab(MainRegistry.partsTab);
    public static final Item transporter_linker = new ItemTransporterLinker("transporter_linker").setMaxStackSize(1).setCreativeTab(MainRegistry.controlTab);
    public static final Item rp_fuselage_20_12 = new ItemCustomMissilePart("rp_f_20_12").makeFuselage(ItemMissile.FuelType.ANY, 64000F, 4000, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_fuselage_20_6 = new ItemCustomMissilePart("rp_f_20_6").makeFuselage(ItemMissile.FuelType.ANY, 32000F, 2100, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_fuselage_20_3 = new ItemCustomMissilePart("rp_f_20_3").makeFuselage(ItemMissile.FuelType.ANY, 16000F, 1200, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_fuselage_20_1 = new ItemCustomMissilePart("rp_f_20_1").makeFuselage(ItemMissile.FuelType.ANY, 6000F, 500, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_legs_20 = new ItemCustomMissilePart("rp_l_20").makeStability(0, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1);
    public static final Item rp_capsule_20 = new ItemCustomMissilePart("rp_c_20").makeWarhead(ItemMissile.WarheadType.APOLLO, 15F, 8_000, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item rp_station_core_20 = new ItemCustomMissilePart("rp_sc_20").makeWarhead(ItemMissile.WarheadType.SATELLITE, 15F, 64_000, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item rp_pod_20 = new ItemCustomMissilePart("rp_pod_20").makeWarhead(ItemMissile.WarheadType.APOLLO, 15F, 4_000, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item rp_fuselage_20_12_hydrazine = new ItemCustomMissilePart("mp_fuselage_20_hydrazine").makeFuselage(ItemMissile.FuelType.HYDRAZINE, 12500, 1000, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20).setHealth(25F);
    public static final Item mp_thruster_20_methalox = new ItemCustomMissilePart("mp_thruster_20_methalox").makeThruster(ItemMissile.FuelType.METHALOX, 1F, 100_000, ItemMissile.PartSize.SIZE_20, 890_000, 2400, 320).setHealth(30F);
    public static final Item mp_thruster_20_methalox_dual = new ItemCustomMissilePart("mp_thruster_20_methalox_dual").makeThruster(ItemMissile.FuelType.METHALOX, 1F, 100_000, ItemMissile.PartSize.SIZE_20, 1_184_000, 3200, 320).setHealth(30F);
    public static final Item mp_thruster_20_methalox_triple = new ItemCustomMissilePart("mp_thruster_20_methalox_triple").makeThruster(ItemMissile.FuelType.METHALOX, 1F, 100_000, ItemMissile.PartSize.SIZE_20, 1_456_000, 4400, 320).setHealth(30F);
    public static final Item mp_thruster_20_hydrogen = new ItemCustomMissilePart("mp_thruster_20_hydrogen").makeThruster(ItemMissile.FuelType.HYDROGEN, 1F, 100_000, ItemMissile.PartSize.SIZE_20, 480_000, 2600, 380).setHealth(30F);
    public static final Item mp_thruster_20_hydrogen_dual = new ItemCustomMissilePart("mp_thruster_20_hydrogen_dual").makeThruster(ItemMissile.FuelType.HYDROGEN, 1F, 100_000, ItemMissile.PartSize.SIZE_20, 640_000, 3400, 380).setHealth(30F);
    public static final Item mp_thruster_20_hydrogen_triple = new ItemCustomMissilePart("mp_thruster_20_hydrogen_triple").makeThruster(ItemMissile.FuelType.HYDROGEN, 1F, 100_000, ItemMissile.PartSize.SIZE_20, 938_000, 4500, 380).setHealth(30F);
    public static final Item flesh = new ItemBakedSpace("flesh").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_magma = new ItemBakedSpace("ingot_magma").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_nickel = new ItemBakedSpace("crystal_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_zinc = new ItemBakedSpace("crystal_zinc").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_niobium = new ItemBakedSpace("crystal_niobium").setCreativeTab(MainRegistry.partsTab);
    public static final Item nickel_salts = new ItemBakedSpace("nickel_salts").setCreativeTab(MainRegistry.partsTab);
    public static final Item ammonium_nitrate = new ItemBakedSpace("ammonium_nitrate").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_mineral = new ItemCustomLoreSpace("crystal_mineral").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_cleaned = new ItemCustomLoreSpace("crystal_cleaned", "crystal_mineralcf").setCreativeTab(MainRegistry.partsTab);
    public static final Item mineral_dust = new ItemCustomLoreSpace("mineral_dust", "powder_mineral").setCreativeTab(MainRegistry.partsTab);
    public static final Item chunk_ore = new ItemEnumMultiSpace("chunk_ore", ItemEnumsSpace.EnumChunkType.class, true, true).setCreativeTab(MainRegistry.partsTab);
    public static final Item mineral_fragment = new ItemMineralOre("mineral_fragment").setCreativeTab(MainRegistry.partsTab);
    public static final Item swarm_member = new ItemBaseSpace("swarm_member").setCreativeTab(MainRegistry.partsTab);
    public static final Item saltleaf = new ItemBakedSpace("saltleaf", "salt_leaf").setCreativeTab(MainRegistry.partsTab);
    public static final Item beryllium_mirror = new ItemBakedSpace("beryllium_mirror").setCreativeTab(MainRegistry.partsTab);
    public static final Item teacup_empty = new ItemBakedSpace("teacup_empty", "teacup").setCreativeTab(MainRegistry.consumableTab);
    public static final Item glass_empty = new ItemBakedSpace("glass_empty").setCreativeTab(MainRegistry.consumableTab);
    public static final Item cmug_empty = new ItemBakedSpace("cmug_empty", "mug_empty").setCreativeTab(MainRegistry.consumableTab);
    public static final Item lox_tank = new ItemConsumableSpace("lox_tank").setMaxStackSize(16).setCreativeTab(MainRegistry.consumableTab);
    public static final Item turbine_syngas = new ItemBakedSpace("turbine_syngas").setCreativeTab(MainRegistry.partsTab);
    public static final Item blade_syngas = new ItemBakedSpace("blade_syngas").setCreativeTab(MainRegistry.partsTab);
    public static final Item stick_pvc = new ItemCustomLoreSpace("stick_pvc").setCreativeTab(MainRegistry.partsTab);
    public static final Item stick_vinyl = new ItemCustomLoreSpace("stick_vinyl").setCreativeTab(MainRegistry.partsTab);
    public static final Item sat_dyson_relay = new ItemSatelliteSpace(32_000, "sat_dyson_relay").setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item sat_war = new ItemSatelliteSpace(128_000, "sat_war").setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item scuttertail = new ItemBakedSpace("scuttertail").setCreativeTab(MainRegistry.partsTab);
    public static final Item leaf_rubber = new ItemBakedSpace("rubber_leaves").setCreativeTab(MainRegistry.partsTab);
    public static final Item leaf_pet = new ItemBakedSpace("pet_leaves").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_rubber = new ItemBakedSpace("powder_rubber").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_pvc = new ItemBakedSpace("powder_pvc").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_nickel = new ItemBakedSpace("ingot_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_nickel = new ItemBakedSpace("powder_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_nickel = new ItemBakedSpace("nugget_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item plate_nickel = new ItemBakedSpace("plate_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_gallium = new ItemBakedSpace("ingot_gallium").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_gallium = new ItemBakedSpace("nugget_gallium").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_gallium= new ItemBakedSpace("powder_gallium").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_gallium_tiny= new ItemBakedSpace("powder_gallium_tiny").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_wd2004_tiny = new ItemCustomLoreSpace("powder_wd2004_tiny").setRarity(EnumRarity.EPIC).setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_wd2004 = new ItemCustomLoreSpace("powder_wd2004").setRarity(EnumRarity.EPIC).setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_tt = new ItemCustomLoreSpace("ingot_tt", "ingot_techtactium").setRarity(EnumRarity.UNCOMMON).setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_ttas = new ItemCustomLoreSpace("ingot_ttas", "ingot_techtactium_as").setRarity(EnumRarity.RARE).setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_gaas = new ItemCustomLoreSpace("ingot_gaas", "ingot_gaas1").setRarity(EnumRarity.RARE).setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_gaas = new ItemBakedSpace("nugget_gaas").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_gaas = new ItemBakedSpace("billet_gaas", "billet_gaas1").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_zinc = new ItemBakedSpace("nugget_zinc").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_zinc = new ItemBakedSpace("ingot_zinc").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_zinc = new ItemBakedSpace("powder_zinc").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_hafnium = new ItemCustomLoreSpace("ingot_hafnium").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_hafnium = new ItemCustomLoreSpace("nugget_hafnium").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_iridium = new ItemCustomLoreSpace("ingot_iridium").setRarity(EnumRarity.RARE).setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_stainless = new ItemBakedSpace("ingot_stainless").setCreativeTab(MainRegistry.partsTab);
    public static final Item plate_stainless = new ItemBakedSpace("plate_stainless").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_menthol = new ItemBakedSpace("ingot_menthol").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_menthol = new ItemBakedSpace("nugget_menthol").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_menthol = new ItemBakedSpace("billet_menthol").setCreativeTab(MainRegistry.partsTab);
    public static final Item butter = new ItemBakedSpace("butter", "ingot_butter").setCreativeTab(MainRegistry.consumableTab);
    public static final Item min_cream = new ItemLemonSpace(10, 1.0F, false, "min_cream", "ice_cream_min").setCreativeTab(MainRegistry.consumableTab);
    public static final Item chocolate_mint_billet = new ItemLemonSpace(5, 5F, true, "chocolate_mint_billet").setCreativeTab(MainRegistry.consumableTab);


    public static void preInit() {
        for (Item item : ALL_ITEMS) {
            ForgeRegistries.ITEMS.register(item);
        }

        for (Block block : ModBlocksSpace.ALL_BLOCKS) {
            if (block instanceof ICustomBlockItem) {
                ((ICustomBlockItem) block).registerItem();
            } else {
                ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
    }
}
