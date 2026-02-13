package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.items.ModItems;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityDysonLauncher;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineDysonLauncher extends BlockDummyableSpace implements ILookOverlay, ITooltipProvider {

    public MachineDysonLauncher(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityDysonLauncher();
        if(meta == 6) return new TileEntityProxyCombo(true, false, false);
        if(meta >= 7) return new TileEntityProxyCombo(false, true, false);
        return null;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            return true;
        } else if(!player.isSneaking()) {
            int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

            if(posC == null)
                return false;

            TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

            if(!(te instanceof TileEntityDysonLauncher launcher))
                return false;

            ItemStack heldStack = player.getHeldItem(hand);

            if (!heldStack.isEmpty() && heldStack.getItem() == ModItems.sat_chip) {
                if (!launcher.inventory.getStackInSlot(1).isEmpty())
                    return false;

                launcher.inventory.setStackInSlot(1, heldStack.copy());
                heldStack.setCount(0);
                world.playSound(null, pos, HBMSoundHandler.upgradePlug,
                        SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else if (heldStack.isEmpty() && !launcher.inventory.getStackInSlot(1).isEmpty()) {
                if (player.inventory.addItemStackToInventory(launcher.inventory.getStackInSlot(1).copy())) {
                    launcher.inventory.setStackInSlot(1, ItemStack.EMPTY);
                    launcher.markDirty();
                    world.playSound(null, pos, HBMSoundHandler.upgradePlug,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }

        return true;
    }

    @Override
    public boolean checkRequirement(World world, int x, int y, int z, ForgeDirection dir, int o) {
        int sx = x;
        int sz = z;

        int ox = dir.offsetX;
        int oz = dir.offsetZ;

        x += ox * o;
        z += oz * o;

        // LEG FUCKER
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {0, 14, 1, 0, 6, -5}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {0, 14, 1, 0, -5, 6}, sx, y, sz, dir)) return false;

        // BACKDISH CRAPPER
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 2, y + 7, z + oz * 2, new int[] {2, 0, 0, 3, 2, 2}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox, y + 6, z + oz, new int[] {2, 0, 0, 3, 3, 3}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x, y + 5, z, new int[] {2, 0, 0, 3, 3, 3}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox, y + 4, z - oz, new int[] {2, 0, 0, 3, 3, 3}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 2, y + 3, z - oz * 2, new int[] {2, 0, 0, 3, 2, 2}, sx, y, sz, dir)) return false;

        // DISH SHITTER
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {2, 0, 0, 0, 3, 3}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {0, 0, 0, 2, 3, 3}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 5, y + 13, z + oz * 5, new int[] {2, 0, 0, 2, 10, 5}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 4, y + 12, z + oz * 4, new int[] {2, 0, 0, 2, 10, 7}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 3, y + 11, z + oz * 3, new int[] {2, 0, 0, 2, 10, 8}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox * 2, y + 10, z + oz * 2, new int[] {2, 0, 0, 2, 10, 9}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x + ox, y + 9, z + oz, new int[] {2, 0, 0, 2, 10, 10}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x, y + 8, z, new int[] {2, 0, 0, 2, 10, 10}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox, y + 7, z - oz, new int[] {2, 0, 0, 2, 10, 10}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 2, y + 6, z - oz * 2, new int[] {2, 0, 0, 2, 10, 10}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 3, y + 5, z - oz * 3, new int[] {2, 0, 0, 2, 10, 10}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 4, y + 4, z - oz * 4, new int[] {2, 0, 0, 2, 9, 9}, sx, y, sz, dir)) return false;

        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 3, y + 3, z - oz * 3, new int[] {2, 0, 2, 0, 8, 8}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 4, y + 2, z - oz * 4, new int[] {2, 0, 2, 0, 7, 7}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 7, y + 2, z - oz * 7, new int[] {1, 0, 0, 0, 6, 6}, sx, y, sz, dir)) return false;

        if(!MultiblockHandlerXR.checkSpace(world, x, y + 2, z, new int[] {0, 0, 8, -8, 3, 3}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x, y + 1, z, new int[] {1, 1, 8, -8, 1, 1}, sx, y, sz, dir)) return false;

        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 7, y, z - oz * 7, new int[] {1, 0, 0, 0, 4, 4}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 6, y, z - oz * 6, new int[] {1, 0, 0, 0, 5, 5}, sx, y, sz, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(world, x - ox * 5, y, z - oz * 5, new int[] {1, 0, 0, 0, 6, 6}, sx, y, sz, dir)) return false;

        // BASE PIECE (OF ASS)
        return MultiblockHandlerXR.checkSpace(world, x, y, z, new int[]{3, 0, 7, 0, 2, 2}, sx, y, sz, dir);
    }

    @Override
    protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        int ox = dir.offsetX;
        int oz = dir.offsetZ;
        int rx = rot.offsetX;
        int rz = rot.offsetZ;

        x += ox * o;
        z += oz * o;

        // LEG FUCKER
        MultiblockHandlerXR.fillSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {0, 14, 1, 0, 6, -5}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {0, 14, 1, 0, -5, 6}, this, dir);

        // BACKDISH CRAPPER
        MultiblockHandlerXR.fillSpace(world, x + ox * 2, y + 7, z + oz * 2, new int[] {2, 0, 0, 3, 2, 2}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox, y + 6, z + oz, new int[] {2, 0, 0, 3, 3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y + 5, z, new int[] {2, 0, 0, 3, 3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox, y + 4, z - oz, new int[] {2, 0, 0, 3, 3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 2, y + 3, z - oz * 2, new int[] {2, 0, 0, 3, 2, 2}, this, dir);

        // DISH SHITTER
        MultiblockHandlerXR.fillSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {2, 0, 0, 0, 3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox * 6, y + 14, z + oz * 6, new int[] {0, 0, 0, 2, 3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox * 5, y + 13, z + oz * 5, new int[] {2, 0, 0, 2, 10, 5}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox * 4, y + 12, z + oz * 4, new int[] {2, 0, 0, 2, 10, 7}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox * 3, y + 11, z + oz * 3, new int[] {2, 0, 0, 2, 10, 8}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox * 2, y + 10, z + oz * 2, new int[] {2, 0, 0, 2, 10, 9}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + ox, y + 9, z + oz, new int[] {2, 0, 0, 2, 10, 10}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y + 8, z, new int[] {2, 0, 0, 2, 10, 10}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox, y + 7, z - oz, new int[] {2, 0, 0, 2, 10, 10}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 2, y + 6, z - oz * 2, new int[] {2, 0, 0, 2, 10, 10}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 3, y + 5, z - oz * 3, new int[] {2, 0, 0, 2, 10, 10}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 4, y + 4, z - oz * 4, new int[] {2, 0, 0, 2, 9, 9}, this, dir);

        MultiblockHandlerXR.fillSpace(world, x - ox * 3, y + 3, z - oz * 3, new int[] {2, 0, 2, 0, 8, 8}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 4, y + 2, z - oz * 4, new int[] {2, 0, 2, 0, 7, 7}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 7, y + 2, z - oz * 7, new int[] {1, 0, 0, 0, 6, 6}, this, dir);

        MultiblockHandlerXR.fillSpace(world, x, y + 2, z, new int[] {0, 0, 8, -8, 3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y + 1, z, new int[] {1, 1, 8, -8, 1, 1}, this, dir);

        MultiblockHandlerXR.fillSpace(world, x - ox * 7, y, z - oz * 7, new int[] {1, 0, 0, 0, 4, 4}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 6, y, z - oz * 6, new int[] {1, 0, 0, 0, 5, 5}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - ox * 5, y, z - oz * 5, new int[] {1, 0, 0, 0, 6, 6}, this, dir);

        // BASE PIECE (OF ASS)
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {3, 0, 7, 0, 2, 2}, this, dir);


        // and finally, extra metas
        makeExtra(world, x - rx * 2, y, z - rz * 2);
        makeExtra(world, x - ox - rx * 2, y, z - oz - rz * 2);
        makeExtra(world, x - ox * 2 - rx * 2, y, z - oz * 2 - rz * 2);
        makeExtra(world, x - ox * 3 - rx * 2, y, z - oz * 3 - rz * 2);
        makeExtra(world, x - ox * 4 - rx * 2, y, z - oz * 4 - rz * 2);

        makeExtra(world, x - ox * 8, y, z - oz * 8);
    }

    @Override
    public int[] getDimensions() {
        // just the major offsets, for placing on walls and ceilings (rare)
        return new int[] {16, 0, 8, 0, 0, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityDysonLauncher launcher)) return;

        List<String> text = new ArrayList<>();

        if(launcher.swarmId > 0) {
            text.add("ID: " + launcher.swarmId);
            text.add("Swarm: " + launcher.swarmCount + " members");
            text.add((launcher.power < TileEntityDysonLauncher.MAX_POWER ? TextFormatting.RED : TextFormatting.GREEN) + "Power: " + BobMathUtil.getShortNumber(launcher.power) + "HE");
        } else {
            text.add("No Satellite ID-Chip installed!");
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

}