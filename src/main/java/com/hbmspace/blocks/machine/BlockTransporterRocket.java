package com.hbmspace.blocks.machine;

import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.main.ChunkLoaderManager;
import com.hbmspace.tileentity.machine.TileEntityTransporterBase;
import com.hbmspace.tileentity.machine.TileEntityTransporterRocket;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockTransporterRocket extends BlockDummyableSpace {

    public BlockTransporterRocket(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityTransporterRocket();
        if(meta >= 6) return new TileEntityProxyCombo().fluid();
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return playerIn.getHeldItem(hand).isEmpty() || playerIn.getHeldItem(hand).getItem() != ModItemsSpace.transporter_linker;
        }

        if (playerIn.getHeldItem(hand).isEmpty() || playerIn.getHeldItem(hand).getItem() != ModItemsSpace.transporter_linker) {
            return standardOpenBehavior(worldIn, pos, playerIn, 0);
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (world.isRemote) return;

        // If we don't have a position, we failed to place, and should skip chunkloading
        int[] corePos = findCore(world, pos.getX(), pos.getY(), pos.getZ());
        if (corePos != null) {
            ChunkLoaderManager.forceChunk(world, corePos[0], corePos[1], corePos[2]);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        int meta = getMetaFromState(state);
        if (meta >= 12) {
            ChunkLoaderManager.unforceChunk(world, pos.getX(), pos.getY(), pos.getZ());

            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityTransporterBase) {
                ((TileEntityTransporterBase) te).unlinkTransporter();
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {1, 0, 1, 1, 1, 2};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 0, 1, 1, 1, 2}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {1, 0, 1, 1, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {1, 0, 1, 1, -2, 2}, this, dir);

        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);


        this.makeExtra(world, x + 1, y, z + 1);
        this.makeExtra(world, x + 1, y, z - 1);
        this.makeExtra(world, x - 1, y, z + 1);
        this.makeExtra(world, x - 1, y, z - 1);

        this.makeExtra(world, x + dir.offsetX - rot.offsetX * 2, y, z + dir.offsetZ - rot.offsetZ * 2);
        this.makeExtra(world, x - dir.offsetX - rot.offsetX * 2, y, z - dir.offsetZ - rot.offsetZ * 2);
    }

}
