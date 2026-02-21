package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.api.recipe.IRecipeRegisterListener;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.materials.MatsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.inventory.recipes.anvil.AnvilRecipes.constructionRecipes;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

public class AnvilRecipeTweaker implements IRecipeRegisterListener {

    private static boolean listenerRegistered = false;
    private static boolean recipesAdded = false;

    public static void registerListener() {
        if (!listenerRegistered) {
            MatsSpace.forceInit();
            SerializableRecipe.additionalListeners.add(new AnvilRecipeTweaker());
            listenerRegistered = true;
        }
    }

    @Override
    public void onRecipeLoad(String recipeClass) {
        if ("AnvilRecipes".equals(recipeClass)) {
            addRecipesIfNeeded();
        }
    }

    public static void init() {
        for (SerializableRecipe handler : SerializableRecipe.recipeHandlers) {
            if (handler instanceof AnvilRecipes && handler.modified) {
                return;
            }
        }
        addRecipesIfNeeded();
    }

    private static synchronized void addRecipesIfNeeded() {
        if (recipesAdded) return;
        recipesAdded = true;

        registerCustomRecipes();
    }

    private static void registerCustomRecipes() {
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(new RecipesCommon.OreDictStack(NI.ingot()), new AnvilRecipes.AnvilOutput(new ItemStack(ModItemsSpace.plate_nickel))).setTier(3));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(new RecipesCommon.OreDictStack(STAINLESS.ingot()), new AnvilRecipes.AnvilOutput(new ItemStack(ModItemsSpace.plate_stainless))).setTier(3));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(new RecipesCommon.OreDictStack(COALCOKE.dust()), new AnvilRecipes.AnvilOutput(new ItemStack(ModItems.coke, 1, ItemEnums.EnumCokeType.COAL.ordinal()))).setTier(3));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(new RecipesCommon.OreDictStack(LIGCOKE.dust()), new AnvilRecipes.AnvilOutput(new ItemStack(ModItems.coke, 1, ItemEnums.EnumCokeType.LIGNITE.ordinal()))).setTier(3));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(new RecipesCommon.OreDictStack(PETCOKE.dust()), new AnvilRecipes.AnvilOutput(new ItemStack(ModItems.coke, 1, ItemEnums.EnumCokeType.PETROLEUM.ordinal()))).setTier(3));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(new RecipesCommon.OreDictStack(STAINLESS.ingot(), 1), new AnvilRecipes.AnvilOutput(new ItemStack(ModBlocksSpace.deco_stainless, 4))).setTier(1).setOverlay(AnvilRecipes.OverlayType.CONSTRUCTION));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(
                new RecipesCommon.AStack[]{
                        new RecipesCommon.OreDictStack(KEY_PLANKS, 6),
                        new RecipesCommon.OreDictStack(STEEL.plate(), 8),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 1, ItemEnums.EnumCircuitType.ANALOG),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 8, ItemEnums.EnumCircuitType.VACUUM_TUBE),
                        new RecipesCommon.ComparableStack(ModBlocks.pole_satellite_receiver, 1),
                }, new AnvilRecipes.AnvilOutput(new ItemStack(ModBlocksSpace.machine_dish_controller))).setTier(3));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(STEEL.shell(), 6),
                        new RecipesCommon.ComparableStack(ModItemsSpace.plate_stainless, 8),
                        new RecipesCommon.ComparableStack(ModBlocks.concrete_smooth, 4),
                        new RecipesCommon.ComparableStack(ModBlocks.heater_heatex),
                        new RecipesCommon.ComparableStack(ModBlocks.deco_pipe_quad, 10),
                },
                new AnvilRecipes.AnvilOutput(new ItemStack(ModBlocksSpace.machine_atmo_tower))).setTier(2));
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(
                new RecipesCommon.AStack[] {
                        new RecipesCommon.OreDictStack(STEEL.shell(), 4),
                        new RecipesCommon.ComparableStack(ModItemsSpace.plate_stainless, 4),
                        new RecipesCommon.ComparableStack(ModBlocks.concrete_smooth, 4),
                        new RecipesCommon.ComparableStack(ModItems.turbine_titanium, 1),
                },
                new AnvilRecipes.AnvilOutput(new ItemStack(ModBlocksSpace.machine_atmo_vent))).setTier(2));
        /*constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(
                new RecipesCommon.AStack[] {new RecipesCommon.ComparableStack(ModBlocks.glass_quartz, 3), new RecipesCommon.ComparableStack(ModItems.pill_herbal, 2), new RecipesCommon.ComparableStack(ModItems.powder_magic, 2)},
                new AnvilRecipes.AnvilOutput(new ItemStack(ModItems.flask_infusion, 1, ItemFlask.EnumInfusion.NITAN.ordinal()))).setTier(2));*/
        constructionRecipes.add(new AnvilRecipes.AnvilConstructionRecipe(new RecipesCommon.ComparableStack(ModBlocksSpace.deco_stainless, 4), new AnvilRecipes.AnvilOutput[]{new AnvilRecipes.AnvilOutput(new ItemStack(ModItemsSpace.ingot_stainless, 1))}).setTier(1));
    }
}