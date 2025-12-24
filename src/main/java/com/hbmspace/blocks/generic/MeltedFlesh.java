package com.hbmspace.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockLayering;
import com.hbm.items.IDynamicModels;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.trait.CBT_Temperature;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MeltedFlesh extends BlockLayering implements IDynamicModelsSpace {

    public MeltedFlesh(Material material, String s, String texture) {
        super(material, s, texture);
        this.setTickRandomly(true);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public @NotNull Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (this == ModBlocksSpace.flesh_block) {
            return ModItemsSpace.flesh;
        } /*else if (this == ModBlocks.charred_flesh_block) {
            return ModItems.grilled_flesh;
        }*/
        return ModItems.powder_coal_tiny;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        BlockPos belowPos = pos.down();
        IBlockState belowState = worldIn.getBlockState(belowPos);
        Block belowBlock = belowState.getBlock();

        if (belowBlock == Blocks.ICE || belowBlock == Blocks.PACKED_ICE) {
            return false;
        }

        if (belowBlock.isLeaves(belowState, worldIn, belowPos)) {
            return true;
        }

        if (belowBlock == this) {
            return true;
        }

        return belowState.isOpaqueCube() && belowState.getMaterial().blocksMovement();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.checkAndDropBlock(worldIn, pos);
    }

    private void checkAndDropBlock(World worldIn, BlockPos pos) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Random rand) {
        super.randomDisplayTick(state, worldIn, pos, rand);

        CBT_Temperature temperature = CelestialBody.getTrait(worldIn, CBT_Temperature.class);
        if (temperature != null && temperature.degrees > 100) {
            double fx = pos.getX() + rand.nextFloat();
            double fy = pos.getY() + rand.nextFloat() * 0.5F + 0.5F;
            double fz = pos.getZ() + rand.nextFloat();

            if (this == ModBlocksSpace.flesh_block) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx, fy, fz, 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle(EnumParticleTypes.CLOUD, fx, fy, fz, 0.0D, 0.0D, 0.0D);
            } else if (this == ModBlocksSpace.charred_flesh_block) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, fx, fy, fz, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void updateTick(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        CBT_Temperature temperature = CelestialBody.getTrait(worldIn, CBT_Temperature.class);
        if (temperature != null && temperature.degrees > 100) {
            if (this == ModBlocksSpace.flesh_block) {
                worldIn.setBlockState(pos, ModBlocksSpace.charred_flesh_block.getDefaultState(), 3);
            } else if (this == ModBlocksSpace.charred_flesh_block) {
                worldIn.setBlockState(pos, ModBlocksSpace.carbonized_flesh_block.getDefaultState(), 3);
            }
        }
    }
}
