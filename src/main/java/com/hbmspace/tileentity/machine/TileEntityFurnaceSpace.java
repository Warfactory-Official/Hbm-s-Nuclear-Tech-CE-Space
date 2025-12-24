package com.hbmspace.tileentity.machine;

import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.tileentity.TESpaceUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileEntityFurnaceSpace extends TileEntityFurnace {

    /*// Overrides the vanilla furnace TE to check airbreathing

    @Override
    public void update() {
        boolean wasBurning = this.isBurning();
        boolean dirty = false;

        if (this.isBurning()) {
            this.setField(0, this.getField(0) - 1);
        }

        if (!this.world.isRemote) {
            ItemStack input = this.getStackInSlot(0);
            ItemStack fuel  = this.getStackInSlot(1);

            if (this.isBurning() || (!fuel.isEmpty() && !input.isEmpty())) {
                if (!this.isBurning() && this.canSmelt()) {
                    int burnTime = getItemBurnTime(fuel);

                    // If the machine can't combust, provide no burning (lava works without an atmosphere though!)
                    if (burnTime > 0
                            && fuel.getItem() != Items.LAVA_BUCKET
                            && !TESpaceUtil.breatheAir(this.world, this.pos, 0)) {
                        burnTime = 0;
                    }

                    this.setField(1, burnTime);
                    this.setField(0, burnTime);

                    if (this.isBurning()) {
                        dirty = true;

                        if (!fuel.isEmpty()) {
                            ItemStack fuelCopy = fuel.copy();
                            fuel.shrink(1);

                            if (fuel.isEmpty()) {
                                ItemStack container = fuelCopy.getItem().getContainerItem(fuelCopy);
                                this.setInventorySlotContents(1, container.isEmpty() ? ItemStack.EMPTY : container);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    this.setField(2, this.getField(2) + 1);

                    if (this.getField(2) == 200) {
                        this.setField(2, 0);
                        this.smeltItem();
                        dirty = true;
                    }
                } else {
                    this.setField(2, 0);
                }
            }

            boolean isBurningNow = this.isBurning();
            if (wasBurning != isBurningNow) {
                dirty = true;
                BlockFurnaceSpace.updateFurnaceBlockState(isBurningNow, this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
            }
        }

        if (dirty) {
            this.markDirty();
        }
    }*/
}
