package com.hbmspace.mixin.mod.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.VersatileConfig;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.generic.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ExplosionNukeGeneric.class, remap = false)
public class MixinExplosionNukeGeneric {

    @Inject(method = "wasteDest", at = @At("HEAD"), cancellable = true)
    private static void spaceWasteDest(World world, BlockPos pos, CallbackInfo ci) {
        if (world.isRemote) return;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocksSpace.ore_uranium) return;

        if (world.rand.nextInt(VersatileConfig.getSchrabOreChance()) == 1) {
            world.setBlockState(pos, ModBlocksSpace.ore_schrabidium.getDefaultState().withProperty(BlockOre.META, state.getValue(BlockOre.META)));
        } else {
            world.setBlockState(pos, ModBlocks.ore_uranium_scorched.getDefaultState());
        }

        ci.cancel();
    }
}
