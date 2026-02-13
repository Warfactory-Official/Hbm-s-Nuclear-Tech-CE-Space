package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineVacuumCircuit;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineVacuumCircuit extends BlockDummyableSpace implements ILookOverlay {

    public MachineVacuumCircuit(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineVacuumCircuit();
        return new TileEntityProxyCombo().inventory().power().fluid();
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.standardOpenBehavior(world, pos.getX(), pos.getY(), pos.getZ(), player, 0);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {0, 0, 1, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        TileEntity tile = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(tile instanceof TileEntityMachineVacuumCircuit machine)) return;

        List<String> text = new ArrayList<>();

        if(!machine.canOperate) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.noAir") + " ! ! !");
        }

        if(text.isEmpty()) return;

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

}
