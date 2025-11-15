package com.hbmspace.items;

import com.hbm.items.IDynamicModels;
import com.hbm.items.ModItems;
import com.hbm.items.special.ItemCustomLore;

public class ItemCustomLoreSpace extends ItemCustomLore implements IDynamicModelsSpace {
    public ItemCustomLoreSpace(String s, String texturePath){
        super(s, texturePath);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public ItemCustomLoreSpace(String s){
        super(s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }
}
