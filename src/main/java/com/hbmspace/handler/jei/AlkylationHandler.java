package com.hbmspace.handler.jei;

import com.hbm.handler.jei.JEIUniversalHandler;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.recipes.AlkylationRecipes;
import mezz.jei.api.IGuiHelper;
import net.minecraft.item.ItemStack;

public class AlkylationHandler extends JEIUniversalHandler {

    public AlkylationHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.ALKYLATION, "Alkylation", new ItemStack[]{new ItemStack(ModBlocksSpace.machine_alkylation)}, wrapRecipes2(AlkylationRecipes.getRecipes()));
    }
}
