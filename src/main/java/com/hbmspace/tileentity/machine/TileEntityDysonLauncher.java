package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.blocks.BlockDummyable;
import com.hbm.handler.threading.PacketThreading;
import com.hbm.items.ISatChip;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.dim.trait.CBT_Dyson;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityDysonLauncher extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2 {

    public int swarmId;
    public int swarmCount;

    public long power;
    public static final long MAX_POWER = 20_000_000;

    private static final int MEMBERS_PER_LAUNCH = 4;

    // SHAKE IT LIKE IT'S HEAT, OVERDRIVE
    boolean sunsetOverdrive = false;

    public boolean isOperating;
    public boolean isSpinningDown;
    public int operatingTime;

    public float rotation;
    public float lastRotation;
    public float speed;

    public int payloadTicks;

    public int satCount;

    public TileEntityDysonLauncher() {
        super(2, 4, false, true);
    }

    @Override
    public String getDefaultName() {
        return "container.machineDysonLauncher";
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            for(DirPos pos : getConPos()) trySubscribe(world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            for(DirPos pos : getInvPos()) tryLoad(pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());

            swarmId = ISatChip.getFreqS(inventory.getStackInSlot(1));
            swarmCount = CBT_Dyson.count(world, swarmId);

            isOperating = !isSpinningDown && power >= getPowerPerTick() && !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ModItemsSpace.swarm_member && swarmId > 0;

            if(isSpinningDown) {
                operatingTime++;

                if(operatingTime > getSpinDownTime()) {
                    isSpinningDown = false;
                    operatingTime = 0;
                }
            } else if(isOperating) {
                if(operatingTime == 0) {
                    float pitch = sunsetOverdrive ? 1.0F : 0.25F;
                    world.playSound(null, pos.getX(), pos.getY() + 8, pos.getZ(), HBMSpaceSoundHandler.spinCharge, SoundCategory.BLOCKS, 1.5F, pitch);
                }

                operatingTime++;
                power -= getPowerPerTick();

                if(operatingTime > getSpinUpTime()) {
                    int toLaunch = Math.min(inventory.getStackInSlot(0).getCount(), MEMBERS_PER_LAUNCH);
                    CBT_Dyson.launch(world, swarmId, toLaunch);

                    CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
                    double pressure = atmosphere != null ? atmosphere.getPressure() : 0;
                    double scaledPressure = 1.0 - Math.pow(1.0 - pressure, 3);

                    float volume = Math.min((float)scaledPressure * 16.0F, 4.0F);

                    ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
                    ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

                    world.playSound(null, pos.getX() + rot.offsetX * 6, pos.getY() + 8, pos.getZ() + rot.offsetZ * 6, HBMSpaceSoundHandler.spinShot, SoundCategory.BLOCKS, volume, 0.9F + world.rand.nextFloat() * 0.3F);
                    world.playSound(null, pos.getX() + rot.offsetX * 6, pos.getY() + 8, pos.getZ() + rot.offsetZ * 6, HBMSpaceSoundHandler.spinShot, SoundCategory.BLOCKS, volume, 1F + world.rand.nextFloat() * 0.3F);

                    int count = Math.min(20, (int)(pressure * 80));

                    double posX = pos.getX() + rot.offsetX * 9;
                    double posY = pos.getY() + 12;
                    double posZ = pos.getZ() + rot.offsetZ * 9;

                    NBTTagCompound data = new NBTTagCompound();
                    data.setInteger("count", count);
                    data.setString("type", "spinlaunch");
                    data.setFloat("scale", 3);
                    data.setDouble("moX", dir.offsetX * 10);
                    data.setDouble("moY", 10);
                    data.setDouble("moZ", dir.offsetZ * 10);
                    data.setInteger("maxAge", 10 + count / 2 + world.rand.nextInt(5));
                    PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(data, posX, posY, posZ), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 150));
                    ItemStack stack = inventory.getStackInSlot(0);
                    stack.shrink(toLaunch);
                    if(stack.getCount() <= 0) inventory.setStackInSlot(0, ItemStack.EMPTY);
                    else inventory.setStackInSlot(0, stack);

                    operatingTime = 0;
                    isSpinningDown = true;
                }
            } else {
                operatingTime = 0;
            }

            networkPackNT(250);
        } else {
            float acceleration = sunsetOverdrive ? 2.5F : 0.75F;
            float deceleration = sunsetOverdrive ? 15.0F : 3.0F;
            float resetSpeed = sunsetOverdrive ? 30.0F : 8.0F;

            if(isOperating) {
                speed += acceleration;
                if(speed > 90) speed = 90;
            } else if(speed > 0.1F) {
                speed -= deceleration;
                if(speed < resetSpeed) speed = resetSpeed;
            }

            lastRotation = rotation;
            if(!isOperating && speed <= resetSpeed && rotation > 360 - resetSpeed * 1.5) {
                lastRotation -= 360;
                rotation = 0;
                speed = 0;
            } else {
                rotation += speed;
            }

            if(rotation >= 360) {
                rotation -= 360;
                lastRotation -= 360;
            }

            if(isSpinningDown) {
                payloadTicks++;
            } else {
                payloadTicks = 0;
            }
        }
    }

    private int getSpinUpTime() { return sunsetOverdrive ? 38 : 132; }
    private int getSpinDownTime() { return sunsetOverdrive ? 12 : 68; }
    private long getPowerPerTick() { return MAX_POWER / getSpinUpTime(); }

    public DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        return new DirPos[] {
                new DirPos(pos.getX() - rot.offsetX * 3, pos.getY(), pos.getZ() - rot.offsetZ * 3, rot.getOpposite()),
                new DirPos(pos.getX() - dir.offsetX - rot.offsetX * 3, pos.getY(), pos.getZ() - dir.offsetZ - rot.offsetZ * 3, rot.getOpposite()),
                new DirPos(pos.getX() - dir.offsetX * 2 - rot.offsetX * 3, pos.getY(), pos.getZ() - dir.offsetZ * 2 - rot.offsetZ * 3, rot.getOpposite()),
                new DirPos(pos.getX() - dir.offsetX * 3 - rot.offsetX * 3, pos.getY(), pos.getZ() - dir.offsetZ * 3 - rot.offsetZ * 3, rot.getOpposite()),
                new DirPos(pos.getX() - dir.offsetX * 4 - rot.offsetX * 3, pos.getY(), pos.getZ() - dir.offsetZ * 4 - rot.offsetZ * 3, rot.getOpposite()),
        };
    }

    public DirPos[] getInvPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);

        return new DirPos[] {
                new DirPos(pos.getX() - dir.offsetX * 9, pos.getY(), pos.getZ() - dir.offsetZ * 9, dir.getOpposite()),
        };
    }

    private void tryLoad(int x, int y, int z, ForgeDirection dir) {
        if (!inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getCount() >= MEMBERS_PER_LAUNCH) return;

        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te == null) return;

        EnumFacing facing = EnumFacing.VALUES[dir.ordinal()];
        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        if (handler == null) return;

        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (!stack.isEmpty() && stack.getItem() == ModItemsSpace.swarm_member) {
                ItemStack extracted = handler.extractItem(slot, 1, false);
                if (!extracted.isEmpty()) {
                    ItemStack cur = inventory.getStackInSlot(0);
                    if (cur.isEmpty()) {
                        inventory.setStackInSlot(0, extracted);
                    } else {
                        cur.grow(1);
                        inventory.setStackInSlot(0, cur);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(swarmId);
        buf.writeLong(power);
        buf.writeBoolean(isOperating);
        buf.writeBoolean(isSpinningDown);
        buf.writeInt(swarmCount);
        buf.writeBoolean(sunsetOverdrive);
        buf.writeInt(!inventory.getStackInSlot(0).isEmpty() ? inventory.getStackInSlot(0).getCount() : 0);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        swarmId = buf.readInt();
        power = buf.readLong();
        isOperating = buf.readBoolean();
        isSpinningDown = buf.readBoolean();
        swarmCount = buf.readInt();
        sunsetOverdrive = buf.readBoolean();
        satCount = buf.readInt();
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("power", power);
        nbt.setBoolean("spinDown", isSpinningDown);
        nbt.setInteger("time", operatingTime);
        nbt.setBoolean("overdrive", sunsetOverdrive);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        power = nbt.getLong("power");
        isSpinningDown = nbt.getBoolean("spinDown");
        operatingTime = nbt.getInteger("time");
        sunsetOverdrive = nbt.getBoolean("overdrive");
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == 0) return itemStack.getItem() == ModItemsSpace.swarm_member;
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(EnumFacing e) {
        return new int[] {0};
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return MAX_POWER; }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 100,
                    pos.getY(),
                    pos.getZ() - 100,
                    pos.getX() + 100,
                    pos.getY() + 100,
                    pos.getZ() + 100
            );
        }

        return bb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

}
