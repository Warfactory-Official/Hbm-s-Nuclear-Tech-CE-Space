package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.BlockBase;
import com.hbm.blocks.ModBlocks;
import com.hbm.items.IDynamicModels;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockKelp extends BlockBase implements IDynamicModelsSpace {

    public static final PropertyBool TOP = PropertyBool.create("top");
    private final BlockBakeFrame[] frames;

    public BlockKelp(String s) {
        super(Material.WATER, SoundType.PLANT, s);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TOP, false).withProperty(BlockLiquid.LEVEL, 15));

        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);

        this.frames = new BlockBakeFrame[] {
                new BlockBakeFrame(BlockBakeFrame.BlockForm.CROSS, "laythe_kelp"),
                new BlockBakeFrame(BlockBakeFrame.BlockForm.CROSS, "laythe_kelp_top")
        };
        this.setTickRandomly(true);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TOP, BlockLiquid.LEVEL);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TOP) ? 8 : 0;
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TOP, meta >= 8);
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
    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean canPlaceBlockAt(@NotNull World world, @NotNull BlockPos pos) {
        return canBlockStay(world, pos);
    }

    public boolean canBlockStay(World world, BlockPos pos) {
        IBlockState downState = world.getBlockState(pos.down());
        Block downBlock = downState.getBlock();
        return downBlock instanceof BlockKelp || downBlock == ModBlocksSpace.laythe_silt;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockState(pos, Blocks.WATER.getDefaultState());
        } else {
            this.updateBlockMeta(world, pos, state);
        }
    }

    @Override
    public void onBlockAdded(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        this.updateBlockMeta(world, pos, state);
    }

    private void updateBlockMeta(World world, BlockPos pos, IBlockState state) {
        BlockPos up = pos.up();
        boolean hasKelpAbove = world.getBlockState(up).getBlock() == this;
        boolean isTop = !hasKelpAbove;

        if (state.getValue(TOP) != isTop) {
            world.setBlockState(pos, state.withProperty(TOP, isTop), 2);
        }
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return ModItemsSpace.saltleaf;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        for (BlockBakeFrame frame : frames) {
            frame.registerBlockTextures(map);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        Item item = Item.getItemFromBlock(this);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMapperBase() {
            @Override
            protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
                String variant = "top=" + state.getValue(TOP);
                return new ModelResourceLocation(loc, variant);
            }
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel itemBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:item/generated"));

            for (int i = 0; i < frames.length; i++) {
                BlockBakeFrame frame = frames[i];
                IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(frame.getBaseModel()));

                ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
                frame.putTextures(builder);

                IBakedModel bakedModel = baseModel.retexture(builder.build()).bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.BLOCK,
                        ModelLoader.defaultTextureGetter()
                );

                String variant = "top=" + (i == 1);
                event.getModelRegistry().putObject(new ModelResourceLocation(this.getRegistryName(), variant), bakedModel);
            }

            BlockBakeFrame itemFrame = frames[0];
            String texture = itemFrame.getSpriteLoc(0).toString();
            IBakedModel bakedItem = itemBaseModel.retexture(ImmutableMap.of("layer0", texture)).bake(
                    ModelRotation.X0_Y0,
                    DefaultVertexFormats.ITEM,
                    ModelLoader.defaultTextureGetter()
            );
            event.getModelRegistry().putObject(new ModelResourceLocation(this.getRegistryName(), "inventory"), bakedItem);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}