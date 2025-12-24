package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.vecmath.Vector3f;
import java.util.Objects;
import java.util.function.Function;

public class BlockLogNT extends BlockLog implements IDynamicModelsSpace {

    private final BlockBakeFrame blockFrame;

    public BlockLogNT(String regName, String sideTexture, String topTexture) {
        super();
        this.setTranslationKey(regName);
        this.setRegistryName(regName);

        this.blockFrame = new BlockBakeFrame(topTexture, sideTexture);

        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        BlockLog.EnumAxis axis = BlockLog.EnumAxis.Y;
        int i = meta & 12;

        if (i == 4) axis = BlockLog.EnumAxis.X;
        else if (i == 8) axis = BlockLog.EnumAxis.Z;

        return this.getDefaultState().withProperty(LOG_AXIS, axis);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        BlockLog.EnumAxis axis = state.getValue(LOG_AXIS);

        if (axis == BlockLog.EnumAxis.X) i |= 4;
        else if (axis == BlockLog.EnumAxis.Z) i |= 8;

        return i;
    }

    @Override
    public @NotNull IBlockState getStateForPlacement(@NotNull World worldIn, @NotNull BlockPos pos, EnumFacing facing,
                                                     float hitX, float hitY, float hitZ, int meta,
                                                     @NotNull EntityLivingBase placer, @NotNull EnumHand hand) {
        return this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
    }

    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(blockFrame.getBaseModel()));

            // axis=y/x/z -> normal pillar (end=top, side=side)
            ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();
            blockFrame.putTextures(textureMap);
            IModel pillarModel = baseModel.retexture(textureMap.build());

            // axis=none -> bark all around (end=side, side=side)
            ImmutableMap<String, String> barkTextures = ImmutableMap.<String, String>builder()
                    .put("end", blockFrame.getSpriteLoc(1).toString())
                    .put("side", blockFrame.getSpriteLoc(1).toString())
                    .put("particle", blockFrame.getSpriteLoc(1).toString())
                    .build();
            IModel barkModel = baseModel.retexture(barkTextures);

            Function<ResourceLocation, TextureAtlasSprite> getter = ModelLoader.defaultTextureGetter();

            TRSRTransformation tY = TRSRTransformation.identity();
            TRSRTransformation tZ = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                    null,
                    TRSRTransformation.quatFromXYZDegrees(new Vector3f(90f, 0f, 0f)),
                    null,
                    null
            ));

            TRSRTransformation tX = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                    null,
                    TRSRTransformation.quatFromXYZDegrees(new Vector3f(0f, 0f, 90f)),
                    null,
                    null
            ));

            IBakedModel modelY = pillarModel.bake(tY, DefaultVertexFormats.BLOCK, getter);
            IBakedModel modelZ = pillarModel.bake(tZ, DefaultVertexFormats.BLOCK, getter);
            IBakedModel modelX = pillarModel.bake(tX, DefaultVertexFormats.BLOCK, getter);
            IBakedModel modelNone = barkModel.bake(tY, DefaultVertexFormats.BLOCK, getter);

            ResourceLocation rl = Objects.requireNonNull(getRegistryName());

            // item
            event.getModelRegistry().putObject(new ModelResourceLocation(rl, "inventory"), modelY);

            // world (BlockLog uses "axis" property)
            event.getModelRegistry().putObject(new ModelResourceLocation(rl, "axis=y"), modelY);
            event.getModelRegistry().putObject(new ModelResourceLocation(rl, "axis=z"), modelZ);
            event.getModelRegistry().putObject(new ModelResourceLocation(rl, "axis=x"), modelX);
            event.getModelRegistry().putObject(new ModelResourceLocation(rl, "axis=none"), modelNone);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this),
                0,
                new ModelResourceLocation(Objects.requireNonNull(this.getRegistryName()), "inventory")
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        blockFrame.registerBlockTextures(map);
    }
}
