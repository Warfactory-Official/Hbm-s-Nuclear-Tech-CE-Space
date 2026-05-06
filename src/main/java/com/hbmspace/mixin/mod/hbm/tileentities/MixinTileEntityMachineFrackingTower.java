package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbm.world.feature.OilSpot;
import com.hbmspace.blocks.generic.BlockOreFluid;
import com.hbmspace.dim.SolarSystem;
import com.hbm.tileentity.machine.oil.TileEntityMachineFrackingTower;
import com.hbm.tileentity.machine.oil.TileEntityOilDrillBase;
import com.hbmspace.util.OilSpaceUtil;
import com.hbmspace.world.feature.OilSpotSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityMachineFrackingTower.class, remap = false)
public abstract class MixinTileEntityMachineFrackingTower extends TileEntityOilDrillBase {

    @Shadow protected static int destructionRange;
    @Shadow protected static int solutionRequired;

    public void onSuck(BlockOreFluid block, BlockPos targetPos) {
        OilSpaceUtil.defaultOnSuck((TileEntity) (Object) this, block, targetPos, tanks);
        IBlockState state = this.world.getBlockState(pos);
        int meta = state.getBlock().getMetaFromState(state);

        tanks[2].setFill(tanks[2].getFill() - solutionRequired);

        if(meta == SolarSystem.Body.TEKTO.ordinal()) {
            OilSpotSpace.generateCrack(world, pos.getX(), pos.getZ(), destructionRange, 10);
        } else {
            OilSpot.generateOilSpot(world, pos.getX(), pos.getZ(), destructionRange, 10, false);
        }
    }
}