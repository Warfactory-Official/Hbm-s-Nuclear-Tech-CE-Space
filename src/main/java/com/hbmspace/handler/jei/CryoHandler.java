package com.hbmspace.handler.jei;

import com.hbm.handler.jei.JEIUniversalHandler;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.recipes.CryoRecipes;
import mezz.jei.api.IGuiHelper;
import net.minecraft.item.ItemStack;

public class CryoHandler extends JEIUniversalHandler {

    public CryoHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.CRYO, "Cryogenic Distillation", new ItemStack[]{new ItemStack(ModBlocksSpace.machine_cryo_distill)}, CryoRecipes.getCryoRecipes());
    }
}
