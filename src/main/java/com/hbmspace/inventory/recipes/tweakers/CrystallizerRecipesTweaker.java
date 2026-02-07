package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.CrystallizerRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.recipes.CrystallizerRecipes.registerRecipe;
import static com.hbmspace.inventory.OreDictManagerSpace.ZI;

public class CrystallizerRecipesTweaker {

    public static void init() {
        if (RecipeTweakerManager.isModified(CrystallizerRecipes.class)) return;
        final int baseTime = 600;
        FluidStack sulfur = new FluidStack(Fluids.SULFURIC_ACID, 500);
        FluidStack nitric = new FluidStack(Fluids.NITRIC_ACID, 500);
        FluidStack organic = new FluidStack(Fluids.SOLVENT, 500);
        FluidStack chloric = new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 500);
        FluidStack schrabidic = new FluidStack(Fluids.SCHRABIDIC, 1000);

        registerRecipe(P_RED.ore(), new CrystallizerRecipes.CrystallizerRecipe(ModItems.crystal_phosphorus, baseTime).prod(0.05F));
        registerRecipe(ZI.ore(), new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_zinc, baseTime).prod(0.05F), nitric);

        registerRecipe(NB.ore(), new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_niobium, baseTime).prod(0.05F), sulfur);
        registerRecipe((new RecipesCommon.ComparableStack(ModBlocksSpace.ore_mineral, 1, OreDictionary.WILDCARD_VALUE)), new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_mineral, baseTime).prod(0.05F)); //temp
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.crystal_mineral), new CrystallizerRecipes.CrystallizerRecipe(ModItems.crystal_diamond, baseTime).prod(0.05F));

        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.saltleaf), new CrystallizerRecipes.CrystallizerRecipe(ModItems.gem_sodalite, baseTime).setReq(5), new FluidStack(com.hbmspace.inventory.fluid.Fluids.SCUTTERBLOOD, 1_000));
        registerRecipe(MALACHITE.ingot(), new CrystallizerRecipes.CrystallizerRecipe(ModItems.crystal_copper, baseTime).prod(0.1F), new FluidStack(com.hbmspace.inventory.fluid.Fluids.COPPERSULFATE, 350));
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.nickel_salts), new CrystallizerRecipes.CrystallizerRecipe(ModItemsSpace.crystal_nickel, baseTime), nitric);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.leaf_rubber), new CrystallizerRecipes.CrystallizerRecipe(ModItems.ingot_rubber, baseTime).setReq(64), chloric);

        int mineraltime = 300;
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust), new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 0), mineraltime));
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust), new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 1), mineraltime), nitric);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust), new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 2), mineraltime), sulfur);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust), new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 3), mineraltime), organic);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust), new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 4), mineraltime), chloric);
        registerRecipe(new RecipesCommon.ComparableStack(ModItemsSpace.mineral_dust), new CrystallizerRecipes.CrystallizerRecipe(new ItemStack(ModItemsSpace.mineral_fragment, 1, 5), mineraltime), schrabidic);
    }
}
