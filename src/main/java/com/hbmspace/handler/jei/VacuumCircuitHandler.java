package com.hbmspace.handler.jei;

import com.hbm.handler.jei.JEIUniversalHandler;
import com.hbm.handler.jei.JeiRecipes;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.inventory.recipes.VacuumCircuitRecipes;
import mezz.jei.api.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VacuumCircuitHandler extends JEIUniversalHandler {

    public VacuumCircuitHandler(IGuiHelper helper) {
        super(helper, JEIConfigSpace.VACUUM_CIRCUIT, ModBlocksSpace.machine_vacuum_circuit.getTranslationKey(),
                new ItemStack[]{new ItemStack(ModBlocksSpace.machine_vacuum_circuit)}, VacuumCircuitRecipes.getRecipes());
    }

    @Override
    protected void buildRecipes(HashMap<Object, Object> recipeMap, ItemStack[] machines) {
        for (Map.Entry<Object, Object> entry : recipeMap.entrySet()) {
            List<List<ItemStack>> inputsList = extractInputLists(entry.getKey());
            if (inputsList.isEmpty()) continue;

            ItemStack output = extractFirstOutput(entry.getValue());
            if (output.isEmpty()) continue;

            int duration = 0;
            int consumption = 0;

            for (VacuumCircuitRecipes.VacuumCircuitRecipe sol : VacuumCircuitRecipes.recipes) {
                if (ItemStack.areItemStacksEqual(sol.output, output)) {
                    duration = sol.duration;
                    consumption = (int)sol.consumption;
                    break;
                }
            }

            recipes.add(new VacuumCircuitRecipe(
                    inputsList,
                    new ItemStack[]{output.copy()},
                    machines,
                    duration,
                    consumption
            ));
        }
    }

    private ItemStack extractFirstOutput(Object value) {
        if (value instanceof ItemStack) {
            return ((ItemStack) value).copy();
        } else if (value instanceof ItemStack[] arr) {
            return arr.length > 0 ? arr[0].copy() : ItemStack.EMPTY;
        } else if (value instanceof List list) {
            if (list.isEmpty()) return ItemStack.EMPTY;
            Object first = list.get(0);
            if (first instanceof ItemStack) {
                return ((ItemStack) first).copy();
            } else if (first instanceof List inner) {
                if (inner.isEmpty()) return ItemStack.EMPTY;
                Object firstInner = inner.get(0);
                if (firstInner instanceof ItemStack) {
                    return ((ItemStack) firstInner).copy();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static class VacuumCircuitRecipe extends JeiRecipes.JeiUniversalRecipe {
        private final int duration;
        private final int consumption;

        public VacuumCircuitRecipe(List<List<ItemStack>> inputs, ItemStack[] outputs, ItemStack[] machine, int duration, int consumption) {
            super(inputs, outputs, machine);
            this.duration = duration;
            this.consumption = consumption;
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
            FontRenderer fontRenderer = minecraft.fontRenderer;
            String durationStr = String.format(Locale.US, "%,d", duration) + " ticks";
            String consumptionStr = String.format(Locale.US, "%,d", consumption) + " HE/t";
            int side = 160;
            fontRenderer.drawString(durationStr, side - fontRenderer.getStringWidth(durationStr), 43, 0x404040);
            fontRenderer.drawString(consumptionStr, side - fontRenderer.getStringWidth(consumptionStr), 55, 0x404040);
        }
    }
}
