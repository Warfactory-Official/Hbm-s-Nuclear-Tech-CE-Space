package com.hbmspace.items;

import com.hbm.blocks.ICustomBlockItem;
import com.hbm.lib.RefStrings;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.armor.ItemModOxy;
import com.hbmspace.items.tool.ItemTransporterLinker;
import com.hbmspace.items.weapon.ItemCustomRocket;
import net.minecraft.block.Block;
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
    public static final Item ingot_nickel = new ItemBakedSpace("ingot_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item powder_nickel = new ItemBakedSpace("powder_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item nugget_nickel = new ItemBakedSpace("nugget_nickel").setCreativeTab(MainRegistry.partsTab);
    public static final Item plate_nickel = new ItemBakedSpace("plate_nickel").setCreativeTab(MainRegistry.partsTab);


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
