package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardTransceiver;
import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.BobMathUtil;
import com.hbm.util.CrucibleUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.materials.MatsSpace;
import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class TileEntityMachineMagma extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2, IFluidStandardTransceiver {

    public boolean operating;

    public long power;
    public long consumption = 10_000;

    // TODO: probably to handle cooling fluids, remove me if we don't do that
    public FluidTankNTM[] tanks;

    public static final int maxLiquid = MaterialShapes.BLOCK.q(16);
    public List<Mats.MaterialStack> liquids = new ArrayList<>();

    public float drillSpeed;
    public float drillRotation;
    public float prevDrillRotation;

    public float lavaHeight;
    public float prevLavaHeight;

    public boolean validPosition = true;

    protected Mats.MaterialStack[] defaultOutputs = new Mats.MaterialStack[] {
            new Mats.MaterialStack(Mats.MAT_SLAG, MaterialShapes.INGOT.q(1)),
            new Mats.MaterialStack(MatsSpace.MAT_RICH_MAGMA, MaterialShapes.QUANTUM.q(4)),
    };

    public TileEntityMachineMagma() {
        super(0, true, true);
        tanks = new FluidTankNTM[0];
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            for(DirPos pos : getConPos()) {
                trySubscribe(world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            }

            // baby drill, baby
            operating = canOperate();
            if(operating) {
                power -= consumption;

                int timeBetweenOutputs = 10;

                if(world.getTotalWorldTime() % timeBetweenOutputs == 0) {
                    for(Mats.MaterialStack mat : getOutputs()) {
                        int totalLiquid = 0;
                        for(Mats.MaterialStack m : liquids) totalLiquid += m.amount;

                        int toAdd = mat.amount;

                        if(totalLiquid + toAdd <= maxLiquid) {
                            addToStack(mat);
                        } else {
                            break;
                        }
                    }
                }
            }

            // pour me a drink, barkeep
            if(!liquids.isEmpty()) {
                ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - 10);

                Vec3d impact = new Vec3d(0, 0, 0);
                Mats.MaterialStack didPour = CrucibleUtil.pourFullStack(world, pos.getX() + 0.5D + dir.offsetX * 3.875D, pos.getY() + 1.25D, pos.getZ() + 0.5D + dir.offsetZ * 3.875D, 6, true, liquids, MaterialShapes.INGOT.q(1), impact);

                if(didPour != null) {
                    NBTTagCompound data = new NBTTagCompound();
                    data.setString("type", "foundry");
                    data.setInteger("color", didPour.material.moltenColor);
                    data.setByte("dir", (byte) dir.ordinal());
                    data.setFloat("off", 0.625F);
                    data.setFloat("base", 0.625F);
                    data.setFloat("len", Math.max(1F, pos.getY() + 1 - (float) (Math.ceil(impact.y) - 0.875)));
                    PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(data, pos.getX() + 0.5D + dir.offsetX * 3.875D, pos.getY() + 1, pos.getZ() + 0.5D + dir.offsetZ * 3.875D), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 50));
                }
            }

            liquids.removeIf(o -> o.amount <= 0);

            networkPackNT(250);
        } else {
            prevLavaHeight = lavaHeight;
            prevDrillRotation = drillRotation;

            if(operating) {
                drillSpeed += 0.15F;
                if(drillSpeed > 15F) drillSpeed = 15F;

                lavaHeight += (float) ((world.rand.nextFloat() - 0.5) * 0.01);
                lavaHeight = (float) BobMathUtil.lerp(0.02D, lavaHeight, 0.9D);
            } else {
                drillSpeed -= 0.3F;
                if(drillSpeed < 0F) drillSpeed = 0F;

                lavaHeight = (float)BobMathUtil.lerp(0.02D, lavaHeight, 0D);
            }

            drillRotation += drillSpeed;

            if(drillRotation > 360F) {
                drillRotation -= 360F;
                prevDrillRotation -= 360F;
            }
        }
    }

    private DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);
        return new DirPos[] {
                new DirPos(pos.getX() - dir.offsetX * 4, pos.getY() - 1, pos.getZ() - dir.offsetZ * 4, dir),
                new DirPos(pos.getX() - dir.offsetX * 4, pos.getY() - 2, pos.getZ() - dir.offsetZ * 4, dir),
                new DirPos(pos.getX() - dir.offsetX * 4 + rot.offsetX, pos.getY() - 1, pos.getZ() - dir.offsetZ * 4 + rot.offsetZ, dir),
                new DirPos(pos.getX() - dir.offsetX * 4 + rot.offsetX, pos.getY() - 2, pos.getZ() - dir.offsetZ * 4 + rot.offsetZ, dir),
                new DirPos(pos.getX() - dir.offsetX * 4 - rot.offsetX, pos.getY() - 1, pos.getZ() - dir.offsetZ * 4 - rot.offsetZ, dir),
                new DirPos(pos.getX() - dir.offsetX * 4 - rot.offsetX, pos.getY() - 2, pos.getZ() - dir.offsetZ * 4 - rot.offsetZ, dir),
        };
    }

    private boolean canOperate() {
        // Currently only functions on Moho, so the simplest solution is acceptable
        CelestialBody body = CelestialBody.getBody(world);
        if(!body.name.equals("moho")) return false;

        validPosition = isValidPosition();
        if(!validPosition) return false;

        return power >= consumption;
    }

    private boolean isValidPosition() {
        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                if(world.getBlockState(pos.add(x, -4, z)).getBlock() != Blocks.LAVA) return false;
            }
        }

        return true;
    }

    // Returns materials produced at this location, varied by perlin noise
    private Mats.MaterialStack[] getOutputs() {
        return defaultOutputs;
    }

    private void addToStack(Mats.MaterialStack matStack) {
        for(Mats.MaterialStack mat : liquids) {
            if(mat.material == matStack.material) {
                mat.amount += matStack.amount;
                return;
            }
        }

        liquids.add(matStack.copy());
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeBoolean(operating);
        buf.writeLong(power);
        buf.writeBoolean(validPosition);

        for (FluidTankNTM tank : tanks) tank.serialize(buf);

        buf.writeShort(liquids.size());
        for(Mats.MaterialStack sta : liquids) {
            buf.writeInt(sta.material.id);
            buf.writeInt(sta.amount);
        }
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        operating = buf.readBoolean();
        power = buf.readLong();
        validPosition = buf.readBoolean();

        for (FluidTankNTM tank : tanks) tank.deserialize(buf);

        liquids.clear();
        int mats = buf.readShort();
        for(int i = 0; i < mats; i++) {
            liquids.add(new Mats.MaterialStack(Mats.matById.get(buf.readInt()), buf.readInt()));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        power = nbt.getLong("power");

        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(nbt, "t" + i);

        int[] liquidData = nbt.getIntArray("liquids");
        for(int i = 0; i < liquidData.length / 2; i++) {
            NTMMaterial mat = Mats.matById.get(liquidData[i * 2]);
            if(mat == null) continue;
            liquids.add(new Mats.MaterialStack(mat, liquidData[i * 2 + 1]));
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("power", power);

        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(nbt, "t" + i);

        int[] liquidData = new int[liquids.size() * 2];
        for(int i = 0; i < liquids.size(); i++) { Mats.MaterialStack sta = liquids.get(i); liquidData[i * 2] = sta.material.id; liquidData[i * 2 + 1] = sta.amount; }
        nbt.setIntArray("liquids", liquidData);
        return super.writeToNBT(nbt);
    }

    @Override
    public long getPower() {
        return power;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return 1_000_000;
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return tanks;
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return tanks;
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return tanks;
    }

    @Override
    public String getDefaultName() {
        return "container.machineMagma";
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 4,
                    pos.getY() - 3,
                    pos.getZ() - 4,
                    pos.getX() + 5,
                    pos.getY() + 3,
                    pos.getZ() + 5
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
