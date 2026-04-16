package com.hbmspace.entity.projectile;

import com.hbm.lib.ModDamageSource;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
// TODO we need that in the main mod, not in the addon. I'm just a lazy piece of shit
@AutoRegister(name = "entity_ntm_siege_laser", trackingRange = 1000)
public class EntitySiegeLaser extends EntityThrowable {

    private float damage = 2;
    private float explosive = 0F;
    private float breakChance = 0F;
    private boolean incendiary = false;

    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntitySiegeLaser.class, DataSerializers.VARINT);

    public EntitySiegeLaser(World world) {
        super(world);
    }

    public EntitySiegeLaser(World world, EntityLivingBase entity) {
        super(world, entity);
    }

    public EntitySiegeLaser(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(COLOR, 0xFFFFFF);
    }

    public EntitySiegeLaser setDamage(float damage) {
        this.damage = damage;
        return this;
    }

    public EntitySiegeLaser setExplosive(float explosive) {
        this.explosive = explosive;
        return this;
    }

    public EntitySiegeLaser setBreakChance(float breakChance) {
        this.breakChance = breakChance;
        return this;
    }

    public EntitySiegeLaser setIncendiary() {
        this.incendiary = true;
        return this;
    }

    public EntitySiegeLaser setColor(int color) {
        this.getDataManager().set(COLOR, color);
        return this;
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.ticksExisted > 60) {
            this.setDead();
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {

        if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
            DamageSource dmg;

            if (this.getThrower() != null) {
                dmg = new EntityDamageSourceIndirect(ModDamageSource.s_laser, this, this.getThrower());
            } else {
                dmg = new DamageSource(ModDamageSource.s_laser);
            }

            if (result.entityHit.attackEntityFrom(dmg, this.damage)) {
                this.setDead();

                if (this.incendiary) {
                    result.entityHit.setFire(3);
                }

                if (this.explosive > 0) {
                    this.world.newExplosion(this, result.hitVec.x, result.hitVec.y, result.hitVec.z, this.explosive, this.incendiary, false);
                }
            }
        }
        else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {

            if (this.explosive > 0) {
                this.world.newExplosion(this, result.hitVec.x, result.hitVec.y, result.hitVec.z, this.explosive, this.incendiary, false);

            } else if (this.incendiary) {
                EnumFacing dir = result.sideHit;
                int x = result.getBlockPos().getX() + dir.getXOffset();
                int y = result.getBlockPos().getY() + dir.getYOffset();
                int z = result.getBlockPos().getZ() + dir.getZOffset();

                if (this.world.isAirBlock(new BlockPos(x, y, z))) {
                    this.world.setBlockState(new BlockPos(x, y, z), Blocks.FIRE.getDefaultState());
                }
            }

            if (this.rand.nextFloat() < this.breakChance) {
                this.world.destroyBlock(result.getBlockPos(), false);
            }

            this.setDead();
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setFloat("damage", this.damage);
        nbt.setFloat("explosive", this.explosive);
        nbt.setFloat("breakChance", this.breakChance);
        nbt.setBoolean("incendiary", this.incendiary);
        nbt.setInteger("color", this.getColor());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.damage = nbt.getFloat("damage");
        this.explosive = nbt.getFloat("explosive");
        this.breakChance = nbt.getFloat("breakChance");
        this.incendiary = nbt.getBoolean("incendiary");
        this.setColor(nbt.getInteger("color"));
    }
}
