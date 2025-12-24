package com.hbmspace.inventory.container;

import com.hbm.api.energymk2.IBatteryItem;
import com.hbm.inventory.SlotBattery;
import com.hbm.inventory.SlotTakeOnly;
import com.hbmspace.tileentity.machine.TileEntityMachineMilkReformer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerMachineMilkReformer extends Container {

    private TileEntityMachineMilkReformer reformer;

    public ContainerMachineMilkReformer(InventoryPlayer invPlayer, TileEntityMachineMilkReformer tedf) {

        reformer = tedf;

        //Battery
        this.addSlotToContainer(new SlotBattery(tedf.inventory, 0, 79, 8));
        //Canister Input
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 1, 45, 88));
        //Canister Output
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 2, 45, 106));
        //Reformate Input
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 3, 95, 88));
        //Reformate Output
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 4, 95, 106));
        //Gas Input
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 5, 122, 88));
        //Gas Output
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 6, 122, 106));
        //Hydrogen Input
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 7, 149, 88));
        //Hydrogen Oil Output
        this.addSlotToContainer(new SlotTakeOnly(tedf.inventory, 8, 149, 106));


        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 156 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 214));
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer p_82846_1_, int par2) {
        ItemStack var3 = ItemStack.EMPTY;
        Slot var4 = this.inventorySlots.get(par2);

        if(var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if(par2 <= 10) {
                if(!this.mergeItemStack(var5, 11, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {

                if(var3.getItem() instanceof IBatteryItem) {
                    if(!this.mergeItemStack(var5, 1, 0, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if(!this.mergeItemStack(var5, 1, 2, false))
                        if(!this.mergeItemStack(var5, 3, 4, false))
                            if(!this.mergeItemStack(var5, 5, 6, false))
                                if(!this.mergeItemStack(var5, 7, 8, false))
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
        return reformer.isUseableByPlayer(player);
    }
}
