package com.hbmspace.items;

import com.hbm.items.IDynamicModels;
import com.hbm.items.ItemEnumMulti;
import com.hbm.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEnumMultiSpace<E extends Enum<E>> extends ItemEnumMulti<E> implements IDynamicModelsSpace {
    public ItemEnumMultiSpace(String registryName, E[] theEnum, boolean multiName, boolean multiTexture) {
        super(registryName, theEnum, multiName, multiTexture);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public ItemEnumMultiSpace(String registryName, E[] theEnum, boolean multiName, String texture) {
        super(registryName, theEnum, multiName, texture);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public ItemEnumMultiSpace<E> setCreativeTab(CreativeTabs tab) {
        return (ItemEnumMultiSpace<E>) super.setCreativeTab(tab);
    }
}
