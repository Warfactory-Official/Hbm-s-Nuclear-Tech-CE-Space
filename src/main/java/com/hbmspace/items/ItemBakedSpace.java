package com.hbmspace.items;

import com.hbm.items.IDynamicModels;
import com.hbm.items.ItemBakedBase;
import com.hbm.items.ModItems;

public class ItemBakedSpace extends ItemBakedBase implements IDynamicModelsSpace {

    public ItemBakedSpace(String s, String texturePath){
        super(s, texturePath);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public ItemBakedSpace(String s){
        super(s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }
}
