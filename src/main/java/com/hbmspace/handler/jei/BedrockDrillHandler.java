package com.hbmspace.handler.jei;

import com.hbm.blocks.ModBlocks;
import mezz.jei.api.IGuiHelper;
import net.minecraft.item.ItemStack;

public class BedrockDrillHandler extends JEICelestialHandler {

    public BedrockDrillHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.BEDROCK, "jei.bedrock_drilling", new ItemStack[] { new ItemStack(ModBlocks.machine_excavator) }, CelestialJeiRecipes.getBedrockRecipes());
    }

}
