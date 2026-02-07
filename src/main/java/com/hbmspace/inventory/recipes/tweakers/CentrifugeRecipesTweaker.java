package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.recipes.CentrifugeRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.materials.MatsSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.P_RED;
import static com.hbm.inventory.recipes.CentrifugeRecipes.recipes;
import static com.hbmspace.inventory.OreDictManagerSpace.NI;

public class CentrifugeRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(CentrifugeRecipes.class)) return;
        recipes.put(new RecipesCommon.OreDictStack(NI.ore()), new ItemStack[]{
                new ItemStack(ModItemsSpace.chunk_ore, 2, ItemEnumsSpace.EnumChunkType.PENTLANDITE.ordinal()),
                new ItemStack(ModItems.sulfur, 1),
                new ItemStack(ModItems.powder_iron, 1),
                new ItemStack(Blocks.GRAVEL, 1)});

        recipes.put(new RecipesCommon.OreDictStack(P_RED.ore()), new ItemStack[]{
                new ItemStack(Items.BLAZE_POWDER, 2),
                new ItemStack(ModItems.powder_fire, 2),
                new ItemStack(ModItems.ingot_phosphorus),
                new ItemStack(Blocks.GRAVEL)});

        //nitric acid needs air chem to use, something that c
        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_fragment, 1, 0), new ItemStack[]{ //peroxide, easy to use and get
                new ItemStack(ModItems.powder_niobium, 4),
                new ItemStack(ModItems.powder_cobalt, 2),
                new ItemStack(ModItems.powder_zirconium, 1),
                new ItemStack(ModItems.powder_beryllium, 1)});

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_fragment, 1, 1), new ItemStack[]{ //nitric acid, harder and energy expensive
                new ItemStack(ModItemsSpace.powder_gallium, 4),
                new ItemStack(ModItems.powder_beryllium, 2),
                new ItemStack(ModItems.powder_niobium, 1),
                new ItemStack(ModItems.fragment_lanthanium, 1)});

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_fragment, 1, 2), new ItemStack[]{ //sulfuric acid, less harder
                new ItemStack(ModItems.powder_beryllium, 4),
                new ItemStack(ModItems.powder_niobium, 2),
                new ItemStack(ModItemsSpace.powder_gallium, 1),
                new ItemStack(ModItems.fragment_lanthanium, 1)});

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_fragment, 1, 3), new ItemStack[]{// solvent uses *oil* something that sulfuric doesnt
                new ItemStack(ModItems.powder_cobalt, 4),
                new ItemStack(ModItemsSpace.powder_gallium, 2),
                new ItemStack(ModItems.powder_niobium, 1),
                new ItemStack(ModItems.powder_coltan, 1)});

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_fragment, 1, 4), new ItemStack[]{  // chlorine is important mid-lategame. since it makes Plastics
                new ItemStack(ModItems.powder_zirconium, 4),
                new ItemStack(ModItems.powder_neodymium, 2),
                new ItemStack(ModItems.powder_niobium, 1),
                new ItemStack(ModItems.powder_cobalt, 1)});

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_fragment, 1, 5), new ItemStack[]{ // shchrab acid can go fuck itself
                new ItemStack(ModItems.powder_co60, 1),
                new ItemStack(ModItems.nugget_bismuth, 1),
                new ItemStack(ModItems.powder_asbestos, 6),
                new ItemStack(ModItems.nugget_technetium, 1)});

        recipes.put(new RecipesCommon.ComparableStack(ModBlocksSpace.ferric_clay, 1), new ItemStack[]{
                new ItemStack(Items.CLAY_BALL, 1),
                new ItemStack(Items.CLAY_BALL, 1),
                new ItemStack(ModItems.powder_iron, 1), //temp
                new ItemStack(ModItems.powder_iron, 1)});

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.ingot_magma, 1), new ItemStack[]{
                MatsSpace.MAT_GALLIUM.make(ModItems.bedrock_ore_fragment, 4),
                Mats.MAT_ARSENIC.make(ModItems.bedrock_ore_fragment, 2),
                new ItemStack(ModItems.powder_meteorite_tiny, 2),
                new ItemStack(ModItems.dust_tiny, 3),
        });

        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.crystal_mineral), new ItemStack[]{new ItemStack(ModItemsSpace.mineral_dust, 2), new ItemStack(ModItems.powder_iron, 2), new ItemStack(ModItems.powder_aluminium, 2), new ItemStack(ModItems.powder_lithium_tiny, 1)});
        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.crystal_nickel), new ItemStack[]{new ItemStack(ModItemsSpace.powder_nickel, 2), new ItemStack(ModItemsSpace.powder_nickel, 2), new ItemStack(ModItems.powder_iron, 2), new ItemStack(ModItems.powder_titanium, 1)});
        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.crystal_niobium), new ItemStack[]{new ItemStack(ModItems.powder_niobium, 2), new ItemStack(ModItems.powder_niobium, 2), new ItemStack(ModItems.powder_iron, 2), new ItemStack(ModItemsSpace.nugget_hafnium, 1)}); //THERE WE GO
        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.crystal_zinc), new ItemStack[]{new ItemStack(ModItemsSpace.powder_zinc, 2), new ItemStack(ModItemsSpace.powder_zinc, 2), new ItemStack(ModItems.sulfur, 2), new ItemStack(ModItems.powder_aluminium, 1)});
        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.nickel_salts), new ItemStack[]{new ItemStack(ModItems.powder_iron, 2), new ItemStack(ModItemsSpace.powder_nickel, 2), new ItemStack(ModItems.powder_sodium, 1), new ItemStack(ModItems.sulfur, 1)});
    }
}
