package com.hbmspace.inventory.gui;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbmspace.inventory.container.ContainerMachineDischarger;
import com.hbmspace.tileentity.machine.TileEntityMachineDischarger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIMachineDischarger extends GuiInfoContainer {

    private static final ResourceLocation texture = new ResourceLocation("hbm:textures/gui/gui_discharger.png");
    private static ResourceLocation geer = new ResourceLocation("hbm:textures/gui/geer2.png");
    private TileEntityMachineDischarger diFurnace;

    public GUIMachineDischarger(InventoryPlayer invPlayer, TileEntityMachineDischarger tedf) {
        super(new ContainerMachineDischarger(invPlayer, tedf));
        diFurnace = tedf;

        this.xSize = 176;
        this.ySize = 222;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        super.drawScreen(mouseX, mouseY, f);
        super.renderHoveredToolTip(mouseX, mouseY);

        this.drawElectricityInfo(this, mouseX, mouseY, guiLeft + 8, guiTop + 106 - 88, 16, 88, diFurnace.power, diFurnace.maxPower);
        this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 30, guiTop + 25, 8, 80, mouseX, mouseY, new String[] {"Temperature: " + (diFurnace.temp) + "°C"});
        String[] text = new String[] { "Accepted Fuels:",
                "Uranium-233",
                "Schrabidium",
                "Dineutronium" };
        this.drawCustomInfoStat(mouseX, mouseY, guiLeft - 16, guiTop + 36, 16, 16, guiLeft - 8, guiTop + 36 + 16, text);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j) {
        String name = this.diFurnace.hasCustomName() ? this.diFurnace.getName() : I18n.format(this.diFurnace.getName());

        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
        //this.fontRenderer.drawString(I18n.format(String.valueOf(diFurnace.getPower()) + " HE"), this.xSize / 2 - this.fontRenderer.getStringWidth(String.valueOf(diFurnace.getPower()) + " HE") / 2, 16, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }



    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawTexturedModalRect(guiLeft -16, guiTop + 35, 176, 106, 16, 50);
        //this.drawInfoPanel(guiLeft - 16, guiTop + 10, 16, 16, 2);
        if (diFurnace.getPower() > 0) {
            int i = (int) diFurnace.getPowerScaled(88);
            drawTexturedModalRect(guiLeft + 8, guiTop + 106 - i, 176, 88 - i, 16, i);

        }

        if (diFurnace.isProcessing()) {
            int j1 = diFurnace.getProgressScaled(88);
            Minecraft.getMinecraft().getTextureManager().bindTexture(geer);
            drawTexturedModalRect(guiLeft - 36, guiTop + 109 - j1, 176, 88 - j1, 185, 108);
        }
        if(diFurnace.temp > 20) {
            int i = (int) diFurnace.getTempScaled(88);
            drawTexturedModalRect(guiLeft + 30, guiTop + 106 - i, 192, 88 - i, 20, i);
            if(diFurnace.temp < 2000) {
                drawTexturedModalRect(guiLeft + 28, guiTop + 108, 176, 88, 8, 18);
            }
            if(diFurnace.temp < 800) {
                drawTexturedModalRect(guiLeft + 28, guiTop + 108, 184, 88, 8, 18);
            }
            if(diFurnace.temp < 200) {
                drawTexturedModalRect(guiLeft + 28, guiTop + 108, 192, 88, 8, 18);
            }

        }
    }
}
