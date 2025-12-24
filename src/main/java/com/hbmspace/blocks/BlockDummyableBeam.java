package com.hbmspace.blocks;

import com.hbm.blocks.ILookOverlay;
import com.hbm.lib.ForgeDirection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockDummyableBeam extends BlockDummyableSpace implements ILookOverlay {

    // Passes on any interactions to the true dummyable

    public BlockDummyableBeam(Material mat, String s) {
        super(mat, s);
        setLightLevel(1.0F);
        setLightOpacity(0);
    }

    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {0, 0, 0, 0, 0, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public int[] findCore(IBlockAccess world, int x, int y, int z) {
        IBlockState state = world.getBlockState(new BlockPos(x, y, z));
        int metadata = this.getMetaFromState(state);

        // if it's an extra, remove the extra-ness
        if (metadata >= extra) metadata -= extra;

        ForgeDirection dir = ForgeDirection.getOrientation(metadata).getOpposite();

        x += dir.offsetX;
        y += dir.offsetY;
        z += dir.offsetZ;

        Block b = world.getBlockState(new BlockPos(x, y, z)).getBlock();

        if (b instanceof BlockDummyableSpace && !(b instanceof BlockDummyableBeam)) {
            return ((BlockDummyableSpace) b).findCore(world, x, y, z);
        }

        return null;
    }

    @Override
    public void breakBlock(@NotNull World world, @NotNull BlockPos pos, IBlockState state) {
        int metadata = this.getMetaFromState(state);

        // if it's an extra, remove the extra-ness
        if (metadata >= extra) metadata -= extra;

        ForgeDirection dir = ForgeDirection.getOrientation(metadata).getOpposite();

        BlockPos corePos = pos.add(dir.offsetX, dir.offsetY, dir.offsetZ);
        IBlockState coreState = world.getBlockState(corePos);
        Block b = coreState.getBlock();

        if (b instanceof BlockDummyableSpace) {
            b.breakBlock(world, corePos, coreState);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, World world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        if (world.isRemote || safeRem) return;

        int metadata = this.getMetaFromState(state);

        // if it's an extra, remove the extra-ness
        if (metadata >= extra) metadata -= extra;

        ForgeDirection dir = ForgeDirection.getOrientation(metadata).getOpposite();
        BlockPos corePos = pos.add(dir.offsetX, dir.offsetY, dir.offsetZ);
        Block b = world.getBlockState(corePos).getBlock();

        if (!(b instanceof BlockDummyableSpace)) {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void updateTick(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        if (world.isRemote) return;

        int metadata = this.getMetaFromState(state);

        // if it's an extra, remove the extra-ness
        if (metadata >= extra) metadata -= extra;

        ForgeDirection dir = ForgeDirection.getOrientation(metadata).getOpposite();
        BlockPos corePos = pos.add(dir.offsetX, dir.offsetY, dir.offsetZ);
        Block b = world.getBlockState(corePos).getBlock();

        if (!(b instanceof BlockDummyableSpace)) {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        int metadata = this.getMetaFromState(state);

        // if it's an extra, remove the extra-ness
        if (metadata >= extra) metadata -= extra;

        ForgeDirection dir = ForgeDirection.getOrientation(metadata).getOpposite();

        BlockPos corePos = pos.add(dir.offsetX, dir.offsetY, dir.offsetZ);
        IBlockState coreState = world.getBlockState(corePos);
        Block b = coreState.getBlock();

        if (b instanceof BlockDummyableSpace && !(b instanceof BlockDummyableBeam)) {
            return b.onBlockActivated(world, corePos, coreState, player, hand, facing, hitX, hitY, hitZ);
        }

        return false;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {
        IBlockState state = world.getBlockState(new BlockPos(x, y, z));
        int metadata = this.getMetaFromState(state);

        // if it's an extra, remove the extra-ness
        if (metadata >= extra) metadata -= extra;

        ForgeDirection dir = ForgeDirection.getOrientation(metadata).getOpposite();

        x += dir.offsetX;
        y += dir.offsetY;
        z += dir.offsetZ;

        Block b = world.getBlockState(new BlockPos(x, y, z)).getBlock();

        if (b instanceof BlockDummyableSpace && !(b instanceof BlockDummyableBeam) && b instanceof ILookOverlay) {
            ((ILookOverlay) b).printHook(event, world, x, y, z);
        }
    }

}
