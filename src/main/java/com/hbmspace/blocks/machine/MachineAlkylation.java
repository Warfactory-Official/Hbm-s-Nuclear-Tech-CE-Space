package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.oil.TileEntityMachineAlkylation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineAlkylation extends BlockDummyableSpace implements ILookOverlay {

    public MachineAlkylation(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12)
            return new TileEntityMachineAlkylation();
        if(meta >= extra)
            return new TileEntityProxyCombo(false, true, true);

        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {3, 0, 2, 2, 1, 1};
    }

    @Override
    public int getOffset() {
        return 2;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        makeExtra(world, x + rot.offsetX, y, z + rot.offsetZ);
        makeExtra(world, x + rot.offsetX + dir.offsetX * 2, y, z + rot.offsetZ + dir.offsetZ * 2);
        makeExtra(world, x + rot.offsetX - dir.offsetX * 2, y, z + rot.offsetZ - dir.offsetZ * 2);
        makeExtra(world, x - rot.offsetX, y, z - rot.offsetZ);
        makeExtra(world, x - rot.offsetX + dir.offsetX * 2, y, z - rot.offsetZ + dir.offsetZ * 2);
        makeExtra(world, x - rot.offsetX - dir.offsetX * 2, y, z - rot.offsetZ - dir.offsetZ * 2);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null)
            return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityMachineAlkylation alkylation))
            return;

        List<String> text = new ArrayList<>();

        text.add((alkylation.power < alkylation.getMaxPower() / 20 ? TextFormatting.RED : TextFormatting.GREEN) + "Power: " + BobMathUtil.getShortNumber(alkylation.power) + "HE");

        for(int i = 0; i < alkylation.tanks.length; i++) {
            if(alkylation.tanks[i].getTankType() == Fluids.NONE) continue;
            text.add((i < 2 ? (TextFormatting.GREEN + "-> ") : (TextFormatting.RED + "<- ")) + TextFormatting.RESET + alkylation.tanks[i].getTankType().getLocalizedName() + ": " + alkylation.tanks[i].getFill() + "/" + alkylation.tanks[i].getMaxFill() + "mB");
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(!world.isRemote && !player.isSneaking()) {

            if(!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof IItemFluidIdentifier) {
                int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

                if(posC == null)
                    return false;

                TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

                if(!(te instanceof TileEntityMachineAlkylation alkylation))
                    return false;

                FluidType type = ((IItemFluidIdentifier) player.getHeldItem(hand).getItem()).getType(world, posC[0], posC[1], posC[2], player.getHeldItem(hand));
                alkylation.tanks[0].setTankType(type);
                alkylation.markDirty();
                player.sendMessage(
                        new TextComponentString("Changed type to ")
                                .setStyle(new Style().setColor(TextFormatting.YELLOW))
                                .appendSibling(new TextComponentTranslation(type.getConditionalName()))
                                .appendSibling(new TextComponentString("!"))
                );

                return true;
            }
            return false;

        } else {
            return true;
        }
    }
}
