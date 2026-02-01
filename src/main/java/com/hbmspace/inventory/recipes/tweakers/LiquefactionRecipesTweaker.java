package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.LiquefactionRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.init.Blocks;

import java.lang.reflect.Field;
import java.util.HashMap;

import static com.hbm.inventory.OreDictManager.KEY_COBBLESTONE;
import static com.hbmspace.inventory.OreDictManagerSpace.KEY_STONE;

// someone kill me
public class LiquefactionRecipesTweaker {

    public static void init() {
        if(RecipeTweakerManager.isModified(LiquefactionRecipes.class)) return;
        try {
            Field recipesField = LiquefactionRecipes.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            HashMap<Object, FluidStack> recipes = (HashMap<Object, FluidStack>) recipesField.get(null);

            recipes.remove(new RecipesCommon.ComparableStack(Blocks.COBBLESTONE), new FluidStack(250, Fluids.LAVA));
            recipes.remove(new RecipesCommon.ComparableStack(Blocks.STONE), new FluidStack(250, Fluids.LAVA));
            recipes.remove(new RecipesCommon.ComparableStack(Blocks.PACKED_ICE),				new FluidStack(1000, Fluids.WATER));

            recipes.put(new RecipesCommon.ComparableStack(Blocks.PACKED_ICE),				new FluidStack(2000, Fluids.WATER));

            recipes.put(KEY_COBBLESTONE,									new FluidStack(250, Fluids.LAVA));
            recipes.put(KEY_STONE,											new FluidStack(250, Fluids.LAVA));
            recipes.put(new RecipesCommon.ComparableStack(ModBlocksSpace.dry_ice),				new FluidStack(1000, Fluids.CARBONDIOXIDE));
            recipes.put(new RecipesCommon.ComparableStack(ModItemsSpace.flesh),			new FluidStack(100, Fluids.BLOOD));
            recipes.put(new RecipesCommon.ComparableStack(ModItems.ingot_osmiridium),	new FluidStack(24000, Fluids.ETHANOL));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
