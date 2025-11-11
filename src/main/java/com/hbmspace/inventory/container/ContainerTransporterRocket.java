package com.hbmspace.inventory.container;

import com.hbmspace.tileentity.machine.TileEntityTransporterRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerTransporterRocket extends Container {

    ItemStackHandler transporterInventory;

    public ContainerTransporterRocket(InventoryPlayer invPlayer, TileEntityTransporterRocket invTransporter) {
        transporterInventory = invTransporter.inventory;

        addSlots(invTransporter.inventory, 0, 8, 34, 2, 4); // Input slots
        addSlots(invTransporter.inventory, 8, 98, 34, 2, 4); // Output slots

        addSlots(invTransporter.inventory, 16, 8, 106, 1, 4); // Input Fluid ID slots
        addSlots(invTransporter.inventory, 20, 188, 90, 1, 2); // Fuel Fluid ID slots

        addSlots(invPlayer, 9, 8, 154, 3, 9); // Player inventory
        addSlots(invPlayer, 0, 8, 212, 1, 9); // Player hotbar
    }

    // I'm gonna make a farken helper function for this shit, why was it done the old way for 9 whole ass years?
    private void addSlots(ItemStackHandler inv, int from, int x, int y, int rows, int cols) {
        int slotSize = 18;
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                this.addSlotToContainer(new SlotItemHandler(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }

    private void addSlots(IInventory inv, int from, int x, int y, int rows, int cols) {
        int slotSize = 18;
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                this.addSlotToContainer(new Slot(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer p_82846_1_, int par2) {
        ItemStack var3 = ItemStack.EMPTY;
        Slot var4 = this.inventorySlots.get(par2);

        if(var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if(par2 <= transporterInventory.getSlots() - 1) {
                if(!this.mergeItemStack(var5, transporterInventory.getSlots(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!this.mergeItemStack(var5, 0, transporterInventory.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if(var5.getCount() == 0) {
                var4.putStack(ItemStack.EMPTY);
            } else {
                var4.onSlotChanged();
            }

            var4.onTake(p_82846_1_, var5);
        }

        return var3;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer p_75145_1_) {
        return true;
    }

}
