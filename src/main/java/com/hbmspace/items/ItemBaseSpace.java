package com.hbmspace.items;

import com.hbm.items.ItemBase;
import com.hbm.items.ModItems;

public class ItemBaseSpace extends ItemBase {
    public ItemBaseSpace(String s){
        super(s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
    }
}
