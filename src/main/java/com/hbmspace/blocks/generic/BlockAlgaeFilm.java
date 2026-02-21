package com.hbmspace.blocks.generic;

import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.render.model.BlockAlgaeBakedModel;
import com.hbmspace.tileentity.machine.TileEntityAlgaeFilm;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockAlgaeFilm extends BlockContainer implements ITileEntityProvider, ILookOverlay, ITooltipProvider, IDynamicModelsSpace {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected final int META_COUNT = 16;

    public BlockAlgaeFilm(Material mat, String registryName) {
        super(mat);
        this.setRegistryName(registryName);
        this.setTranslationKey(registryName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setSoundType(SoundType.METAL);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
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
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TileEntityAlgaeFilm();
    }

    @Override
    public @NotNull IBlockState getStateForPlacement(@NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        // 1.7.10 logic: faced towards the player (opposite of looking direction)
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof TileEntityAlgaeFilm film)) return;

        List<String> text = new ArrayList<>();

        if (!film.canOperate) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.noGravity") + " ! ! !");
        }

        text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + film.tanks[0].getTankType().getLocalizedName() + ": " + film.tanks[0].getFill() + "/" + film.tanks[0].getMaxFill() + "mB");
        text.add(TextFormatting.RED + "<- " + TextFormatting.RESET + film.tanks[1].getTankType().getLocalizedName() + ": " + film.tanks[1].getFill() + "/" + film.tanks[1].getMaxFill() + "mB");

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        // Assuming addStandardInfo is available via ITooltipProvider or a utility method in your workspace
        // If not, implementing the standard info logic directly is required.
        // addStandardInfo(stack, worldIn, tooltip, flagIn);
    }

    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        map.registerSprite(new ResourceLocation("hbm:blocks/algae_film"));
    }

    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        TextureAtlasSprite sprite = Minecraft.getMinecraft()
                .getTextureMapBlocks()
                .getAtlasSprite(new ResourceLocation("hbm:blocks/algae_film").toString());

        IBakedModel baked = BlockAlgaeBakedModel.forBlock(ResourceManagerSpace.algae_film, sprite);

        // Register for all facing variants
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), "facing=" + facing.getName());
            event.getModelRegistry().putObject(mrl, baked);
        }

        // Register for inventory
        ModelResourceLocation inventoryMrl = new ModelResourceLocation(getRegistryName(), "meta=0");
        event.getModelRegistry().putObject(inventoryMrl, baked);
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

    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMapperBase() {
            @Override
            protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
                return new ModelResourceLocation(loc, "facing=" + state.getValue(FACING).getName());
            }
        };
    }
}