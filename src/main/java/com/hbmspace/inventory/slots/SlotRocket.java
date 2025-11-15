package com.hbmspace.inventory.slots;

import com.hbm.items.weapon.ItemMissile;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.weapon.ItemCustomRocket;
import com.hbmspace.tileentity.machine.TileEntityMachineRocketAssembly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotRocket extends SlotItemHandler {

    public SlotRocket(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof ItemCustomRocket;
    }

    public static class SlotRocketPart extends SlotLayer {

        private ItemMissile.PartType type;

        public SlotRocketPart(IItemHandler itemHandler, int index, int x, int y, int layer, ItemMissile.PartType type) {
            super(itemHandler, index, x, y, layer);
            this.type = type;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            if (!super.isItemValid(stack)) return false;
            if (stack.isEmpty()) return false;
            if (!(stack.getItem() instanceof ItemMissile part)) return false;
            return part.type == type;
        }

    }

    public static class SlotCapsule extends SlotItemHandler {

        public SlotCapsule(IItemHandler itemHandler, int index, int x, int y) {
            super(itemHandler, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            if (stack.isEmpty()) return false;
            if (!(stack.getItem() instanceof ItemMissile item)) return false;

            if (item.type != ItemMissile.PartType.WARHEAD) return false;
            if (item == ModItemsSpace.rp_pod_20) return false;

            return item.attributes[0] == ItemMissile.WarheadType.APOLLO || item.attributes[0] == ItemMissile.WarheadType.SATELLITE;
        }

    }

    public static class SlotDrive extends SlotLayer {

        TileEntityMachineRocketAssembly inventory;

        public SlotDrive(TileEntityMachineRocketAssembly inventory, IItemHandler itemHandler, int index, int x, int y, int layer) {
            super(itemHandler, index, x, y, layer);
            this.inventory = inventory;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            if (stack.isEmpty()) return false;
            return stack.getItem() instanceof ItemVOTVdrive;
        }

        @Override
        public void setLayer(int layer) {
            super.setLayer(layer);
            inventory.currentStage = layer;
        }

    }

}
