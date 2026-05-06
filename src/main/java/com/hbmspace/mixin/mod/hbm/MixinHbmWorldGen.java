package com.hbmspace.mixin.mod.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.lib.HbmWorldGen;
import com.hbm.world.generator.DungeonToolbox;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.enums.EnumAddonFlowerPlantTypes;
import com.hbmspace.main.SpaceMain;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = HbmWorldGen.class, remap = false)
public class MixinHbmWorldGen {
    @Redirect(
            method = "generateOres",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/hbm/world/generator/DungeonToolbox;generateOre(Lnet/minecraft/world/World;Ljava/util/Random;IIIIIILnet/minecraft/block/Block;Lnet/minecraft/block/Block;)V"
            )
    )
    public void redirectLithiumToZinc(World world, Random rand, int x, int z, int count, int size, int min, int max, Block ore, Block replace) {
        if (ore == ModBlocks.ore_gneiss_lithium) {
            DungeonToolbox.generateOre(world, rand, x, z, WorldConfigSpace.zincSpawn, 6, 5, 32, ModBlocksSpace.ore_zinc);
        } else {
            DungeonToolbox.generateOre(world, rand, x, z, count, size, min, max, ore, replace);
        }
    }


    @Inject(
            method = "generatePlants",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;nextInt(I)I",
                    ordinal = 2,
                    remap = false
            )
    )
    private void injectCustomPlants(World world, Random rand, int chunkMinX, int chunkMinZ, CallbackInfo ci, @Local(ordinal = 0) int x, @Local(ordinal = 1) int z) {
        if (rand.nextInt(32) == 0) {
            SpaceMain.INSTANCE_STRAWBERRY.generate(world, rand, new BlockPos(x, 0, z));
        }
        if (rand.nextInt(32) == 0) {
            SpaceMain.INSTANCE_MINT.generate(world, rand, new BlockPos(x, 0, z));
        }
    }
}
