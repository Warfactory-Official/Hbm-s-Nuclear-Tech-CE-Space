package com.hbmspace.blocks.machine;

import com.google.common.collect.ImmutableMap;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.tileentity.machine.storage.TileEntityCombatDropPod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CombatDropPod extends BlockContainer implements IDynamicModelsSpace {

    public CombatDropPod(Material mat, String registryName) {
        super(mat);
        this.setRegistryName(registryName);
        this.setTranslationKey(registryName);
        this.setSoundType(SoundType.METAL);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World p_149915_1_, int p_149915_2_) {
        return new TileEntityCombatDropPod();
    }


    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return ModItems.ingot_steel;
    }

    @Override public int quantityDropped(@NotNull Random rand) {
        return 16;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel blockBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation("block/cube_all"));
            ImmutableMap<String, String> blockTextures = ImmutableMap.of("all", "hbm:blocks/block_steel");
            IModel blockRetextured = blockBaseModel.retexture(blockTextures);
            IBakedModel blockBaked = blockRetextured.bake(
                    ModelRotation.X0_Y0,
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );
            ModelResourceLocation worldLocation = new ModelResourceLocation(getRegistryName(), "normal");
            event.getModelRegistry().putObject(worldLocation, blockBaked);
            IModel itemBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation("item/generated"));
            ImmutableMap<String, String> itemTextures = ImmutableMap.of("layer0", "hbm:blocks/" + getRegistryName().getPath());
            IModel itemRetextured = itemBaseModel.retexture(itemTextures);
            IBakedModel itemBaked = itemRetextured.bake(
                    ModelRotation.X0_Y0,
                    DefaultVertexFormats.ITEM,
                    ModelLoader.defaultTextureGetter()
            );
            ModelResourceLocation inventoryLocation = new ModelResourceLocation(getRegistryName(), "inventory");
            event.getModelRegistry().putObject(inventoryLocation, itemBaked);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMapperBase() {
            @Override
            protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
                return new ModelResourceLocation(loc, "normal");
            }
        };
    }

}
