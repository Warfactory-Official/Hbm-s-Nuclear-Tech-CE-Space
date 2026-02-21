package com.hbmspace.blocks.generic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector3f;

import java.util.Collections;
import java.util.List;
import java.util.Map;
// Th3_Sl1ze: achtung, ai slop inbound
// dealing with specific baked models is fucking pain
public class BlockVinylVine extends BlockVine implements IDynamicModelsSpace {

    public BlockVinylVine(String name) {
        super();
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setCreativeTab(MainRegistry.controlTab);
        this.setSoundType(SoundType.PLANT);
        this.setHardness(0.2F);

        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        if (canBlockStayCustom(worldIn, pos)) {
            return;
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public boolean canPlaceBlockAt(@NotNull World worldIn, @NotNull BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) || this.canBlockStayCustom(worldIn, pos);
    }

    private boolean canBlockStayCustom(World world, BlockPos pos) {
        BlockPos posBelow = pos.down();
        IBlockState stateBelow = world.getBlockState(posBelow);
        Block blockBelow = stateBelow.getBlock();
        return !stateBelow.isOpaqueCube() || blockBelow instanceof BlockRubberLeaves;
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
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        map.registerSprite(new ResourceLocation("hbm", "blocks/vine"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new DefaultStateMapper();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("hbm:blocks/vine");
        ItemCameraTransforms transforms = createBlockTransforms();

        List<BakedQuad> itemQuads = Lists.newArrayList();
        addDoublesidedQuads(itemQuads, EnumFacing.SOUTH, sprite, 0);

        IBakedModel bakedItemModel = new SimpleBakedModel(
                itemQuads,
                createEmptyFaceMap(), // MUST NOT be Collections.emptyMap(), must contain lists for keys
                false,
                false,
                sprite,
                transforms,
                ItemOverrideList.NONE
        );

        event.getModelRegistry().putObject(
                new ModelResourceLocation(this.getRegistryName(), "inventory"),
                bakedItemModel
        );

        StateMapperBase mapper = new DefaultStateMapper();

        for (IBlockState state : this.getBlockState().getValidStates()) {
            List<BakedQuad> quads = Lists.newArrayList();

            if (state.getValue(UP)) {
                addDoublesidedQuads(quads, EnumFacing.UP, sprite, -1);
            }
            if (state.getValue(NORTH)) {
                addDoublesidedQuads(quads, EnumFacing.NORTH, sprite, -1);
            }
            if (state.getValue(EAST)) {
                addDoublesidedQuads(quads, EnumFacing.EAST, sprite, -1);
            }
            if (state.getValue(SOUTH)) {
                addDoublesidedQuads(quads, EnumFacing.SOUTH, sprite, -1);
            }
            if (state.getValue(WEST)) {
                addDoublesidedQuads(quads, EnumFacing.WEST, sprite, -1);
            }

            if (quads.isEmpty()) {
                addDoublesidedQuads(quads, EnumFacing.SOUTH, sprite, -1);
            }

            IBakedModel finalModel = new SimpleBakedModel(
                    quads,
                    createEmptyFaceMap(),
                    false,
                    false,
                    sprite,
                    transforms,
                    ItemOverrideList.NONE
            );

            ModelResourceLocation stateMRL = new ModelResourceLocation(
                    this.getRegistryName(),
                    mapper.getPropertyString(state.getProperties())
            );

            event.getModelRegistry().putObject(stateMRL, finalModel);
        }
    }

    private Map<EnumFacing, List<BakedQuad>> createEmptyFaceMap() {
        Map<EnumFacing, List<BakedQuad>> map = Maps.newEnumMap(EnumFacing.class);
        for (EnumFacing f : EnumFacing.VALUES) {
            map.put(f, Collections.emptyList());
        }
        return map;
    }

    private void addDoublesidedQuads(List<BakedQuad> quads, EnumFacing face, TextureAtlasSprite sprite, int tintIndex) {
        float offset = 0.0f; // Flush with block face

        Vector3f from = new Vector3f(0, 0, 0);
        Vector3f to = new Vector3f(16, 16, 16);

        switch (face) {
            case UP:    from.set(0, 16f - offset, 0); to.set(16, 16f - offset, 16); break;
            case DOWN:  from.set(0, offset, 0);       to.set(16, offset, 16);       break;
            case NORTH: from.set(0, 0, offset);       to.set(16, 16, offset);       break;
            case SOUTH: from.set(0, 0, 16f - offset); to.set(16, 16, 16f - offset); break;
            case WEST:  from.set(offset, 0, 0);       to.set(offset, 16, 16);       break;
            case EAST:  from.set(16f - offset, 0, 0); to.set(16f - offset, 16, 16); break;
        }

        FaceBakery bakery = new FaceBakery();
        quads.add(createQuad(bakery, from, to, face, sprite, tintIndex));
        quads.add(createQuad(bakery, from, to, face.getOpposite(), sprite, tintIndex));
    }

    private BakedQuad createQuad(FaceBakery bakery, Vector3f from, Vector3f to, EnumFacing face, TextureAtlasSprite sprite, int tintIndex) {
        BlockFaceUV uv = new BlockFaceUV(new float[]{0, 0, 16, 16}, 0);
        BlockPartFace partFace = new BlockPartFace(null, tintIndex, "", uv);

        return bakery.makeBakedQuad(
                from,
                to,
                partFace,
                sprite,
                face,
                ModelRotation.X0_Y0,
                null,
                false,
                true
        );
    }

    private ItemCameraTransforms createBlockTransforms() {
        return new ItemCameraTransforms(
                getTransform(0, 0, 0, 75, 45, 0, 0.375f),       // Third Person Left
                getTransform(0, 0, 0, 75, 45, 0, 0.375f),       // Third Person Right
                getTransform(0, 0, 0, 0, 45, 0, 0.4f),          // First Person Left
                getTransform(0, 0, 0, 0, 45, 0, 0.4f),          // First Person Right
                getTransform(0, 0, 0, 0, 0, 0, 1.0f),           // Head
                getTransform(0, 0, 0, 0, 0, 0, 1.0f),      // GUI
                getTransform(0, 3, 0, 0, 0, 0, 0.25f),          // Ground
                getTransform(0, 0, 0, 0, 0, 0, 0.5f)            // Fixed
        );
    }

    private ItemTransformVec3f getTransform(float tx, float ty, float tz, float rx, float ry, float rz, float s) {
        return new ItemTransformVec3f(
                new Vector3f(rx, ry, rz),
                new Vector3f(tx / 16f, ty / 16f, tz / 16f), // Vanilla JSONs use pixels (0-16), code uses 0-1
                new Vector3f(s, s, s)
        );
    }
}