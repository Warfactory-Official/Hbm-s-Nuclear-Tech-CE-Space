package com.hbmspace.inventory.gui;

import com.hbmspace.config.SpaceConfig;
import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toserver.NBTControlPacket;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import com.hbm.util.I18nUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.trait.CBT_War;
import com.hbmspace.inventory.container.ContainerStardar;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineStardar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GUIMachineStardar extends GuiInfoContainer {

    public static ResourceLocation texture = new ResourceLocation( "hbm" + ":textures/gui/machine/gui_stardar.png");
    private static final ResourceLocation nightTexture = new ResourceLocation("hbm", "textures/misc/space/night.png");

    private TileEntityMachineStardar star;
    private CelestialBody currentBody;

    private int mX, mY; // current mouse position
    private int lX, lY; // mouse position last frame (for flinging)
    private int sX, sY; // starting mouse position (for verifying clicks)
    private boolean dragging = false;

    private float starX = 0, starY = 0;
    private float velocityX = 0, velocityY = 0;
    private List<POI> pList = new ArrayList<>();
    Random rnd = new Random();

    private final DynamicTexture groundTexture;
    private final ResourceLocation groundMap;
    private final int[] groundColors;

    public void init() {
        currentBody = CelestialBody.getBody(star.getWorld());
        boolean inOrbit = CelestialBody.inOrbit(star.getWorld());

        for(CelestialBody landable : CelestialBody.getLandableBodies()) {
            if(landable != currentBody || inOrbit) {
                int posX = rnd.nextInt(400) - 200;
                int posY = rnd.nextInt(400) - 200;
                pList.add(new POI(posX, posY, landable));
            }
        }
    }

    public GUIMachineStardar(InventoryPlayer iplayer, TileEntityMachineStardar restard) {
        super(new ContainerStardar(iplayer, restard));
        this.star = restard;

        this.xSize = 176;
        this.ySize = 256;
        init();

        groundTexture = new DynamicTexture(256, 256);
        groundMap = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("groundMap", groundTexture);
        groundColors = groundTexture.getTextureData();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        super.drawScreen(mouseX, mouseY, f);
        super.renderHoveredToolTip(mouseX, mouseY);

        this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 129, guiTop + 143, 18, 18, mouseX, mouseY, new String[] {"Program new orbital station into drive"} );
        this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 149, guiTop + 143, 18, 18, mouseX, mouseY, new String[] {"Program current body into drive"} );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float interp, int x, int y) {
        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GUISpaceUtil.pushScissor(this.mc, guiLeft, guiTop, ySize, 9, 9, 158, 108);

        if(!star.radarMode) {
            if(!Mouse.isButtonDown(0)) {
                velocityX *= 0.85;
                velocityY *= 0.85;
                starX += velocityX;
                starY += velocityY;
                starX = MathHelper.clamp(starX, -256 + 158, 256);
                starY = MathHelper.clamp(starY, -256 + 108, 256);
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                starY++;
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                starY--;
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                starX++;
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                starX--;
            }

            if(star.heightmap == null) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(nightTexture);
                drawTexturedModalRect(guiLeft, guiTop, (int) starX * -1, (int) starY * -1, 256, 256);

                Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

                for(POI peepee : pList) {
                    int px = (int) (guiLeft + starX + peepee.offsetX);
                    int py = (int) (guiTop + starY + peepee.offsetY);

                    drawTexturedModalRect(px, py, xSize + peepee.body.getProcessingLevel(currentBody) * 8, 0, 8, 8);
                }
            } else {
                if(star.updateHeightmap) {
                    for(int i = 0; i < star.heightmap.length; i++) {
                        int h = star.heightmap[i] % 16 * 16;

                        int r = 0;
                        int g = h;
                        int b = 0;
                        int a = 255;

                        groundColors[i] = a << 24 | r << 16 | g << 8 | b;
                    }

                    groundTexture.updateDynamicTexture();
                    star.updateHeightmap = false;
                }

                mc.getTextureManager().bindTexture(groundMap);
                drawModalRectWithCustomSizedTexture(guiLeft, guiTop, (int) starX * -1 - 256 - 9, (int) starY * -1 - 256 - 9, 256, 256, 512, 512);
            }
        }
        else {
            if(!Mouse.isButtonDown(0)) {
                velocityX *= 0.85;
                velocityY *= 0.85;
                starX += velocityX;
                starY += velocityY;
                starX = MathHelper.clamp(starX, -256 + 158, 256);
                starY = MathHelper.clamp(starY, -256 + 108, 256);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(nightTexture);
            drawTexturedModalRect(guiLeft, guiTop, (int) starX * -1, (int) starY * -1, 256, 256);

            if(CelestialBody.getBody(star.getWorld()).hasTrait(CBT_War.class)) {
                CBT_War wardat = CelestialBody.getTrait(star.getWorld(), CBT_War.class);
                for (int i = 0; i < wardat.getProjectiles().size(); i++) {
                    CBT_War.Projectile projectile = wardat.getProjectiles().get(i);
                    int projvel = (int) projectile.getTravel();
                    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

                    float randomAngle = projectile.GUIangle;
                    float offsetX = (float) Math.cos(Math.toRadians(randomAngle)) * projvel;
                    float offsetY = (float) Math.sin(Math.toRadians(randomAngle)) * projvel;

                    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

                    long currentTime = System.currentTimeMillis();

                    if (currentTime % 300 < 120) {
                        drawTexturedModalRect(
                                (int) (guiLeft + starX + offsetX + 85),
                                (int) (guiTop + starY + offsetY + 60),
                                xSize + 44, 0, 8, 8
                        );
                    }
                }
            }

            for(Map.Entry<Integer, Satellite> entry : SatelliteSavedData.getClientSats().entrySet()) {
                float radius = 20 + ((float)entry.getKey() / 1000);
                float initialAngle = ((float)entry.getKey() / 1000) * 10f;

                long currentTime = System.currentTimeMillis();

                float angle = (((float)currentTime / 80) % 360 + initialAngle) % 360;

                float offsetX = (float) Math.cos(Math.toRadians(angle)) * radius;
                float offsetY = (float) Math.sin(Math.toRadians(angle)) * radius;

                Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

                drawTexturedModalRect(
                        (int) (guiLeft + starX + offsetX + 85),
                        (int) (guiTop + starY + offsetY + 60),
                        xSize + 8, 0, 8, 8
                );
            }

            GlStateManager.pushMatrix();

            Minecraft.getMinecraft().getTextureManager().bindTexture(CelestialBody.getBody(star.getWorld()).texture);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            int offsetX = 85;
            int offsetY = 60;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(guiLeft + starX + offsetX - 5, guiTop + starY + offsetY + 11, 0).tex(0F, 1F).endVertex();
            buffer.pos(guiLeft + starX + offsetX + 11, guiTop + starY + offsetY + 11, 0).tex(1F, 1F).endVertex();
            buffer.pos(guiLeft + starX + offsetX + 11, guiTop + starY + offsetY - 5, 0).tex(1F, 0F).endVertex();
            buffer.pos(guiLeft + starX + offsetX - 5, guiTop + starY + offsetY - 5, 0).tex(0F, 0F).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();

        }
        GUISpaceUtil.popScissor();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        ItemStack slotStack = inventorySlots.getSlot(0).getStack();

        if(!star.radarMode) {
            if(checkClick(mx, my, 9, 9, 158, 108)) {
                if(star.heightmap == null) {
                    for(POI poi : pList) {
                        int px = (int) (starX + poi.offsetX);
                        int py = (int) (starY + poi.offsetY);

                        // Has a small buffer area around the POI to improve click targeting
                        drawCustomInfoStat(mx - guiLeft, my - guiTop, px - 2, py - 2, 12, 12, px + 8, py + 10, new String[]{I18nUtil.resolveKey("body." + poi.body.name), "Processing Tier: " + poi.body.getProcessingLevel(currentBody)});
                    }
                } else {
                    GUISpaceUtil.pushScissor(this.mc, guiLeft, guiTop, ySize, 9, 9, 158, 108);

                    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

                    // translate from mouse coordinate to heightmap coordinate
                    int hx = (mx - (int)starX - guiLeft - 9 + 256) / 2;
                    int hz = (my - (int)starY - guiTop - 9 + 256) / 2;

                    String info = landingInfo(hx, hz);
                    int altitude = altitude(hx, hz);
                    boolean canLand = info == null;

                    int sx = mx - ((mx + (int)starX) % 2);
                    int sy = my - ((my + (int)starY) % 2);
                    drawTexturedModalRect(sx - 6 - guiLeft, sy - 6 - guiTop, xSize + (canLand ? 14 : 0), 28, 14, 14);

                    GUISpaceUtil.popScissor();

                    fontRenderer.drawString(canLand ? "Valid location" : info, 10, 128, canLand ? 0x00FF00 : 0xFF0000);
                    if(altitude > 0) fontRenderer.drawString("Target altitude: " + altitude, 10, 148, 0x00FF00);
                }
            } else if(star.heightmap != null) {
                fontRenderer.drawString("Select landing zone", 10, 128, 0x00FF00);
            }

            if(star.heightmap == null) {
                if(slotStack.isEmpty()) {
                    fontRenderer.drawString("Insert drive", 10, 128, 0x00FF00);
                } else {
                    if(slotStack.getItem() == ModItemsSpace.full_drive) {
                        if(ItemVOTVdrive.getDestination(slotStack).body == SolarSystem.Body.ORBIT) {
                            fontRenderer.drawString("Orbital station ready", 10, 128, 0x00FF00);
                        } else {
                            fontRenderer.drawString("Loading heightmap...", 10, 128, 0x00FF00);
                            fontRenderer.drawString("Please wait", 10, 148, 0x00FF00);
                        }
                    } else if(slotStack.getItem() == ModItemsSpace.hard_drive) {
                        fontRenderer.drawString("Select body", 10, 128, 0x00FF00);
                        fontRenderer.drawString("Drag map to pan", 10, 148, 0x00FF00);
                    }
                }
            }
        }
    }

    private String landingInfo(int x, int z) {
        if(star.heightmap == null) return "No heightmap";
        if(x < 3 || x > 252 || z < 3 || z > 252) return "Outside bounds";

        for(int ox = x - 2; ox <= x + 2; ox++) {
            for(int oz = z - 2; oz <= z + 2; oz++) {
                if(star.heightmap[256 * oz + ox] != star.heightmap[256 * z + x]) {
                    return "Area not flat";
                }
            }
        }

        return null;
    }

    private int altitude(int x, int z) {
        if(star.heightmap == null) return -1;
        if(x < 0 || x > 255 || z < 0 || z > 255) return -1;

        return star.heightmap[256 * z + x];
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        //if(star.radarMode) return;
        int button = Mouse.getEventButton();
        if(starX > 256) {
            velocityX = 0;
            starX = 256;
        }

        if(starX < -256 + 158) {
            velocityX = 0;
            starX = -256 + 158;
        }

        if(starY < -256 + 108) {
            velocityY = 0;
            starY = -256 + 108;
        }

        if(starY > 256) {
            velocityY = 0;
            starY = 256;
        }

        if(dragging && button == 0 && !Mouse.getEventButtonState()) {
            velocityX = (mX - lX) * 0.8f;
            velocityY = (mY - lY) * 0.8f;
            dragging = false;
        }
    }

    @Override
    protected void mouseClickMove(int x, int y, int p_146273_3_, long p_146273_4_) {
        super.mouseClickMove(x, y, p_146273_3_, p_146273_4_);
        if(!dragging) return;
        //if(star.radarMode) return;
        int deltaX = x - mX;
        int deltaY = y - mY;
        starX += deltaX;
        starY += deltaY;
        lX = mX;
        lY = mY;
        mX = x;
        mY = y;
    }

    @Override
    protected void mouseClicked(int x, int y, int i) throws IOException {
        super.mouseClicked(x, y, i);
        if(checkClick(x, y, 9, 9, 158, 108)) {
            dragging = true;

            sX = mX = lX = x;
            sY = mY = lY = y;
        } else {
            dragging = false;
        }

        // Clicking ORB will generate a new orbital station
        if(checkClick(x, y, 129, 143, 18, 18)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            NBTTagCompound data = new NBTTagCompound();
            data.setInteger("pid", SpaceConfig.orbitDimension);

            PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, star.getPos()));
        }

        // Clicking RAD will activate Orrey Mode
        if(checkClick(x, y, 129, 123, 18, 18)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            NBTTagCompound data = new NBTTagCompound();
            data.setBoolean("radarmode", true);
            if(star.radarMode) {
                data.setBoolean("radarmode", false);
            }
            //starY =0;
            //starX =0;
            PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, star.getPos()));
        }

        // Clicking SLF will load in the current body
        if(checkClick(x, y, 149, 143, 18, 18)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            NBTTagCompound data = new NBTTagCompound();
            data.setInteger("pid", star.getWorld().provider.getDimension());
            data.setInteger("ix", star.getPos().getX());
            data.setInteger("iz", star.getPos().getZ());

            PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, star.getPos()));
        }
    }

    // which==-1 is mouseMove, which==0 or which==1 is mouseUp
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 1) return;

        if (Math.abs(sX - mouseX) > 2 || Math.abs(sY - mouseY) > 2) return;

        if (checkClick(mouseX, mouseY, 9, 9, 158, 108)) {
            if (star.heightmap == null) {
                for (POI poi : pList) {
                    int poiX = (int) (starX + poi.offsetX);
                    int poiY = (int) (starY + poi.offsetY);

                    // Again, a small buffer area around
                    if (checkClick(mouseX, mouseY, poiX - 2, poiY - 2, 12, 12)) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                        NBTTagCompound data = new NBTTagCompound();
                        data.setInteger("pid", poi.body.dimensionId);

                        PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, star.getPos().getX(), star.getPos().getY(), star.getPos().getZ()));
                        break;
                    }
                }
            } else {
                // translate from mouse coordinate to heightmap coordinate
                int hx = (mouseX - (int) starX - guiLeft - 9 + 256) / 2;
                int hz = (mouseY - (int) starY - guiTop - 9 + 256) / 2;

                // check landing zone is valid
                boolean canLand = landingInfo(hx, hz) == null;
                if (canLand) {
                    mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                    NBTTagCompound data = new NBTTagCompound();
                    data.setInteger("px", hx);
                    data.setInteger("pz", hz);

                    PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, star.getPos().getX(), star.getPos().getY(), star.getPos().getZ()));
                }
            }
        }
    }

    public static class POI {

        int offsetX;
        int offsetY;

        CelestialBody body;

        public POI(int offsetX, int offsetY, CelestialBody body) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;

            this.body = body;
        }

    }

}
