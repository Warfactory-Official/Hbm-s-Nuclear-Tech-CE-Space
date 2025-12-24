package com.hbmspace.inventory.container;

import com.hbm.inventory.SlotBattery;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.inventory.container.ContainerBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerHydroponic extends ContainerBase {

    public ContainerHydroponic(InventoryPlayer invPlayer, IItemHandler inv) {
        super(invPlayer, inv);

        // Inputs
        addSlotToContainer(new SlotItemHandler(inv, 0, 67, 18));
        addSlotToContainer(new SlotItemHandler(inv, 1, 67, 54));

        // Battery
        addSlotToContainer(new SlotBattery(inv, 2, 147, 54));

        // Outputs
        for(int i = 0; i < 3; i++) addSlotToContainer(new SlotTakeOnly(inv, i + 3, 111, 18 + i * 18));

        playerInv(invPlayer, 8, 104, 162);
    }

}
