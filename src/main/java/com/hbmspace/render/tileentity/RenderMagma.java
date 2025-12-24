package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineMagma;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;

@AutoRegister
public class RenderMagma extends TileEntitySpecialRenderer<TileEntityMachineMagma> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineMagma tile, double x, double y, double z, float interp, int destroyStage, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();
            GlStateManager.enableCull();

            switch(tile.getBlockMetadata() - BlockDummyable.offset) {
                case 3: GlStateManager.rotate(0, 0F, 1F, 0F); break;
                case 5: GlStateManager.rotate(90, 0F, 1F, 0F); break;
                case 2: GlStateManager.rotate(180, 0F, 1F, 0F); break;
                case 4: GlStateManager.rotate(270, 0F, 1F, 0F); break;
            }

            GlStateManager.translate(0, -((BlockDummyable) ModBlocksSpace.machine_magma).getHeightOffset(), 0);


            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            bindTexture(ResourceManagerSpace.magma_drill_tex);
            ResourceManagerSpace.magma_drill.renderAllExcept("DrillHead", "Blades");

            float drillRotation = tile.prevDrillRotation + (tile.drillRotation - tile.prevDrillRotation) * interp;
            float lavaHeight = tile.prevLavaHeight + (tile.lavaHeight - tile.prevLavaHeight) * interp;

            GlStateManager.pushMatrix();
            {

                GlStateManager.rotate(drillRotation, 0, 1, 0);
                ResourceManagerSpace.magma_drill.renderPart("DrillHead");

            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {

                GlStateManager.rotate(-drillRotation, 0, 1, 0);
                ResourceManagerSpace.magma_drill.renderPart("Blades");

            }
            GlStateManager.popMatrix();

            if (lavaHeight > 0.01F) {
                IBlockState lavaState = Blocks.LAVA.getDefaultState(); // still lava (LEVEL=0 by default)
                TextureAtlasSprite lavaSprite = Minecraft.getMinecraft()
                        .getBlockRendererDispatcher()
                        .getBlockModelShapes()
                        .getTexture(lavaState);

                this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                double minU = lavaSprite.getMinU();
                double maxU = lavaSprite.getMaxU();
                double minV = lavaSprite.getMinV();
                double maxV = lavaSprite.getMaxV();

                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
                for (int ox = -1; ox <= 1; ox++) {
                    for (int oz = -1; oz <= 1; oz++) {

                        double x0 = ox - 0.5D;
                        double x1 = ox + 0.5D;
                        double z0 = oz - 0.5D;
                        double z1 = oz + 0.5D;

                        // top face (Y+), normal (0,1,0)
                        buffer.pos(x0, lavaHeight, z1).tex(minU, maxV).normal(0F, 1F, 0F).endVertex();
                        buffer.pos(x1, lavaHeight, z1).tex(maxU, maxV).normal(0F, 1F, 0F).endVertex();
                        buffer.pos(x1, lavaHeight, z0).tex(maxU, minV).normal(0F, 1F, 0F).endVertex();
                        buffer.pos(x0, lavaHeight, z0).tex(minU, minV).normal(0F, 1F, 0F).endVertex();
                    }
                }
                tessellator.draw();
            }

            GlStateManager.shadeModel(GL11.GL_FLAT);

        }
        GlStateManager.popMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_magma);
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase( ) {
            public void renderInventory() {
                GlStateManager.translate(0, -2, 0);
                GlStateManager.scale(3, 3, 3);
            }
            public void renderCommon() {
                GlStateManager.rotate(90, 0F, 1F, 0F);
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.magma_drill_tex); ResourceManagerSpace.magma_drill.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }};
    }

}
