package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockEnumMetaSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class BlockRubberCacti extends BlockEnumMetaSpace<BlockRubberCacti.EnumBushType> {

    private static final AxisAlignedBB TALL_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 3.0D, 0.9D);

    public BlockRubberCacti(String registryName) {
        super(Material.PLANTS, SoundType.PLANT, registryName, EnumBushType.VALUES, true, true);
    }

    public enum EnumBushType {
        CACT,
        BUSH,
        FLOWER;
        public static final EnumBushType[] VALUES = values();
    }

    @Override
    protected BlockBakeFrame[] generateBlockFrames(String registryName) {
        return Arrays.stream(blockEnum)
                .map(Enum::name)
                .map(name -> registryName + "." + name.toLowerCase(Locale.US))
                .map(tex -> new BlockBakeFrame(BlockBakeFrame.BlockForm.CROSS, tex))
                .toArray(BlockBakeFrame[]::new);
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return TALL_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean canPlaceBlockAt(@NotNull World worldIn, @NotNull BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos) {
        return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }

    protected boolean canPlaceBlockOn(Block block) {
        return block == ModBlocksSpace.vinyl_sand ||
                block == ModBlocksSpace.rubber_grass ||
                block == ModBlocksSpace.rubber_silt ||
                block == ModBlocksSpace.rubber_farmland;
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos);
    }

    protected void checkAndDropBlock(World world, BlockPos pos) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        EnumBushType type = getEnumFromState(state);

        if (type == EnumBushType.FLOWER) {
            return ModItemsSpace.paraffin_seeds;
        }
        return Item.getItemFromBlock(this);
    }

    @Override
    public int damageDropped(@NotNull IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        for (BlockBakeFrame frame : blockFrames) {
            for (String texture : frame.textureArray) {
                ResourceLocation spriteLoc = new ResourceLocation("hbm", "blocks/" + texture);
                map.registerSprite(spriteLoc);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        for (int meta = 0; meta < blockEnum.length; meta++) {
            BlockBakeFrame blockFrame = blockFrames[meta % blockFrames.length];
            try {
                IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("hbmspace", "block/cross_tall_tinted"));
                ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();
                String texturePath = "hbm" + ":blocks/" + blockFrame.textureArray[0];
                textureMap.put("cross", texturePath);
                textureMap.put("particle", texturePath);
                IModel retexturedModel = baseModel.retexture(textureMap.build());
                TRSRTransformation transform = new TRSRTransformation(new Vector3f(0, 1, 0), null, null, null);

                IBakedModel bakedModel = retexturedModel.bake(
                        transform, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter()
                );
                ModelResourceLocation modelLocation = new ModelResourceLocation(getRegistryName(), "meta=" + meta);
                event.getModelRegistry().putObject(modelLocation, bakedModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}