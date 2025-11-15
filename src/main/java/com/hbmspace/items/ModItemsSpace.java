package com.hbmspace.items;

import com.hbm.blocks.ICustomBlockItem;
import com.hbm.items.weapon.ItemMissile;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.armor.ItemModOxy;
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
    public static final Item rp_fuselage_20_12_hydrazine = new ItemCustomMissilePart("rp_f_20_12_hydrazine").makeFuselage(ItemMissile.FuelType.ANY, 64_000, 4000, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_fuselage_20_12 = new ItemCustomMissilePart("rp_f_20_12").makeFuselage(ItemMissile.FuelType.ANY, 64000F, 4000, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_fuselage_20_6 = new ItemCustomMissilePart("rp_f_20_6").makeFuselage(ItemMissile.FuelType.ANY, 32000F, 2100, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_fuselage_20_3 = new ItemCustomMissilePart("rp_f_20_3").makeFuselage(ItemMissile.FuelType.ANY, 16000F, 1200, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_fuselage_20_1 = new ItemCustomMissilePart("rp_f_20_1").makeFuselage(ItemMissile.FuelType.ANY, 6000F, 500, ItemMissile.PartSize.SIZE_20, ItemMissile.PartSize.SIZE_20);
    public static final Item rp_legs_20 = new ItemCustomMissilePart("rp_l_20").makeStability(0, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1);
    public static final Item rp_capsule_20 = new ItemCustomMissilePart("rp_c_20").makeWarhead(ItemMissile.WarheadType.APOLLO, 15F, 8_000, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item rp_station_core_20 = new ItemCustomMissilePart("rp_sc_20").makeWarhead(ItemMissile.WarheadType.SATELLITE, 15F, 64_000, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item rp_pod_20 = new ItemCustomMissilePart("rp_pod_20").makeWarhead(ItemMissile.WarheadType.APOLLO, 15F, 4_000, ItemMissile.PartSize.SIZE_20).setMaxStackSize(1).setCreativeTab(MainRegistry.missileTab);
    public static final Item chunk_ore = new ItemEnumMultiSpace("chunk_ore", ItemEnumsSpace.EnumChunkType.class, true, true).setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_nickel = new ItemBakedSpace("ingot_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_nickel = new ItemBakedSpace("powder_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_nickel = new ItemBakedSpace("nugget_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item plate_nickel = new ItemBakedSpace("plate_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item ingot_gallium = new ItemBakedSpace("ingot_gallium").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_gallium = new ItemBakedSpace("nugget_gallium").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_gallium= new ItemBakedSpace("powder_gallium").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_gallium_tiny= new ItemBakedSpace("powder_gallium_tiny").setCreativeTab(MainRegistry.partsTab);
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
