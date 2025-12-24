package com.hbmspace.inventory.container;

import com.hbm.inventory.container.ContainerBase;
import com.hbmspace.tileentity.machine.TileEntityMachineWarController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMachineWarController extends ContainerBase {

    private TileEntityMachineWarController shill;

    public ContainerMachineWarController(InventoryPlayer invPlayer, TileEntityMachineWarController tedf) {
        super(invPlayer, tedf.inventory);
        shill = tedf;

        // 4 Slots (shifted upwards by modifying y values)
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 0, 6, 52));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 1, 29, 11));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 2, 49, 11));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 3, 29, 31));

        playerInv(invPlayer,8,103,162);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // TileEntity Slots to Player Inventory
            if(index < 4) {
                if(!this.mergeItemStack(itemstack1, 4, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!this.mergeItemStack(itemstack1, 0, 4, false)) {
                return ItemStack.EMPTY;
            }

            if(itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return shill.isUseableByPlayer(player);
    }
}
