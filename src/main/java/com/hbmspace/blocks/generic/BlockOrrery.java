package com.hbmspace.blocks.generic;

import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockContainerBakeableSpace;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class BlockOrrery extends BlockContainerBakeableSpace {

    public BlockOrrery(Material mat, String name) {
        super(mat, name, new BlockBakeFrame("dummy_orrery"));
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        return new TileEntityOrrery();
    }

    @Override
    public @NotNull EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }
    @AutoRegister
    public static class TileEntityOrrery extends TileEntity {

        private AxisAlignedBB bb = null;

        @Override
        public @NotNull AxisAlignedBB getRenderBoundingBox() {
            if(bb == null) {
                bb = new AxisAlignedBB(
                        pos.getX() - 49,
                        pos.getY() - 19,
                        pos.getZ() - 49,
                        pos.getX() + 50,
                        pos.getY() + 20,
                        pos.getZ() + 50
                );
            }

            return bb;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public double getMaxRenderDistanceSquared() {
            return 65536.0D;
        }
    }
}