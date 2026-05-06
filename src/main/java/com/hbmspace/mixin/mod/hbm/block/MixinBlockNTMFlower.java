package com.hbmspace.mixin.mod.hbm.block;

import com.hbm.blocks.PlantEnums.EnumFlowerPlantType;
import com.hbm.blocks.generic.BlockNTMFlower;
import com.hbm.blocks.generic.BlockPlantEnumMeta;
import com.hbmspace.enums.EnumAddonFlowerPlantTypes;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.Random;

@Mixin(BlockNTMFlower.class)
public abstract class MixinBlockNTMFlower extends BlockPlantEnumMeta<EnumFlowerPlantType> {

    public MixinBlockNTMFlower(String registryName, EnumFlowerPlantType[] values) {
        super(registryName, values);
    }

    @Override
    public @NotNull Item getItemDropped(IBlockState state, @NotNull Random rand, int fortune) {
        int meta = state.getValue(META);

        if (EnumAddonFlowerPlantTypes.STRAWBERRY != null && meta == EnumAddonFlowerPlantTypes.STRAWBERRY.ordinal()) {
            return ModItemsSpace.strawberry;
        }

        if (EnumAddonFlowerPlantTypes.MINT != null && meta == EnumAddonFlowerPlantTypes.MINT.ordinal()) {
            return ModItemsSpace.mint_leaves;
        }

        return super.getItemDropped(state, rand, fortune);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int meta = state.getValue(META);

        if (EnumAddonFlowerPlantTypes.STRAWBERRY != null && meta == EnumAddonFlowerPlantTypes.STRAWBERRY.ordinal()) {
            drops.add(new ItemStack(ModItemsSpace.strawberry, 1, 0));
            return;
        }

        if (EnumAddonFlowerPlantTypes.MINT != null && meta == EnumAddonFlowerPlantTypes.MINT.ordinal()) {
            drops.add(new ItemStack(ModItemsSpace.mint_leaves, 1, 0));
            return;
        }

        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int meta = state.getValue(META);

        if (EnumAddonFlowerPlantTypes.STRAWBERRY != null && meta == EnumAddonFlowerPlantTypes.STRAWBERRY.ordinal()) {
            return List.of(new ItemStack(ModItemsSpace.strawberry, 1, 0));
        }

        if (EnumAddonFlowerPlantTypes.MINT != null && meta == EnumAddonFlowerPlantTypes.MINT.ordinal()) {
            return List.of(new ItemStack(ModItemsSpace.mint_leaves, 1, 0));
        }

        return super.getDrops(world, pos, state, fortune);
    }
}