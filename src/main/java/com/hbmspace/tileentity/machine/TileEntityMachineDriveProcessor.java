package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.Library;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.BufferUtil;
import com.hbm.util.EnumUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerDriveProcessor;
import com.hbmspace.inventory.gui.GUIMachineDriveProcessor;
import com.hbmspace.items.enums.ItemEnumsSpace;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityMachineDriveProcessor extends TileEntityMachineBase implements ITickable, ISpaceGuiProvider, IControlReceiver, IEnergyReceiverMK2 {

    public long power;
    public long maxPower = 2_000;

    public boolean isProcessing;
    public int progress;
    public int maxProgress = 100; // 5 seconds

    public String status = "";
    public boolean hasDrive = false;

    private int lastTier;

    public TileEntityMachineDriveProcessor() {
        super(4, false, true);
        this.inventory = new ItemStackHandler(4) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    public void update() {
        if(!world.isRemote) {

            power = Library.chargeTEFromItems(inventory, 3, power, maxPower);
            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                trySubscribe(world, pos.getX() + dir.offsetX, pos.getY() + dir.offsetY, pos.getZ() + dir.offsetZ, dir);

            if(power < maxPower * 0.75) {
                isProcessing = false;
                status = TextFormatting.RED + "No power ";
            } else if(inventory.getStackInSlot(0).isEmpty() || inventory.getStackInSlot(0).getItem() != ModItemsSpace.full_drive) {
                isProcessing = false;
                status = "";
            } else if(getProcessingTier() < ItemVOTVdrive.getProcessingTier(inventory.getStackInSlot(0), CelestialBody.getBody(world))) {
                isProcessing = false;
                status = TextFormatting.RED + "Low tier ";
            }

            if(lastTier != getProcessingTier()) {
                status = "";
            }

            if(isProcessing) {
                power -= 200;

                status = TextFormatting.GREEN + "" + TextFormatting.ITALIC + "Processing  ";
                progress++;

                if(progress >= maxProgress) {
                    progress = 0;
                    isProcessing = false;
                    ItemVOTVdrive.setProcessed(inventory.getStackInSlot(0), true);
                    status = TextFormatting.GREEN + "Done! ";
                }
            } else {
                progress = 0;
            }

            lastTier = getProcessingTier();
            hasDrive = !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ModItemsSpace.full_drive;

            networkPackNT(15);
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeLong(power);
        buf.writeBoolean(isProcessing);
        buf.writeInt(progress);
        buf.writeBoolean(hasDrive);

        BufferUtil.writeString(buf, status);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        power = buf.readLong();
        isProcessing = buf.readBoolean();
        progress = buf.readInt();
        hasDrive = buf.readBoolean();

        status = BufferUtil.readString(buf);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("power", power);
        nbt.setBoolean("isProcessing", isProcessing);
        nbt.setInteger("progress", progress);
        nbt.setString("status", status);
        nbt.setInteger("lastTier", lastTier);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        power = nbt.getLong("power");
        isProcessing = nbt.getBoolean("isProcessing");
        progress = nbt.getInteger("progress");
        status = nbt.getString("status");
        lastTier = nbt.getInteger("lastTier");
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return isUseableByPlayer(player);
    }

    private int getProcessingTier() {
        if(inventory.getStackInSlot(2).isEmpty() || inventory.getStackInSlot(2).getItem() != ModItemsSpace.circuit) return 0;

        ItemEnumsSpace.EnumCircuitType num = EnumUtil.grabEnumSafely(ItemEnumsSpace.EnumCircuitType.VALUES, inventory.getStackInSlot(2).getItemDamage());

        return switch (num) {
            case PROCESST1 -> 1;
            case PROCESST2 -> 2;
            case PROCESST3 -> 3;
            default -> 0;
        };
    }

    private void processDrive(boolean process) {
        if(!process) {
            isProcessing = false;
            return;
        }

        if(power < maxPower * 0.75) return;
        if(inventory.getStackInSlot(0).isEmpty() || inventory.getStackInSlot(0).getItem() != ModItemsSpace.full_drive) return;
        if(ItemVOTVdrive.getProcessed(inventory.getStackInSlot(0))) return;

        // Check that our installed upgrade is a high enough tier
        if(getProcessingTier() >= ItemVOTVdrive.getProcessingTier(inventory.getStackInSlot(0), CelestialBody.getBody(world))) {
            isProcessing = true;
        }
    }

    private void cloneDrive() {
        if(power < maxPower * 0.75) return;
        if(inventory.getStackInSlot(0).isEmpty() || inventory.getStackInSlot(0).getItem() != ModItemsSpace.full_drive) return;
        if(inventory.getStackInSlot(1).isEmpty() || inventory.getStackInSlot(1).getItem() != ModItemsSpace.hard_drive) {
            status = TextFormatting.RED + "No target ";
            return;
        }

        ItemVOTVdrive.markCopied(inventory.getStackInSlot(0));
        inventory.setStackInSlot(1, inventory.getStackInSlot(0).copy());

        status = TextFormatting.GREEN + "Drive cloned ";
    }

    @Override
    public void receiveControl(EntityPlayerMP player, NBTTagCompound data) {
        if(data.hasKey("process")) {
            processDrive(data.getBoolean("process"));
        }

        if(data.hasKey("clone")) {
            cloneDrive();
        }
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerDriveProcessor(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIMachineDriveProcessor(player.inventory, this);
    }

    @Override
    public String getDefaultName() {
        return "container.machineDriveProcessor";
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 1,
                    pos.getY(),
                    pos.getZ() - 1,
                    pos.getX() + 2,
                    pos.getY() + 1,
                    pos.getZ() + 2
            );
        }

        return bb;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

}
