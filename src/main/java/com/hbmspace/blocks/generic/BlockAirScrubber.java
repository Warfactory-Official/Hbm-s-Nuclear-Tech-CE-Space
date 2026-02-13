package com.hbmspace.blocks.generic;

import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityAirScrubber;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockAirScrubber extends BlockDummyableSpace implements ILookOverlay, ITooltipProvider {

    public BlockAirScrubber(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityAirScrubber();
        if(meta >= 6) return new TileEntityProxyCombo(false, true, true);
        return null;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());
        if(posC == null) return;

        TileEntity tile = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));
        if(!(tile instanceof TileEntityAirScrubber scrubber)) return;

        List<String> text = new ArrayList<>();

        text.add((scrubber.getPower() <= 200 ? TextFormatting.RED : TextFormatting.GREEN) + "Power: " + BobMathUtil.getShortNumber(scrubber.getPower()) + "HE");
        text.add(TextFormatting.RED + "<- " + TextFormatting.RESET + scrubber.tank.getTankType().getLocalizedName() + ": " + scrubber.tank.getFill() + "/" + scrubber.tank.getMaxFill() + "mB");

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {1, 0, 0, 0, 0, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

}
