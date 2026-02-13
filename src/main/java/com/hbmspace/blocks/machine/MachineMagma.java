package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.inventory.material.Mats;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.tileentity.machine.TileEntityMachineMagma;
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

public class MachineMagma extends BlockDummyableSpace implements ILookOverlay, ITooltipProvider {

    public MachineMagma(String s) {
        super(Material.IRON, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineMagma();
        if(meta >= 6) return new TileEntityProxyCombo().power().fluid();
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {3, 3, 3, 3, 3, 3};
    }

    @Override
    public int getOffset() {
        return 3;
    }

    @Override
    public int getHeightOffset() {
        return 3;
    }

    @Override
    protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        x += dir.offsetX * o;
        y += dir.offsetY * o;
        z += dir.offsetZ * o;

        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);
        this.makeExtra(world, x - dir.offsetX * 3, y - 1, z - dir.offsetZ * 3);
        this.makeExtra(world, x - dir.offsetX * 3, y - 2, z - dir.offsetZ * 3);
        this.makeExtra(world, x - dir.offsetX * 3 + rot.offsetX, y - 1, z - dir.offsetZ * 3 + rot.offsetZ);
        this.makeExtra(world, x - dir.offsetX * 3 - rot.offsetX, y - 1, z - dir.offsetZ * 3 - rot.offsetZ);
        this.makeExtra(world, x - dir.offsetX * 3 + rot.offsetX, y - 2, z - dir.offsetZ * 3 + rot.offsetZ);
        this.makeExtra(world, x - dir.offsetX * 3 - rot.offsetX, y - 2, z - dir.offsetZ * 3 - rot.offsetZ);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null)
            return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityMachineMagma drill))
            return;

        List<String> text = new ArrayList<>();

        CelestialBody body = CelestialBody.getBody(world);

        if(!body.name.equals("moho")) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! MUST BE ON MOHO ! ! !");
        } else if(!drill.validPosition) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! INSUFFICIENT LAVA FOUND ! ! !");
        } else {
            text.add((drill.power < drill.consumption ? TextFormatting.RED : TextFormatting.GREEN) + "Power: " + BobMathUtil.getShortNumber(drill.power) + "HE");

            for(int i = 0; i < drill.tanks.length; i++)
                text.add((i == 0 ? (TextFormatting.GREEN + "-> ") : (TextFormatting.RED + "<- ")) + TextFormatting.RESET + drill.tanks[i].getTankType().getLocalizedName() + ": " + drill.tanks[i].getFill() + "/" + drill.tanks[i].getMaxFill() + "mB");

            for(Mats.MaterialStack sta : drill.liquids) {
                text.add(TextFormatting.YELLOW + I18nUtil.resolveKey(sta.material.getTranslationKey()) + ": " + Mats.formatAmount(sta.amount, false));
            }
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

}
