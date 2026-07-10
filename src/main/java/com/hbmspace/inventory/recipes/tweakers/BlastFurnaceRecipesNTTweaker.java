package com.hbmspace.inventory.recipes.tweakers;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.BlastFurnaceRecipe;
import com.hbm.inventory.recipes.BlastFurnaceRecipesNT;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;

import static com.hbm.inventory.OreDictManager.STEEL;
import static com.hbmspace.inventory.OreDictManagerSpace.NI;

public class BlastFurnaceRecipesNTTweaker {

    public static void init() {
        BlastFurnaceRecipesNT recs = BlastFurnaceRecipesNT.INSTANCE;

        recs.register((BlastFurnaceRecipe) new BlastFurnaceRecipe("blast.stainless").setDuration(400)
                .inputItems(new RecipesCommon.OreDictStack(STEEL.ingot()), new RecipesCommon.OreDictStack(NI.ingot()))
                .outputItems(new ItemStack(ModItemsSpace.ingot_stainless, 2)));
    }
}
