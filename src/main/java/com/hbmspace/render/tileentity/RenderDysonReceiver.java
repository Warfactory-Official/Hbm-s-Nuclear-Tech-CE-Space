package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.amlfrom1710.Vec3;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbm.render.util.BeamPronter;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityDysonReceiver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderDysonReceiver extends TileEntitySpecialRenderer<TileEntityDysonReceiver> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityDysonReceiver tileEntity, double x, double y, double z, float f, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();
            GlStateManager.enableCull();


            switch(tileEntity.getBlockMetadata() - BlockDummyable.offset) {
                case 2: GlStateManager.rotate(0, 0F, 1F, 0F); break;
                case 4: GlStateManager.rotate(90, 0F, 1F, 0F); break;
                case 3: GlStateManager.rotate(180, 0F, 1F, 0F); break;
                case 5: GlStateManager.rotate(270, 0F, 1F, 0F); break;
            }

            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            bindTexture(ResourceManagerSpace.dyson_receiver_tex);
            ResourceManagerSpace.dyson_receiver.renderPart("DysonReceiver");

            float t = tileEntity.isReceiving ? tileEntity.getWorld().getTotalWorldTime() + f : 0;

            GlStateManager.translate(0.0F, 1.5F, 0.0F);

            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(t, 0, 0, 1);
                GlStateManager.translate(0.0F, -1.5F, 0.0F);
                ResourceManagerSpace.dyson_receiver.renderPart("Coil1");
                ResourceManagerSpace.dyson_receiver.renderPart("Coil3");
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(t, 0, 0, -1);
                GlStateManager.translate(0.0F, -1.5F, 0.0F);
                ResourceManagerSpace.dyson_receiver.renderPart("Coil2");
            }
            GlStateManager.popMatrix();

            int length = tileEntity.beamLength;
            int color = 0xff8800;

            if(tileEntity.isReceiving) {
                GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
                GlStateManager.disableLighting();

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
                // TODO it used alpha on 1.7
                BeamPronter.prontBeamwithDepth(new Vec3d(0, 0, length), BeamPronter.EnumWaveType.SPIRAL, BeamPronter.EnumBeamType.SOLID, color, color, 0, 1, 0F, 2, 0.4F);
                BeamPronter.prontBeamwithDepth(new Vec3d(0, 0, length), BeamPronter.EnumWaveType.RANDOM, BeamPronter.EnumBeamType.SOLID, color, color, (int)(tileEntity.getWorld().getTotalWorldTime() % 1000), (length / 2), 0.0625F, 2, 0.4F);

                GlStateManager.enableLighting();
                GL11.glPopAttrib();
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -4, 0);
                GlStateManager.scale(1.5D, 1.5D, 1.5D);
            }
            public void renderCommon() {
                GlStateManager.scale(0.55, 0.55, 0.55);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.dyson_receiver_tex);
                ResourceManagerSpace.dyson_receiver.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.dyson_receiver);
    }

}
