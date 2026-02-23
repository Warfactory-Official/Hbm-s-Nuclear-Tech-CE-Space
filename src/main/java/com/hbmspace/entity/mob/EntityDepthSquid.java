package com.hbmspace.entity.mob;

import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
@AutoRegister(name = "entity_depthsquid", trackingRange = 80, eggColors = {0x00B4Df, 0x016085})
public class EntityDepthSquid extends EntitySquid implements IEntityEnumMulti {

    public enum DepthSquid {
        AQUA,
        BLACK,
        ORANGE,
        OURPLE,
        RED,
        SILVER,
        VICIOUS,
    }

    public DepthSquid type;

    public EntityDepthSquid(World world) {
        super(world);

        type = DepthSquid.values()[world.rand.nextInt(DepthSquid.values().length)];
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return type;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        super.travel(strafe, vertical, forward);

        // reimplement limb swing because... mojang squid implementation just blasts through everything.
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if(f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.limbSwingAmount += (f6 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

}