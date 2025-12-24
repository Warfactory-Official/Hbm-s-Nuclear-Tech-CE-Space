package com.hbmspace.inventory.container;

import com.hbm.api.energymk2.IBatteryItem;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbmspace.tileentity.machine.TileEntityMachineCryoDistill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerMachineCryoDistill extends Container {

    private TileEntityMachineCryoDistill cryo;

    public ContainerMachineCryoDistill(InventoryPlayer invPlayer, TileEntityMachineCryoDistill tedf) {

        cryo = tedf;

        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 0, 145,  71));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 1, 57,  71));
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 2, 57,  89));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 3, 79,  71));
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 4,  79,  89));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 5,  101, 71));
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 6, 101,  89));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 7, 19, 71));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 8, 123,  71));
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 9, 123, 89));
        int offset = 6;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 156 + i * 18 - offset));
            }
        }

        for(int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 214 - offset));
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer p_82846_1_, int par2) {
        ItemStack var3 = ItemStack.EMPTY;
        Slot var4 = this.inventorySlots.get(par2);

        if(var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if(par2 <= 9) {
                if(!this.mergeItemStack(var5, 8, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {

                if(var3.getItem() instanceof IBatteryItem) {
                    if(!this.mergeItemStack(var5, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(var3.getItem() instanceof IItemFluidIdentifier) {
                    if(!this.mergeItemStack(var5, 11, 12, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if(!this.mergeItemStack(var5, 1, 2, false))
                        if(!this.mergeItemStack(var5, 3, 4, false))
                            if(!this.mergeItemStack(var5, 5, 6, false))
                                if(!this.mergeItemStack(var5, 7, 8, false))
                                    if(!this.mergeItemStack(var5, 9, 10, false))
                                        return ItemStack.EMPTY;
                }
            }

            if(var5.getCount() == 0) {
                var4.putStack(ItemStack.EMPTY);
            } else {
                var4.onSlotChanged();
            }
        }

        return var3;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer player) {
        return cryo.isUseableByPlayer(player);
    }
}
