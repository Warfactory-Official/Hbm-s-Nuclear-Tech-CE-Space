package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardTransceiver;
import com.hbm.api.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toserver.NBTControlPacket;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.BufferUtil;
import com.hbm.util.InventoryUtil;
import com.hbmspace.items.tool.ItemTransporterLinker;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.stream.IntStream;

public abstract class TileEntityTransporterBase extends TileEntityMachineBase implements ITickable, IGUIProvider, IControlReceiver, IFluidStandardTransceiver {

    private String name = "Transporter";

    public FluidTankNTM[] tanks;

    public TileEntityTransporterBase(int slotCount, int tankCount, int tankSize) {
        this(slotCount, tankCount, tankSize, 0, 0, 0);
    }

    public TileEntityTransporterBase(int slotCount, int tankCount, int tankSize, int extraSlots, int extraTanks, int extraTankSize) {
        super(slotCount + tankCount / 2 + extraSlots + extraTanks, true, false);

        tanks = new FluidTankNTM[tankCount + extraTanks];
        for(int i = 0; i < tankCount; i++) {
            tanks[i] = new FluidTankNTM(Fluids.NONE, tankSize);
        }
        for(int i = tankCount; i < tankCount + extraTanks; i++) {
            tanks[i] = new FluidTankNTM(Fluids.NONE, extraTankSize);
        }

        inputSlotMax = slotCount / 2;
        outputSlotMax = slotCount;

        inputTankMax = tankCount / 2;
        outputTankMax = tankCount;
    }

    // The transporter we're sending our contents to
    protected TileEntityTransporterBase linkedTransporter;
    private ItemTransporterLinker.TransporterInfo linkedTransporterInfo;

    private int inputSlotMax;
    private int outputSlotMax;

    private int inputTankMax;
    private int outputTankMax;

    @Override
    public String getName() {
        return "container.transporter";
    }

    @Override
    public void update() {
        if(world.isRemote) return;

        // Set tank types and split fills
        for(int i = 0; i < inputTankMax; i++) {
            tanks[i].setType(outputSlotMax + i, inventory);

            // Evenly distribute fluids between all matching tanks
            for(int o = i + 1; o < inputTankMax; o++) {
                splitFill(tanks[i], tanks[o]);
            }
        }
        for(int i = inputTankMax; i < outputTankMax; i++) {
            for(int o = i + 1; o < outputTankMax; o++) {
                splitFill(tanks[i], tanks[o]);
            }
        }
        for(int i = outputTankMax; i < tanks.length; i++) {
            tanks[i].setType(outputSlotMax + inputTankMax + i - outputTankMax, inventory);
        }

        updateConnections();

        fetchLinkedTransporter();

        if(linkedTransporter != null && canSend(linkedTransporter)) {
            boolean isDirty = false;

            int sentItems = 0;
            int sentFluid = 0;

            // Move all items into the target
            for(int i = 0; i < inputSlotMax; i++) {
                if(!inventory.getStackInSlot(i).isEmpty()) {
                    int beforeSize = inventory.getStackInSlot(i).getCount();
                    inventory.setStackInSlot(i, InventoryUtil.tryAddItemToInventory(linkedTransporter.inventory, linkedTransporter.inputSlotMax, linkedTransporter.outputSlotMax - 1, inventory.getStackInSlot(i)));
                    int afterSize = !inventory.getStackInSlot(i).isEmpty() ? inventory.getStackInSlot(i).getCount() : 0;
                    sentItems += beforeSize - afterSize;
                    isDirty = true;
                }
            }

            // Move all fluids into the target
            for(int i = 0; i < inputTankMax; i++) {
                int o = i+inputTankMax;

                linkedTransporter.tanks[o].setTankType(tanks[i].getTankType());

                int sourceFillLevel = tanks[i].getFill();
                int targetFillLevel = linkedTransporter.tanks[o].getFill();

                int spaceAvailable = linkedTransporter.tanks[o].getMaxFill() - targetFillLevel;
                int amountToSend = Math.min(sourceFillLevel, spaceAvailable);

                if(amountToSend > 0) {
                    linkedTransporter.tanks[o].setFill(targetFillLevel + amountToSend);
                    tanks[i].setFill(sourceFillLevel - amountToSend);
                    sentFluid += amountToSend;
                    isDirty = true;
                }
            }



            hasSent(linkedTransporter, sentItems + (sentFluid / 1000));

            if(isDirty) {
                markChanged();
                linkedTransporter.markChanged();
            }
        }

        this.networkPackNT(250);
    }

    private void updateConnections() {
        // Sending/Receiving tanks
        for(DirPos pos : getConPos()) {
            for(int i = 0; i < outputTankMax; i++) {
                if(tanks[i].getTankType() != Fluids.NONE) {
                    trySubscribe(tanks[i].getTankType(), world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
                    this.sendFluid(tanks[i], world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
                }
            }
        }

        // Fuel tanks
        for(DirPos pos : getTankPos()) {
            for(int i = outputTankMax; i < tanks.length; i++) {
                if(tanks[i].getTankType() != Fluids.NONE) {
                    trySubscribeFuel(tanks[i].getTankType(), world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
                }
            }
        }

        // Inserter
        for(DirPos pos : getInsertPos()) {
            tryLoad(pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
        }

        // Extractor
        for(DirPos pos : getExtractPos()) {
            tryUnload(pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
        }
    }

    private void tryLoad(int x, int y, int z, ForgeDirection dir) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te == null || !te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.toEnumFacing()))
            return;

        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.toEnumFacing());
        if (handler == null) return;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.extractItem(i, 1, true);
            if (stack.isEmpty()) continue;

            // Try to stack into existing slots
            for (int j = 0; j < inputSlotMax; j++) {
                ItemStack slotStack = inventory.getStackInSlot(j);
                if (slotStack != null && ItemStack.areItemsEqual(slotStack, stack) && ItemStack.areItemStackTagsEqual(slotStack, stack)
                        && slotStack.getCount() < slotStack.getMaxStackSize() && slotStack.getCount() < inventory.getSlotLimit(j)) {
                    handler.extractItem(i, 1, false);
                    slotStack.grow(1);
                    return;
                }
            }

            // Try to place in empty slot
            for (int j = 0; j < inputSlotMax; j++) {
                if (inventory.getStackInSlot(j).isEmpty()) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    inventory.setStackInSlot(j, copy);
                    handler.extractItem(i, 1, false);
                    return;
                }
            }
        }
    }

    private void tryUnload(int x, int y, int z, ForgeDirection dir) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te == null || !te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.toEnumFacing()))
            return;

        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.toEnumFacing());
        if (handler == null) return;

        for (int i = inputSlotMax; i < outputSlotMax; i++) {
            ItemStack out = inventory.getStackInSlot(i);
            if (out.isEmpty()) continue;

            // Try to stack into existing slots in target
            for (int j = 0; j < handler.getSlots(); j++) {
                ItemStack target = handler.getStackInSlot(j);
                if (ItemStack.areItemsEqual(out, target) && ItemStack.areItemStackTagsEqual(out, target)
                        && target.getCount() < target.getMaxStackSize() && target.getCount() < handler.getSlotLimit(j)) {
                    ItemStack stack = inventory.getStackInSlot(i).copy();
                    stack.shrink(1);
                    inventory.setStackInSlot(i, stack);
                    handler.insertItem(j, new ItemStack(out.getItem(), 1, out.getMetadata()), false);
                    return;
                }
            }

            // Try to place in empty slot
            for (int j = 0; j < handler.getSlots(); j++) {
                if (handler.getStackInSlot(j).isEmpty()) {
                    ItemStack copy = out.copy();
                    copy.setCount(1);
                    int inserted = handler.insertItem(j, copy, false).getCount();
                    if (inserted == 0) {
                        ItemStack stack = inventory.getStackInSlot(i).copy();
                        stack.shrink(1);
                        inventory.setStackInSlot(i, stack);
                        return;
                    }
                }
            }
        }
    }


    public void trySubscribeFuel(FluidType type, World world, int x, int y, int z, ForgeDirection dir) {
        fuelReceiver.trySubscribe(type, world, x, y, z, dir);
    }

    FuelReceiver fuelReceiver = new FuelReceiver();

    private class FuelReceiver implements IFluidStandardReceiverMK2 {

        boolean valid = true;

        @Override
        public boolean isLoaded() {
            return valid && TileEntityTransporterBase.this.isLoaded();
        }

        @Override
        public FluidTankNTM[] getAllTanks() {
            return TileEntityTransporterBase.this.getAllTanks();
        }

        @Override
        public FluidTankNTM[] getReceivingTanks() {
            return (FluidTankNTM[]) Arrays.copyOfRange(tanks, outputTankMax, tanks.length);
        }

        @Override
        public IEnergyReceiverMK2.ConnectionPriority getFluidPriority() {
            return IEnergyReceiverMK2.ConnectionPriority.HIGH;
        }

    }

    @Override
    public void invalidate() {
        super.invalidate();
        fuelReceiver.valid = false;
    }

    // splitting is commutative, order don't matter
    private void splitFill(FluidTankNTM in, FluidTankNTM out) {
        if(in.getTankType() == out.getTankType()) {
            int fill = in.getFill() + out.getFill();

            float iFill = in.getMaxFill();
            float oFill = out.getMaxFill();
            float total = iFill + oFill;
            float iFrac = iFill / total;
            float oFrac = oFill / total;

            in.setFill(MathHelper.ceil(iFrac * (float) fill));
            out.setFill(MathHelper.floor(oFrac * (float)fill));

            // cap filling (this will generate 1mB of fluid in rare cases)
            if(out.getFill() == out.getMaxFill() - 1) out.setFill(out.getMaxFill());
        }
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return (FluidTankNTM[]) Arrays.copyOfRange(tanks, inputTankMax, outputTankMax);
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return (FluidTankNTM[]) Arrays.copyOfRange(tanks, 0, inputTankMax);
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return tanks;
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        if(linkedTransporterInfo != null) {
            buf.writeBoolean(true);
            buf.writeInt(linkedTransporterInfo.dimensionId);
            buf.writeInt(linkedTransporterInfo.x);
            buf.writeInt(linkedTransporterInfo.y);
            buf.writeInt(linkedTransporterInfo.z);
        } else {
            buf.writeBoolean(false);
        }

        for(int i = 0; i < tanks.length; i++) tanks[i].serialize(buf);

        BufferUtil.writeString(buf, name);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        linkedTransporter = null;
        if(buf.readBoolean()) {
            int id = buf.readInt();
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            linkedTransporterInfo = new ItemTransporterLinker.TransporterInfo("Linked Transporter", id, x, y, z);
        } else {
            linkedTransporterInfo = null;
        }

        for(int i = 0; i < tanks.length; i++) tanks[i].deserialize(buf);

        name = BufferUtil.readString(buf);
    }

    protected abstract DirPos[] getConPos();
    protected abstract DirPos[] getTankPos();
    protected abstract DirPos[] getInsertPos();
    protected abstract DirPos[] getExtractPos();

    // Designated overrides for delaying sending or requiring fuel
    protected abstract boolean canSend(TileEntityTransporterBase linkedTransporter);
    protected abstract void hasSent(TileEntityTransporterBase linkedTransporter, int quantitySent);
    protected abstract void hasConnected(TileEntityTransporterBase linkedTransporter);

    // Turns items and fluids into a "mass" of sorts
    protected int itemCount() {
        int count = 0;
        for(int i = 0; i < inputSlotMax; i++) {
            if(!inventory.getStackInSlot(i).isEmpty()) count += inventory.getStackInSlot(i).getCount();
        }
        for(int i = 0; i < inputTankMax; i++) {
            count += tanks[i].getFill() / 1000;
        }
        return count;
    }

    public String getTransporterName() {
        return name;
    }

    public void setTransporterName(String name) {
        this.name = name;
        NBTTagCompound data = new NBTTagCompound();
        data.setString("name", name);
        PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, pos));
    }

    private void fetchLinkedTransporter() {
        if(linkedTransporter == null && linkedTransporterInfo != null) {
            World transporterWorld = DimensionManager.getWorld(linkedTransporterInfo.dimensionId);
            TileEntity te = transporterWorld.getTileEntity(new BlockPos(linkedTransporterInfo.x, linkedTransporterInfo.y, linkedTransporterInfo.z));
            if(te instanceof TileEntityTransporterBase) {
                linkedTransporter = (TileEntityTransporterBase) te;
            }
        }
    }

    public ItemTransporterLinker.TransporterInfo getLinkedTransporter() {
        return linkedTransporterInfo;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(EnumFacing side) {
        return IntStream.range(0, outputSlotMax).toArray();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return i < inputSlotMax;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int side) {
        return i >= inputSlotMax && i < outputSlotMax;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        name = nbt.getString("name");
        linkedTransporter = null;
        int dimensionId = nbt.getInteger("dimensionId");
        int[] coords = nbt.getIntArray("linkedTo");
        if(coords.length > 0) {
            linkedTransporterInfo = new ItemTransporterLinker.TransporterInfo("Linked Transporter", dimensionId, coords[0], coords[1], coords[2]);
        } else {
            linkedTransporterInfo = null;
        }
        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(nbt, "t" + i);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setString("name", name);
        if(linkedTransporterInfo != null) {
            nbt.setInteger("dimensionId", linkedTransporterInfo.dimensionId);
            nbt.setIntArray("linkedTo", new int[] { linkedTransporterInfo.x, linkedTransporterInfo.y, linkedTransporterInfo.z });
        }
        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(nbt, "t" + i);
        return super.writeToNBT(nbt);
    }

    public void unlinkTransporter() {
        if(linkedTransporter != null) {
            linkedTransporter.linkedTransporter = null;
            linkedTransporter.linkedTransporterInfo = null;
        }

        linkedTransporter = null;
        linkedTransporterInfo = null;
    }

    // Is commutative, will automatically link and unlink its pair
    @Override
    public void receiveControl(NBTTagCompound nbt) {
        if(nbt.hasKey("name")) name = nbt.getString("name");
        if(nbt.hasKey("unlink")) {
            unlinkTransporter();
        }
        if(nbt.hasKey("linkedTo")) {
            // If already linked, unlink the target
            if(linkedTransporter != null) {
                linkedTransporter.linkedTransporter = null;
                linkedTransporter.linkedTransporterInfo = null;
            }

            linkedTransporter = null;

            int[] coords = nbt.getIntArray("linkedTo");
            int dimensionId = nbt.getInteger("dimensionId");
            linkedTransporterInfo = new ItemTransporterLinker.TransporterInfo("Linked Transporter", dimensionId, coords[0], coords[1], coords[2]);

            fetchLinkedTransporter();

            if(linkedTransporter != null) {
                linkedTransporter.linkedTransporterInfo = ItemTransporterLinker.TransporterInfo.from(world.provider.getDimension(), this);
                linkedTransporter.fetchLinkedTransporter();

                hasConnected(linkedTransporter);
            }
        }

        this.markDirty();
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return this.isUseableByPlayer(player);
    }

}
