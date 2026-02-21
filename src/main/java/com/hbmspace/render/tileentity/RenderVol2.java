package com.hbmspace.render.tileentity;

import com.hbm.render.util.BeamPronter;
import com.hbmspace.blocks.generic.BlockVolcanoV2;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.render.util.BeamPronterSpace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.Vec3d;

@AutoRegister
public class RenderVol2 extends TileEntitySpecialRenderer<BlockVolcanoV2.TileEntityLightningVolcano> {

    @Override
    public void render(BlockVolcanoV2.TileEntityLightningVolcano te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y, z + 0.5D);
        GlStateManager.enableLighting();
        GlStateManager.enableCull();

        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);

        switch(te.getBlockMetadata()) {
            case 0:
                GlStateManager.translate(0.0D, 0.5D, -0.5D);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F); break;
            case 1:
                GlStateManager.translate(0.0D, 0.5D, 0.5D);
                GlStateManager.rotate(90.0F, -1.0F, 0.0F, 0.0F); break;
            case 2: GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F); break;
            case 4: GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F); break;
            case 3: GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F); break;
            case 5: GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F); break;
        }

        GlStateManager.translate(0.0D, 0.5D, 0.5D);
        float scale = te.flashd;
        float alphad = 1.0F - Math.min(1.0F, scale / 110.0F);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, alphad);

        if(te.chargetime == 0) {
            BeamPronterSpace.prontBeam(new Vec3d(0.0D, 0.0D, -100.0D - scale), BeamPronter.EnumWaveType.SPIRAL, BeamPronter.EnumBeamType.SOLID, 0x101020, 0x101020, 0, 8, 0.0F, 6, 3.0F * alphad, alphad * 2.0F);
            BeamPronterSpace.prontBeam(new Vec3d(0.0D, 0.0D, -100.0D - scale), BeamPronter.EnumWaveType.RANDOM, BeamPronter.EnumBeamType.SOLID, 0x202060, 0x202060, (int)(te.getWorld().getTotalWorldTime() / 2L) % 1000, 26, 3.0F, 2, 0.0625F * 2.0F, 0.5F * alphad);
            BeamPronterSpace.prontBeam(new Vec3d(0.0D, 0.0D, -100.0D - scale), BeamPronter.EnumWaveType.RANDOM, BeamPronter.EnumBeamType.SOLID, 0x202060, 0x202060, (int)(te.getWorld().getTotalWorldTime() / 4L) % 1000, 26, 3.0F, 2, 0.0625F * 2.0F, 0.5F * alphad);
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }
}
