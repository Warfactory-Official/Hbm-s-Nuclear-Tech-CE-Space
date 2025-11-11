package com.hbmspace.inventory.container;

import com.hbm.api.energymk2.IBatteryItem;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotUpgrade;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemMachineUpgrade;
import com.hbmspace.inventory.recipes.VacuumCircuitRecipes;
import com.hbmspace.tileentity.machine.TileEntityMachineVacuumCircuit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerVacuumCircuit extends Container {

    private TileEntityMachineVacuumCircuit sucker;

    public ContainerVacuumCircuit(InventoryPlayer playerInv, TileEntityMachineVacuumCircuit tile) {
        sucker = tile;

        //Inputs
        for(int i = 0; i < 2; i++) for(int j = 0; j < 2; j++) this.addSlotToContainer(new SlotNonRetarded(tile.inventory, i * 2 + j, 11 + j * 18, 39 + i * 18));
        //Output
        this.addSlotToContainer(new SlotCraftingOutput(playerInv.player, tile.inventory, 4, 85, 48));
        //Battery
        this.addSlotToContainer(new SlotItemHandler(tile.inventory, 5, 132, 72));

        //Upgrades
        this.addSlotToContainer(new SlotUpgrade(tile.inventory, 6, 10, 13));
        this.addSlotToContainer(new SlotUpgrade(tile.inventory, 7, 28, 13));

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 122 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 180));
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int index) {
        ItemStack rStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            rStack = stack.copy();

            if(index <= 7) {
                if(!this.mergeItemStack(stack, 8, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {

                if(rStack.getItem() instanceof IBatteryItem || rStack.getItem() == ModItems.battery_creative) {
                    if(!this.mergeItemStack(stack, 5, 6, false)) return ItemStack.EMPTY;
                } else if(rStack.getItem() instanceof ItemMachineUpgrade) {
                    if(!this.mergeItemStack(stack, 6, 8, false)) return ItemStack.EMPTY;
                } else {
                    for(RecipesCommon.AStack t : VacuumCircuitRecipes.wafer) if(t.matchesRecipe(stack, false)) if(!this.mergeItemStack(stack, 0, 2, false)) return ItemStack.EMPTY;
                    for(RecipesCommon.AStack t : VacuumCircuitRecipes.pcb) if(t.matchesRecipe(stack, false)) if(!this.mergeItemStack(stack, 2, 4, false)) return ItemStack.EMPTY;
                    return ItemStack.EMPTY;
                }
            }

            if(stack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return rStack;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer player) {
        return sucker.isUseableByPlayer(player);
    }

}
