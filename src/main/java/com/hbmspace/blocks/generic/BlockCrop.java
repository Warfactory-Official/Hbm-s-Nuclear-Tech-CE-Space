package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.main.MainRegistry;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.IPlantableBreathing;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class BlockCrop extends BlockBush implements IGrowable, IPlantableBreathing, IDynamicModelsSpace {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);

    protected int maxGrowthStage = 7;
    protected final Block soilBlock;

    private final Predicate<CBT_Atmosphere> atmospherePredicate;

    public final boolean canHydro;

    private String textureBase;

    @SideOnly(Side.CLIENT)
    private BlockBakeFrame[] cropFrames;

    public BlockCrop(Block block, Predicate<CBT_Atmosphere> atmospherePredicate, boolean canHydro, String s, String tex) {
        super();
        this.setTickRandomly(true);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();

        this.soilBlock = block;
        this.atmospherePredicate = atmospherePredicate;
        this.canHydro = canHydro;
        this.textureBase = tex;

        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));

        this.setTranslationKey(s);
        this.setRegistryName(s);
        this.setHarvestLevel("shovel", 0);
        this.setCreativeTab(MainRegistry.controlTab);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public boolean canBreathe(CBT_Atmosphere atmosphere) {
        if (atmosphere == null) return false;
        return this.atmospherePredicate.test(atmosphere);
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == this.soilBlock;
    }

    public void incrementGrowStage(World world, Random rand, BlockPos pos, IBlockState state) {
        int age = state.getValue(AGE);
        int growStage = age + MathHelper.getInt(rand, 2, 5);

        if (growStage > maxGrowthStage) {
            growStage = maxGrowthStage;
        }

        world.setBlockState(pos, state.withProperty(AGE, growStage), 2);
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        // TODO
        /*if (this == ModBlocks.crop_strawberry) {
            return ModItems.strawberry;
        }

        if (this == ModBlocks.crop_mint) {
            return ModItems.mint_leaves;
        }

        if (this == ModBlocks.crop_coffee) {
            return ModItems.bean_raw;
        }

        if (this == ModBlocks.crop_tea) {
            return state.getValue(AGE) >= 7 ? ModItems.tea_leaf : ModItems.teaseeds;
        }

        if (this == ModBlocks.crop_paraffin) {
            return ModItems.paraffin_seeds;
        }*/

        return Item.getItemFromBlock(this);
    }

    protected void checkAndDropBlock(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!this.canBlockStay(world, pos, state)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, @NotNull IBlockState state) {
        BlockPos down = pos.down();
        IBlockState soilState = world.getBlockState(down);
        return soilState.getBlock().canSustainPlant(soilState, world, down, EnumFacing.UP, this);
    }

    @Override
    public void updateTick(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        super.updateTick(world, pos, state, rand);

        if (!world.isAreaLoaded(pos, 1)) return;

        this.checkAndDropBlock(world, pos, state);
        if (world.getBlockState(pos).getBlock() != this) return;

        int growStage = state.getValue(AGE) + 1;
        if (growStage > 7) growStage = 7;

        world.setBlockState(pos, state.withProperty(AGE, growStage), 2);
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        this.checkAndDropBlock(world, pos, state);
    }

    // IGrowable (bonemeal)
    @Override
    public boolean canGrow(@NotNull World world, @NotNull BlockPos pos, IBlockState state, boolean isClient) {
        return state.getValue(AGE) != 7;
    }

    @Override
    public boolean canUseBonemeal(@NotNull World world, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
        return true;
    }

    @Override
    public void grow(@NotNull World world, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
        this.incrementGrowStage(world, rand, pos, state);
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, @NotNull Random rand) {
        int age = state.getValue(AGE);
        if (age >= 7) {
            return 4;
        } else {
            return age / 2;
        }
    }

    @Override
    public void getDrops(@NotNull NonNullList<ItemStack> drops, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        // TODO
        /*
        int age = state.getValue(AGE);
        if (age < 7) return;
        World w = (world instanceof World) ? (World) world : null;
        Random rand = (w != null) ? w.rand : new Random();
        if (this == ModBlocks.crop_tea) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (rand.nextInt(15) <= age) {
                    drops.add(new ItemStack(ModItems.teaseeds, 1, 0));
                }
            }
        }

        if (this == ModBlocks.crop_paraffin) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (rand.nextInt(15) <= age) {
                    drops.add(new ItemStack(ModItems.paraffin_seeds));
                    drops.add(new ItemStack(ModItems.oil_tar, 1, ItemEnums.EnumTarType.WAX.ordinal()));
                }
            }
        }*/
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        int clamped = meta;
        if (clamped < 0) clamped = 0;
        if (clamped > 7) clamped = 7;
        return this.getDefaultState().withProperty(AGE, clamped);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        return NULL_AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    private void ensureFrames() {
        if (this.cropFrames != null) return;

        String base = this.textureBase;
        if (base == null || base.isEmpty()) {
            ResourceLocation rl = this.getRegistryName();
            base = (rl != null) ? rl.getPath() : "missing_texture";
        }

        String[] tex = new String[]{
                base + "_1",
                base + "_1",
                base + "_2",
                base + "_2",
                base + "_3",
                base + "_3",
                base + "_4",
                base + "_5"
        };

        this.cropFrames = new BlockBakeFrame[8];
        for (int i = 0; i < 8; i++) {
            this.cropFrames[i] = new BlockBakeFrame(BlockBakeFrame.BlockForm.CROSS, tex[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void bakeModel(ModelBakeEvent event) {
        try {
            this.ensureFrames();

            IBakedModel[] baked = new IBakedModel[8];

            for (int age = 0; age < 8; age++) {
                BlockBakeFrame frame = this.cropFrames[age];
                IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(frame.getBaseModel()));

                ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();
                frame.putTextures(textureMap);

                IModel retexturedModel = baseModel.retexture(textureMap.build());
                baked[age] = retexturedModel.bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.BLOCK,
                        ModelLoader.defaultTextureGetter()
                );

                ModelResourceLocation worldLoc = new ModelResourceLocation(Objects.requireNonNull(this.getRegistryName()), "age=" + age);
                event.getModelRegistry().putObject(worldLoc, baked[age]);
            }

            String base = this.textureBase;
            if (base == null || base.isEmpty()) {
                ResourceLocation rl = this.getRegistryName();
                base = (rl != null) ? rl.getPath() : "missing_texture";
            }

            ModelResourceLocation invLoc = new ModelResourceLocation(Objects.requireNonNull(this.getRegistryName()), "inventory");
            IModel invBase = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));
            IModel invRetex = invBase.retexture(ImmutableMap.of(
                    "layer0", new ResourceLocation("hbm", "blocks/" + textureBase + "_1").toString()
            ));
            event.getModelRegistry().putObject(invLoc, invRetex.bake(
                    ModelRotation.X0_Y0,
                    DefaultVertexFormats.ITEM,
                    ModelLoader.defaultTextureGetter()
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this),
                0,
                new ModelResourceLocation(Objects.requireNonNull(this.getRegistryName()), "inventory")
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerSprite(TextureMap map) {
        this.ensureFrames();
        for (BlockBakeFrame frame : this.cropFrames) {
            frame.registerBlockTextures(map);
        }
    }

    @Override
    public Object getSelf() {
        return this;
    }
}
