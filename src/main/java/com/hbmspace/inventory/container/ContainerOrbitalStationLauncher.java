package com.hbmspace.inventory.container;

import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.container.ContainerBase;
import com.hbm.items.weapon.ItemMissile;
import com.hbmspace.handler.RocketStruct;
import com.hbmspace.inventory.slots.SlotRocket;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStationLauncher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerOrbitalStationLauncher extends ContainerBase {

    public ContainerOrbitalStationLauncher(InventoryPlayer invPlayer, TileEntityOrbitalStationLauncher machine) {
        super(invPlayer, machine);

        int slotId = 0;

        // Target drive
        addSlotToContainer(new SlotNonRetarded(machine.inventory, slotId++, 42, 55));

        // Fuel in
        addSlotToContainer(new SlotItemHandler(machine.inventory, slotId++, 42, 73));

        // Fuel out
        addSlotToContainer(new SlotItemHandler(machine.inventory, slotId++, 42, 91));

        // Capsule slot
        addSlotToContainer(new SlotRocket.SlotCapsule(machine.inventory, slotId++, 18, 13));

        // Stages
        for(int i = 0; i < RocketStruct.MAX_STAGES; i++) {
            addSlotToContainer(new SlotRocket.SlotRocketPart(machine.inventory, slotId++, 18, 44, i, ItemMissile.PartType.FUSELAGE));
            addSlotToContainer(new SlotRocket.SlotRocketPart(machine.inventory, slotId++, 18, 62, i, ItemMissile.PartType.FINS));
            addSlotToContainer(new SlotRocket.SlotRocketPart(machine.inventory, slotId++, 18, 80, i, ItemMissile.PartType.THRUSTER));
        }

        // Drives
        for(int i = 0; i < RocketStruct.MAX_STAGES; i++) {
            addSlotToContainer(new SlotRocket.SlotDrive(machine, slotId++, 161, 54, i));
            addSlotToContainer(new SlotRocket.SlotDrive(machine, slotId++, 170, 87, i));
        }

        addSlots(invPlayer, 9, 8, 142, 3, 9); // Player inventory
        addSlots(invPlayer, 0, 8, 200, 1, 9); // Player hotbar
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {

        // L/R: ClickType.PICKUP
        // M3: ClickType.CLONE (creative)
        // SHIFT: ClickType.QUICK_MOVE
        // DRAG: ClickType.QUICK_CRAFT

        if (slotId >= tile.getSlots() && clickTypeIn == ClickType.QUICK_MOVE) {
            return ItemStack.EMPTY;
        }

        if (slotId < tile.getSlots() - RocketStruct.MAX_STAGES * 2 || slotId >= tile.getSlots()) {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }

        Slot slot = this.getSlot(slotId);

        ItemStack ret = ItemStack.EMPTY;
        ItemStack held = player.inventory.getItemStack();

        if (slot.getHasStack()) {
            ret = slot.getStack().copy();
        }

        slot.putStack(!held.isEmpty() ? held.copy() : ItemStack.EMPTY);

        if (slot.getHasStack()) {
            slot.getStack().setCount(1);
        }

        slot.onSlotChanged();

        return ret;
    }

    public void addSlots(InventoryPlayer inv, int from, int x, int y, int rows, int cols) {
        this.addSlots(inv, from, x, y, rows, cols, 18);
    }

    public void addSlots(InventoryPlayer inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                this.addSlotToContainer(new Slot(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }

}
