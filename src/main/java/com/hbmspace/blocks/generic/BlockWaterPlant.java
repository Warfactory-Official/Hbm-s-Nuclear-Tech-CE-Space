package com.hbmspace.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockMeta;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbm.items.IDynamicModels;
import com.hbmspace.items.IDynamicModelsSpace;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockWaterPlant extends BlockMeta implements IDynamicModelsSpace {

    public BlockWaterPlant(String name) {
        super(Material.WATER, name, (short) 1, true,
                new BlockBakeFrame(BlockBakeFrame.BlockForm.CROP, name));
        setSoundType(SoundType.PLANT);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);

        this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0).withProperty(BlockLiquid.LEVEL, 15));
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META, BlockLiquid.LEVEL);
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        // Logic from 1.7.10: random.nextInt(4) -> 0 to 3 items
        int count = new Random().nextInt(4);
        if (count > 0) {
            ret.add(new ItemStack(ModItemsSpace.saltleaf, count));
        }
        return ret;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        if (!world.getBlockState(pos.up()).getMaterial().isLiquid()) return false;
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos, this.getDefaultState());
    }

    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP);
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        if (!this.canBlockStay(world, pos, state) || !world.getBlockState(pos.up()).getMaterial().isLiquid()) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        // Register the item to use a specific "inventory" variant, distinct from the block state
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        // The frame is stored in the superclass array
        BlockBakeFrame frame = this.blockFrames[0];
        String texture = frame.getSpriteLoc(0).toString();

        try {
            // 1. Bake the Block Model (CROP form, 3D cross)
            IModel blockBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation(frame.getBaseModel()));
            ImmutableMap.Builder<String, String> blockTextureMap = ImmutableMap.builder();
            frame.putTextures(blockTextureMap);

            IBakedModel bakedBlockModel = blockBaseModel.retexture(blockTextureMap.build()).bake(
                    ModelRotation.X0_Y0,
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );

            // BlockMeta's default StateMapper maps metadata 0 to "meta=0"
            ModelResourceLocation blockLoc = new ModelResourceLocation(this.getRegistryName(), "meta=0");
            event.getModelRegistry().putObject(blockLoc, bakedBlockModel);

            // 2. Bake the Item Model (Generated, 2D sprite)
            IModel itemBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:item/generated"));
            ImmutableMap<String, String> itemTextures = ImmutableMap.of("layer0", texture);

            IBakedModel bakedItemModel = itemBaseModel.retexture(itemTextures).bake(
                    ModelRotation.X0_Y0,
                    DefaultVertexFormats.ITEM,
                    ModelLoader.defaultTextureGetter()
            );

            // Register to the "inventory" location defined in registerModel
            ModelResourceLocation itemLoc = new ModelResourceLocation(this.getRegistryName(), "inventory");
            event.getModelRegistry().putObject(itemLoc, bakedItemModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}