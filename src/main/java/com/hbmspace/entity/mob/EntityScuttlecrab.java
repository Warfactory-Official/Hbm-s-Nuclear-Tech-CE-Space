package com.hbmspace.entity.mob;

import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
@AutoRegister(name = "entity_scuttlecrab", trackingRange = 80, eggColors = {0xF17951, 0xEDDABB})
public class EntityScuttlecrab extends EntityWaterMob implements IEntityEnumMulti {

    public enum Scuttlecrab {
        TROPICAL,
        CLAYINFUSED,
    }

    public Scuttlecrab type;

    public EntityScuttlecrab(World world) {
        super(world);

        type = Scuttlecrab.TROPICAL;
        if(world.rand.nextInt(8) == 0) type = Scuttlecrab.CLAYINFUSED;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return type;
    }


    private boolean seafloorWalking = false;

    @Override
    public boolean isInWater() {
        if(seafloorWalking) return false;
        return this.inWater;
    }

    @Override
    public void onLivingUpdate() {
        seafloorWalking = true;
        super.onLivingUpdate();
        seafloorWalking = false;
    }


    // Crab walk!
    float crabDirection = 1;
    int switchTimer = 0;

    @Override
    public void travel(float strafe, float vertical, float forward) {
        switchTimer--;
        if(switchTimer <= 0) {
            crabDirection *= -1;
            switchTimer = 20 + world.rand.nextInt(80);
        }

        super.travel(forward * crabDirection, vertical, strafe);
    }

    @Override
    protected Item getDropItem() {
        return Items.CLAY_BALL;
    }

}