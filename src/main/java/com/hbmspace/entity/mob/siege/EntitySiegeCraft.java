package com.hbmspace.entity.mob.siege;

import com.hbm.entity.mob.EntityUFOBase;
import com.hbmspace.entity.projectile.EntitySiegeLaser;
import com.hbm.entity.siege.SiegeTier;
import com.hbm.handler.threading.PacketThreading;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.lib.ModDamageSource;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// TODO we need that in the main mod, not in the addon. I'm just a lazy piece of shit
@AutoRegister(name = "entity_micro_ufo", updateFrequency = 3, eggColors = {0x5B963E, 0xC0B286})
public class EntitySiegeCraft extends EntityUFOBase {

    private int attackCooldown;
    private int beamCountdown;

    private int lifetime = 0;
    private boolean isRetreating = false;
    private static final int MAX_LIFETIME = 60 * 4 * 20;

    private static final DataParameter<Integer> TIER = EntityDataManager.createKey(EntitySiegeCraft.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> BEAM = EntityDataManager.createKey(EntitySiegeCraft.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> LOCK_X = EntityDataManager.createKey(EntitySiegeCraft.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LOCK_Y = EntityDataManager.createKey(EntitySiegeCraft.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LOCK_Z = EntityDataManager.createKey(EntitySiegeCraft.class, DataSerializers.FLOAT);

    public EntitySiegeCraft(World world) {
        super(world);
        this.setSize(7F, 1F);
        this.isImmuneToFire = true;
        this.ignoreFrustumCheck = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(TIER, 0);
        this.getDataManager().register(BEAM, false);
        this.getDataManager().register(LOCK_X, 0F);
        this.getDataManager().register(LOCK_Y, 0F);
        this.getDataManager().register(LOCK_Z, 0F);
    }

    public void setTier(SiegeTier tier) {
        this.getDataManager().set(TIER, tier.id);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(tier.speedMod);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(tier.health * 25);
        this.setHealth(this.getMaxHealth());
    }

    public SiegeTier getTier() {
        SiegeTier tier = SiegeTier.tiers[this.getDataManager().get(TIER)];
        return tier != null ? tier : SiegeTier.CLAY;
    }

    public void setBeam(boolean beam) {
        this.getDataManager().set(BEAM, beam);
    }

    public boolean getBeam() {
        return this.getDataManager().get(BEAM);
    }

    public void setLockon(double x, double y, double z) {
        this.getDataManager().set(LOCK_X, (float) x);
        this.getDataManager().set(LOCK_Y, (float) y);
        this.getDataManager().set(LOCK_Z, (float) z);
    }

    public Vec3d getLockon() {
        return new Vec3d(
                this.getDataManager().get(LOCK_X),
                this.getDataManager().get(LOCK_Y),
                this.getDataManager().get(LOCK_Z)
        );
    }

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float damage) {
        if (this.isEntityInvulnerable(source)) return false;

        SiegeTier tier = this.getTier();

        if (tier.fireProof && source.isFireDamage()) {
            this.extinguish();
            return false;
        }

        if (tier.noFriendlyFire && source instanceof EntityDamageSource && !(source.getTrueSource() instanceof EntityPlayer)) {
            return false;
        }

        damage -= tier.dt;
        if (damage < 0) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.HOSTILE, 5F, 1.0F + rand.nextFloat() * 0.5F);
            return false;
        }

        damage *= (1F - tier.dr);
        return super.attackEntityFrom(source, damage);
    }

    @Override
    protected void onDeathUpdate() {
        this.beamCountdown = 200;
        this.setBeam(false);
        this.motionY -= 0.05D;

        if (this.deathTime == 19 && !world.isRemote) {
            NBTTagCompound data = new NBTTagCompound();
            data.setString("type", "tinytot");
            PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(data, posX, posY + 0.5, posZ),
                    new NetworkRegistry.TargetPoint(this.dimension, posX, posY, posZ, 250));
            this.world.playSound(null, posX, posY, posZ, HBMSoundHandler.mukeExplosion, SoundCategory.HOSTILE, 15.0F, 1.0F);
        }

        super.onDeathUpdate();
    }

    @Override
    protected void updateEntityActionState() {
        super.updateEntityActionState();

        if (this.courseChangeCooldown > 0) this.courseChangeCooldown--;
        if (this.scanCooldown > 0) this.scanCooldown--;

        if (!world.isRemote) {
            lifetime++;
            if (lifetime > MAX_LIFETIME) isRetreating = true;

            if (isRetreating) {
                this.target = null;
                this.setWaypoint((int) posX, 255, (int) posZ);
                approachPosition(20);
                if (this.posY > 250 || lifetime > MAX_LIFETIME + 300) {
                    this.setDead();
                    return;
                }
            }

            if (this.attackCooldown > 0) this.attackCooldown--;
            if (this.beamCountdown > 0) this.beamCountdown--;

            if (rand.nextInt(50) == 0) {
                NBTTagCompound dPart = new NBTTagCompound();
                dPart.setString("type", "tau");
                dPart.setByte("count", (byte) (2 + rand.nextInt(3)));
                PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(dPart, posX + rand.nextGaussian() * 2, posY + rand.nextGaussian(), posZ + rand.nextGaussian() * 2),
                        new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 50));
            }

            boolean beam = false;

            if (this.target == null || this.beamCountdown <= 0) {
                this.beamCountdown = 300;
            } else {
                if (this.beamCountdown >= 60 && this.beamCountdown < 120) {
                    double x = this.target.posX;
                    double y = this.target.posY + this.target.height * 0.5;
                    double z = this.target.posZ;
                    this.setLockon(x, y, z);

                    if (this.beamCountdown == 110) {
                        this.world.playSound(null, this.target.posX, this.target.posY, this.target.posZ, HBMSoundHandler.stingerLockon, SoundCategory.HOSTILE, 2F, 0.75F);
                    }
                }

                if (this.beamCountdown >= 40 && this.beamCountdown < 100) {
                    Vec3d lockon = this.getLockon();
                    NBTTagCompound fx = new NBTTagCompound();
                    fx.setString("type", "vanillaburst");
                    fx.setString("mode", "reddust");
                    fx.setDouble("motion", 0.2D);
                    fx.setInteger("count", 5);
                    PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(fx, lockon.x, lockon.y, lockon.z),
                            new NetworkRegistry.TargetPoint(this.dimension, lockon.x, lockon.y, lockon.z, 100));
                }

                if (this.beamCountdown < 40) {
                    Vec3d lockon = this.getLockon();

                    if (this.beamCountdown == 39) {
                        this.world.playSound(null, lockon.x, lockon.y, lockon.z, HBMSoundHandler.ufoBlast, SoundCategory.HOSTILE, 5.0F, 0.9F + world.rand.nextFloat() * 0.2F);
                    }

                    List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(lockon.x - 2, lockon.y - 2, lockon.z - 2, lockon.x + 2, lockon.y + 2, lockon.z + 2));

                    for (Entity e : entities) {
                        if (e instanceof EntityLivingBase living) {
                            if (this.canAttackClass(living.getClass())) {
                                e.attackEntityFrom(ModDamageSource.causeCombineDamage(this, e), 1000F);
                                e.setFire(5);

                                ContaminationUtil.contaminate(living, HazardType.RADIATION, ContaminationType.CREATIVE, 5F);
                            }
                        }
                    }

                    NBTTagCompound data = new NBTTagCompound();
                    data.setString("type", "plasmablast");
                    data.setFloat("r", 0.0F);
                    data.setFloat("g", 0.75F);
                    data.setFloat("b", 1.0F);
                    data.setFloat("pitch", -90 + rand.nextFloat() * 180);
                    data.setFloat("yaw", rand.nextFloat() * 180F);
                    data.setFloat("scale", 5F);
                    PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(data, lockon.x, lockon.y, lockon.z),
                            new NetworkRegistry.TargetPoint(dimension, lockon.x, lockon.y, lockon.z, 150));
                    beam = true;
                }
            }

            this.setBeam(beam);

            if (this.attackCooldown == 0 && this.target != null) {
                this.attackCooldown = 30 + rand.nextInt(10);

                double x = posX;
                double y = posY;
                double z = posZ;

                Vec3d vec = new Vec3d(target.posX - x, target.posY + target.height * 0.5 - y, target.posZ - z).normalize();
                SiegeTier tier = this.getTier();

                float health = getHealth() / getMaxHealth();

                int r = (int) (0xff * (1 - health));
                int g = (int) (0xff * health);
                int b = 0;
                int color = (r << 16) | (g << 8) | b;

                for (int i = 0; i < 7; i++) {
                    Vec3d copy = new Vec3d(vec.x, vec.y, vec.z).rotateYaw((float) (Math.PI / 180F * (i - 3) * 5F));

                    EntitySiegeLaser laser = new EntitySiegeLaser(world, this);
                    laser.setPosition(x, y, z);
                    laser.shoot(copy.x, copy.y, copy.z, 1F, 0.0F);
                    laser.setColor(color);
                    laser.setDamage(tier.damageMod);
                    // TODO when I'll update CE dependency
                    /*laser.setBreakChance(tier.laserBreak * 2);
                    if (tier.laserIncendiary) laser.setIncendiary();*/
                    world.spawnEntity(laser);
                }

                this.playSound(HBMSoundHandler.ballsLaser, 2.0F, 1.0F);
            }
        }

        if (this.courseChangeCooldown > 0) {
            double speed = this.target == null ? 0.5D : 1.0D + (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 3.0D);
            if (isRetreating) speed *= 2.0D;
            approachPosition(speed);
        }
    }

    @Override
    protected void setCourseWithoutTaget() {
        int x = (int) Math.floor(posX + rand.nextGaussian() * 15);
        int z = (int) Math.floor(posZ + rand.nextGaussian() * 15);
        int y = this.world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY() + 5 + rand.nextInt(6);
        this.setWaypoint(x, y, z);
    }

    @Override
    public IEntityLivingData onInitialSpawn(@NotNull DifficultyInstance difficulty, IEntityLivingData livingdata) {
        this.setTier(SiegeTier.tiers[rand.nextInt(SiegeTier.getLength())]);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void dropFewItems(boolean byPlayer, int fortune) {
        if (byPlayer) {
            for (ItemStack drop : this.getTier().dropItem) {
                this.entityDropItem(drop.copy(), 0F);
            }
        }
    }
}
