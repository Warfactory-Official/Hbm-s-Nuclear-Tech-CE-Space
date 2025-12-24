package com.hbmspace.inventory.gui;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toserver.NBTControlPacket;
import com.hbmspace.inventory.container.ContainerLaunchPadRocket;
import com.hbmspace.render.misc.RocketPronter;
import com.hbmspace.tileentity.bomb.TileEntityLaunchPadRocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

public class GUILaunchPadRocket extends GuiInfoContainer {

    private static ResourceLocation texture = new ResourceLocation("hbm" + ":textures/gui/machine/gui_launchpad_rocket.png");
    private TileEntityLaunchPadRocket machine;

    public GUILaunchPadRocket(InventoryPlayer invPlayer, TileEntityLaunchPadRocket machine) {
        super(new ContainerLaunchPadRocket(invPlayer, machine));
        this.machine = machine;

        xSize = 188;
        ySize = 236;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        super.drawScreen(mouseX, mouseY, f);
        super.renderHoveredToolTip(mouseX, mouseY);

        this.drawElectricityInfo(this, mouseX, mouseY, guiLeft + 167, guiTop + 36, 16, 52, machine.power, machine.maxPower);

        this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 56, guiTop + 20, 18, 17, mouseX, mouseY, new String[]{"COMMIT TO LAUNCH"} );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int p = (int) (machine.power * 52 / machine.maxPower);
        drawTexturedModalRect(guiLeft + 167, guiTop + 36 + 52 - p, xSize, 8 + 52 - p, 16, p);

        if(machine.rocket != null) {
            int ox = machine.canLaunch() ? 12 : 0;
            drawTexturedModalRect(guiLeft + 59, guiTop + 43, xSize + ox, 0, 12, 8);

            ox = machine.power > machine.maxPower * 0.75 ? 0 : 6;
            drawTexturedModalRect(guiLeft + 172, guiTop + 23, xSize + 16 + ox, 8, 6, 8);

            GlStateManager.pushMatrix();
            {

                GUISpaceUtil.pushScissor(this.mc, guiLeft, guiTop, ySize, 97, 18, 50, 106);

                GlStateManager.translate(guiLeft + 122, guiTop + 119, 100);
                GlStateManager.rotate((int) (System.currentTimeMillis() / 10) % 360, 0, -1, 0);

                double size = 86;
                double height = machine.rocket.getHeight();
                double targetScale = size / Math.max(height, 6);

                GlStateManager.scale(-targetScale, -targetScale, -targetScale);

                RocketPronter.prontRocket(machine.rocket, Minecraft.getMinecraft().getTextureManager());

                GUISpaceUtil.popScissor();

            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {

                GlStateManager.scale(0.5F, 0.5F, 0.5F);

                List<String> issues = machine.findIssues();
                for(int i = 0; i < issues.size(); i++) {
                    String issue = issues.get(i);
                    fontRenderer.drawString(issue, (guiLeft + 6) * 2, (guiTop + 66) * 2 + i * 8, 0xFFFFFF);
                }

            }
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int i) throws IOException {
        super.mouseClicked(x, y, i);

        // COMMIT TO LAUNCH
        if(machine.rocket != null && machine.rocket.validate() && checkClick(x, y, 56, 20, 18, 17)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            NBTTagCompound data = new NBTTagCompound();
            data.setBoolean("launch", true);
            PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, machine.getPos()));
        }
    }

}
