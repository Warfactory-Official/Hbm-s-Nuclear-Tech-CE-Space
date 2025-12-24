package com.hbmspace.items.special;

import com.hbm.items.ModItems;
import com.hbm.items.special.ItemConsumable;
import com.hbmspace.items.ModItemsSpace;

public class ItemConsumableSpace extends ItemConsumable {

    public ItemConsumableSpace(String s){
        super(s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
    }
}
