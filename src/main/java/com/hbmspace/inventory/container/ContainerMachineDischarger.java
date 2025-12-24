package com.hbmspace.inventory.container;

import com.hbm.util.InventoryUtil;
import com.hbmspace.tileentity.machine.TileEntityMachineDischarger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerMachineDischarger extends Container {

    private TileEntityMachineDischarger nukeBoy;

    public ContainerMachineDischarger(InventoryPlayer invPlayer, TileEntityMachineDischarger tedf) {

        nukeBoy = tedf;

        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 0, 87, 63));
        //this.addSlotToContainer(new SlotCraftingOutput(invPlayer.player, tedf, 1, 134, 63));
        //this.addSlotToContainer(new Slot(tedf, 2, 26, 18));
        this.addSlotToContainer(new SlotItemHandler(tedf.inventory, 1, 8, 108));

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 56));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142 + 56));
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer p_82846_1_, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        Slot var4 = this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if(par2 <= nukeBoy.inventory.getSlots() - 1) {
                if(!InventoryUtil.mergeItemStack(this.inventorySlots, var5, nukeBoy.inventory.getSlots(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!InventoryUtil.mergeItemStack(this.inventorySlots, var5, 0, nukeBoy.inventory.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if (var5.getCount() == 0)
            {
                var4.putStack(ItemStack.EMPTY);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer player) {
        return nukeBoy.isUseableByPlayer(player);
    }

    @Override
    public void updateProgressBar(int i, int j) {
        if(i == 1)
        {
            nukeBoy.power = j;
        }
    }
}
