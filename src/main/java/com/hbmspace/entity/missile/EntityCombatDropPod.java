package com.hbmspace.entity.missile;

import com.hbm.explosion.ExplosionLarge;
import com.hbmspace.tileentity.machine.storage.TileEntityCombatDropPod;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@AutoRegister(name = "entity_combat_pod", trackingRange = 1000)
public class EntityCombatDropPod extends EntityThrowable {

    private NBTTagCompound entityType;
    private int amount;
    private int color;

    public EntityCombatDropPod(World world) {
        super(world);
        this.ignoreFrustumCheck = true;
        this.isImmuneToFire = true;
    }

    public void setPayload(NBTTagCompound entityData, int amount, int color) {
        this.entityType = entityData;
        this.amount = amount;
        this.setColor(color);
    }

    public void setColor(int color) {
        this.color = color;
        this.getDataManager().set(COLOR, color);
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityCombatDropPod.class, DataSerializers.VARINT);

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(COLOR, 0);
    }

    @Override
    public void onUpdate() {
        this.motionY = -1.0D;
        this.motionX = 0.0D;
        this.motionZ = 0.0D;

        this.lastTickPosX = this.prevPosX = this.posX;
        this.lastTickPosY = this.prevPosY = this.posY;
        this.lastTickPosZ = this.prevPosZ = this.posZ;

        for (int i = 0; i < 4; i++) {
            if (this.world.getBlockState(new BlockPos((int) (posX - 0.5), (int) (posY + 1), (int) (posZ - 0.5))).getMaterial() != Material.AIR) {

                if (!this.world.isRemote) {
                    ExplosionLarge.spawnParticles(world, posX, posY + 1, posZ, 50);
                    this.world.playSound(null, posX, posY, posZ, HBMSpaceSoundHandler.hatchImpact, SoundCategory.BLOCKS, 10.0F, 0.5F + rand.nextFloat() * 0.5F);

                    int x = (int) this.posX;
                    int y = (int) this.posY + 1;
                    int z = (int) this.posZ;

                    BlockPos pos = new BlockPos(x, y, z);
                    this.world.setBlockState(pos, ModBlocksSpace.combat_drop.getDefaultState());

                    TileEntity te = this.world.getTileEntity(pos);

                    if (te instanceof TileEntityCombatDropPod capsule) {
                        if (this.entityType != null) {
                            capsule.setPayload(this.entityType, this.amount, this.color);
                            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                        }
                    }
                }

                this.setDead();
                break;
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
        }
    }

    @Override
    protected void onImpact(@NotNull RayTraceResult result) {
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 500000;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.entityType = nbt.getCompoundTag("entityType");
        this.amount = nbt.getInteger("amount");
        this.setColor(nbt.getInteger("color"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("color", color);
        nbt.setInteger("amount", amount);
        if (entityType != null) nbt.setTag("entityType", entityType);
    }
}
