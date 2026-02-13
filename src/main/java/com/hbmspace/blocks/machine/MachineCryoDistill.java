package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineCryoDistill;
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

public class MachineCryoDistill extends BlockDummyableSpace implements ILookOverlay {

    public MachineCryoDistill(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineCryoDistill();
        if(meta >= 6) return new TileEntityProxyCombo().fluid().power();
        return null;
    }

    @Override
    public int[] getDimensions() {
        // Not used in filling, but checks that this whole space is safe
        return new int[] {3, 2, 3, 3, 2, 2};
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.standardOpenBehavior(world, pos, player, 0);
    }

    @Override
    public int getOffset() {
        return 3;
    }

    @Override
    public int getHeightOffset() {
        return 2;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        // Midpoint plane
        MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {0, 0, 3, 3, 2, 2}, this, dir);

        // Each side of the walkway
        MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {0, 2, 3, 0, 2, 2}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {0, 2, -2, 3, 2, 2}, this, dir);

        // Top tanks
        MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {2, 0, 3, -1, 2, 2}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {3, 0, -2, 3, 2, 2}, this, dir);


        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        safeRem = true;

        // Front face outputs
        makeExtra(world, x - rot.offsetX * 2, y - 2, z - rot.offsetZ * 2);
        makeExtra(world, x - rot.offsetX, y - 2, z - rot.offsetZ);
        makeExtra(world, x + rot.offsetX, y - 2, z + rot.offsetZ);
        makeExtra(world, x + rot.offsetX * 2, y - 2, z + rot.offsetZ * 2);

        // Side inputs
        makeExtra(world, x - dir.offsetX * 4 - rot.offsetX * 2, y - 2, z - dir.offsetZ * 4 - rot.offsetZ * 2);
        makeExtra(world, x - dir.offsetX * 5 - rot.offsetX * 2, y - 2, z - dir.offsetZ * 5 - rot.offsetZ * 2);

        safeRem = false;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {

        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        int cx = posC[0];
        int cy = posC[1];
        int cz = posC[2];

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityMachineCryoDistill distill)) return;

        ForgeDirection dir = ForgeDirection.getOrientation(distill.getBlockMetadata() - offset);
        List<String> text = new ArrayList<>();

        if(hitCheck(dir, cx, cy, cz, -1, -2, -2, pos)) {
            text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + I18nUtil.resolveKey("hbmfluid." + distill.tanks[0].getTankType().getName().toLowerCase()));
        }
        if(hitCheck(dir, cx, cy, cz, -2, -2, -2, pos)) {
            text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + "Power");
        }

        if(hitCheck(dir, cx, cy, cz, 3, -2, -2, pos)
                || hitCheck(dir, cx, cy, cz, 3, -1, -2, pos)
                || hitCheck(dir, cx, cy, cz, 3, 1, -2, pos)
                || hitCheck(dir, cx, cy, cz, 3, 2, -2, pos)) {
            text.add(TextFormatting.RED + "<- " + TextFormatting.RESET + I18nUtil.resolveKey("hbmfluid." + distill.tanks[1].getTankType().getName().toLowerCase()));
            text.add(TextFormatting.RED + "<- " + TextFormatting.RESET + I18nUtil.resolveKey("hbmfluid." + distill.tanks[2].getTankType().getName().toLowerCase()));
            text.add(TextFormatting.RED + "<- " + TextFormatting.RESET + I18nUtil.resolveKey("hbmfluid." + distill.tanks[3].getTankType().getName().toLowerCase()));
            text.add(TextFormatting.RED + "<- " + TextFormatting.RESET + I18nUtil.resolveKey("hbmfluid." + distill.tanks[4].getTankType().getName().toLowerCase()));
        }

        if(!text.isEmpty()) {
            ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
        }
    }


    protected boolean hitCheck(ForgeDirection dir, int coreX, int coreY, int coreZ, int exDir, int exRot, int exY, BlockPos hitPos) {
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        int iX = coreX + dir.offsetX * exDir + rot.offsetX * exRot;
        int iY = coreY + exY;
        int iZ = coreZ + dir.offsetZ * exDir + rot.offsetZ * exRot;

        return iX == hitPos.getX() && iZ == hitPos.getZ() && iY == hitPos.getY();
    }

}
