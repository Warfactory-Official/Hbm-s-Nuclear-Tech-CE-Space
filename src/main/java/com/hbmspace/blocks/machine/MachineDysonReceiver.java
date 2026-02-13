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
import com.hbmspace.tileentity.machine.TileEntityDysonReceiver;
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
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineDysonReceiver extends BlockDummyableSpace implements ILookOverlay, ITooltipProvider {

    public MachineDysonReceiver(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityDysonReceiver();
        if(meta >= 6) return new TileEntityProxyCombo(false, false, false);
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

            if(!(te instanceof TileEntityDysonReceiver receiver))
                return false;

            ItemStack heldStack = player.getHeldItem(hand);

            if (!heldStack.isEmpty() && heldStack.getItem() == ModItems.sat_chip) {
                if (!receiver.inventory.getStackInSlot(0).isEmpty())
                    return false;

                receiver.inventory.setStackInSlot(0, heldStack.copy());
                heldStack.setCount(0);
                world.playSound(null, pos, HBMSoundHandler.upgradePlug,
                        SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else if (heldStack.isEmpty() && !receiver.inventory.getStackInSlot(0).isEmpty()) {
                if (player.inventory.addItemStackToInventory(receiver.inventory.getStackInSlot(0).copy())) {
                    receiver.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    receiver.markDirty();
                    world.playSound(null, pos, HBMSoundHandler.upgradePlug,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }

        return true;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {2, 0, 4, 2, 2, 2};
    }

    @Override
    public int getOffset() {
        return 2;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        // Main structure
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {1, 0, 8, -4, 1, 1}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {4, 0, 4, 0, 1, 1}, this, dir);

        // Dish
        MultiblockHandlerXR.fillSpace(world, x, y + 10, z, new int[] {1, 0, 6, 6, 6, 6}, this, dir);

        // Tower
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {17, 0, 1, 1, 1, 1}, this, dir);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityDysonReceiver receiver)) return;

        long energyOutput = 0;
        if(receiver.isReceiving) {
            energyOutput = TileEntityDysonReceiver.getEnergyOutput(receiver.swarmCount) / receiver.swarmConsumers * 20;
        }

        List<String> text = new ArrayList<>();

        if(receiver.swarmId > 0) {
            text.add("ID: " + receiver.swarmId);
            text.add("Swarm: " + receiver.swarmCount + " members");
            text.add("Consumers: " + receiver.swarmConsumers + " consumers");
            text.add("Power: " + BobMathUtil.getShortNumber(energyOutput) + "HE/s");
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