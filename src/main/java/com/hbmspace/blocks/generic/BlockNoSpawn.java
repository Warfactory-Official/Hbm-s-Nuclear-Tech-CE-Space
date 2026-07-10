package com.hbmspace.blocks.generic;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockBakeBaseSpace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

// TODO that should be in main NTM since colored concrete uses that (tho I may leave it here for reverse compat idk)
public class BlockNoSpawn extends BlockBakeBaseSpace implements ITooltipProvider {

    public BlockNoSpawn(String s) {
        super(Material.GRASS, s, BlockBakeFrame.column("astroturf_top", "astroturf_side"));
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }
}
