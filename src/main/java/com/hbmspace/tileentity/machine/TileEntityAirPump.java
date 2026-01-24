package com.hbmspace.tileentity.machine;

import com.hbm.api.fluid.IFluidStandardReceiver;
import com.hbm.handler.CompatHandler;
import com.hbm.handler.ThreeInts;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.AtmosphereBlob;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.handler.atmosphere.IAtmosphereProvider;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Optional.InterfaceList({@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")})
@AutoRegister
public class TileEntityAirPump extends TileEntityMachineBase implements ITickable, IFluidStandardReceiver, IAtmosphereProvider, SimpleComponent, CompatHandler.OCComponent{

    private int onTicks = 0;
    private boolean registered = false;

    // Bubble registration wait, so it has time to fill out the bubble before running (for recovering fill in a chunk that just loaded)
    private int recovering = 0;

    // Tile initialisation wait, for systems that run too fast and finish bubbling before chunks have time to load
    private int registerWait = 10;

    private AtmosphereBlob currentBlob;
    private int airFill = 0; // How many blocks are filled with air in the blob
    private int wasteFill = 0; // How many air blocks worth of waste gas has been produced
    private int bonusFill = 0; // How much waste gas has been returned, is consumed before the tank

    public FluidTankNTM tank;

    private Random rand = new Random();

    // Used for synchronizing printable info
    public CBT_Atmosphere currentAtmosphere;

    private TileEntityAirScrubber scrubber;

    public TileEntityAirPump() {
        super(1, true, false);
        tank = new FluidTankNTM(Fluids.OXYGEN, 16000);
    }

    public void spawnParticles() {

        if(world.getTotalWorldTime() % 2 == 0) {
            NBTTagCompound data = new NBTTagCompound();
            data.setString("type", "tower");
            data.setFloat("lift", 0.1F);
            data.setFloat("base", 0.3F);
            data.setFloat("max", 1F);
            data.setInteger("life", 20 + world.rand.nextInt(20));
            data.setInteger("color",tank.getTankType().getColor());

            data.setDouble("posX", pos.getX() + 0.5 + world.rand.nextDouble() - 0.5);
            data.setDouble("posZ", pos.getZ() + 0.5 + world.rand.nextDouble() -0.5);
            data.setDouble("posY", pos.getY() + 1);

            MainRegistry.proxy.effectNT(data);

        }
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            if(onTicks > 0) onTicks--;
            if(registerWait > 0) registerWait--;

            if(tank.getFill() + bonusFill >= 20) {
                onTicks = 20;

                if(registerWait > 0) {
                    // do nothing
                } else if(!registered) {
                    ChunkAtmosphereManager.proxy.registerAtmosphere(this);
                    registered = true;

                    if(airFill > 1) {
                        recovering = 100;
                    }
                } else if(recovering > 0) {
                    recovering--;
                    if(currentBlob != null) {
                        recovering = 0;
                    }
                } else {
                    if(currentBlob != null) {
                        int size = currentBlob.getBlobSize();
                        if(size != 0) {
                            if(airFill > size) airFill = size;
                            if(wasteFill > size) wasteFill = size;

                            // Fill the blob from the tank, 1mB per block
                            int toFill = Math.min(size - airFill, 20);
                            airFill += toFill;

                            // Fill to the brim, and then trickle randomly afterwards
                            if(toFill > 0) {
                                consumeFromTank(toFill);
                            } else if(rand.nextBoolean()) {
                                consumeFromTank(1);
                                wasteFill++;
                            }

                            // Fill scrubbers with waste gas
                            if(scrubber != null && scrubber.isLoaded && !scrubber.isInvalid()) {
                                int toScrub = MathHelper.clamp(wasteFill, 0, 20);
                                int scrubbed = scrubber.scrub(toScrub);
                                wasteFill -= scrubbed;
                            }
                        } else {
                            currentBlob = null;
                        }
                    }

                    if(currentBlob == null) {
                        // Venting to vacuum
                        consumeFromTank(20);
                        airFill = 0;
                        wasteFill = 0;
                    }
                }
            } else {
                if(registered) {
                    ChunkAtmosphereManager.proxy.unregisterAtmosphere(this);
                    registered = false;
                    currentBlob = null;
                    airFill = 0;
                    wasteFill = 0;
                }
            }

            if(world.getTotalWorldTime() % 5 == 0) {
                currentAtmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(world, pos.getX(), pos.getY(), pos.getZ());
            }

            subscribeToAllAround(tank.getTankType(), this);

            this.networkPackNT(100);

        } else {
            if(onTicks > 0) {
                this.spawnParticles();
            }
        }
    }

    private void consumeFromTank(int amount) {
        int bonusConsumption = MathHelper.clamp(amount, 0, bonusFill);
        bonusFill -= bonusConsumption;
        amount -= bonusConsumption;

        tank.setFill(tank.getFill() - amount);
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if(registered) {
            ChunkAtmosphereManager.proxy.unregisterAtmosphere(this);
            registered = false;
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(onTicks);
        buf.writeInt(airFill);
        buf.writeInt(wasteFill);
        buf.writeInt(bonusFill);
        tank.serialize(buf);

        if(currentAtmosphere != null) {
            buf.writeBoolean(true);
            currentAtmosphere.writeToBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        onTicks = buf.readInt();
        airFill = buf.readInt();
        wasteFill = buf.readInt();
        bonusFill = buf.readInt();
        tank.deserialize(buf);

        if(buf.readBoolean()) {
            currentAtmosphere = new CBT_Atmosphere();
            currentAtmosphere.readFromBytes(buf);
        } else {
            currentAtmosphere = null;
        }
    }

    // I WILL GET THAT ART :contempt:
    @Override
    @Optional.Method(modid = "opencomputers")
    public String getComponentName() {
        return "ntm_atmospheric_vent";
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getAtmosphereDetails(Context context, Arguments args) {
        List<Object[]> fluids = new ArrayList<>();
        if(currentAtmosphere != null && currentAtmosphere.fluids != null && currentAtmosphere.fluids.size() != 0) {
            for (CBT_Atmosphere.FluidEntry fluid : currentAtmosphere.fluids) {
                fluids.add(new Object[]{fluid.fluid.getName(), fluid.pressure});
            }
        } else {
            // If there is no atmosphere, return that it's a vacuum.
            return new Object[] {"VACUUM"};
        }
		/* the return format should look something like the following:
			{{fluid_1_name, fluid_1_pressure},
			{fluid_2_name, fluid_2_pressure}}
		 */
        return fluids.toArray();
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getFluid(Context context, Arguments args) {
        return new Object[] {tank.getFill(), tank.getMaxFill()};
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] isSealed(Context context, Arguments args) {
        return new Object[] {hasSeal()};
    }

    // alright so I'm NOT going to define the `methods()` and `invoke()` functions
    // (they are reserved for blocks with proxies)

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        tank.readFromNBT(nbt, "at");
        airFill = nbt.getInteger("fill");
        wasteFill = nbt.getInteger("waste");
        bonusFill = nbt.getInteger("bonus");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        tank.writeToNBT(nbt, "at");
        nbt.setInteger("fill", airFill);
        nbt.setInteger("waste", wasteFill);
        nbt.setInteger("bonus", bonusFill);
        return super.writeToNBT(nbt);
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return new FluidTankNTM[] {tank};
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return new FluidTankNTM[] {tank};
    }
    @Override
    public String getDefaultName() {
        return "container.atmosphereVent";
    }


    @Override
    public int getMaxBlobRadius() {
        return 256;
    }

    @Override
    public ThreeInts getRootPosition() {
        return new ThreeInts(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public FluidType getFluidType() {
        return tank.getTankType();
    }

    @Override
    public double getFluidPressure() {
        if(currentBlob == null || currentBlob.getBlobSize() == 0) return 0;

        return ((double)airFill / (double)currentBlob.getBlobSize()) * 0.2;
    }

    public boolean hasSeal() {
        return airFill > 1;
    }

    @Override
    public void onBlobCreated(AtmosphereBlob blob) {
        currentBlob = blob;
    }

    @Override
    public void consume(int amount) {
        airFill -= amount;
        wasteFill += amount;
        if(airFill < 1) airFill = 1;
    }

    @Override
    public void produce(int amount) {
        int produced = MathHelper.clamp(amount, 0, wasteFill);
        bonusFill += produced;
        wasteFill -= produced;
    }

    public boolean isRecycling() {
        return bonusFill > 0;
    }

    public boolean registerScrubber(TileEntityAirScrubber scrubber) {
        if(!this.isLoaded || this.isInvalid()) return false;
        if(tank.getTankType() != Fluids.OXYGEN) return false;
        if(this.scrubber == scrubber) return true;
        if(this.scrubber != null && this.scrubber.isLoaded && !this.scrubber.isInvalid() && this.scrubber.canOperate()) return false;
        this.scrubber = scrubber;
        return true;
    }

}
