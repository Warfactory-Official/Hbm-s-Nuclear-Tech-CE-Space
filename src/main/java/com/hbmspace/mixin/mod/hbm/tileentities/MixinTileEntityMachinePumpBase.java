package com.hbmspace.mixin.mod.hbm.tileentities;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.tileentity.machine.TileEntityMachinePumpBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.orbit.WorldProviderOrbit;
import com.hbmspace.dim.trait.CBT_Water;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

import static com.hbm.tileentity.machine.TileEntityMachinePumpBase.groundDepth;

@Mixin(value = TileEntityMachinePumpBase.class, remap = false)
public abstract class MixinTileEntityMachinePumpBase {

    @Final
    @Shadow
    public static HashSet<Block> validBlocks;

    @Shadow
    public FluidTankNTM water;

    @Overwrite
    protected boolean checkGround() {
        TileEntity te = ((TileEntity) (Object) this);
        World world = te.getWorld();
        BlockPos pos = te.getPos();
        if(!world.provider.hasSkyLight()) return false;
        if(world.provider instanceof WorldProviderOrbit) return false;

        CBT_Water table = CelestialBody.getTrait(world, CBT_Water.class);
        if(table == null) return false;

        water.setTankType(table.fluid);

        int validBlocksCount = 0;
        int invalidBlocksCount = 0;

        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y >= -groundDepth; y--) {
                for(int z = -1; z <= 1; z++) {
                    BlockPos checkPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    IBlockState state = world.getBlockState(checkPos);
                    Block b = state.getBlock();

                    if(y == -1 && !b.isNormalCube(state, world, checkPos)) return false;

                    if(validBlocks.contains(b)) validBlocksCount++;
                    else invalidBlocksCount++;
                }
            }
        }

        return validBlocksCount >= invalidBlocksCount;
    }
}
