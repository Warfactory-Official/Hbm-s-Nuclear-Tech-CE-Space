package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.blocks.BlockEnums;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.recipes.ShredderRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.OreDictManagerSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ShredderRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(ShredderRecipes.class)) return;
        ShredderRecipes.removeRecipe(new ItemStack(ModBlocks.ore_nether_fire));
        ShredderRecipes.removeRecipe(new ItemStack(Blocks.STONE));
        // TODO
        ShredderRecipes.setRecipe(Items.WHEAT, new ItemStack(ModItemsSpace.flour));
        ShredderRecipes.setRecipe(ModBlocks.reinforced_light, new ItemStack(Items.GLOWSTONE_DUST, 4));
        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.ore_glowstone, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.GLOWSTONE_DUST, 4));

        ShredderRecipes.setRecipe(ModBlocksSpace.ore_quartz, new ItemStack(ModItems.powder_quartz, 2));
        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.ore_fire, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItems.powder_fire, 6));
        ShredderRecipes.setRecipe(ModBlocksSpace.ore_fire, new ItemStack(ModItems.powder_fire, 6));

        ShredderRecipes.setRecipe(ModBlocksSpace.ore_gas_empty, new ItemStack(Blocks.GRAVEL, 1));
        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.ore_rare, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItems.powder_desh_mix, 1));

        ShredderRecipes.setRecipe(ModItemsSpace.crystal_cleaned, new ItemStack(ModItemsSpace.mineral_dust, 4));
        //ShredderRecipes.setRecipe(ModBlocksSpace.laythe_coral_block, new ItemStack(ModItems.powder_calcium, 4));

        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.ore_mineral, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItemsSpace.mineral_dust, 1)); // it was deserved

        ShredderRecipes.setRecipe(ModItemsSpace.bean_roast, new ItemStack(ModItemsSpace.powder_coffee, 1));

        ShredderRecipes.setRecipe(OreDictManager.DictFrame.fromOne(ModBlocks.stone_resource, BlockEnums.EnumStoneType.LIMESTONE), new ItemStack(ModItems.powder_calcium, 4));
        ShredderRecipes.setRecipe(OreDictManager.DictFrame.fromOne(ModBlocksSpace.stone_resource, BlockEnumsSpace.EnumStoneType.CALCIUM), new ItemStack(ModItems.powder_calcium, 6));

        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.ore_nickel, 1, OreDictionary.WILDCARD_VALUE), OreDictManager.DictFrame.fromOne(ModItemsSpace.chunk_ore, ItemEnumsSpace.EnumChunkType.PENTLANDITE, 2));

        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.vinyl_log), new ItemStack(ModItemsSpace.powder_rubber, 4));
        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.pvc_log), new ItemStack(ModItemsSpace.powder_pvc, 4));
        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.vinyl_planks), new ItemStack(ModItemsSpace.powder_rubber, 1));
        ShredderRecipes.setRecipe(new ItemStack(ModBlocksSpace.pvc_planks), new ItemStack(ModItemsSpace.powder_pvc, 1));

        List<ItemStack> stones = OreDictionary.getOres(OreDictManagerSpace.KEY_STONE);
        List<ItemStack> cobbles = OreDictionary.getOres(OreDictManager.KEY_COBBLESTONE);
        List<ItemStack> sands = OreDictionary.getOres(OreDictManager.KEY_SAND);

        for (ItemStack stone : stones) ShredderRecipes.setRecipe(stone, new ItemStack(Blocks.GRAVEL, 1));
        for (ItemStack cobble : cobbles) ShredderRecipes.setRecipe(cobble, new ItemStack(Blocks.GRAVEL, 1));
        for (ItemStack sand : sands) ShredderRecipes.setRecipe(sand, new ItemStack(ModItems.dust, 2));

        ShredderRecipes.setRecipe(ModItemsSpace.crystal_nickel, new ItemStack(ModItemsSpace.powder_nickel, 3));
        ShredderRecipes.setRecipe(ModItemsSpace.crystal_niobium, new ItemStack(ModItems.powder_niobium, 3));
        ShredderRecipes.setRecipe(ModItemsSpace.crystal_zinc, new ItemStack(ModItemsSpace.powder_zinc, 3));
    }
}
