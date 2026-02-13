package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbmspace.dim.CelestialBody;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.tileentity.machine.TileEntityMachineHTRF4;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineHTRF4 extends BlockDummyableSpace implements ILookOverlay {

    public MachineHTRF4(String s) {
        super(Material.IRON, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineHTRF4();
        if(meta >= 6) return new TileEntityProxyCombo(false, false, true);
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {2, 2, 2, 2, 11, 9};
    }

    @Override
    public int getOffset() {
        return 11;
    }

    @Override
    public int getHeightOffset() {
        return 2;
    }

    @Override
    public ForgeDirection getDirModified(ForgeDirection dir) {
        return dir.getRotation(ForgeDirection.DOWN);
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        this.makeExtra(world, x - rot.offsetX * 9, y, z - rot.offsetZ * 9);
        this.makeExtra(world, x - rot.offsetX * 9 + dir.offsetX, y, z - rot.offsetZ * 9 + dir.offsetZ);
        this.makeExtra(world, x - rot.offsetX * 9 - dir.offsetX, y, z - rot.offsetZ * 9 - dir.offsetZ);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        if(!CelestialBody.inOrbit(world)) return;

        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityMachineHTRF4 thruster))
            return;

        List<String> text = new ArrayList<>();

        if(!thruster.isFacingPrograde()) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.engineFacing") + " ! ! !");
        } else {
            text.add((thruster.power == 0 ? TextFormatting.RED : TextFormatting.GREEN) + BobMathUtil.getShortNumber(thruster.power) + "HE");
            for(int i = 0; i < thruster.tanks.length; i++) {
                FluidTankNTM tank = thruster.tanks[i];
                text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + tank.getTankType().getLocalizedName() + ": " + tank.getFill() + "/" + tank.getMaxFill() + "mB");
            }

            if(world.getTileEntity(pos) instanceof TileEntityProxyCombo) {
                if(posC[0] == pos.getX() || posC[2] == pos.getZ()) {
                    text.add("Connect to Plasma Heater from here");
                } else {
                    text.add("Connect to power from here");
                }
            }
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

}
