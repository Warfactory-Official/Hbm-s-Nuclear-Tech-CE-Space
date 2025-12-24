package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.ICustomBlockItem;
import com.hbm.blocks.IOreType;
import com.hbm.blocks.ModBlocks;
import com.hbm.items.ItemEnums;
import com.hbm.main.MainRegistry;
import com.hbm.util.I18nUtil;
import com.hbmspace.Tags;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BlockOre extends net.minecraft.block.BlockOre implements ICustomBlockItem, IDynamicModelsSpace {
    public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);

    protected final int META_COUNT;

    public static final Map<Block, Set<SolarSystem.Body>> spawnMap = new HashMap<>();
    public static final Map<Block, BlockOre> vanillaMap = new HashMap<>();

    protected final IOreType oreEnum;
    public static int xp;
    public BlockOre(String s, IOreType oreEnum, int harvest) {
        super();
        this.setTranslationKey(s);
        this.setRegistryName(s);
        this.setCreativeTab(MainRegistry.controlTab);
        this.META_COUNT = SolarSystem.Body.values().length;
        this.oreEnum = oreEnum;
        this.setTickRandomly(false);
        this.setHarvestLevel("pickaxe", harvest);
        this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public BlockOre(String s, IOreType oreEnum, Block vanillaBlock) {
        this(s, oreEnum, 1);
        vanillaMap.put(vanillaBlock, this);
    }
    public BlockOre(String s, IOreType oreEnum, Block vanillaBlock, int harvest) {
        this(s, oreEnum, harvest);
        vanillaMap.put(vanillaBlock, this);
    }
    public BlockOre(String s, IOreType oreEnum, int harvest, int xp) {
        this(s, oreEnum, harvest);
        BlockOre.xp = xp;
    }

    public BlockOre setNTMAlt(Block vanillaBlock) {
        vanillaMap.put(vanillaBlock, this); // this will allow the blockntmore mixin to take the block from here and add the space "wooh you can find it here" desc
        return this;
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        if (this.getItemDropped(state, RANDOM, fortune) != Item.getItemFromBlock(this))
            return xp;
        return 0;
    }

    public static void addValidBody(Block ore, SolarSystem.Body body) {
        spawnMap.computeIfAbsent(ore, b -> new HashSet<>()).add(body);
    }


    public static void addAllBodies(Block ore) {
        for (SolarSystem.Body celestial : SolarSystem.Body.values()) {
            addValidBody(ore, celestial);
        }
    }

    public static void addAllExcept(Block ore, SolarSystem.Body body) {
        for (SolarSystem.Body celestial : SolarSystem.Body.values()) {
            if (celestial == body) continue;
            addValidBody(ore, celestial);
        }
    }

    private int rectify(int meta) {
        return meta % this.META_COUNT;
    }


    public int getSubCount() {
        return SolarSystem.Body.values().length;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == this.getCreativeTab() || tab == CreativeTabs.SEARCH) {
            for (int i = 0; i < getSubCount(); i++)
                // 0, 1, 6 metas aren't used in generation at all + meta 6 doesn't have a texture
                if(!Set.of(0, 1, 6).contains(i)) items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        if (oreEnum != null) {
            ItemStack s = oreEnum.getDropFunction().apply(state, rand);
            return s.isEmpty() ? Item.getItemFromBlock(this) : s.getItem();
        }
        /*if (this == ModBlocks.ore_glowstone) return Items.GLOWSTONE_DUST;
        if (this == ModBlocks.ore_fire) return rand != null && rand.nextInt(10) == 0 ? ModItems.ingot_phosphorus : ModItems.powder_fire;
        if (this == ModBlocks.ore_lapis) return Items.DYE;
        if (this == ModBlocks.ore_emerald) return Items.EMERALD;
        if (this == ModBlocks.ore_quartz) return Items.QUARTZ;
        if (this == ModBlocks.ore_diamond) return Items.DIAMOND;*/
        return Item.getItemFromBlock(this);
    }

    /*@Override
    public int quantityDropped(Random rand) {
        if (this == ModBlocks.ore_lapis) return 4 + rand.nextInt(5);
        return 1;
    }*/

    @Override
    public int quantityDroppedWithBonus(int fortune, @NotNull Random rand) {
        if (oreEnum != null) {
            // handled in getDrops via oreEnum.quantityFunction
            return 0;
        }
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getDefaultState(), rand, fortune)) {
            int mult = rand.nextInt(fortune + 2) - 1;
            if (mult < 0) mult = 0;
            return this.quantityDropped(rand) * (mult + 1);
        } else {
            return this.quantityDropped(rand);
        }
    }

    @Override
    public int damageDropped(@NotNull IBlockState state) {
        if (this == ModBlocks.ore_rare) return ItemEnums.EnumChunkType.RARE.ordinal();
        //if (this == ModBlocks.ore_lapis) return 4;
        if (getItemDropped(state, RANDOM, 0) != Item.getItemFromBlock(this)) return 0;
        return rectify(state.getValue(META));
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();

        if (oreEnum != null) {
            int count = oreEnum.getQuantityFunction().apply(state, fortune, rand);
            List<ItemStack> list = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                ItemStack drop = oreEnum.getDropFunction().apply(state, rand);
                if (!drop.isEmpty()) list.add(drop.copy());
            }
            return list;
        }

        Item item = getItemDropped(state, rand, fortune);
        if (item == Item.getItemFromBlock(this)) {
            return Collections.singletonList(new ItemStack(this, 1, damageDropped(state)));
        }

        int count = quantityDroppedWithBonus(fortune, rand);
        /*if (this == ModBlocks.ore_lapis) {
            return Collections.singletonList(new ItemStack(Items.DYE, count, 4));
        }*/
        return Collections.singletonList(new ItemStack(item, count, 0));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!SpaceConfig.showOreLocations) return;
        Set<SolarSystem.Body> bodies = spawnMap.get(this);
        if (bodies == null || bodies.isEmpty()) return;

        if (bodies.size() == SolarSystem.Body.values().length) {
            tooltip.add(TextFormatting.GOLD + "Can be found anywhere");
            return;
        } else if (bodies.size() == SolarSystem.Body.values().length - 1) {
            tooltip.add(TextFormatting.GOLD + "Can be found anywhere except:");
            for (SolarSystem.Body body : SolarSystem.Body.values()) {
                if (bodies.contains(body)) continue;
                tooltip.add(TextFormatting.RED + " - " + I18nUtil.resolveKey("body." + body.name));
            }
            return;
        }

        tooltip.add(TextFormatting.GOLD + "Can be found on:");
        for (SolarSystem.Body body : bodies) {
            tooltip.add(TextFormatting.AQUA + " - " + I18nUtil.resolveKey("body." + body.name));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        int meta = stack.getItemDamage();
        world.setBlockState(pos, state.withProperty(META, rectify(meta)), 2);
    }

    // Blockstate and meta

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(META, rectify(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(META));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(META);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(META, rectify(meta));
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        for (int meta = 0; meta < META_COUNT; meta++) {
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(this),
                    meta,
                    new ModelResourceLocation(this.getRegistryName(), "meta=" + meta)
            );
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        // Register overlay ore texture (this block's texture)
        map.registerSprite(new ResourceLocation(Tags.MODID, "blocks/" + this.getRegistryName().getPath()));
        // Register all stone textures
        for (int i = 0; i < META_COUNT; i++) {
            String stone = getStoneTextureName(SolarSystem.Body.values()[i]);
            map.registerSprite(resolveTexture(stone));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel cube = ModelLoaderRegistry.getModel(new ResourceLocation("block/cube_all"));
            // Since resource loading fucks itself in the ass, I had to move the ore textures to "hbmspace" folder instead of default "hbm" one
            String oreTex = new ResourceLocation(Tags.MODID, "blocks/" + this.getRegistryName().getPath()).toString();

            for (int meta = 0; meta < META_COUNT; meta++) {
                String stoneTex = resolveTexture(getStoneTextureName(SolarSystem.Body.values()[meta])).toString();

                IModel baseStone = cube.retexture(ImmutableMap.of("all", stoneTex));
                IBakedModel bakedStone = baseStone.bake(ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

                IModel baseOre = cube.retexture(ImmutableMap.of("all", oreTex));
                IBakedModel bakedOre = baseOre.bake(ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

                IBakedModel compositeBlock = new CompositeBakedModel(bakedStone, bakedOre);

                ModelResourceLocation mrl = new ModelResourceLocation(this.getRegistryName(), "meta=" + meta);
                event.getModelRegistry().putObject(mrl, compositeBlock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                int meta = state.getValue(META);
                if (meta < META_COUNT) return new ModelResourceLocation(loc, "meta=" + meta);
                else return new ModelResourceLocation(loc, "meta=0");
            }
        };
    }


    private String getStoneTextureName(SolarSystem.Body body) {
        if (body.ordinal() == 0) return "stone";
        return body.getStoneTexture();
    }

    private ResourceLocation resolveTexture(String name) {
        if ("stone".equals(name)) {
            return new ResourceLocation("minecraft", "blocks/stone");
        }
        if (name.contains(":")) {
            String[] parts = name.split(":", 2);
            String ns = parts[0];
            String path = parts[1];
            if (!path.startsWith("blocks/")) path = "blocks/" + path;
            return new ResourceLocation(ns, path);
        } else {
            return new ResourceLocation("hbm:blocks/" + name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT_MIPPED;
    }

    @SideOnly(Side.CLIENT)
    private static class CompositeBakedModel implements IBakedModel {
        private final IBakedModel base;
        private final IBakedModel overlay;

        CompositeBakedModel(IBakedModel base, IBakedModel overlay) {
            this.base = base;
            this.overlay = overlay;
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
            List<BakedQuad> quads = new ArrayList<>(base.getQuads(state, side, rand));
            quads.addAll(overlay.getQuads(state, side, rand));
            return quads;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return base.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return base.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return base.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return base.getParticleTexture();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return base.getItemCameraTransforms();
        }
    }
}
