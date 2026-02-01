package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.PUREXRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.ModItems;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.EnumAddonWatzTypes;
import net.minecraft.item.ItemStack;

public class PUREXRecipesTweaker {

    public static void init(){
        if(RecipeTweakerManager.isModified(PUREXRecipes.class)) return;

        long watzPower = 10_000;

        String autoWatz = "autoswitch.watz";
        
        PUREXRecipes recs = PUREXRecipes.INSTANCE;
        recs.register(new GenericRecipe("purex.watzpu241").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.PU241))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItems.nugget_am242, 12),
                        new ItemStack(ModItems.nugget_am241, 6),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzamf").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.AMF))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItemsSpace.nugget_cm_mix, 6),
                        new ItemStack(ModItemsSpace.nugget_bk247, 3),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzamrg").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.AMRG))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItemsSpace.nugget_cm_mix, 12),
                        new ItemStack(ModItemsSpace.nugget_bk247, 6),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzcmf").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.CMF))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItemsSpace.nugget_cm_mix, 12),
                        new ItemStack(ModItemsSpace.nugget_es253, 3),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzcmrg").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.CMRG))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItemsSpace.nugget_cm_mix, 12),
                        new ItemStack(ModItemsSpace.nugget_cf252, 3),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzbk247").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.BK247))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItems.nugget_am_mix, 12),
                        new ItemStack(ModItems.nugget_pu239, 3),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzcf251").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.CF251))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItemsSpace.nugget_cm_mix, 3),
                        new ItemStack(ModItemsSpace.nugget_cf251, 6),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzcf252").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.CF252))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItemsSpace.nugget_cf252, 3),
                        new ItemStack(ModItemsSpace.nugget_cm_mix, 6),
                        new ItemStack(ModItems.nuclear_waste, 2))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
        recs.register(new GenericRecipe("purex.watzes253").setup(60, watzPower).setNameWrapper("purex.recycle").setGroup(autoWatz, recs)
                .inputItems(new RecipesCommon.ComparableStack(ModItems.watz_pellet_depleted, 1, EnumAddonWatzTypes.ES253))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 500), new FluidStack(Fluids.NITRIC_ACID, 250))
                .outputItems(new ItemStack(ModItemsSpace.nugget_es253, 3),
                        new ItemStack(ModItemsSpace.nugget_cf252, 3),
                        new ItemStack(ModItems.nuclear_waste, 24))
                .outputFluids(new FluidStack(Fluids.WATZ, 1_000))
                .setIconToFirstIngredient());
    }
}
