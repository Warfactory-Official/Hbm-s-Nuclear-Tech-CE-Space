package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.MixerRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.OreDictManagerSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraftforge.oredict.OreDictionary;

import static com.hbm.inventory.recipes.MixerRecipes.register;

public class MixerRecipesTweaker {

    public static void init() {
        if(RecipeTweakerManager.isModified(MixerRecipes.class)) return;
        // TODO replace a few recipes, should find them in 1.7 upstream

        register(com.hbmspace.inventory.fluid.Fluids.SMILK, new MixerRecipes.MixerRecipe(500, 50).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.MILK, 500)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.strawberry, 4)));
        register(com.hbmspace.inventory.fluid.Fluids.COFFEE, new MixerRecipes.MixerRecipe(100, 50).setStack1(new FluidStack(Fluids.WATER, 500)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.powder_coffee, 4)));
        register(com.hbmspace.inventory.fluid.Fluids.TEA, new MixerRecipes.MixerRecipe(200, 50).setStack1(new FluidStack(Fluids.WATER, 500)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.tea_leaf, 2)));

        register(com.hbmspace.inventory.fluid.Fluids.ELBOWGREASE, new MixerRecipes.MixerRecipe(1000, 50).setStack1(new FluidStack(Fluids.REFORMGAS, 500)).setStack2(new FluidStack(Fluids.SYNGAS, 500)));

        register(com.hbmspace.inventory.fluid.Fluids.HTCO4, new MixerRecipes.MixerRecipe(250, 50).setStack1(new FluidStack(Fluids.NITRIC_ACID, 500)).setSolid(new RecipesCommon.ComparableStack(ModItems.nugget_technetium)));

        register(com.hbmspace.inventory.fluid.Fluids.MINSOL, new MixerRecipes.MixerRecipe(500, 50).setStack1(new FluidStack(Fluids.NITRIC_ACID, 500)).setSolid(new RecipesCommon.ComparableStack(ModBlocksSpace.ore_mineral, 1, OreDictionary.WILDCARD_VALUE)));
        register(Fluids.REDMUD, new MixerRecipes.MixerRecipe(50, 100).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 1400)).setSolid(new RecipesCommon.ComparableStack(ModBlocksSpace.duna_sands, 4)));

        register(com.hbmspace.inventory.fluid.Fluids.DICYANOACETYLENE, new MixerRecipes.MixerRecipe(750, 50).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.AMMONIA, 300)).setStack2(new FluidStack(Fluids.UNSATURATEDS, 500))); //too powerful, needs change
        register(Fluids.ETHANOL, new MixerRecipes.MixerRecipe(450, 30).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROETHANE, 250)).setStack2(new FluidStack(Fluids.REDMUD, 300)));

        register(com.hbmspace.inventory.fluid.Fluids.SCUTTERBLOOD, new MixerRecipes.MixerRecipe(550, 50).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 300)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.scuttertail)));
        register(com.hbmspace.inventory.fluid.Fluids.CONGLOMERA, new MixerRecipes.MixerRecipe(1000, 70).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL, 250)).setSolid(new RecipesCommon.ComparableStack(ModBlocksSpace.stone_resource, 1, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal())));
        register(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL, new MixerRecipes.MixerRecipe(1000, 80).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 250)).setSolid(new RecipesCommon.OreDictStack(OreDictManagerSpace.NIM.dust())));
        register(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_COPPER, new MixerRecipes.MixerRecipe(1000, 80).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 250)).setSolid(new RecipesCommon.ComparableStack(ModItems.chunk_ore, 1, ItemEnums.EnumChunkType.MALACHITE)));
        register(com.hbmspace.inventory.fluid.Fluids.VINYL,
                new MixerRecipes.MixerRecipe(1000, 20).setStack1(new FluidStack(Fluids.RADIOSOLVENT, 250)).setSolid(new RecipesCommon.ComparableStack(ModBlocksSpace.pvc_log)),
                new MixerRecipes.MixerRecipe(500, 20).setStack1(new FluidStack(Fluids.RADIOSOLVENT, 125)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.powder_pvc)),
                new MixerRecipes.MixerRecipe(500, 20).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.HGAS, 500)).setStack2(new FluidStack(Fluids.UNSATURATEDS, 250)).setSolid(new RecipesCommon.ComparableStack(ModItems.powder_cadmium)));
        register(com.hbmspace.inventory.fluid.Fluids.CBENZ, new MixerRecipes.MixerRecipe(250, 20).setStack1(new FluidStack(Fluids.RADIOSOLVENT, 500)).setSolid(new RecipesCommon.ComparableStack(ModItemsSpace.leaf_pet, 32)));

        register(com.hbmspace.inventory.fluid.Fluids.LITHYDRO, new MixerRecipes.MixerRecipe(1000, 100).setStack1(new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 125)).setSolid(new RecipesCommon.ComparableStack(ModItems.powder_lithium, 3)));
    }
}
