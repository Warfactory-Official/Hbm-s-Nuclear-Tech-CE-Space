package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.recipes.SolidificationRecipes;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;

import static com.hbm.inventory.fluid.Fluids.*;
import static com.hbmspace.inventory.fluid.Fluids.AQUEOUS_NICKEL;
import static com.hbmspace.inventory.fluid.Fluids.VINYL;

public class SolidificationRecipesTweaker {

    public static void init() {
        if(RecipeTweakerManager.isModified(SolidificationRecipes.class)) return;
        try {
            Method m = SolidificationRecipes.class.getDeclaredMethod("registerRecipe", FluidType.class, int.class, Item.class);
            Method m1 = SolidificationRecipes.class.getDeclaredMethod("registerRecipe", FluidType.class, int.class, Block.class);
            Method m2 = SolidificationRecipes.class.getDeclaredMethod("registerRecipe", FluidType.class, int.class, ItemStack.class);
            m.setAccessible(true);
            m1.setAccessible(true);
            m2.setAccessible(true);
            m2.invoke(null, BLOOD, 1290, new ItemStack(ModItemsSpace.flesh_wafer, 5));
            m1.invoke(null, CARBONDIOXIDE, 1000,			ModBlocksSpace.dry_ice);
            m.invoke(null, AQUEOUS_NICKEL, 500,		ModItemsSpace.nickel_salts);
            m.invoke(null, VINYL, 1000,				ModItems.ingot_pvc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
