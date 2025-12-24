package com.hbmspace.blocks.machine;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.ChatBuilder;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityDishControl;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MachineDishControl extends BlockDummyableSpace implements ITooltipProvider {

    public MachineDishControl(Material p_i45386_1_, String s) {
        super(p_i45386_1_, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World p_149915_1_, int meta) {
        if(meta >= 12) return new TileEntityDishControl();
        return new TileEntityProxyCombo(false, false, false); // no need for extra atm, it's just two blocks
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 0, 0, 0, 0, 0, 1 };
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            return true;
        } else if(!player.isSneaking()) {
            // Find the controller TileEntity
            int[] posC = findCore(world, pos.getX(), pos.getY(), pos.getZ());

            TileEntity e = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));
            if(e instanceof TileEntityDishControl entityDishControl) {

                // Check if a dish was not assigned
                if(!entityDishControl.isLinked) {
                    // No StarDar linked
                    player.sendMessage(ChatBuilder.start("[").color(TextFormatting.DARK_AQUA)
                            .nextTranslation(this.getTranslationKey() + ".name").color(TextFormatting.DARK_AQUA)
                            .next("] ").color(TextFormatting.DARK_AQUA)
                            .next("Dish not linked!").color(TextFormatting.RED).flush());

                    return false;
                }
                BlockPos starPos = new BlockPos(entityDishControl.linkPosition[0], entityDishControl.linkPosition[1], entityDishControl.linkPosition[2]);

                // also check that you're still pointing at a stardar...
                Block block = world.getBlockState(starPos).getBlock();

                if(!(block instanceof MachineStardar)) {
                    // Invalid link
                    player.sendMessage(ChatBuilder.start("[").color(TextFormatting.DARK_AQUA)
                            .nextTranslation(this.getTranslationKey() + ".name").color(TextFormatting.DARK_AQUA)
                            .next("] ").color(TextFormatting.DARK_AQUA)
                            .next("Missing control target!").color(TextFormatting.RED).flush());

                    return false;
                }
                // Get StarDar
                IBlockState stardarState = world.getBlockState(starPos);
                MachineStardar stardarBlock = (MachineStardar) stardarState.getBlock();

                // Trigger the StarDar UI to open
                return stardarBlock.onBlockActivated(
                        world,
                        starPos, stardarState,
                        player, hand, facing, hitX, hitY, hitZ);
            }

            return false;

        } else {
            return false;
        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

}
