package com.hbmspace.blocks.machine;

import com.hbm.api.block.IToolable;
import com.hbm.blocks.ILookOverlay;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.tileentity.machine.TileEntityAtmosphericCompressor;
import net.minecraft.block.material.Material;
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

public class BlockAtmosphericCompressor extends BlockDummyableSpace implements ILookOverlay, IToolable {

    public BlockAtmosphericCompressor(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World p_149915_1_, int meta) {
        if(meta >= 12) return new TileEntityAtmosphericCompressor();
        if(meta >= 8) return new TileEntityProxyCombo(false, true, true);
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 3, 0, 1, 0, 0, 1 };
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

        if(!(te instanceof TileEntityAtmosphericCompressor tower))
            return;

        List<String> text = new ArrayList<>();
        if(!CelestialBody.hasTrait(world, CBT_Atmosphere.class)) {
            text.add(((TextFormatting.RED + "ERROR: ")) + TextFormatting.RESET + I18nUtil.resolveKey("CANNOT COLLECT IN VACUUM"));
        } else {
            text.add((tower.power < tower.getMaxPower() / 20 ? TextFormatting.RED : TextFormatting.GREEN) + "Power: " + BobMathUtil.getShortNumber(tower.power) + "HE");
            text.add(((TextFormatting.RED + "<- ")) + TextFormatting.RESET + I18nUtil.resolveKey("hbmfluid." + tower.tank.getTankType().getName().toLowerCase()) + ": " + tower.tank.getFill() + "/" + tower.tank.getMaxFill() + "mB");
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public boolean onScrew(World world, EntityPlayer player, int x, int y, int z, EnumFacing side, float fX, float fY, float fZ, EnumHand hand, ToolType tool) {

        if(tool != ToolType.SCREWDRIVER)
            return false;

        if(world.isRemote) return true;

        int[] pos = this.findCore(world, x, y, z);

        if(pos == null) return false;

        TileEntity te = world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));

        if(!(te instanceof TileEntityAtmosphericCompressor tile)) return false;

        tile.cycleGas();
        tile.markDirty();

        return true;
    }
}
