package com.hbmspace.entity.mob;

import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.world.World;
@AutoRegister(name = "entity_siftereel", trackingRange = 80, eggColors = {0x5B963E, 0xC0B286})
public class EntitySifterEel extends EntityFish implements IEntityEnumMulti {

    public enum SifterEel {
        PLAIN,
        FAST,
        EXOTIC,
        PHASED,
        ELEMENTAL,
        PERFECT,
    }

    public SifterEel type;

    public EntitySifterEel(World world) {
        super(world, 1.8, 8.0F);

        type = SifterEel.values()[world.rand.nextInt(SifterEel.values().length)];
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return type;
    }

}