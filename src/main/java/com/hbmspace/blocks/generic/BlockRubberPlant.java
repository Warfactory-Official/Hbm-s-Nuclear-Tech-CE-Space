package com.hbmspace.blocks.generic;

import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockEnumMetaSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class BlockRubberPlant extends BlockEnumMetaSpace<BlockRubberPlant.EnumRubberPlantType> {

    public BlockRubberPlant(String s) {
        super(Material.VINE, SoundType.PLANT, s, EnumRubberPlantType.VALUES, false, true);
    }

    public enum EnumRubberPlantType {
        GRASS,
        FERN,
        SHRUB;
        public static final EnumRubberPlantType[] VALUES = values();
    }

    @Override
    protected BlockBakeFrame[] generateBlockFrames(String registryName) {
        return Arrays.stream(blockEnum)
                .map(Enum::name)
                .map(name -> registryName + "." + name.toLowerCase(Locale.US))
                .map(tex -> new BlockBakeFrame(BlockBakeFrame.BlockForm.CROSS, tex))
                .toArray(BlockBakeFrame[]::new);
    }

    @Override
    public boolean canPlaceBlockAt(@NotNull World world, @NotNull BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos);
    }

    protected boolean canPlaceBlockOn(Block block) {
        return block == ModBlocksSpace.rubber_grass || block == ModBlocksSpace.rubber_silt || block == ModBlocksSpace.rubber_farmland;
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        this.checkAndDropBlock(world, pos, state);
    }

    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World world, BlockPos pos) {
        return canPlaceBlockOn(world.getBlockState(pos.down()).getBlock());
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

    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int damageDropped(@NotNull IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return Items.AIR;
    }
}