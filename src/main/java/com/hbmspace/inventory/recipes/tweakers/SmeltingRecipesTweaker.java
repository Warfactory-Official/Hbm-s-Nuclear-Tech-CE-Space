package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.OreDictManager;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SmeltingRecipesTweaker {

    public static void init() {
        // Oredict doesn't work for vanilla smelting :(
        // space ores fuck yeeeeah
        for (int i = 0; i < SolarSystem.Body.values().length - 1; i++) {
            if (i != 1) {
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_iron, 1, i), new ItemStack(Items.IRON_INGOT), 0.7F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_gold, 1, i), new ItemStack(Items.GOLD_INGOT), 1.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_diamond, 1, i), new ItemStack(Items.DIAMOND), 1.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_emerald, 1, i), new ItemStack(Items.EMERALD), 5.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_redstone, 1, i), new ItemStack(Items.REDSTONE), 0.7F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_lapis, 1, i), new ItemStack(Items.DYE, 1, 4), 0.2F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_quartz, 1, i), new ItemStack(Items.QUARTZ), 0.2F);

                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_thorium, 1, i), new ItemStack(ModItems.ingot_th232), 3.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_uranium, 1, i), new ItemStack(ModItems.ingot_uranium), 6.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_titanium, 1, i), new ItemStack(ModItems.ingot_titanium), 3.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_copper, 1, i), new ItemStack(ModItems.ingot_copper), 2.5F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_zinc, 1, i), new ItemStack(ModItemsSpace.ingot_zinc), 2.5F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_tungsten, 1, i), new ItemStack(ModItems.ingot_tungsten), 6.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_aluminium, 1, i), OreDictManager.DictFrame.fromOne(ModItems.chunk_ore, ItemEnums.EnumChunkType.CRYOLITE, 1), 2.5F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_nickel, 1, i), OreDictManager.DictFrame.fromOne(ModItemsSpace.chunk_ore, ItemEnumsSpace.EnumChunkType.PENTLANDITE, 1), 2.5F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_lead, 1, i), new ItemStack(ModItems.ingot_lead), 3.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_beryllium, 1, i), new ItemStack(ModItems.ingot_beryllium), 2.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_schrabidium, 1, i), new ItemStack(ModItems.ingot_schrabidium), 128.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_cobalt, 1, i), new ItemStack(ModItems.ingot_cobalt), 2.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_lanthanium, 1, i), new ItemStack(ModItems.ingot_lanthanium), 4.0F);
                GameRegistry.addSmelting(new ItemStack(ModBlocksSpace.ore_niobium, 1, i), new ItemStack(ModItems.ingot_niobium), 4.0F);
            }
        }

        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.duna_sands), new ItemStack(Blocks.GLASS), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.laythe_silt), new ItemStack(Blocks.GLASS), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.eve_silt), new ItemStack(Blocks.GLASS), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocks.moon_turf), new ItemStack(Blocks.GLASS), 0.1F);

        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.duna_cobble), new ItemStack(ModBlocksSpace.duna_rock), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.dres_rock), new ItemStack(Blocks.STONE), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.ike_regolith), new ItemStack(ModBlocksSpace.ike_stone), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.eve_rock), new ItemStack(Blocks.STONE), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.moho_regolith), new ItemStack(ModBlocksSpace.moho_stone), 0.1F);
        GameRegistry.addSmelting(Item.getItemFromBlock(ModBlocksSpace.moon_rock), new ItemStack(Blocks.STONE), 0.1F);

        GameRegistry.addSmelting(ModItemsSpace.powder_nickel, new ItemStack(ModItemsSpace.ingot_nickel), 1.0F);
        GameRegistry.addSmelting(ModItemsSpace.powder_zinc, new ItemStack(ModItemsSpace.ingot_zinc), 1.0F);
        GameRegistry.addSmelting(ModItemsSpace.powder_gallium, new ItemStack(ModItemsSpace.ingot_gallium), 1.0F);
        GameRegistry.addSmelting(ModItemsSpace.powder_rubber, new ItemStack(ModItems.ingot_rubber), 1.0F);
        GameRegistry.addSmelting(ModItemsSpace.bean_raw, new ItemStack(ModItemsSpace.bean_roast), 1.0F);
        GameRegistry.addSmelting(ModItemsSpace.flesh_wafer, new ItemStack(ModItemsSpace.grilled_flesh), 1.0F);

        GameRegistry.addSmelting(ModItemsSpace.crystal_nickel, new ItemStack(ModItemsSpace.ingot_nickel, 2), 2.0F);
        GameRegistry.addSmelting(ModItemsSpace.crystal_niobium, new ItemStack(ModItems.ingot_niobium, 2), 2.0F);
        GameRegistry.addSmelting(ModItemsSpace.crystal_zinc, new ItemStack(ModItemsSpace.ingot_zinc, 2), 2.0F);
    }
}
