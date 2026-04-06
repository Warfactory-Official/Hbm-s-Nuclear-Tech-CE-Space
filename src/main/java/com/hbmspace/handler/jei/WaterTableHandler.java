package com.hbmspace.handler.jei;

import com.hbm.blocks.ModBlocks;
import mezz.jei.api.IGuiHelper;
import net.minecraft.item.ItemStack;

public class WaterTableHandler extends JEICelestialHandler {

    public WaterTableHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.WATER, "jei.fluid_table", new ItemStack[] {
                new ItemStack(ModBlocks.pump_electric),
                new ItemStack(ModBlocks.pump_steam)
        }, CelestialJeiRecipes.getWalterRecipes());
    }

}
