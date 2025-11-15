package com.hbmspace.blocks.generic;

import com.hbmspace.blocks.BlockBakeBaseSpace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockBaseDrop extends BlockBakeBaseSpace {

    private final Block drops;

    public BlockBaseDrop(Material material, String s, Block drops) {
        super(material, s);
        this.drops = drops;
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return Item.getItemFromBlock(this.drops);
    }

}
