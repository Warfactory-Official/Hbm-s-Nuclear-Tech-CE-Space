package com.hbmspace.blocks.generic;

import java.util.Locale;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.BlockEnumMeta;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.PlantEnums;
import com.hbm.blocks.generic.BlockTallPlant.EnumTallFlower;
import com.hbm.inventory.OreDictManager.DictFrame;
import com.hbm.items.ModItems;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockEnumMetaSpace;
import com.hbmspace.blocks.ModBlocksSpace;

import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class BlockTallPlantWater extends BlockEnumMetaSpace<BlockTallPlantWater.EnumTallPlantWater> implements IPlantable, IGrowable {

    public enum EnumTallPlantWater {
        LAYTHE;
        public static final EnumTallPlantWater[] VALUES = values();
    }

    public BlockTallPlantWater(String s) {
        super(Material.WATER, SoundType.PLANT, s, EnumTallPlantWater.VALUES, true, true);
        this.setTickRandomly(true);
        this.META_COUNT = 16;
    }

    @Override
    protected BlockBakeFrame[] generateBlockFrames(String registryName) {
        BlockBakeFrame[] frames = new BlockBakeFrame[16];
        for (int i = 0; i < 8; i++) {
            int enumIndex = i % EnumTallPlantWater.VALUES.length;
            String textureName = registryName + "." + EnumTallPlantWater.VALUES[enumIndex].name().toLowerCase(Locale.US) + ".lower";
            frames[i] = new BlockBakeFrame(BlockBakeFrame.BlockForm.CROP, textureName);
        }
        for (int i = 8; i < 16; i++) {
            int enumIndex = (i - 8) % EnumTallPlantWater.VALUES.length;
            String textureName = registryName + "." + EnumTallPlantWater.VALUES[enumIndex].name().toLowerCase(Locale.US) + ".upper";
            frames[i] = new BlockBakeFrame(BlockBakeFrame.BlockForm.CROP, textureName);
        }
        return frames;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack stack = super.getPickBlock(state, target, world, pos, player);
        stack.setItemDamage(0);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        if (this.blockFrames != null) {
            for (BlockBakeFrame frame : this.blockFrames) {
                frame.registerBlockTextures(map);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        Item item = Item.getItemFromBlock(this);
        for (EnumTallPlantWater val : EnumTallPlantWater.VALUES) {
            ModelLoader.setCustomModelResourceLocation(item, val.ordinal(),
                    new ModelResourceLocation(this.getRegistryName(), "inventory_variant=" + val.name().toLowerCase(Locale.US)));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMapperBase() {
            @Override
            protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
                int meta = getMetaFromState(state);
                int enumIndex = (meta < 8 ? meta : meta - 8) % EnumTallPlantWater.VALUES.length;
                String suffix = meta < 8 ? "_lower" : "_upper";
                String variant = "variant=" + EnumTallPlantWater.VALUES[enumIndex].name().toLowerCase(Locale.US) + suffix;
                return new ModelResourceLocation(loc, variant);
            }
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel cropModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/crop"));
            IModel itemModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:item/generated"));

            for (int i = 0; i < 16; i++) {
                if (i >= this.blockFrames.length) break;

                BlockBakeFrame frame = this.blockFrames[i];
                String texture = frame.getSpriteLoc(0).toString();

                ImmutableMap<String, String> textures = ImmutableMap.of(
                        "crop", texture,
                        "particle", texture
                );

                IBakedModel bakedBlock = cropModel.retexture(textures).bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.BLOCK,
                        ModelLoader.defaultTextureGetter()
                );

                int enumIndex = (i < 8 ? i : i - 8) % EnumTallPlantWater.VALUES.length;
                String suffix = i < 8 ? "_lower" : "_upper";
                String variant = "variant=" + EnumTallPlantWater.VALUES[enumIndex].name().toLowerCase(Locale.US) + suffix;

                ModelResourceLocation blockLoc = new ModelResourceLocation(this.getRegistryName(), variant);
                event.getModelRegistry().putObject(blockLoc, bakedBlock);
            }

            for (int i = 0; i < EnumTallPlantWater.VALUES.length; i++) {
                BlockBakeFrame frame = this.blockFrames[i];
                String texture = frame.getSpriteLoc(0).toString();

                ImmutableMap<String, String> textures = ImmutableMap.of(
                        "layer0", texture
                );

                IBakedModel bakedItem = itemModel.retexture(textures).bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.ITEM,
                        ModelLoader.defaultTextureGetter()
                );

                ModelResourceLocation itemLoc = new ModelResourceLocation(this.getRegistryName(), "inventory_variant=" + EnumTallPlantWater.VALUES[i].name().toLowerCase(Locale.US));
                event.getModelRegistry().putObject(itemLoc, bakedItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META, BlockLiquid.LEVEL);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return NULL_AABB;
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
    @SideOnly(Side.CLIENT)
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState stateBelow = worldIn.getBlockState(pos.down());
        IBlockState stateAbove = worldIn.getBlockState(pos.up());
        return super.canPlaceBlockAt(worldIn, pos) &&
                canPlaceBlockOn(stateBelow.getBlock()) &&
                stateAbove.getMaterial().isLiquid();
    }

    protected boolean canPlaceBlockOn(Block block) {
        return block == ModBlocksSpace.laythe_silt;
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public static boolean detectCut = true;

    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(world, pos, state)) {
            int meta = getMetaFromState(state);
            if (meta < 8) {
                this.dropBlockAsItem(world, pos, state, 0);
            }
            world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
        }

        if (!detectCut) return;

        BlockPos posAbove = pos.up();
        IBlockState stateAbove = world.getBlockState(posAbove);
        int metaAbove = stateAbove.getBlock() == this ? getMetaFromState(stateAbove) : -1;
        int ownMeta = getMetaFromState(state);

        if (ownMeta < 8 && (metaAbove != ownMeta + 8 || stateAbove.getBlock() != this) && ModBlocks.plant_flower.canPlaceBlockAt(world, pos)) {
            if (ownMeta == EnumTallPlantWater.LAYTHE.ordinal()) {
                world.setBlockState(pos, Blocks.WATER.getDefaultState());
            } else {
                world.setBlockState(pos, ModBlocks.plant_flower.getStateFromMeta(PlantEnums.EnumFlowerPlantType.MUSTARD_WILLOW_0.ordinal()), 3);
            }
        }
    }

    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        int meta = getMetaFromState(state);

        if (meta > 7) {
            IBlockState stateBelow = world.getBlockState(pos.down());
            return stateBelow.getBlock() == this && getMetaFromState(stateBelow) == meta - 8;
        }

        return canPlaceBlockOn(world.getBlockState(pos.down()).getBlock());
    }

    @Override
    public int damageDropped(@NotNull IBlockState state) {
        return getMetaFromState(state) % 8;
    }

    @Override
    public void onBlockHarvested(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player) {
        int meta = getMetaFromState(state);

        if (meta > 7) {
            BlockPos posBelow = pos.down();
            if (worldIn.getBlockState(posBelow).getBlock() == this) {
                worldIn.setBlockToAir(posBelow);
            }
        } else {
            BlockPos posAbove = pos.up();
            if (worldIn.getBlockState(posAbove).getBlock() == this) {
                worldIn.setBlockToAir(posAbove);
            }
        }

        if (player.capabilities.isCreativeMode) {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
        } else {
            IBlockState stateAbove = worldIn.getBlockState(pos.up());
            if (stateAbove.getBlock() == this) {
                this.dropBlockAsItem(worldIn, pos.up(), stateAbove, 0);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), this.getStateFromMeta(stack.getItemDamage() + 8), 2);
    }

    @Override
    public boolean canGrow(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(@NotNull World worldIn, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
        int meta = rectify(getMetaFromState(state));
        if (meta == EnumTallFlower.CD3.ordinal()) {
            return true;
        }
        return rand.nextFloat() < 0.33F;
    }

    @Override
    public void grow(@NotNull World worldIn, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
    }

    protected int rectify(int meta) {
        return meta % 8;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(4);
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return ModItemsSpace.saltleaf;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : RANDOM;
        int count = quantityDropped(rand);
        for (int i = 0; i < count; i++) {
            Item item = this.getItemDropped(state, rand, fortune);
            if (item != ItemStack.EMPTY.getItem()) {
                drops.add(new ItemStack(item));
            }
        }

        int metadata = getMetaFromState(state);
        if (metadata == EnumTallFlower.CD4.ordinal() + 8) {
            drops.add(DictFrame.fromOne(ModItems.plant_item, com.hbm.items.ItemEnums.EnumPlantType.MUSTARDWILLOW, 3 + rand.nextInt(4)));
        }
    }

    @Override
    public void registerItem() {
        ItemBlockTallPlantWater itemBlock = new ItemBlockTallPlantWater(this);
        itemBlock.setRegistryName(this.getRegistryName());
        itemBlock.setCreativeTab(this.getCreativeTab());
        ForgeRegistries.ITEMS.register(itemBlock);
    }

    public static class ItemBlockTallPlantWater extends BlockEnumMeta<EnumTallPlantWater>.EnumMetaBlockItem {

        public ItemBlockTallPlantWater(Block block) {
            ((BlockEnumMeta<EnumTallPlantWater>) block).super(block);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
            if (this.isInCreativeTab(tab)) {
                for (int i = 0; i < EnumTallPlantWater.VALUES.length; i++) {
                    list.add(new ItemStack(this, 1, i));
                }
            }
        }
    }
}