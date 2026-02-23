package com.hbmspace.entity.mob;

import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.Item;
import net.minecraft.world.World;
@AutoRegister(name = "entity_scutterfish", trackingRange = 80, eggColors = {0xC8C9CD, 0x858894})
public class EntityScutterfish extends EntityFish {

    public EntityScutterfish(World world) {
        super(world, 1.5, 6.0F);
    }

    @Override
    protected Item getDropItem() {
        return ModItemsSpace.scuttertail;
    }

}