package com.hbmspace.tileentity.machine.rbmk;

import com.hbm.api.fluid.IFluidStandardReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.lib.Library;
import com.hbm.tileentity.machine.rbmk.RBMKColumn;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKBase;
import com.hbm.util.ParticleUtil;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister
public class TileEntityRBMKBurner extends TileEntityRBMKBase implements IFluidStandardReceiver {

    public FluidTankNTM tank;
    private int lastHot;
    public TileEntityRBMKBurner() {
        super();

        this.tank = new FluidTankNTM(Fluids.GASOLINE, 8000);
    }

    @Override
    public void update() {
        int maxBurn = 10;
        if(!world.isRemote) {

            if(this.world.getTotalWorldTime() % 20 == 0)
                this.trySubscribe(tank.getTankType(), world, pos.getX(), pos.getY() - 1, pos.getZ(), Library.NEG_Y);
            maxBurn += maxBurn;

            if((int)(this.heat) > 19) {
                if(tank.getTankType().hasTrait(FT_Flammable.class)){
                    int heating = Math.min(maxBurn, tank.getFill());
                    {
                        tank.setFill(tank.getFill() - heating );
                        //Math.min(this.heat, maxBurn);
                        long powerProd = tank.getTankType().getTrait(FT_Flammable.class).getHeatEnergy() * heating  / 1_000000; // divided by 1000 per mB
                        this.heat += powerProd;
                    }
                    this.lastHot = heating;
                }

                if(lastHot > 0) {
                    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(), pos.getY() + 4, pos.getZ(), pos.getX() + 1, pos.getY() + 8, pos.getZ() + 1));

                    for(Entity e : entities) {
                        e.setFire(5);
                        e.attackEntityFrom(DamageSource.IN_FIRE, 10);
                    }
                }
            } else {
                this.lastHot = 0;
            }

        } else {

            if(this.lastHot > 100) {
                for(int i = 0; i < 2; i++) {
                    world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, 0, 0.2, 0);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, 0, 0.2, 0);
                    ParticleUtil.spawnGasFlame(world, pos.getX() + world.rand.nextDouble(), pos.getY() + 3.5 + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), world.rand.nextGaussian() * 0.2, 0.1, world.rand.nextGaussian() * 0.2);
                }

                if(world.rand.nextInt(20) == 0)
                    world.spawnParticle(EnumParticleTypes.LAVA, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, 0, 0.0, 0);
            } else if(this.lastHot > 50) {
                for(int i = 0; i < 2; i++) {
                    world.spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, world.rand.nextGaussian() * 0.05, 0.2, world.rand.nextGaussian() * 0.05);
                    world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, 0, 0.2, 0);
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, 0, 0.2, 0);
                    ParticleUtil.spawnGasFlame(world, pos.getX() + world.rand.nextDouble(), pos.getY() + 3.5 + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), world.rand.nextGaussian() * 0.2, 0.1, world.rand.nextGaussian() * 0.2);
                }
            } else if(this.lastHot > 0) {

                if(world.getTotalWorldTime() % 2 == 0)
                    world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, 0, 0.2, 0);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.25 + world.rand.nextDouble() * 0.5, pos.getY() + 4.5, pos.getZ() + 0.25 + world.rand.nextDouble() * 0.5, 0, 0.2, 0);
                ParticleUtil.spawnGasFlame(world, pos.getX() + world.rand.nextDouble(), pos.getY() + 3.5 + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), world.rand.nextGaussian() * 0.2, 0.1, world.rand.nextGaussian() * 0.2);

            }

        }

        super.update();

    }

    private int[] findCore(World world, int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        tank.readFromNBT(nbt, "fuel");
        this.lastHot = nbt.getInteger("burned");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        tank.writeToNBT(nbt, "fuel");
        nbt.setInteger("burned", this.lastHot);
        return super.writeToNBT(nbt);
    }

    @Override
    public RBMKColumn.ColumnType getConsoleType() {
        return RBMKColumn.ColumnType.BLANK; // temporary solution TODO remake it
    }


    @Override
    public FluidTankNTM[] getAllTanks() {
        return new FluidTankNTM[] {tank};
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return new FluidTankNTM[] {tank};
    }

}
