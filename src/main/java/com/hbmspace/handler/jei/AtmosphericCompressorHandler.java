package com.hbmspace.handler.jei;

import com.hbmspace.blocks.ModBlocksSpace;
import mezz.jei.api.IGuiHelper;
import net.minecraft.item.ItemStack;

public class AtmosphericCompressorHandler extends JEICelestialHandler {

    public AtmosphericCompressorHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.ATMO, "jei.atmosphere", new ItemStack[] { new ItemStack(ModBlocksSpace.machine_atmo_vent) }, CelestialJeiRecipes.getAtmoRecipes());
    }
}
