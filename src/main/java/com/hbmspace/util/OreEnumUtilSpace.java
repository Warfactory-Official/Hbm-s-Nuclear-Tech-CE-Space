package com.hbmspace.util;

import com.hbm.blocks.IOreType;
import com.hbm.blocks.OreEnumUtil;
import com.hbm.items.ModItems;
import com.hbm.lib.TriFunction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class OreEnumUtilSpace {

    public OreEnumUtilSpace() {
    }

    public static int base4Rand2Fortune(IBlockState state, int fortune, Random rand) {
        return 4 + rand.nextInt(2) + fortune;
    }
    public static int base4Rand5Fortune(IBlockState state, int fortune, Random rand) {
        return 4 + rand.nextInt(5) + fortune;
    }

    public enum SpaceOreEnum implements IOreType {

        REDSTONE(() -> new ItemStack(Items.REDSTONE), OreEnumUtilSpace::base4Rand2Fortune),
        EMERALD(() -> new ItemStack(Items.EMERALD), OreEnumUtil::vanillaFortune),
        LAPIS(() -> new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), OreEnumUtilSpace::base4Rand5Fortune),
        FIRE((state, rand) -> (rand != null && rand.nextInt(10) == 0)
                ? new ItemStack(ModItems.ingot_phosphorus)
                : new ItemStack(ModItems.powder_fire),
                (state, fortune, rand) -> 1),
        GLOWSTONE(() -> new ItemStack(Items.GLOWSTONE_DUST), (state, fortune, rand) -> 1 + rand.nextInt(3))
        ;

        public final BiFunction<IBlockState, Random, ItemStack> dropFunction;
        public final TriFunction<IBlockState, Integer, Random, Integer> quantityFunction;

        SpaceOreEnum(BiFunction<IBlockState, Random, ItemStack> dropFunction, TriFunction<IBlockState, Integer, Random, Integer> quantityFunction) {
            this.dropFunction = dropFunction;
            this.quantityFunction = quantityFunction;
        }

        SpaceOreEnum(Supplier<ItemStack> drop, TriFunction<IBlockState, Integer, Random, Integer> quantity) {
            this((state, rand) -> new ItemStack(drop.get().getItem(), 1, drop.get().getMetadata()), quantity);
        }

        @Override
        public BiFunction<IBlockState, Random, ItemStack> getDropFunction() { return this.dropFunction; }

        @Override
        public TriFunction<IBlockState, Integer, Random, Integer> getQuantityFunction() { return this.quantityFunction; }
    }
}
