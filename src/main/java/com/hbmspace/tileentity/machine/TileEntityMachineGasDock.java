package com.hbmspace.tileentity.machine;

import com.hbm.api.fluid.IFluidStandardTransceiver;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.lib.Library;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.ParticleUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class TileEntityMachineGasDock extends TileEntityMachineBase implements ITickable, IFluidStandardTransceiver {

    public FluidTankNTM[] tanks;

    public boolean hasRocket = true;
    public int launchTicks = 0;

    private AxisAlignedBB renderBoundingBox;

    public TileEntityMachineGasDock() {
        super(0, true, false);
        this.tanks = new FluidTankNTM[3];
        this.tanks[0] = new FluidTankNTM(com.hbmspace.inventory.fluid.Fluids.JOOLGAS, 64_000);
        this.tanks[1] = new FluidTankNTM(Fluids.HYDROGEN, 32_000);
        this.tanks[2] = new FluidTankNTM(Fluids.OXYGEN, 32_000);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        nbt.setBoolean("hasRocker", hasRocket);
        tanks[0].readFromNBT(nbt, "gas");
        tanks[1].readFromNBT(nbt, "f1");
        tanks[2].readFromNBT(nbt, "f2");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.getBoolean("hasRocker");

        tanks[0].writeToNBT(nbt, "gas");
        tanks[1].writeToNBT(nbt, "f1");
        tanks[2].writeToNBT(nbt, "f2");
        return super.writeToNBT(nbt);
    }


    @Override
    public void update() {
        if(!world.isRemote) {
            updateConnections();

            for(DirPos pos : getConPos()) {
                if(tanks[0].getFill() > 0) {
                    this.sendFluid(tanks[0], world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
                }
            }

            CelestialBody planet = CelestialBody.getPlanet(world);

            launchTicks = MathHelper.clamp(launchTicks + (hasRocket ? -1 : 1), hasRocket ? -20 : 0, 100);

            if(planet.gas != null) {
                tanks[0].setTankType(planet.gas);

                if(hasFuel() && launchTicks <= -20) {
                    hasRocket = false;
                    collectGas();
                } else if(launchTicks >= 100) {
                    hasRocket = true;
                }
            }

            this.networkPackNT(150);
        } else {
            launchTicks = MathHelper.clamp(launchTicks + (hasRocket ? -1 : 1), hasRocket ? -20 : 0, 100);
            if(launchTicks > 0 && launchTicks < 100) {
                ParticleUtil.spawnGasFlame(world, pos.getX() + 0.5, pos.getY() + 0.5 + launchTicks, pos.getZ() + 0.5, 0.0, -1.0, 0.0);

                if(launchTicks < 10) {
                    ExplosionLarge.spawnShock(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1 + world.rand.nextInt(3), 1 + world.rand.nextGaussian());
                }
            }
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(hasRocket);
        for (FluidTankNTM tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        hasRocket = buf.readBoolean();
        for (FluidTankNTM tank : tanks) tank.deserialize(buf);
    }

    private void updateConnections() {
        for(DirPos pos : getConPos()) {
            for(int i = 1; i < tanks.length; i++) {
                if(tanks[i].getTankType() != Fluids.NONE) {
                    trySubscribe(tanks[i].getTankType(), world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
                }
            }
        }
    }

    private void collectGas() {
        if(tanks[1].getFill() < 500) return;
        if(tanks[2].getFill() < 500) return;
        if(tanks[0].getFill() + 8000 > tanks[0].getMaxFill()) return;

        tanks[1].setFill(tanks[1].getFill() - 500);
        tanks[2].setFill(tanks[2].getFill() - 500);
        tanks[0].setFill(tanks[0].getFill() + 8000);
    }

    private boolean hasFuel() {
        return tanks[1].getFill() >= 500 && tanks[2].getFill() >= 500;
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            renderBoundingBox = new AxisAlignedBB(
                    pos.getX() - 1,
                    pos.getY(),
                    pos.getZ() - 1,
                    pos.getX() + 2,
                    pos.getY() + 1,
                    pos.getZ() + 2
            );
        }

        return renderBoundingBox;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return tanks;
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return new FluidTankNTM[] {tanks[0]};
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return new FluidTankNTM[] {tanks[1], tanks[2]};
    }


    private DirPos[] conPos;


    protected DirPos[] getConPos() {
        if(conPos == null) {
            List<DirPos> list = new ArrayList<>();

            // Below
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    list.add(new DirPos(pos.getX() + x, pos.getY() - 1, pos.getZ() + z, Library.NEG_Y));
                }
            }

            // Sides
            for(int i = -1; i <= 1; i++) {
                list.add(new DirPos(pos.getX() + i, pos.getY(), pos.getZ() + 2, Library.POS_Z));
                list.add(new DirPos(pos.getX() + i, pos.getY(), pos.getZ() - 2, Library.NEG_Z));
                list.add(new DirPos(pos.getX() + 2, pos.getY(), pos.getZ() + i, Library.POS_X));
                list.add(new DirPos(pos.getX() - 2, pos.getY(), pos.getZ() + i, Library.NEG_X));
            }

            conPos = list.toArray(new DirPos[0]);
        }

        return conPos;
    }

    @Override
    public String getDefaultName() {
        return "container.gasDock";
    }

}
