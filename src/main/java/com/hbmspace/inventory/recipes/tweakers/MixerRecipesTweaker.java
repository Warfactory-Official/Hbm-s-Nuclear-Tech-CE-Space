package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.MixerRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.BlockEnumsSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Constructor;

import static com.hbm.inventory.recipes.MixerRecipes.register;
import static com.hbmspace.inventory.OreDictManagerSpace.NIM;

public class MixerRecipesTweaker {

    public static void init() {
        // TODO replace a few recipes, should find them in 1.7 upstream
        // Also remind me to change protected to public, because that's fucking atrocious
        MixerRecipes.MixerRecipe r;

        r = newRecipe(500, 50);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.MILK, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.strawberry, 4);
        register(com.hbmspace.inventory.fluid.Fluids.SMILK, r);

        r = newRecipe(100, 50);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.COFFEE, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.powder_coffee, 4);
        register(com.hbmspace.inventory.fluid.Fluids.COFFEE, r);

        r = newRecipe(200, 50);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.TEA, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.tea_leaf, 2);
        register(com.hbmspace.inventory.fluid.Fluids.TEA, r);

        r = newRecipe(1000, 50);
        r.input1 = new FluidStack(Fluids.REFORMGAS, 500);
        r.input2 = new FluidStack(Fluids.SYNGAS, 500);
        register(com.hbmspace.inventory.fluid.Fluids.ELBOWGREASE, r);

        r = newRecipe(250, 50);
        r.input1 = new FluidStack(Fluids.NITRIC_ACID, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModItems.nugget_technetium);
        register(com.hbmspace.inventory.fluid.Fluids.HTCO4, r);

        r = newRecipe(500, 50);
        r.input1 = new FluidStack(Fluids.NITRIC_ACID, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.ore_mineral, 1, OreDictionary.WILDCARD_VALUE);
        register(com.hbmspace.inventory.fluid.Fluids.MINSOL, r);

        r = newRecipe(50, 100);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 1400);
        r.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.duna_sands, 4);
        register(Fluids.REDMUD, r);

        r = newRecipe(750, 50);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.AMMONIA, 300);
        r.input2 = new FluidStack(Fluids.UNSATURATEDS, 500);
        register(com.hbmspace.inventory.fluid.Fluids.DICYANOACETYLENE, r);

        r = newRecipe(450, 30);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.CHLOROETHANE, 250);
        r.input2 = new FluidStack(Fluids.REDMUD, 300);
        register(Fluids.ETHANOL, r);

        r = newRecipe(550, 50);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 300);
        r.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.scuttertail);
        register(com.hbmspace.inventory.fluid.Fluids.SCUTTERBLOOD, r);

        r = newRecipe(1000, 70);
        r.input1 = new FluidStack(Fluids.SULFURIC_ACID, 350);
        r.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.stone_resource, 1, BlockEnumsSpace.EnumStoneType.CONGLOMERATE.ordinal());
        register(com.hbmspace.inventory.fluid.Fluids.CONGLOMERA, r);

        r = newRecipe(1000, 80);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 250);
        r.solidInput = new RecipesCommon.OreDictStack(NIM.dust());
        register(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL, r);

        r = newRecipe(1000, 80);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.BRINE, 250);
        r.solidInput = new RecipesCommon.ComparableStack(ModItems.chunk_ore, 1, ItemEnums.EnumChunkType.MALACHITE);
        register(com.hbmspace.inventory.fluid.Fluids.AQUEOUS_COPPER, r);

        MixerRecipes.MixerRecipe v1 = newRecipe(1000, 20);
        v1.input1 = new FluidStack(Fluids.RADIOSOLVENT, 250);
        v1.solidInput = new RecipesCommon.ComparableStack(ModBlocksSpace.pvc_log);

        MixerRecipes.MixerRecipe v2 = newRecipe(500, 20);
        v2.input1 = new FluidStack(Fluids.RADIOSOLVENT, 125);
        v2.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.powder_pvc);

        MixerRecipes.MixerRecipe v3 = newRecipe(500, 20);
        v3.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.HGAS, 500);
        v3.input2 = new FluidStack(Fluids.UNSATURATEDS, 250);
        v3.solidInput = new RecipesCommon.ComparableStack(ModItems.powder_cadmium);

        register(com.hbmspace.inventory.fluid.Fluids.VINYL, v1, v2, v3);

        r = newRecipe(250, 20);
        r.input1 = new FluidStack(com.hbmspace.inventory.fluid.Fluids.HCL, 500);
        r.solidInput = new RecipesCommon.ComparableStack(ModItemsSpace.leaf_pet, 32);
        register(com.hbmspace.inventory.fluid.Fluids.CBENZ, r);
    }

    // THAT'S why I hate when accesstransformers refuse to work
    private static MixerRecipes.MixerRecipe newRecipe(int output, int processTime) {
        try {
            Constructor<MixerRecipes.MixerRecipe> c =
                    MixerRecipes.MixerRecipe.class.getDeclaredConstructor(int.class, int.class);
            c.setAccessible(true);
            return c.newInstance(output, processTime);
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct MixerRecipe via reflection", e);
        }
    }
}
