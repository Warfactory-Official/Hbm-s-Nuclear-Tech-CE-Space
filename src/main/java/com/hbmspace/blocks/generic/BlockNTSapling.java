package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.ICustomBlockItem;
import com.hbm.blocks.generic.IMetaBlock;
import com.hbm.main.MainRegistry;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.tekto.TTree;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.IPlantableBreathing;
import com.hbmspace.inventory.fluid.Fluids;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockNTSapling extends BlockBush implements ICustomBlockItem, IGrowable, IPlantableBreathing, IDynamicModelsSpace, IMetaBlock {

    public static final PropertyEnum<EnumSapling> VARIANT = PropertyEnum.create("variant", EnumSapling.class);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BlockNTSapling(String name) {
        super();
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumSapling.VINYL));
        this.setCreativeTab(MainRegistry.controlTab);
        this.setSoundType(SoundType.PLANT);

        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return SAPLING_AABB;
    }

    @Override
    public boolean canBreathe(CBT_Atmosphere atmosphere) {
        return atmosphere != null && (atmosphere.hasFluid(Fluids.TEKTOAIR, 0.1) || atmosphere.hasFluid(com.hbm.inventory.fluid.Fluids.CHLORINE, 0.1));
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        Block block = state.getBlock();
        return block == ModBlocksSpace.rubber_silt || block == ModBlocksSpace.rubber_grass || block == ModBlocksSpace.rubber_farmland;
    }

    @Override
    public boolean canBlockStay(@NotNull World worldIn, @NotNull BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) {
            IBlockState soil = worldIn.getBlockState(pos.down());
            boolean isSoilValid = soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this);
            boolean hasLight = worldIn.getLightFromNeighbors(pos) >= 8 || worldIn.canSeeSky(pos);

            return hasLight && isSoilValid;
        }
        return this.canSustainBush(worldIn.getBlockState(pos.down()));
    }

    @Override
    public @NotNull EnumPlantType getPlantType(@NotNull IBlockAccess world, @NotNull BlockPos pos) {
        return EnumPlantType.Plains;
    }


    @Override
    public void updateTick(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }

    @Override
    public boolean canGrow(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
    public void grow(@NotNull World worldIn, @NotNull Random rand, @NotNull BlockPos pos, IBlockState state) {
        EnumSapling variant = state.getValue(VARIANT);
        new TTree(true, 3, 4, 6, 3, 2, false, ModBlocksSpace.vinyl_log, ModBlocksSpace.pet_leaves);
        WorldGenAbstractTree treeGen = switch (variant) {
            case VINYL -> new TTree(false, 2, 4, 5, 3, 2, false, ModBlocksSpace.vinyl_log, ModBlocksSpace.pet_leaves);
            case PVC -> new TTree(true, 2, 5, 7, 4, 3, false, ModBlocksSpace.pvc_log, ModBlocksSpace.rubber_leaves);
        };

        worldIn.setBlockToAir(pos);
        if (!treeGen.generate(worldIn, rand, pos)) {
            worldIn.setBlockState(pos, state, 2);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumSapling.values()[meta % EnumSapling.values().length]);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState state, int fortune) {
        return Collections.singletonList(new ItemStack(this, 1, getMetaFromState(state)));
    }

    @Override
    public @NotNull ItemStack getPickBlock(@NotNull IBlockState state, @NotNull RayTraceResult target, @NotNull World world, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }

    @Override
    public void onBlockPlacedBy(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, getStateFromMeta(stack.getItemDamage()), 2);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int damageDropped(@NotNull IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        String ns = this.getRegistryName().getNamespace();
        for (EnumSapling sapling : EnumSapling.values()) {
            map.registerSprite(new ResourceLocation("hbm:blocks/sapling_" + sapling.getName()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        for (EnumSapling sapling : EnumSapling.values()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), sapling.ordinal(),
                    new ModelResourceLocation(this.getRegistryName(), "inventory_variant=" + sapling.getName()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        BlockBakeFrame crossFrame = new BlockBakeFrame(BlockBakeFrame.BlockForm.CROSS, "dummy");
        ResourceLocation baseModelLoc = new ResourceLocation(crossFrame.getBaseModel());
        ResourceLocation itemBaseLoc = new ResourceLocation("minecraft:item/generated");

        try {
            IModel baseModel = ModelLoaderRegistry.getModel(baseModelLoc);
            IModel itemBaseModel = ModelLoaderRegistry.getModel(itemBaseLoc);

            for (EnumSapling sapling : EnumSapling.values()) {
                String texturePath = "hbm:blocks/sapling_" + sapling.getName();

                ImmutableMap<String, String> blockTextures = ImmutableMap.of(
                        "cross", texturePath,
                        "particle", texturePath
                );

                IBakedModel bakedBlockModel = baseModel.retexture(blockTextures).bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.BLOCK,
                        ModelLoader.defaultTextureGetter()
                );

                ModelResourceLocation stateLoc = new ModelResourceLocation(this.getRegistryName(), "variant=" + sapling.getName());
                event.getModelRegistry().putObject(stateLoc, bakedBlockModel);

                ImmutableMap<String, String> itemTextures = ImmutableMap.of(
                        "layer0", texturePath
                );

                IBakedModel bakedItemModel = itemBaseModel.retexture(itemTextures).bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.ITEM,
                        ModelLoader.defaultTextureGetter()
                );

                ModelResourceLocation itemLoc = new ModelResourceLocation(this.getRegistryName(), "inventory_variant=" + sapling.getName());
                event.getModelRegistry().putObject(itemLoc, bakedItemModel);
            }

        } catch (Exception e) {
            MainRegistry.logger.error("Failed to bake models for {}", this.getRegistryName(), e);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColorHandler() {
        return (_, _, _, _) -> 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return (_, _) -> 0xFFFFFF;
    }

    @Override
    public void registerItem() {
        ItemBlock itemBlock = new ItemBlockSapling(this);
        itemBlock.setRegistryName(this.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
    }


    public enum EnumSapling implements IStringSerializable {
        VINYL,
        PVC;

        @Override
        public @NotNull String getName() {
            return name().toLowerCase(Locale.US);
        }
    }

    public static class ItemBlockSapling extends ItemBlock {
        public ItemBlockSapling(Block block) {
            super(block);
            this.setHasSubtypes(true);
            this.setMaxDamage(0);
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

        @Override
        public @NotNull String getTranslationKey(ItemStack stack) {
            int meta = stack.getItemDamage();
            if (meta >= EnumSapling.values().length) meta = 0;
            return super.getTranslationKey() + "_" + EnumSapling.values()[meta].getName();
        }

        @Override
        public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
            if (this.isInCreativeTab(tab)) {
                for (int i = 0; i < EnumSapling.values().length; i++) {
                    items.add(new ItemStack(this, 1, i));
                }
            }
        }
    }
}