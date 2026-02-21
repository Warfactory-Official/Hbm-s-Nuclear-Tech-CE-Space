package com.hbmspace.items;

import com.hbm.blocks.ICustomBlockItem;
import com.hbm.config.VersatileConfig;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbm.items.armor.ItemModInsert;
import com.hbm.items.machine.ItemRBMKRod;
import com.hbm.items.weapon.ItemMissile;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.enums.EnumAddonRBMKRodTypes;
import com.hbmspace.items.armor.ItemModFlippers;
import com.hbmspace.items.armor.ItemModHeavyBoots;
import com.hbmspace.items.armor.ItemModOxy;
import com.hbmspace.items.enums.ItemEnumMultiSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;
import com.hbmspace.items.food.ItemEnergySpace;
import com.hbmspace.items.food.ItemLemonSpace;
import com.hbmspace.items.food.ItemPillSpace;
import com.hbmspace.items.food.ModItemSeedFood;
import com.hbmspace.items.machine.ItemRBMKPelletSpace;
import com.hbmspace.items.machine.ItemRBMKRodSpace;
import com.hbmspace.items.machine.ItemRTGPelletSpace;
import com.hbmspace.items.machine.ItemSatelliteSpace;
import com.hbmspace.items.special.ItemConsumableSpace;
import com.hbmspace.items.special.ItemMineralOre;
import com.hbmspace.items.special.ItemModRecordSpace;
import com.hbmspace.items.tool.ItemAtmosphereScanner;
import com.hbmspace.items.tool.ItemTransporterLinker;
import com.hbmspace.items.tool.ItemWandTime;
import com.hbmspace.items.weapon.ItemCustomMissilePart;
import com.hbmspace.items.weapon.ItemCustomRocket;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import com.hbmspace.util.RTGSpaceUtil;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModItemsSpace {

    public static final List<Item> ALL_ITEMS = new ArrayList<>();
    public static final Item rocket_custom = new ItemCustomRocket("rocket_custom").setMaxStackSize(1).setCreativeTab(null);
    public static final Item oxy_plss = new ItemModOxy("oxy_plss", 16000, 10, 1).setCreativeTab(MainRegistry.consumableTab).setMaxStackSize(1);
    public static final Item hard_drive = new ItemBakedSpace("hard_drive", "votv_e").setMaxStackSize(64).setCreativeTab(MainRegistry.partsTab);
    public static final ItemEnumMultiSpace<SolarSystem.Body> full_drive = new ItemVOTVdrive("hard_drive_full").setCreativeTab(MainRegistry.controlTab);
    public static final Item circuit = new ItemEnumMultiSpace<>("circuit", ItemEnumsSpace.EnumCircuitType.VALUES, true, true).setCreativeTab(MainRegistry.partsTab);
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
    public static final Item flesh_wafer = new ItemLemonSpace(5, 0.6F, false, "flesh_wafer", "blood_wafer").setCreativeTab(MainRegistry.partsTab);
    public static final Item grilled_flesh = new ItemLemonSpace(6, 0.8F, false, "grilled_flesh", "blood_patty").setCreativeTab(MainRegistry.partsTab);
    public static final Item flesh_burger = new ItemLemonSpace(7, 1.0F, false, "flesh_burger", "blood_burger").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_magma = new ItemBakedSpace("ingot_magma").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_nickel = new ItemBakedSpace("crystal_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_zinc = new ItemBakedSpace("crystal_zinc").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_niobium = new ItemBakedSpace("crystal_niobium").setCreativeTab(MainRegistry.partsTab);
    public static final Item nickel_salts = new ItemBakedSpace("nickel_salts").setCreativeTab(MainRegistry.partsTab);
    public static final Item ammonium_nitrate = new ItemBakedSpace("ammonium_nitrate").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_mineral = new ItemCustomLoreSpace("crystal_mineral").setCreativeTab(MainRegistry.partsTab);
    public static final Item crystal_cleaned = new ItemCustomLoreSpace("crystal_cleaned", "crystal_mineralcf").setCreativeTab(MainRegistry.partsTab);
    public static final Item mineral_dust = new ItemCustomLoreSpace("mineral_dust", "powder_mineral").setCreativeTab(MainRegistry.partsTab);
    public static final Item chunk_ore = new ItemEnumMultiSpace<>("chunk_ore", ItemEnumsSpace.EnumChunkType.VALUES, true, true).setCreativeTab(MainRegistry.partsTab);
    public static final Item mineral_fragment = new ItemMineralOre("mineral_fragment").setCreativeTab(MainRegistry.partsTab);
    public static final Item swarm_member = new ItemBaseSpace("swarm_member").setCreativeTab(MainRegistry.partsTab);
    public static final Item saltleaf = new ItemBakedSpace("saltleaf", "salt_leaf").setCreativeTab(MainRegistry.partsTab);
    public static final Item beryllium_mirror = new ItemBakedSpace("beryllium_mirror").setCreativeTab(MainRegistry.partsTab);
    public static final Item teacup_empty = new ItemBakedSpace("teacup_empty", "teacup").setCreativeTab(MainRegistry.consumableTab);
    public static final Item glass_smilk = new ItemEnergySpace("glass_smilk").setContainerItem(ModItemsSpace.glass_empty);
    public static final Item strawberry = new ModItemSeedFood(3, 0.4F, ModBlocksSpace.crop_strawberry, Blocks.FARMLAND, "strawberry");
    public static final Item mint_leaves = new ModItemSeedFood(3, 0.4F, ModBlocksSpace.crop_mint, Blocks.FARMLAND, "mint_leaves");
    public static final Item bean_raw = new ModItemSeedFood(1, 0.2F, ModBlocksSpace.crop_coffee, Blocks.FARMLAND, "bean_raw", "coffeebeanraw");
    public static final Item powder_coffee = new ItemCustomLoreSpace("powder_coffee").setCreativeTab(MainRegistry.consumableTab);
    public static final Item bean_roast = new ItemBakedSpace("bean_roast", "coffeebeanroast").setCreativeTab(MainRegistry.consumableTab);
    public static final Item teaseeds = new ModItemSeedFood(0, 0, ModBlocksSpace.crop_tea, Blocks.FARMLAND, "teaseeds");
    public static final Item tea_leaf = new ItemBakedSpace("tea_leaf").setCreativeTab(MainRegistry.consumableTab);
    public static final Item teacup = new ItemEnergySpace("teacup", "teacup_full").setContainerItem(ModItemsSpace.teacup_empty).setCreativeTab(MainRegistry.consumableTab);
    public static final Item bottle_honey = new ItemEnergySpace("bottle_honey").setContainerItem(Items.GLASS_BOTTLE).setCreativeTab(MainRegistry.consumableTab);
    public static final Item paraffin_seeds = new ModItemSeedFood(1, 0.2F, ModBlocksSpace.crop_paraffin, ModBlocksSpace.rubber_farmland, "paraffin_seeds");
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
    public static final Item leaf_rubber = new ItemBakedSpace("rubber_leaf", "rubber_leaves").setCreativeTab(MainRegistry.partsTab);
    public static final Item leaf_pet = new ItemBakedSpace("pet_leaf", "pet_leaves").setCreativeTab(MainRegistry.partsTab);
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

    public static final Item ingot_cm242 = new ItemBakedSpace("ingot_cm242").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cm243 = new ItemBakedSpace("ingot_cm243").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cm244 = new ItemBakedSpace("ingot_cm244").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cm245 = new ItemBakedSpace("ingot_cm245").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cm246 = new ItemBakedSpace("ingot_cm246").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cm247 = new ItemBakedSpace("ingot_cm247").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cm_fuel = new ItemBakedSpace("ingot_cm_fuel").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cm_mix = new ItemBakedSpace("ingot_cm_mix").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_bk247 = new ItemBakedSpace("ingot_bk247").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cf251 = new ItemBakedSpace("ingot_cf251").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_cf252 = new ItemBakedSpace("ingot_cf252").setCreativeTab(MainRegistry.partsTab);

    public static final Item ingot_es253 = new ItemBakedSpace("ingot_es253").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_es255 = new ItemBakedSpace("ingot_es255").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_gwenium = new ItemCustomLoreSpace("ingot_gwenium").setCreativeTab(MainRegistry.partsTab);

    public static final Item billet_bk247 = new ItemBakedSpace("billet_bk247").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm242 = new ItemBakedSpace("billet_cm242").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm243 = new ItemBakedSpace("billet_cm243").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm244 = new ItemBakedSpace("billet_cm244").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm245 = new ItemBakedSpace("billet_cm245").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm246 = new ItemBakedSpace("billet_cm246").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm247 = new ItemBakedSpace("billet_cm247").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cf252 = new ItemBakedSpace("billet_cf252").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_es253 = new ItemBakedSpace("billet_es253").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cf251 = new ItemBakedSpace("billet_cf251").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm_mix = new ItemBakedSpace("billet_cm_mix").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cm_fuel = new ItemBakedSpace("billet_cm_fuel").setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_red_copper = new ItemBakedSpace("billet_red_copper").setCreativeTab(MainRegistry.partsTab);
    public static final Item divine_shard = new ItemBakedSpace("divine_shard").setCreativeTab(null);
    public static final Item nugget_lanthanium = new ItemBakedSpace("nugget_lanthanium").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_coke = new ItemEnumMultiSpace<>("powder_coke", ItemEnums.EnumCokeType.VALUES, true, true).setCreativeTab(MainRegistry.partsTab);
    public static final Item s_cream	= new ItemLemonSpace(8, 1.0F, false, "s_cream", "ice_cream_s").setCreativeTab(MainRegistry.consumableTab);
    public static final Item woodemium_briquette = new ItemBakedSpace("woodemium_briquette", "briquette_woodemium").setCreativeTab(MainRegistry.partsTab); // TODO: ITER crafting recipe
    public static final Item ingot_cn989 = new ItemCustomLoreSpace("ingot_cn989").setRarity(EnumRarity.EPIC).setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cn989 = new ItemCustomLoreSpace("nugget_cn989").setRarity(EnumRarity.EPIC).setCreativeTab(MainRegistry.partsTab);
    public static final Item billet_cn989 = new ItemCustomLoreSpace("billet_cn989").setRarity(EnumRarity.EPIC).setCreativeTab(MainRegistry.partsTab);
    public static final Item plate_cn989 = new ItemCustomLoreSpace("plate_cn989").setRarity(EnumRarity.EPIC).setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_cn989 = new ItemCustomLoreSpace("powder_cn989").setRarity(EnumRarity.EPIC).setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_australium_lesser = new ItemCustomLoreSpace("ingot_australium_lesser").setRarity(EnumRarity.UNCOMMON).setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_australium_greater = new ItemCustomLoreSpace("ingot_australium_greater").setRarity(EnumRarity.UNCOMMON).setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_lead = new ItemBakedSpace("powder_lead").setCreativeTab(MainRegistry.partsTab);

    public static final Item nugget_bk247 = new ItemBakedSpace("nugget_bk247").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm242 = new ItemBakedSpace("nugget_cm242").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm243 = new ItemBakedSpace("nugget_cm243").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm244 = new ItemBakedSpace("nugget_cm244").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm245 = new ItemBakedSpace("nugget_cm245").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm246 = new ItemBakedSpace("nugget_cm246").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm247 = new ItemBakedSpace("nugget_cm247").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cf251 = new ItemBakedSpace("nugget_cf251").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cf252 = new ItemBakedSpace("nugget_cf252").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm_fuel = new ItemBakedSpace("nugget_cm_fuel").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_es253 = new ItemBakedSpace("nugget_es253").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_cm_mix = new ItemBakedSpace("nugget_cm_mix").setCreativeTab(MainRegistry.partsTab);

    public static final Item pellet_rtg_americium_depleted = new ItemBakedSpace("pellet_rtg_americium_depleted").setCreativeTab(MainRegistry.controlTab);
    public static final Item pellet_rtg_berkelium = new ItemRTGPelletSpace(20, "pellet_rtg_berkelium").setDecayItem(new ItemStack(pellet_rtg_americium_depleted), (long) (RTGSpaceUtil.getLifespan(13.8F, RTGSpaceUtil.HalfLifeType.LONG, false) * 1.5), 1).setCreativeTab(MainRegistry.controlTab).setMaxStackSize(1);
    public static final Item pellet_rtg_cf251 = new ItemRTGPelletSpace(VersatileConfig.rtgDecay() ? 600 : 200, "pellet_rtg_cf251").setDecays(ItemEnums.EnumDepletedRTGMaterial.LEAD, (long) (RTGSpaceUtil.getLifespan(1F, RTGSpaceUtil.HalfLifeType.SHORT, false) * 2.5), 1).setCreativeTab(MainRegistry.controlTab);
    public static final Item pellet_rtg_cf252 = new ItemRTGPelletSpace(VersatileConfig.rtgDecay() ? 600 : 200, "pellet_rtg_cf252").setDecays(ItemEnums.EnumDepletedRTGMaterial.LEAD, (long) (RTGSpaceUtil.getLifespan(1F, RTGSpaceUtil.HalfLifeType.SHORT, false) * 2.5), 1).setCreativeTab(MainRegistry.controlTab);

    public static final Item rag_blood = new ItemBakedSpace("rag_blood").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_palladium = new ItemCustomLoreSpace("ingot_palladium").setCreativeTab(MainRegistry.partsTab);
    public static final Item animan = new ItemPillSpace(0, "animan").setCreativeTab(MainRegistry.consumableTab);
    public static final Item insert_cmb = new ItemModInsert(9999, 0.7F, 0.9F, 0.4F, 1F, "insert_cmb");
    public static final Item flippers = new ItemModFlippers("flippers");
    public static final Item heavy_boots = new ItemModHeavyBoots("heavy_boots");

    public static final Item coin_airliner = new ItemCustomLoreSpace("coin_airliner").setRarity(EnumRarity.UNCOMMON).setCreativeTab(MainRegistry.consumableTab);

    // TODO hazard registry
    public static final ItemRBMKPelletSpace rbmk_pellet_lecm = new ItemRBMKPelletSpace("Low Enriched Curium-245", "rbmk_pellet_lecm");
    public static final ItemRBMKPelletSpace rbmk_pellet_bk247 = new ItemRBMKPelletSpace("Highly Enriched Berkelium-247", "rbmk_pellet_bk247");
    public static final ItemRBMKPelletSpace rbmk_pellet_mecm = new ItemRBMKPelletSpace("Medium Enriched Curium-245", "rbmk_pellet_mecm");
    public static final ItemRBMKPelletSpace rbmk_pellet_hecm = new ItemRBMKPelletSpace("Highly Enriched Curium-245", "rbmk_pellet_hecm");

    public static final ItemRBMKRodSpace rbmk_fuel_bk247 = (ItemRBMKRodSpace)  new ItemRBMKRodSpace(rbmk_pellet_bk247, "rbmk_fuel_bk247")
				.setYield(100000000D)
				.setStats(50)
				.setFunction(ItemRBMKRod.EnumBurnFunc.LINEAR)
				.setHeat(2D)
				.setMeltingPoint(2993);
    public static final ItemRBMKRodSpace rbmk_fuel_lecm = (ItemRBMKRodSpace) new ItemRBMKRodSpace(rbmk_pellet_lecm, "rbmk_fuel_lecm")
				.setYield(30000000D)
				.setStats(20, 5)
				.setFunction(EnumAddonRBMKRodTypes.SLOW_LINEAR)
				.setDepletionFunction(EnumAddonRBMKRodTypes.CF_SLOPE)
				.setHeat(1.25D)
				.setMeltingPoint(1340)
				.setDiffusion(0.4D);
    public static final ItemRBMKRodSpace rbmk_fuel_mecm = (ItemRBMKRodSpace) new ItemRBMKRodSpace(rbmk_pellet_mecm, "rbmk_fuel_mecm")
				.setYield(27000000D)
				.setStats(30, 10)
				.setFunction(EnumAddonRBMKRodTypes.SLOW_LINEAR)
				.setDepletionFunction(EnumAddonRBMKRodTypes.CF_SLOPE)
				.setHeat(1.25D)
				.setMeltingPoint(1720)
				.setDiffusion(0.3D);
    public static final ItemRBMKRodSpace rbmk_fuel_hecm = (ItemRBMKRodSpace) new ItemRBMKRodSpace(rbmk_pellet_hecm, "rbmk_fuel_hecm")
				.setYield(24000000D)
				.setStats(60, 25)
				.setFunction(EnumAddonRBMKRodTypes.SLOW_LINEAR)
				.setDepletionFunction(EnumAddonRBMKRodTypes.CF_SLOPE)
				.setHeat(1.3D)
				.setMeltingPoint(1880)
				.setDiffusion(0.3D);


    public static final Item ball_ferric_clay = new ItemBakedSpace("ball_ferric_clay", "ferric_clay").setCreativeTab(MainRegistry.partsTab);

    public static final Item flour = new ItemBakedSpace("flour").setCreativeTab(MainRegistry.consumableTab);
    public static final Item wand_time = new ItemWandTime("wand_time").setMaxStackSize(1).setCreativeTab(MainRegistry.consumableTab).setFull3D();
    public static final Item atmosphere_scanner = new ItemAtmosphereScanner("atmosphere_scanner").setMaxStackSize(1).setCreativeTab(MainRegistry.consumableTab);

    public static final Item record_gs = new ItemModRecordSpace("gs", HBMSpaceSoundHandler.recordGodSpeed, "record_gs").setCreativeTab(CreativeTabs.MISC);
    public static final Item record_gp = new ItemModRecordSpace("gp", HBMSpaceSoundHandler.recordGoop, "record_gp").setCreativeTab(CreativeTabs.MISC);
    public static final Item record_el = new ItemModRecordSpace("el", HBMSpaceSoundHandler.recordEthereal, "record_el").setCreativeTab(CreativeTabs.MISC);

    /*public static final Item fence_gate = new ItemModDoor().setUnlocalizedName("fence_gate").setCreativeTab(MainRegistry.blockTab).setTextureName(RefStrings.MODID + ":fence_metal");*/


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

    public static void swapStackSizes(RegistryEvent.Register<Item> event){
        var filteredSet = ModItems.ALL_ITEMS.stream().filter( o -> o instanceof ItemMissile).collect(Collectors.toSet());

        for(Item itemMissile : filteredSet){
           itemMissile.setMaxStackSize(64);
           event.getRegistry().register(itemMissile);
        }

    }
}
