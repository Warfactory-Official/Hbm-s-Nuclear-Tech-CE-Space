package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.main.MainRegistry;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
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

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockRubberLeaves extends BlockLeaves implements IDynamicModelsSpace {

    protected BlockBakeFrame bakeFrame;

    public BlockRubberLeaves(String name) {
        super();
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setCreativeTab(MainRegistry.controlTab);
        this.setSoundType(SoundType.PLANT);
        this.setHardness(0.2F);
        this.setLightOpacity(1);

        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(CHECK_DECAY, true)
                .withProperty(DECAYABLE, true));

        this.bakeFrame = new BlockBakeFrame(BlockBakeFrame.BlockForm.ALL_UNTINTED, name);

        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        if (this == ModBlocksSpace.pet_leaves) {
            return ModItemsSpace.leaf_pet;
        }
        return ModItemsSpace.leaf_rubber;
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean canSilkHarvest(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player) {
        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, float chance, int fortune) {
        // Super handles the primary drop logic (calling getItemDropped) and decay checks
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);

        if (!worldIn.isRemote) {
            Random rand = worldIn.rand;

            // Custom extra drops logic
            if (this == ModBlocksSpace.rubber_leaves && rand.nextFloat() < 0.3F) {
                spawnAsEntity(worldIn, pos, new ItemStack(ModItemsSpace.leaf_rubber));

                if (rand.nextFloat() < 0.5F) {
                    spawnAsEntity(worldIn, pos, new ItemStack(ModBlocksSpace.sapling_pvc, 1, 1));
                }
            }
            if (this == ModBlocksSpace.pet_leaves && rand.nextFloat() < 0.3F) {
                spawnAsEntity(worldIn, pos, new ItemStack(ModItemsSpace.leaf_pet));

                if (rand.nextFloat() < 0.5F) {
                    spawnAsEntity(worldIn, pos, new ItemStack(ModBlocksSpace.sapling_pvc, 1, 0));
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(@NotNull IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, @NotNull EnumFacing side) {
        IBlockState neighbor = blockAccess.getBlockState(pos.offset(side));
        return !neighbor.isOpaqueCube();
    }

    @Override
    public BlockPlanks.@NotNull EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@NotNull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(DECAYABLE, (meta & 4) == 0)
                .withProperty(CHECK_DECAY, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (!state.getValue(DECAYABLE)) i |= 4;
        if (state.getValue(CHECK_DECAY)) i |= 8;
        return i;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColorHandler() {
        return (state, worldIn, pos, tintIndex) -> 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return (stack, tintIndex) -> 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        if (bakeFrame != null) bakeFrame.registerBlockTextures(map);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMap.Builder().ignore(CHECK_DECAY, DECAYABLE).build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this),
                0,
                new ModelResourceLocation(this.getRegistryName(), "normal")
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        if (bakeFrame == null) return;

        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(bakeFrame.getBaseModel()));

            ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();
            bakeFrame.putTextures(textureMap);

            IBakedModel bakedModel = baseModel.retexture(textureMap.build()).bake(
                    ModelRotation.X0_Y0,
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );

            ModelResourceLocation normalLoc = new ModelResourceLocation(this.getRegistryName(), "normal");
            ModelResourceLocation inventoryLoc = new ModelResourceLocation(this.getRegistryName(), "inventory");

            event.getModelRegistry().putObject(normalLoc, bakedModel);
            event.getModelRegistry().putObject(inventoryLoc, bakedModel);

        } catch (Exception e) {
        }
    }
}