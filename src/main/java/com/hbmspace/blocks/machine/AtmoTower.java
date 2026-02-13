package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityAtmoTower;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AtmoTower extends BlockDummyableSpace implements ILookOverlay {

    public AtmoTower(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World p_149915_1_, int meta) {

        if(meta >= 12)
            return new TileEntityAtmoTower();

        if(meta >= 8)
            return new TileEntityProxyCombo(false, true, true);

        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 6, 0, 1, 0, 0, 1 };
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        x = x + dir.offsetX * o;
        z = z + dir.offsetZ * o;

        ForgeDirection dr2 = dir.getRotation(ForgeDirection.UP);

        this.makeExtra(world, x - dir.offsetX - dr2.offsetX, y, z - dir.offsetZ - dr2.offsetZ);
        this.makeExtra(world, x, y, z - dir.offsetZ - dr2.offsetZ);
        this.makeExtra(world, x - dir.offsetX - dr2.offsetX, y, z);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null)
            return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityAtmoTower tower))
            return;

        List<String> text = new ArrayList<>();
        text.add((tower.power < tower.getMaxPower() / 20 ? TextFormatting.RED : TextFormatting.GREEN) + "Power: " + BobMathUtil.getShortNumber(tower.power) + "HE");

        for(int i = 0; i < tower.tanks.length; i++)
            text.add((i < 1 ? (TextFormatting.GREEN + "-> ") : (TextFormatting.RED + "<- ")) + TextFormatting.RESET + I18nUtil.resolveKey("hbmfluid." + tower.tanks[i].getTankType().getName().toLowerCase()) + ": " + tower.tanks[i].getFill() + "/" + tower.tanks[i].getMaxFill() + "mB");

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);

    }


}
