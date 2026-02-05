package com.hbmspace.handler.jei;

import com.hbm.blocks.ModBlocks;
import com.hbm.handler.jei.JEIUniversalHandler;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.fluid.Fluids;
import com.hbm.items.machine.ItemFluidIcon;
import mezz.jei.api.IGuiHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class DairyHandler extends JEIUniversalHandler {

    public DairyHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.DAIRY, "Dairy", new ItemStack[]{new ItemStack(ModBlocksSpace.machine_milk_reformer)}, getDairyRecipesForJEI());
    }

    public static HashMap<Object, Object> getDairyRecipesForJEI() {

        HashMap<Object, Object> recipes = new HashMap<>();

        ItemStack[] in = new ItemStack[] {
                ItemFluidIcon.make(Fluids.MILK, 100),
        };

        ItemStack[] out = new ItemStack[] {
                ItemFluidIcon.make(Fluids.EMILK, 50),
                ItemFluidIcon.make(Fluids.CMILK, 35),
                ItemFluidIcon.make(Fluids.CREAM, 15),
        };

        recipes.put(in, out);

        return recipes;
    }

}
