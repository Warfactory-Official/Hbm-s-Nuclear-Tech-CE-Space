package com.hbmspace.inventory.gui;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.items.ISatChip;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toserver.NBTControlPacket;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import com.hbmspace.inventory.container.ContainerMachineWarController;
import com.hbmspace.packet.toserver.SatActivatePacket;
import com.hbmspace.saveddata.satellites.SatelliteWar;
import com.hbmspace.tileentity.machine.TileEntityMachineWarController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIWarController extends GuiInfoContainer {

    private List<String> commandHistory = new ArrayList<>();
    private static final int MAX_HISTORY = 6; // Number of lines to keep visible
    private GuiTextField textField;

    private static final ResourceLocation texture = new ResourceLocation("hbm:textures/gui/machine/gui_controlpanel.png");
    private TileEntityMachineWarController sucker;

    public GUIWarController(InventoryPlayer playerInv, TileEntityMachineWarController tile) {
        super(new ContainerMachineWarController(playerInv, tile));

        this.sucker = tile;
        this.xSize = 176;
        this.ySize = 204;
    }

    @Override
    public void drawScreen(int x, int y, float interp) {
        super.drawScreen(x, y, interp);
        super.renderHoveredToolTip(x, y);

        this.drawElectricityInfo(this, x, y, guiLeft + 132, guiTop + 18, 16, 52, sucker.getPower(), sucker.getMaxPower());
        this.drawCustomInfoStat(x, y, guiLeft + 52, guiTop + 19, 8, 8, guiLeft + 52, guiTop + 19, this.getUpgradeInfo(sucker));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j) {
        // String name = this.sucker.hasCustomInventoryName() ? this.sucker.getInventoryName() : I18n.format(this.sucker.getInventoryName());
        // this.fontRenderer.drawString(name, this.xSize / 2 -
        // this.fontRenderer.getStringWidth(name) / 2 - 1, 60, 4210752);
        // this.fontRenderer.drawString(I18n.format("container.inventory"), 8,
        // this.ySize - 115 + 2, 4210752);

        int yOffset = 15;
        for(int x = Math.max(0, commandHistory.size() - MAX_HISTORY); x < commandHistory.size(); x++) {
            String text = commandHistory.get(x);

            float scale = 0.5F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(text, (int) (78 / scale), (int) (yOffset / scale), 0x00FF00);
            GlStateManager.popMatrix();

            yOffset += (int) (12 * scale);
        }

        String typedText = textField.getText();
        float textScale = 0.5F;

        GlStateManager.pushMatrix();
        GlStateManager.scale(textScale, textScale, textScale);
        fontRenderer.drawString("> " + typedText + (textField.isFocused() && (mc.world.getTotalWorldTime() % 20 < 10) ? "_" : ""), (int) (78 / textScale), (int) (57 / textScale), 0x00FF00);
        GlStateManager.popMatrix();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(textField.textboxKeyTyped(typedChar, keyCode)) return;

        if(keyCode == Keyboard.KEY_RETURN) {
            processCommand(textField.getText().trim());
            textField.setText(""); // Clear after processing
        }

        super.keyTyped(typedChar, keyCode);
    }

    private void processCommand(String command) {
        if(command.isEmpty()) return;

        int satId = ISatChip.getFreqS(sucker.inventory.getStackInSlot(2));
        Satellite sat = SatelliteSavedData.getClientSats().get(satId);
        String[] parts = command.split(" ");

        String cmd = parts[0].toLowerCase();
        NBTTagCompound data = new NBTTagCompound();

        switch(cmd) {

            case "setpos":
                if(parts.length < 3) {
                    addCommandHistory("> " + command);
                    addCommandHistory("Error, invalid args", TextFormatting.RED);
                    return;
                }

                String xValueStr = parts[1];
                String zValueStr = parts[2];
                if(sucker.inventory.getStackInSlot(1).isEmpty()) {
                    addCommandHistory("> " + command);
                    addCommandHistory("No drive.", TextFormatting.RED);
                    return;
                }
                if(!xValueStr.matches("-?\\d+") || !zValueStr.matches("-?\\d+")) {
                    addCommandHistory("> " + command);
                    addCommandHistory("Invalid number format.", TextFormatting.RED);
                    return;
                }

                int xValue = Integer.parseInt(xValueStr);
                int zValue = Integer.parseInt(zValueStr);

                addCommandHistory("> " + command);
                addCommandHistory("Set to: X=" + xValue + ", Z=" + zValue);

                data.setInteger("xcoord", xValue);
                data.setInteger("zcoord", zValue);

                PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, sucker.getPos()));
                break;

            case "health":
                addCommandHistory("> " + command);
                addCommandHistory("Requesting " + cmd + "...");

                if(sat == null) {
                    addCommandHistory("Satellite not in orbit!", TextFormatting.RED);
                } else {
                    if(sat instanceof SatelliteWar) {
                        addCommandHistory("health: " + ((SatelliteWar) sat).getInterp());
                    }
                }

                break;

            case "fire":
                if(sat == null) {
                    addCommandHistory("Satellite not in orbit!", TextFormatting.RED);
                } else {
                    if(sat instanceof SatelliteWar) {
                        addCommandHistory("Firing!");

                        PacketDispatcher.wrapper.sendToServer(new SatActivatePacket(satId));
                    } else {
                        addCommandHistory("Wrong satellite" + TextFormatting.RED);
                    }
                }
                break;

            case "getsat":
                addCommandHistory("> " + command);
                if(sucker.inventory.getStackInSlot(2).isEmpty()) {
                    addCommandHistory("No satellite chip in slot 2.", TextFormatting.RED);
                } else {
                    addCommandHistory("Requesting Satellite ID: " + satId);

                    if(sat == null) {
                        addCommandHistory("Satellite not in orbit!", TextFormatting.RED);
                    } else {
                        addCommandHistory("Satellite: " + sat.getClass().getSimpleName());
                    }
                }
                break;

            default:
                addCommandHistory("> " + command);
                addCommandHistory("Unknown command.", TextFormatting.RED);
                break;
        }

    }

    private void addCommandHistory(String text) {
        addToHistory(text, null);
    }

    private void addCommandHistory(String text, TextFormatting color) {
        addToHistory(text, color);
    }

    private void addToHistory(String text, TextFormatting color) {
        if(color != null) {
            ITextComponent chatComponent = new TextComponentString(text);
            chatComponent.getStyle().setColor(color);
            commandHistory.add(chatComponent.getFormattedText());
        } else {
            commandHistory.add(text);
        }

        if(commandHistory.size() > MAX_HISTORY * 2) {
            commandHistory = commandHistory.subList(commandHistory.size() - MAX_HISTORY * 2, commandHistory.size());
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float interp, int x, int y) {
        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        textField = new GuiTextField(0, fontRenderer, guiLeft - 74, guiTop + 28, 100, 20);
        textField.setMaxStringLength(100);
        textField.setFocused(true);
        textField.setEnableBackgroundDrawing(false);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

}
