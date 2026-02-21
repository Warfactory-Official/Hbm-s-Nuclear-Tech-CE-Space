package com.hbmspace.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.model.AbstractWavefrontBakedModel;
import com.hbm.render.model.BakedModelTransforms;
import com.hbmspace.blocks.generic.BlockAlgaeFilm;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BlockAlgaeBakedModel extends AbstractWavefrontBakedModel {

    private final TextureAtlasSprite sprite;
    private final boolean forBlock;

    // Cache for 4 horizontal directions (indices 2-5, mapped to 0-3)
    @SuppressWarnings("unchecked")
    private final List<BakedQuad>[] cache = new List[4];
    private List<BakedQuad> itemQuads;

    public BlockAlgaeBakedModel(HFRWavefrontObject model, TextureAtlasSprite sprite, boolean forBlock, float ty) {
        // ty is -0.5F to shift the model from center (standard 1.12) to bottom (1.7.10 behavior)
        super(model, DefaultVertexFormats.ITEM, 1.0F, 0.0F, ty, 0.0F, BakedModelTransforms.forDeco(BakedModelTransforms.standardBlock()));
        this.sprite = sprite;
        this.forBlock = forBlock;
    }

    public static BlockAlgaeBakedModel forBlock(HFRWavefrontObject model, TextureAtlasSprite sprite) {
        // 1.7.10 rendering was at 'y' (bottom), 1.12 standard is 'y+0.5' (center).
        // We offset by -0.5 to place it on the floor.
        return new BlockAlgaeBakedModel(model, sprite, true, -0.5F);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if (side != null) return Collections.emptyList();

        if (!forBlock) {
            if (itemQuads == null) {
                // Item render also translated down by 0.5 in 1.7.10
                itemQuads = super.bakeSimpleQuads(null, 0.0F, 0.0F, 0.0F, false, false, sprite);
            }
            return itemQuads;
        }

        EnumFacing facing = EnumFacing.NORTH;
        if (state != null && state.getPropertyKeys().contains(BlockAlgaeFilm.FACING)) {
            facing = state.getValue(BlockAlgaeFilm.FACING);
        }

        // Map horizontal index (2-5) to cache index (0-3)
        int index = facing.getHorizontalIndex();
        if (index < 0) index = 0; // Fallback

        List<BakedQuad> quads = cache[index];
        if (quads != null) return quads;

        quads = buildQuadsForFacing(facing);
        cache[index] = quads;
        return quads;
    }

    private List<BakedQuad> buildQuadsForFacing(EnumFacing facing) {
        float yaw = 0.0F;

        // 1.7.10 Rotations:
        // Meta 2 (North): 0 deg
        // Meta 3 (South): 180 deg
        // Meta 4 (West): 90 deg
        // Meta 5 (East): 270 deg

        switch (facing) {
            case NORTH:
                yaw = 0.0F;
                break;
            case SOUTH:
                yaw = (float) Math.PI; // 180 deg
                break;
            case WEST:
                yaw = 0.5F * (float) Math.PI; // 90 deg
                break;
            case EAST:
                yaw = 1.5F * (float) Math.PI; // 270 deg
                break;
            default:
                break;
        }

        return super.bakeSimpleQuads(null, 0.0F, 0.0F, yaw, true, true, sprite);
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleTexture() {
        return sprite;
    }
}