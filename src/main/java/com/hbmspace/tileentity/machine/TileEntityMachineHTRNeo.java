package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardReceiver;
import com.hbm.api.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.main.MainRegistry;
import com.hbm.sound.AudioWrapper;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.tileentity.machine.fusion.IFusionPowerReceiver;
import com.hbm.uninos.UniNodespace;
import com.hbm.uninos.networkproviders.PlasmaNetwork;
import com.hbm.util.BobMathUtil;
import com.hbmspace.api.tile.IPropulsion;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister
public class TileEntityMachineHTRNeo extends TileEntityMachineBase
        implements ITickable, IPropulsion, IFluidStandardTransceiverMK2, IFluidStandardReceiver, IEnergyReceiverMK2, IFusionPowerReceiver{

    //i smushed these together because i need you so bad
    protected PlasmaNetwork.PlasmaNode plasmaNode;

    public long plasmaEnergy;
    public long plasmaEnergySync;
    public long power;
    private boolean hasRegistered;

    public static long maxPower = 1_000_000_000L;

    public static final int COOLANT_USE = 50;
    public static final double PLASMA_EFFICIENCY = 1.35D;
    public static long MINIMUM_PLASMA = 5_000_000L;
    private float speed;
    public double lastTime;
    public double time;
    private float soundtime;
    private AudioWrapper audio;

    public FluidTankNTM[] tanks = new FluidTankNTM[]{
            new FluidTankNTM(Fluids.PERFLUOROMETHYL_COLD, 4_000),
            new FluidTankNTM(Fluids.PERFLUOROMETHYL, 4_000)
    };



    public boolean isOn;
    public int fuelCost;

    public TileEntityMachineHTRNeo() {
        super(0, true, true);
    }

    public boolean hasMinimumPlasma() {
        return plasmaEnergy >= MINIMUM_PLASMA;
    }

    public boolean isCool() {
        return tanks[0].getFill() >= COOLANT_USE &&
                tanks[1].getFill() + COOLANT_USE <= tanks[1].getMaxFill();
    }


    @Override
    public void update() {

        if(!world.isRemote) {
            if(!hasRegistered) {
                if(isFacingPrograde()) registerPropulsion();
                hasRegistered = true;
                isOn = false;
            }
            for(DirPos pos : getConPos()) {
                this.trySubscribe(tanks[0].getTankType(), world, pos);
            }
            plasmaEnergySync = plasmaEnergy;


            if(plasmaNode == null || plasmaNode.expired) {
                ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset).getRotation(ForgeDirection.UP);
                plasmaNode = UniNodespace.getNode(world, pos.add(dir.offsetX * -10, 0, dir.offsetZ * -10), PlasmaNetwork.THE_PROVIDER);
                if(plasmaNode == null) {

                    plasmaNode = (PlasmaNetwork.PlasmaNode) new PlasmaNetwork.PlasmaNode(PlasmaNetwork.THE_PROVIDER,
                            new BlockPos(pos.getX() + dir.offsetX * -10, pos.getY() , pos.getZ() + dir.offsetZ * -10))
                            .setConnections(new DirPos(pos.getX() + dir.offsetX * -11, pos.getY() , pos.getZ() + dir.offsetZ * -11,dir));


                    UniNodespace.createNode(world, plasmaNode);
                }



            }
            if(plasmaNode != null && plasmaNode.hasValidNet()) plasmaNode.net.addReceiver(this);
            //world.setBlock(pos.getX() - rot.offsetX * 5 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * 3,ModBlocks.absorber);
            //world.setBlock(pos.getX() - rot.offsetX * -1 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * 3,ModBlocks.absorber);
            //world.setBlock(pos.getX() - rot.offsetX * 5 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * -3,ModBlocks.absorber);
            //world.setBlock(pos.getX() - rot.offsetX * -1 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * -3,ModBlocks.absorber);


            this.networkPackNT(200);
            this.plasmaEnergy = 0;
        }		 else {
            if(isOn) {
                speed += 0.05F;
                if(speed > 1) speed = 1;

                if(soundtime > 18) {
                    if(audio == null) {
                        audio = createAudioLoop();
                        audio.startSound();
                    } else if(!audio.isPlaying()) {
                        audio = rebootAudio(audio);
                    }

                    audio.updateVolume(getVolume(1F));
                    audio.keepAlive();

                    ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset).getRotation(ForgeDirection.UP);

                    NBTTagCompound data = new NBTTagCompound();
                    data.setDouble("posX", pos.getX() + dir.offsetX * 12);
                    data.setDouble("posY", pos.getY() + 1);
                    data.setDouble("posZ", pos.getZ() + dir.offsetZ * 12);
                    data.setString("type", tanks[0].getTankType() == Fluids.PLASMA_BF ? "missileContrailbf" :"missileContrailf");
                    data.setFloat("scale", 3);
                    data.setDouble("moX", dir.offsetX * 10);
                    data.setDouble("moY", 0);
                    data.setDouble("moZ", dir.offsetZ * 10);
                    data.setInteger("maxAge", 40 + world.rand.nextInt(40));
                    MainRegistry.proxy.effectNT(data);
                }
            } else {
                speed -= 0.05F;
                if(speed < 0) speed = 0;

                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }
            }
        }

        lastTime = time;
        time += speed;
    }





    @Override
    public void onChunkUnload() {
        super.onChunkUnload();

        if(hasRegistered) {
            unregisterPropulsion();
            hasRegistered = false;
        }

        //if(audio != null) {
        //audio.stopSound();
        //audio = null;
        //}
    }
    @Override
    public void invalidate() {
        super.invalidate();

        if(hasRegistered) {
            unregisterPropulsion();
            hasRegistered = false;
        }
        if(!world.isRemote) {
            if(this.plasmaNode != null) UniNodespace.destroyNode(world, pos, PlasmaNetwork.THE_PROVIDER);
        }
    }
    @Override
    public boolean receivesFusionPower() {
        return true;
    }

    @Override
    public void receiveFusionPower(long fusionPower, double neutronPower) {
        plasmaEnergy = fusionPower;

    }

    private DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);
        //world.setBlock(pos.getX() - rot.offsetX * 5 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * 3,ModBlocks.absorber);
        //world.setBlock(pos.getX() - rot.offsetX * -1 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * 3,ModBlocks.absorber);
        //world.setBlock(pos.getX() - rot.offsetX * 5 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * -3,ModBlocks.absorber);
        //world.setBlock(pos.getX() - rot.offsetX * -1 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 0 - dir.offsetZ * -3,ModBlocks.absorber);


        return new DirPos[] {
                new DirPos(pos.getX() - rot.offsetX * 5 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - dir.offsetZ * 3, rot),
                new DirPos(pos.getX() - rot.offsetX * -1 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - dir.offsetZ * 3, rot),
                new DirPos(pos.getX() - rot.offsetX * 5 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - dir.offsetZ * -3, rot),
                new DirPos(pos.getX() - rot.offsetX * -1 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - dir.offsetZ * -3, rot), //this is ass but since it only faces one direction its "excusable"

        };
    }

    @Override
    public boolean canPerformBurn(int shipMass, double deltaV) {


        fuelCost = SolarSystem.getFuelCost(deltaV, shipMass, 100); //static temporary lolegaloge

        if(plasmaEnergySync < fuelCost) {
            System.out.println("false");
            System.out.println(plasmaEnergySync);
            System.out.println(fuelCost);

            return false;
        }

        return true;
    }

    @Override
    public void addErrors(List<String> errors) {

        if(plasmaEnergySync < fuelCost) {
            errors.add(TextFormatting.RED + "Insufficient power: needs " + BobMathUtil.getShortNumber(fuelCost) + " HE");
        }

        if(!isCool()) {
            errors.add(TextFormatting.RED + "Coolant loop not operational!");
        }
    }

    @Override
    public float getThrust() {
        return 1_600_000_000.0F;
    }

    @Override
    public int startBurn() {
        isOn = true;
        plasmaEnergy -= fuelCost;
        if(isCool()) {
            tanks[0].setFill(-COOLANT_USE);
            tanks[1].setFill(+COOLANT_USE);
        }

        return 180;
    }

    @Override
    public int endBurn() {
        isOn = false;
        return 180;
    }



    @Override public FluidTankNTM[] getAllTanks() { return tanks; }
    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[]{ tanks[0] }; }
    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[]{ tanks[1] }; }

    @Override public long getPower() { return power; }
    @Override public void setPower(long p) { power = p; }
    @Override public long getMaxPower() { return maxPower; }

    /* -------------------------
     *   NBT / Sync
     * ------------------------- */

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(plasmaEnergySync);
        buf.writeBoolean(isOn);
        buf.writeInt(fuelCost);
        tanks[0].serialize(buf);
        tanks[1].serialize(buf);

    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        plasmaEnergy = buf.readLong();
        isOn = buf.readBoolean();
        fuelCost = buf.readInt();
        tanks[0].deserialize(buf);
        tanks[1].deserialize(buf);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("plasma", plasmaEnergy);
        nbt.setBoolean("on", isOn);

        tanks[0].writeToNBT(nbt, "t0");
        tanks[1].writeToNBT(nbt, "t1");
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        plasmaEnergy = nbt.getLong("plasma");
        isOn = nbt.getBoolean("on");

        tanks[0].readFromNBT(nbt, "t0");
        tanks[1].readFromNBT(nbt, "t1");
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) bb = new AxisAlignedBB(pos.getX() - 11, pos.getY() - 2, pos.getZ() - 11, pos.getX() + 12, pos.getY() + 3, pos.getZ() + 12);
        return bb;
    }

    @Override
    public String getDefaultName() {
        return "container.htrfneo";
    }

    public boolean isFacingPrograde() {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset) == ForgeDirection.SOUTH;
    }

}
