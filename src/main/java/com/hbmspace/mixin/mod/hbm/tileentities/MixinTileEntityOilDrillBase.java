package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.machine.oil.TileEntityOilDrillBase;
import com.hbm.util.BobMathUtil;
import com.hbmspace.blocks.generic.BlockOreFluid;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

@Mixin(value = TileEntityOilDrillBase.class, remap = false)
public abstract class MixinTileEntityOilDrillBase {

    @Shadow public FluidTankNTM[] tanks;

    @Shadow HashSet<BlockPos> processed;

    @Shadow public abstract boolean canPump();

    @Overwrite
    public boolean canSuckBlock(Block b) {
        return (b instanceof BlockOreFluid && b != ModBlocks.ore_bedrock_oil) || BlockOreFluid.getFullBlock(b) != null;
    }

    @Overwrite
    public boolean trySuck(int y) {
        TileEntity te = (TileEntity) (Object) this;
        World world = te.getWorld();
        BlockPos pos = te.getPos();

        BlockPos startPos = new BlockPos(pos.getX(), y, pos.getZ());
        Block startBlock = world.getBlockState(startPos).getBlock();

        if (!canSuckBlock(startBlock)) return false;
        if (!this.canPump()) return true;

        Queue<BlockPos> queue = new ArrayDeque<>();
        processed.clear();
        queue.offer(startPos);
        processed.add(startPos);

        int nodesVisited = 0;
        while (!queue.isEmpty() && nodesVisited < 256) {
            BlockPos currentPos = queue.poll();
            nodesVisited++;
            Block currentBlock = world.getBlockState(currentPos).getBlock();

            if (currentBlock instanceof BlockOreFluid) {
                onSuck((BlockOreFluid) currentBlock, currentPos);
                return true;
            }

            if (BlockOreFluid.getFullBlock(currentBlock) == null) continue;

            for (ForgeDirection dir : BobMathUtil.getShuffledDirs()) {
                BlockPos neighborPos = currentPos.add(dir.offsetX, dir.offsetY, dir.offsetZ);
                if (!processed.contains(neighborPos) && canSuckBlock(world.getBlockState(neighborPos).getBlock())) {
                    processed.add(neighborPos);
                    queue.offer(neighborPos);
                }
            }
        }
        return false;
    }

    public void onSuck(BlockOreFluid block, BlockPos targetPos) {
        World world = ((TileEntity) (Object) this).getWorld();
        IBlockState state = world.getBlockState(targetPos);
        int meta = block.getMetaFromState(state);

        tanks[0].setTankType(block.getPrimaryFluid(meta));
        tanks[1].setTankType(block.getSecondaryFluid(meta));

        tanks[0].setFill(Math.min(tanks[0].getFill() + getPrimaryFluidAmount(block, meta), tanks[0].getMaxFill()));
        if (tanks[1].getTankType() != Fluids.NONE) {
            tanks[1].setFill(Math.min(tanks[1].getFill() + getSecondaryFluidAmount(block, meta), tanks[1].getMaxFill()));
        }

        attemptDrain(block, targetPos, meta);
    }

    protected int getPrimaryFluidAmount(BlockOreFluid block, int meta) {
        return block.getPrimaryFluidAmount(meta);
    }

    protected int getSecondaryFluidAmount(BlockOreFluid block, int meta) {
        return block.getSecondaryFluidAmount(meta);
    }

    protected void attemptDrain(BlockOreFluid block, BlockPos targetPos, int meta) {
        World world = ((TileEntity) (Object) this).getWorld();
        block.drain(world, targetPos, meta, 1);
    }
}