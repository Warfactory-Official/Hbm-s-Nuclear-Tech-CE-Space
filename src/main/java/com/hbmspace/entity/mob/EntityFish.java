package com.hbmspace.entity.mob;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class EntityFish extends EntityWaterMob {

    private Vec3d currentSwimTarget;

    public EntityFish(World world, double swimSpeed, float watchDistance) {
        super(world);

        if (this.getNavigator() instanceof PathNavigateGround) {
            ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        }

        this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, watchDistance));
        this.tasks.addTask(1, new EntityAILookIdle(this));
        // Th3_Sl1ze: thanks mojank that EntityWaterMob doesn't extend EntityCreature now!!!
        this.tasks.addTask(2, new EntityAIBase() {
            private double xPosition;
            private double yPosition;
            private double zPosition;

            {
                this.setMutexBits(1);
            }

            @Override
            public boolean shouldExecute() {
                if (EntityFish.this.rand.nextInt(120) != 0) {
                    return false;
                }
                this.xPosition = EntityFish.this.posX + (EntityFish.this.rand.nextDouble() - 0.5D) * 20.0D;
                this.yPosition = EntityFish.this.posY + (EntityFish.this.rand.nextDouble() - 0.5D) * 6.0D;
                this.zPosition = EntityFish.this.posZ + (EntityFish.this.rand.nextDouble() - 0.5D) * 20.0D;
                return true;
            }

            @Override
            public boolean shouldContinueExecuting() {
                return !EntityFish.this.getNavigator().noPath();
            }

            @Override
            public void startExecuting() {
                EntityFish.this.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, swimSpeed);
            }
        });

        this.currentSwimTarget = new Vec3d(posX, posY, posZ);
        this.setSize(2.0F, 2.0F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10.0);
    }

    @Override
    protected void damageEntity(@NotNull DamageSource source, float amount) {
        super.damageEntity(source, amount);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();

        if(!this.isInWater()) {
            this.rotationPitch += 0.2F;
            this.rotationYaw = 0.0F;
            this.setJumping(true);
            if(this.onGround) {
                this.addVelocity(0.4 * rand.nextDouble() - 0.4 * rand.nextDouble(), 0.0, 0.4 * rand.nextDouble() - 0.4 * rand.nextDouble());
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.updateSwimTarget();
        this.moveTowardsTarget();
        updateRotation();
    }

    private void updateSwimTarget() {
        if(rand.nextInt(200) == 0 || !this.isInWater()) {
            double targetX = this.posX + (rand.nextDouble() - 0.5) * 20.0;
            double targetY = MathHelper.clamp(this.posY + (rand.nextDouble() - 0.5) * 8.0, 0.0, this.world.getHeight() - 1);
            double targetZ = this.posZ + (rand.nextDouble() - 0.5) * 20.0;
            this.currentSwimTarget = new Vec3d(targetX, targetY, targetZ);
        }
    }

    private void moveTowardsTarget() {
        double deltaX = this.currentSwimTarget.x - this.posX;
        double deltaY = this.currentSwimTarget.y - this.posY;
        double deltaZ = this.currentSwimTarget.z - this.posZ;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        double dirX = deltaX / distance;
        double dirY = deltaY / distance;
        double dirZ = deltaZ / distance;

        double speed = 0.01; // You can adjust this value for different speeds
        this.motionX += dirX * speed + (rand.nextDouble() - 0.5) * 0.02; // Added randomness
        this.motionY += dirY * speed + (rand.nextDouble() - 0.5) * 0.02;
        this.motionZ += dirZ * speed + (rand.nextDouble() - 0.5) * 0.02;

        updateRotation((float)dirX, (float)dirY, (float)dirZ);
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(this.isInWater()) {
            this.motionY *= 0.8F;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

            if(f6 > 1.0F) {
                f6 = 1.0F;
            }

            this.limbSwingAmount += (f6 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    private void updateRotation() {
        double deltaX = this.motionX;
        double deltaZ = this.motionZ;
        double deltaY = this.motionY;
        float targetYaw = (float) (Math.atan2(deltaZ, deltaX) * (180D / Math.PI)) - 90F;
        float targetPitch = (float) -(Math.atan2(deltaY, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)) * (180D / Math.PI));

        this.rotationYaw = this.updateRotation(this.rotationYaw, targetYaw, 10.0F);
        this.rotationPitch = this.updateRotation(this.rotationPitch, targetPitch, 10.0F);
    }

    private float updateRotation(float currentRotation, float targetRotation, float maxIncrement) {
        float deltaRotation = MathHelper.wrapDegrees(targetRotation - currentRotation);
        if(deltaRotation > maxIncrement) {
            deltaRotation = maxIncrement;
        }
        if(deltaRotation < -maxIncrement) {
            deltaRotation = -maxIncrement;
        }
        return currentRotation + deltaRotation;
    }

}