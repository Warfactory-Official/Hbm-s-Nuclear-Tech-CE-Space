package com.hbmspace.handler.jei;

import com.hbm.handler.jei.JEIUniversalHandler;
import com.hbm.handler.jei.JeiRecipes;
import com.hbmspace.dim.CelestialBody;
import com.hbm.util.Clock;
import mezz.jei.api.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class JEICelestialHandler extends JEIUniversalHandler {

    public JEICelestialHandler(IGuiHelper helper, String uid, String titleKey, ItemStack[] machines, HashMap<Object, Object> recipeMap) {
        super(helper, uid, titleKey, machines, recipeMap);
    }

    @Override
    protected void buildRecipes(HashMap<Object, Object> recipeMap, ItemStack[] machines) {
        for (Map.Entry<Object, Object> entry : recipeMap.entrySet()) {
            if (entry.getKey() instanceof CelestialBody body) {
                ItemStack[] outputs = extractOutput(entry.getValue());

                if (outputs.length > 0) {
                    recipes.add(new JeiCelestialRecipe(body, outputs, machines));
                }
            }
        }
    }

    public static class JeiCelestialRecipe extends JeiRecipes.JeiUniversalRecipe {
        protected final CelestialBody body;

        public JeiCelestialRecipe(CelestialBody body, ItemStack[] outputs, ItemStack[] machines) {
            super(Collections.emptyList(), outputs, machines);
            this.body = body;
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

            if (body != null && body.texture != null) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                minecraft.getTextureManager().bindTexture(body.texture);

                double uvOffset = (double) (Clock.get_ms() % 4000L) / 4000.0D;

                double minX = 32;
                double minY = 12;
                double maxX = minX + 32;
                double maxY = minY + 32;

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(minX, maxY, 0).tex(uvOffset, 1).endVertex();
                buffer.pos(maxX, maxY, 0).tex(1 + uvOffset, 1).endVertex();
                buffer.pos(maxX, minY, 0).tex(1 + uvOffset, 0).endVertex();
                buffer.pos(minX, minY, 0).tex(uvOffset, 0).endVertex();
                tessellator.draw();

                String name = I18n.format("body." + body.name);
                int stringWidth = minecraft.fontRenderer.getStringWidth(name);
                minecraft.fontRenderer.drawString(name, 48 - stringWidth / 2, 48, 0x000000);
            }
        }
    }
}
