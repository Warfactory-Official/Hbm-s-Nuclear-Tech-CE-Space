package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbmspace.dim.CelestialBody;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.inventory.fluid.trait.FT_Rocket;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineHTR3;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineHTR3 extends BlockDummyableSpace implements ILookOverlay {

    public MachineHTR3(String s) {
        super(Material.IRON, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineHTR3();
        if(meta >= 6) return new TileEntityProxyCombo(false, false, true);
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {3, 3, 3, 3, 5, 5};
    }

    @Override
    public int getOffset() {
        return 5;
    }

    @Override
    public int getHeightOffset() {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(!world.isRemote && !player.isSneaking()) {

            if(!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof IItemFluidIdentifier) {
                int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

                if(posC == null)
                    return false;

                TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

                if(!(te instanceof TileEntityMachineHTR3 htr3))
                    return false;

                FluidType type = ((IItemFluidIdentifier) player.getHeldItem(hand).getItem()).getType(world, posC[0], posC[1], posC[2], player.getHeldItem(hand));

                FT_Heatable heatable = type.getTrait(FT_Heatable.class);

                if(heatable != null && heatable.getFirstStep().typeProduced.hasTrait(FT_Rocket.class)) {
                    htr3.tanks[0].setTankType(heatable.getFirstStep().typeProduced);
                    htr3.markDirty();
                }

                return true;
            }
            return false;

        } else {
            return true;
        }
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

        this.makeExtra(world, x - rot.offsetX * 5, y, z - rot.offsetZ * 5);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        if(!CelestialBody.inOrbit(world)) return;

        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityMachineHTR3 thruster))
            return;

        List<String> text = new ArrayList<>();

        if(!thruster.isFacingPrograde()) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.engineFacing") + " ! ! !");
        } else {
            for(int i = 0; i < thruster.tanks.length; i++) {
                FluidTankNTM tank = thruster.tanks[i];
                text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + tank.getTankType().getLocalizedName() + ": " + tank.getFill() + "/" + tank.getMaxFill() + "mB");
            }

            if(world.getTileEntity(pos) instanceof TileEntityProxyCombo) {
                text.add("Connect to PWR from here");
            }
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

}
