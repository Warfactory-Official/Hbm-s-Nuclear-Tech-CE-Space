package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.AnnihilatorRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.machine.ItemBlueprints;
import com.hbm.util.Tuple;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.enums.ItemEnumsSpace;

import java.math.BigInteger;

import static com.hbm.inventory.OreDictManager.AL;
import static com.hbm.inventory.recipes.AnnihilatorRecipes.recipes;
import static com.hbmspace.inventory.OreDictManagerSpace.STAINLESS;

public class AnnihilatorRecipessTweaker {

    public static void init() {
        recipes.put(AL.ingot(),						new AnnihilatorRecipes.AnnihilatorRecipe(new Tuple.Pair(new BigInteger("512"), ItemBlueprints.make(GenericRecipes.POOL_PREFIX_528 + "aluminium"))));
        recipes.put(STAINLESS.ingot(),				new AnnihilatorRecipes.AnnihilatorRecipe(new Tuple.Pair(new BigInteger("1024"), ItemBlueprints.make(GenericRecipes.POOL_PREFIX_528 + "stainless"))));
        recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.circuit, 1, ItemEnumsSpace.EnumCircuitType.GASCHIP),	new AnnihilatorRecipes.AnnihilatorRecipe(new Tuple.Pair(new BigInteger("512"), ItemBlueprints.make(GenericRecipes.POOL_PREFIX_528 + "gaschip"))));
    }
}
