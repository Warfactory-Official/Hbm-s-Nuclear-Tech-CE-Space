package com.hbmspace.entity.mob;

import com.hbm.handler.WeightedRandomChestContentFrom1710;
import com.hbm.itempool.ItemPool;
import com.hbm.itempool.ItemPoolsComponent;

import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.world.World;
@AutoRegister(name = "entity_scrapfish", trackingRange = 80, eggColors = {0xDF9835, 0x510E13})
public class EntityScrapFish extends EntityFish implements IEntityEnumMulti {

    public enum ScrapFish {
        STEEL,
        ALUMINIUM,
        ISOTOPE,
        CADMIUM,
        TECH,
        BLOOD,
        HORROR,
    }

    public ScrapFish type;

    public EntityScrapFish(World world) {
        super(world, 0.8, 4.0F);

        type = ScrapFish.values()[world.rand.nextInt(ScrapFish.values().length)];
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return type;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        WeightedRandomChestContentFrom1710[] pool = ItemPool.getPool(ItemPoolsComponent.POOL_MACHINE_PARTS);
        int j = rand.nextInt(3 + lootingModifier) + 1;

        for(int k = 0; k < j; ++k) {
            this.entityDropItem(ItemPool.getStack(pool, rand).copy(), 0.0F);
        }
    }

}