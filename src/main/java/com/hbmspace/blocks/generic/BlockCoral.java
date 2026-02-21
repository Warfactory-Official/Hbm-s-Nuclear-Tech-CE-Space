package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.BlockEnumMeta;
import com.hbm.items.ModItems;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockEnumMetaSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public class BlockCoral extends BlockEnumMetaSpace<BlockCoral.EnumCoral> implements ITileEntityProvider {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockCoral(String s) {
        super(Material.WATER, SoundType.PLANT, s, EnumCoral.VALUES, false, true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0).withProperty(FACING, EnumFacing.UP));
        GameRegistry.registerTileEntity(TileEntityCoral.class, new ResourceLocation("hbmspace", "coral_te"));
    }

    public enum EnumCoral {
        TUBE,
        BRAIN,
        BUBBLE,
        FIRE,
        HORN;
        public static final EnumCoral[] VALUES = values();
    }

    @Override
    protected BlockBakeFrame[] generateBlockFrames(String registryName) {
        BlockBakeFrame[] frames = new BlockBakeFrame[EnumCoral.values().length];
        for (int i = 0; i < EnumCoral.values().length; i++) {
            String textureName = registryName + "." + EnumCoral.values()[i].name().toLowerCase(Locale.US);
            frames[i] = new BlockBakeFrame(BlockBakeFrame.BlockForm.CROSS, textureName);
        }
        return frames;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        // Keeps META for variant and LEVEL for liquid compat, adds FACING for rendering
        return new BlockStateContainer(this, META, FACING, BlockLiquid.LEVEL);
    }

    @Override
    public @NotNull IBlockState getActualState(@NotNull IBlockState state, IBlockAccess worldIn, @NotNull BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityCoral) {
            return state.withProperty(FACING, ((TileEntityCoral) te).facing);
        }
        return state;
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
    public boolean canPlaceBlockAt(@NotNull World worldIn, @NotNull BlockPos pos) {
        if (!worldIn.getBlockState(pos.up()).getMaterial().isLiquid()) return false;
        for (EnumFacing facing : EnumFacing.values()) {
            if (canAttachTo(worldIn, pos, facing)) return true;
        }
        return false;
    }

    public boolean canAttachTo(World world, BlockPos pos, EnumFacing facing) {
        BlockPos anchorPos = pos.offset(facing.getOpposite());
        IBlockState anchorState = world.getBlockState(anchorPos);
        return anchorState.isSideSolid(world, anchorPos, facing);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        return canAttachTo(worldIn, pos, facing);
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos, getActualState(state, worldIn, pos)) || !worldIn.getBlockState(pos.up()).getMaterial().isLiquid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return ModItems.powder_calcium;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(ModItems.powder_calcium));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void registerItem() {
        ItemBlockCoral itemBlock = new ItemBlockCoral(this);
        itemBlock.setRegistryName(this.getRegistryName());
        itemBlock.setCreativeTab(this.getCreativeTab());
        ForgeRegistries.ITEMS.register(itemBlock);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TileEntityCoral();
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState state) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMapperBase() {
            @Override
            protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
                int meta = state.getValue(META);
                EnumFacing facing = state.getValue(FACING);
                String props = String.format("facing=%s,meta=%d", facing.getName(), meta);
                return new ModelResourceLocation(loc, props);
            }
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        Item item = Item.getItemFromBlock(this);
        for (EnumCoral coral : EnumCoral.values()) {
            ModelLoader.setCustomModelResourceLocation(item, coral.ordinal(),
                    new ModelResourceLocation(this.getRegistryName(), "inventory_variant=" + coral.name().toLowerCase(Locale.US)));
        }
    }

    // Th3_Sl1ze: welp, guys, sorry, can't shrink 5x6 = 30 down to 16 nor I want to use block allocators or smth like that
    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel itemBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:item/generated"));

            for (int meta = 0; meta < EnumCoral.values().length; meta++) {
                EnumCoral coral = EnumCoral.values()[meta];
                BlockBakeFrame blockFrame = blockFrames[meta % blockFrames.length];
                String texture = blockFrame.getSpriteLoc(0).toString();

                ImmutableMap<String, String> itemTextures = ImmutableMap.of("layer0", texture);
                IBakedModel bakedItemModel = itemBaseModel.retexture(itemTextures).bake(
                        ModelRotation.X0_Y0,
                        DefaultVertexFormats.ITEM,
                        ModelLoader.defaultTextureGetter()
                );

                ModelResourceLocation itemLoc = new ModelResourceLocation(this.getRegistryName(), "inventory_variant=" + coral.name().toLowerCase(Locale.US));
                event.getModelRegistry().putObject(itemLoc, bakedItemModel);

                IModel blockBaseModel = ModelLoaderRegistry.getModel(new ResourceLocation(blockFrame.getBaseModel()));
                ImmutableMap.Builder<String, String> blockTextureMap = ImmutableMap.builder();
                blockFrame.putTextures(blockTextureMap);
                IModel retexturedBlock = blockBaseModel.retexture(blockTextureMap.build());

                for (EnumFacing facing : EnumFacing.values()) {
                    ModelRotation rotation = getRotation(facing);
                    IBakedModel bakedBlockModel = retexturedBlock.bake(
                            rotation,
                            DefaultVertexFormats.BLOCK,
                            ModelLoader.defaultTextureGetter()
                    );

                    ModelResourceLocation blockLoc = new ModelResourceLocation(this.getRegistryName(), String.format("facing=%s,meta=%d", facing.getName(), meta));
                    event.getModelRegistry().putObject(blockLoc, bakedBlockModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ModelRotation getRotation(EnumFacing facing) {
        return switch (facing) {
            case DOWN -> ModelRotation.X180_Y0;
            case NORTH -> ModelRotation.X90_Y0;
            case SOUTH -> ModelRotation.X90_Y180;
            case WEST -> ModelRotation.X90_Y270;
            case EAST -> ModelRotation.X90_Y90;
            default -> ModelRotation.X0_Y0;
        };
    }

    public static class TileEntityCoral extends TileEntity {
        public EnumFacing facing = EnumFacing.UP;

        @Override
        public void readFromNBT(@NotNull NBTTagCompound compound) {
            super.readFromNBT(compound);
            facing = EnumFacing.byIndex(compound.getInteger("facing"));
        }

        @Override
        public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
            super.writeToNBT(compound);
            compound.setInteger("facing", facing.getIndex());
            return compound;
        }

        @Override
        public @NotNull NBTTagCompound getUpdateTag() {
            return writeToNBT(new NBTTagCompound());
        }

        @Override
        public void handleUpdateTag(@NotNull NBTTagCompound tag) {
            readFromNBT(tag);
        }

        @Nullable
        @Override
        public SPacketUpdateTileEntity getUpdatePacket() {
            return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
        }

        @Override
        public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity pkt) {
            handleUpdateTag(pkt.getNbtCompound());
        }
    }

    public static class ItemBlockCoral extends BlockEnumMeta<EnumCoral>.EnumMetaBlockItem {
        public ItemBlockCoral(Block block) {
            ((BlockEnumMeta<EnumCoral>) block).super(block);
        }


        @Override
        public boolean placeBlockAt(@NotNull ItemStack stack, @NotNull EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull IBlockState newState) {
            EnumFacing placementFacing = side;

            BlockCoral coral = (BlockCoral) this.block;

            if (!coral.canAttachTo(world, pos, placementFacing)) {
                boolean found = false;
                for(EnumFacing f : EnumFacing.values()) {
                    if(coral.canAttachTo(world, pos, f)) {
                        placementFacing = f;
                        found = true;
                        break;
                    }
                }
                if(!found) return false;
            }

            if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityCoral) {
                    ((TileEntityCoral) te).facing = placementFacing;
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                }
                return true;
            }
            return false;
        }
    }
}
