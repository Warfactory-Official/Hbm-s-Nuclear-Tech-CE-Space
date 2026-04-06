package com.hbmspace.handler.jei;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.dim.CelestialBody;
import mezz.jei.api.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OilExtractionHandler extends JEICelestialHandler {

    public OilExtractionHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.OIL, "jei.oil_extract", new ItemStack[] {
                new ItemStack(ModBlocks.machine_well),
                new ItemStack(ModBlocks.machine_pumpjack),
                new ItemStack(ModBlocks.machine_fracking_tower)
        }, CelestialJeiRecipes.getOilRecipes());
    }

    @Override
    protected void buildRecipes(HashMap<Object, Object> recipeMap, ItemStack[] machines) {
        for (Map.Entry<Object, Object> entry : recipeMap.entrySet()) {
            if (entry.getKey() instanceof CelestialBody body) {
                ItemStack[] outputs = extractOutput(entry.getValue());

                if (outputs.length > 0) {
                    recipes.add(new JeiOilRecipe(body, outputs, machines));
                }
            }
        }
    }

    public static class JeiOilRecipe extends JeiCelestialRecipe {
        public JeiOilRecipe(CelestialBody body, ItemStack[] outputs, ItemStack[] machines) {
            super(body, outputs, machines);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

            if (body != null && ("kerbin".equals(body.name) || "tekto".equals(body.name))) {
                minecraft.fontRenderer.drawString("Frackable", 102, 48, 0x000000);
            }
        }
    }
}
