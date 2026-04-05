package com.hbmspace.tileentity.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.main.MainRegistry;
import com.hbm.sound.AudioWrapper;
import com.hbm.tileentity.machine.albion.TileEntityCooledBase;
import com.hbm.tileentity.machine.fusion.IFusionPowerReceiver;
import com.hbm.uninos.UniNodespace;
import com.hbm.uninos.networkproviders.PlasmaNetwork;
import com.hbm.util.BobMathUtil;
import com.hbmspace.api.tile.IPropulsion;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.handler.atmosphere.IBlockSealable;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister
public class TileEntityMachineHTRNeo extends TileEntityCooledBase implements ITickable, IPropulsion, IFusionPowerReceiver, IBlockSealable {

    //i smushed these together because i need you so bad
    protected PlasmaNetwork.PlasmaNode plasmaNode;

    public long plasmaEnergy;
    public long plasmaEnergySync;

    public static long maxPower = 200_000_000L;

    public float rotor;
    public float prevRotor;
    public float rotorSpeed;
    private float soundtime;
    private AudioWrapper audio;

    private boolean hasRegistered;
    public boolean isOn;
    public int fuelCost;
    public float thrustAmount;

    public float plasmaR;
    public float plasmaG;
    public float plasmaB;

    public TileEntityMachineHTRNeo() {
        super(0);
    }

    @Override
    public void update() {

        if(!world.isRemote) {
            if(!hasRegistered) {
                if(isFacingPrograde()) registerPropulsion();
                hasRegistered = true;
                isOn = false;
            }
            for(DirPos pos : this.getConPos()) {
                this.trySubscribe(world, pos);
                this.trySubscribe(coolantTanks[0].getTankType(), world, pos);
                this.tryProvide(coolantTanks[1], world, pos);
            }

            plasmaEnergySync = plasmaEnergy;

            this.temperature += temp_passive_heating;
            if(this.temperature > KELVIN + 20) this.temperature = KELVIN + 20;

            if(this.temperature > temperature_target) {
                int cyclesTemp = (int) Math.ceil((Math.min(this.temperature - temperature_target, temp_change_max)) / temp_change_per_mb);
                int cyclesCool = coolantTanks[0].getFill();
                int cyclesHot = coolantTanks[1].getMaxFill() - coolantTanks[1].getFill();
                int cycles = BobMathUtil.min(cyclesTemp, cyclesCool, cyclesHot);

                coolantTanks[0].setFill(coolantTanks[0].getFill() - cycles);
                coolantTanks[1].setFill(coolantTanks[1].getFill() + cycles);
                this.temperature -= temp_change_per_mb * cycles;
            }

            if(isOn) {
                soundtime++;

                if(soundtime == 1) {
                    this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), HBMSpaceSoundHandler.htrfstart, SoundCategory.BLOCKS, 1.5F, 1F);
                } else if(soundtime > 30) {
                    soundtime = 30;
                }
            } else {
                soundtime--;

                if(soundtime == 29) {
                    this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), HBMSoundHandler.htrstop, SoundCategory.BLOCKS, 2.0F, 1F);
                } else if(soundtime <= 0) {
                    soundtime = 0;
                }
            }


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

            this.networkPackNT(200);
            this.plasmaEnergy = 0;
        } else {
            if(power >= maxPower || isOn) this.rotorSpeed += 0.125F;
            else this.rotorSpeed -= 0.125F;

            this.rotorSpeed = MathHelper.clamp(this.rotorSpeed, 0F, 15F);

            this.prevRotor = this.rotor;
            this.rotor += this.rotorSpeed;

            if(this.rotor >= 360F) {
                this.rotor -= 360F;
                this.prevRotor -= 360F;
            }
            if(isOn) {
                if(soundtime > 28) {
                    if(audio == null) {
                        audio = createAudioLoop();
                        audio.startSound();
                    } else if(!audio.isPlaying()) {
                        audio = rebootAudio(audio);
                    }

                    audio.updateVolume(getVolume(1F));
                    audio.keepAlive();
                }

                thrustAmount += 0.01F;
                if(thrustAmount > 1) thrustAmount = 1;
            } else {
                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }

                thrustAmount -= 0.01F;
                if(thrustAmount < 0) thrustAmount = 0;
            }
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return MainRegistry.proxy.getLoopedSound(HBMSoundHandler.htrloop, SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 0.25F, 27.5F, 1.0F, 20);
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();

        if(hasRegistered) {
            unregisterPropulsion();
            hasRegistered = false;
        }
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
    public void receiveFusionPower(long fusionPower, double neutronPower, float r, float g, float b) {
        plasmaEnergy = fusionPower;
        // genuinely useful method refactor
        plasmaR = r;
        plasmaG = g;
        plasmaB = b;
    }

    @Override
    public DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        return new DirPos[] {
                new DirPos(pos.getX() - rot.offsetX * 5 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 5 - dir.offsetZ * 3, dir.getOpposite()),
                new DirPos(pos.getX() - rot.offsetX * -1 - dir.offsetX * 3, pos.getY() - 2, pos.getZ() - rot.offsetZ * -1 - dir.offsetZ * 3, dir.getOpposite()),
                new DirPos(pos.getX() - rot.offsetX * 5 - dir.offsetX * -3, pos.getY() - 2, pos.getZ() - rot.offsetZ * 5 - dir.offsetZ * -3, dir),
                new DirPos(pos.getX() - rot.offsetX * -1 - dir.offsetX * -3, pos.getY() - 2, pos.getZ() - rot.offsetZ * -1 - dir.offsetZ * -3, dir),

        };
    }

    @Override
    public boolean canPerformBurn(int shipMass, double deltaV) {
        fuelCost = SolarSystem.getFuelCost(deltaV, shipMass, 250); // i think this engine *itself* would have a base ISP..?

        if(plasmaEnergySync < fuelCost) return false;
        if(power < maxPower) return false;
        return isCool();
    }

    @Override
    public void addErrors(List<String> errors) {
        if(plasmaEnergySync < fuelCost) {
            errors.add(TextFormatting.RED + "Insufficient plasma energy: needs " + BobMathUtil.getShortNumber(fuelCost) + " TU");
        }

        if(power < maxPower) {
            errors.add(TextFormatting.RED + "Insufficient power");
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
        power = 0;

        return 180;
    }

    @Override
    public int endBurn() {
        isOn = false;
        return 180;
    }

    @Override
    public long getMaxPower() {
        return maxPower;
    }

    /*
     * -------------------------
     * NBT / Sync
     * -------------------------
     */

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeLong(plasmaEnergySync);
        buf.writeBoolean(isOn);
        buf.writeInt(fuelCost);
        buf.writeFloat(soundtime);

        buf.writeFloat(plasmaR);
        buf.writeFloat(plasmaG);
        buf.writeFloat(plasmaB);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        plasmaEnergy = buf.readLong();
        isOn = buf.readBoolean();
        fuelCost = buf.readInt();
        soundtime = buf.readFloat();

        plasmaR = buf.readFloat();
        plasmaG = buf.readFloat();
        plasmaB = buf.readFloat();
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("plasma", plasmaEnergy);
        nbt.setBoolean("on", isOn);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        plasmaEnergy = nbt.getLong("plasma");
        isOn = nbt.getBoolean("on");
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null)
            bb = new AxisAlignedBB(
                    pos.getX() - 11,
                    pos.getY() - 2,
                    pos.getZ() - 11,
                    pos.getX() + 12,
                    pos.getY() + 3,
                    pos.getZ() + 12
            );

        return bb;
    }

    @Override
    public String getDefaultName() {
        return "container.htrfneo";
    }

    public boolean isFacingPrograde() {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset) == ForgeDirection.SOUTH;
    }

    @Override
    public boolean isSealed(World world, int x, int y, int z) {
        return true;
    }

}
