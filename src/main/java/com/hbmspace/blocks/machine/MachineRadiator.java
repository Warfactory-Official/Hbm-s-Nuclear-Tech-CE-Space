package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityRadiator;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineRadiator extends BlockDummyableSpace implements ILookOverlay {

    public MachineRadiator(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityRadiator();
        if(meta >= 6) return new TileEntityProxyCombo(false, false, true);
        return null;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        x = x + dir.offsetX * o;
        z = z + dir.offsetZ * o;

        makeExtra(world, x + rot.offsetX, y + 1, z + rot.offsetZ);
        makeExtra(world, x + rot.offsetX, y - 1, z + rot.offsetZ);
        makeExtra(world, x - rot.offsetX, y + 1, z - rot.offsetZ);
        makeExtra(world, x - rot.offsetX, y - 1, z - rot.offsetZ);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {1, 1, 0, 0, 1, 1};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public int getHeightOffset() {
        return 1;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {
        int[] pos = this.findCore(world, x, y, z);

        if(pos == null)
            return;

        TileEntity te = world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));

        if(!(te instanceof TileEntityRadiator tower))
            return;

        List<String> text = new ArrayList<>();
        // TODO
        /*if(!tower.vacuumOptimised) {
            CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
            if(CelestialBody.inOrbit(world) || atmosphere == null || atmosphere.getPressure() < 0.01) {
                text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.noVacuum") + " ! ! !");
            }
        }*/

        for(int i = 0; i < tower.tanks.length; i++)
            text.add((i < 1 ? (TextFormatting.GREEN + "-> ") : (TextFormatting.RED + "<- ")) + TextFormatting.RESET + tower.tanks[i].getTankType().getLocalizedName() + ": " + tower.tanks[i].getFill() + "/" + tower.tanks[i].getMaxFill() + "mB");

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

}
