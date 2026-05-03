package com.hbmspace.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
import com.hbm.render.block.BlockBakeFrame;
import com.hbmspace.blocks.BlockEnumMetaSpace;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Random;

public class BlockOreBasalt extends BlockEnumMetaSpace<BlockOreBasalt.EnumBasaltOreType> {

    public BlockOreBasalt(String s) {
        super(Material.ROCK, SoundType.STONE, s, EnumBasaltOreType.VALUES, true, true);
    }

    public enum EnumBasaltOreType {
        SULFUR(ModItems.sulfur),
        FLUORITE(ModItems.fluorite),
        ASBESTOS(ModItems.ingot_asbestos),
        GEM(ModItems.gem_volcanic),
        MOLYSITE(ModItems.powder_molysite);

        public final Item drop;

        EnumBasaltOreType(Item drop) {
            this.drop = drop;
        }

        public static final EnumBasaltOreType[] VALUES = values();
    }

    @Override
    protected BlockBakeFrame[] generateBlockFrames(String registryName) {
        BlockBakeFrame[] frames = new BlockBakeFrame[blockEnum.length];

        for(int i = 0; i < blockEnum.length; i++) {
            String name = blockEnum[i].name().toLowerCase(Locale.US);
            // Replicates 1.7.10 logic: side is the standard multi-texture, top/bottom is multi-texture + "_top"
            frames[i] = BlockBakeFrame.sideTopBottom(
                    registryName + "_" + name,
                    registryName + "_" + name + "_top",
                    registryName + "_" + name + "_top"
            );
        }
        return frames;
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        EnumBasaltOreType type = getEnumFromState(state);
        return type.drop;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
        Item item = this.getItemDropped(state, rand, fortune);
        if(item != Items.AIR) {
            drops.add(new ItemStack(item, 1, this.damageDropped(state)));
        }
    }

    @Override
    public void onEntityWalk(World world, @NotNull BlockPos pos, @NotNull Entity entity) {
        IBlockState state = world.getBlockState(pos);
        if(getEnumFromState(state) == EnumBasaltOreType.ASBESTOS && world.isAirBlock(pos.up())) {
            if(world.rand.nextInt(10) == 0) {
                world.setBlockState(pos.up(), ModBlocks.gas_asbestos.getDefaultState());
            }
            for(int i = 0; i < 5; i++) {
                world.spawnParticle(EnumParticleTypes.TOWN_AURA, pos.getX() + world.rand.nextFloat(), pos.getY() + 1.1D, pos.getZ() + world.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World world, @NotNull BlockPos pos, @NotNull IBlockState state, float chance, int fortune) {
        if(!world.isRemote && getEnumFromState(state) == EnumBasaltOreType.ASBESTOS) {
            world.setBlockState(pos, ModBlocks.gas_asbestos.getDefaultState());
        }
        super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
    }
}
