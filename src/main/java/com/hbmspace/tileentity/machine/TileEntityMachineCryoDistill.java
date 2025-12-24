package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.api.fluid.IFluidStandardTransceiver;
import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.Library;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import com.hbm.tileentity.IPersistentNBT;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.Tuple;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerMachineCryoDistill;
import com.hbmspace.inventory.gui.GUIMachineCryoDistill;
import com.hbmspace.inventory.recipes.CryoRecipes;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityMachineCryoDistill extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2, IFluidStandardTransceiver, IPersistentNBT, ISpaceGuiProvider{

    public long power;
    public static final long maxPower = 1_000_000;

    public FluidTankNTM[] tanks;

    public TileEntityMachineCryoDistill() {
        super(11, true, true);

        this.tanks = new FluidTankNTM[5];
        this.tanks[0] = new FluidTankNTM(com.hbmspace.inventory.fluid.Fluids.EARTHAIR, 64_000);
        this.tanks[1] = new FluidTankNTM(Fluids.NITROGEN, 24_000);
        this.tanks[2] = new FluidTankNTM(Fluids.OXYGEN, 24_000);
        this.tanks[3] = new FluidTankNTM(Fluids.KRYPTON, 24_000);
        this.tanks[4] = new FluidTankNTM(Fluids.CARBONDIOXIDE, 24_000);
    }

    @Override
    public String getDefaultName() {
        return "container.cryoDistillator";
    }

    @Override
    public void update() {

        if(!world.isRemote) {
            power = Library.chargeTEFromItems(inventory, 0, power, maxPower);
            tanks[0].setType(7, inventory);

            DirPos[] con = getConPos();

            // Subscribe to powernet
            trySubscribe(world, con[5].getPos().getX(), con[5].getPos().getY(), con[5].getPos().getZ(), con[5].getDir());

            // Subscribe input tank
            trySubscribe(tanks[0].getTankType(), world, con[0].getPos().getX(), con[0].getPos().getY(), con[0].getPos().getZ(), con[0].getDir());

            distill();

            tanks[1].unloadTank(1, 2, inventory);
            tanks[2].unloadTank(3, 4, inventory);
            tanks[3].unloadTank(5, 6, inventory);
            tanks[4].unloadTank(8, 9, inventory);

            for(int i = 1; i < 5; i++) {
                for(int o = 1; o < 5; o++) {
                    if(tanks[i].getFill() > 0) {
                        this.sendFluid(tanks[i], world, con[o].getPos().getX(), con[o].getPos().getY(), con[o].getPos().getZ(), con[o].getDir());
                    }
                }
            }

            networkPackNT(15);
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeLong(power);
        for(int i = 0; i < 5; i++) tanks[i].serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        power = buf.readLong();
        for(int i = 0; i < 5; i++) tanks[i].deserialize(buf);
    }

    private void distill() {

        Tuple.Quartet<FluidStack, FluidStack, FluidStack, FluidStack> out = CryoRecipes.getOutput(tanks[0].getTankType());
        if(out == null) {
            tanks[1].setTankType(Fluids.NONE);
            tanks[2].setTankType(Fluids.NONE);
            tanks[3].setTankType(Fluids.NONE);
            tanks[4].setTankType(Fluids.NONE);
            return;
        }

        tanks[1].setTankType(out.getW().type);
        tanks[2].setTankType(out.getX().type);
        tanks[3].setTankType(out.getY().type);
        tanks[4].setTankType(out.getZ().type);

        if(power < 20_000) return;
        if(tanks[0].getFill() < 100) return;

        if(tanks[1].getFill() + out.getW().fill > tanks[1].getMaxFill()) return;
        if(tanks[2].getFill() + out.getX().fill > tanks[2].getMaxFill()) return;
        if(tanks[3].getFill() + out.getY().fill > tanks[3].getMaxFill()) return;
        if(tanks[4].getFill() + out.getZ().fill > tanks[4].getMaxFill()) return;


        tanks[0].setFill(tanks[0].getFill() - 100);
        tanks[1].setFill(tanks[1].getFill() + out.getW().fill);
        tanks[2].setFill(tanks[2].getFill() + out.getX().fill);
        tanks[3].setFill(tanks[3].getFill() + out.getY().fill);
        tanks[4].setFill(tanks[4].getFill() + out.getZ().fill);


        power -= 20_000;
    }

    public DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        return new DirPos[] {
                // Input
                new DirPos(pos.getX() + dir.offsetX * -1 + rot.offsetX * -3, pos.getY() - 2, pos.getZ() + dir.offsetZ * -1 + rot.offsetZ * -3, rot.getOpposite()),

                // Outputs
                new DirPos(pos.getX() + dir.offsetX * 4 + rot.offsetX * -2, pos.getY() - 2, pos.getZ() + dir.offsetZ * 4 + rot.offsetZ * -2, dir),
                new DirPos(pos.getX() + dir.offsetX * 4 + rot.offsetX * -1, pos.getY() - 2, pos.getZ() + dir.offsetZ * 4 + rot.offsetZ * -1, dir),
                new DirPos(pos.getX() + dir.offsetX * 4 + rot.offsetX * 1, pos.getY() - 2, pos.getZ() + dir.offsetZ * 4 + rot.offsetZ * 1, dir),
                new DirPos(pos.getX() + dir.offsetX * 4 + rot.offsetX * 2, pos.getY() - 2, pos.getZ() + dir.offsetZ * 4 + rot.offsetZ * 2, dir),

                // Power
                new DirPos(pos.getX() + dir.offsetX * -2 + rot.offsetX * -3, pos.getY() - 2, pos.getZ() + dir.offsetZ * -2 + rot.offsetZ * -3, rot.getOpposite()),
        };
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        power = nbt.getLong("power");
        tanks[0].readFromNBT(nbt, "input");
        tanks[1].readFromNBT(nbt, "o1");
        tanks[2].readFromNBT(nbt, "o2");
        tanks[3].readFromNBT(nbt, "o3");
        tanks[4].readFromNBT(nbt, "o4");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setLong("power", power);
        tanks[0].writeToNBT(nbt, "input");
        tanks[1].writeToNBT(nbt, "o1");
        tanks[2].writeToNBT(nbt, "o2");
        tanks[3].writeToNBT(nbt, "o3");
        tanks[4].writeToNBT(nbt, "o4");
        return super.writeToNBT(nbt);
    }

    AxisAlignedBB bb = null;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 4,
                    pos.getY() - 2,
                    pos.getZ() - 4,
                    pos.getX() + 6,
                    pos.getY() + 5,
                    pos.getZ() + 4
            );
        }

        return bb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
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
        return maxPower;
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return tanks;
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return new FluidTankNTM[] {tanks[1], tanks[2], tanks[3], tanks[4]};
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return new FluidTankNTM[] {tanks[0]};
    }

    @Override
    public boolean canConnect(ForgeDirection dir) {
        return dir != ForgeDirection.UNKNOWN && dir != ForgeDirection.DOWN;
    }

    @Override
    public boolean canConnect(FluidType type, ForgeDirection dir) {
        return dir != ForgeDirection.UNKNOWN && dir != ForgeDirection.DOWN;
    }

    @Override
    public void writeNBT(NBTTagCompound nbt) {
        if(tanks[0].getFill() == 0 && tanks[1].getFill() == 0 && tanks[2].getFill() == 0 && tanks[3].getFill() == 0 && tanks[4].getFill() == 0) return;
        NBTTagCompound data = new NBTTagCompound();
        for(int i = 0; i < 5; i++) this.tanks[i].writeToNBT(data, "" + i);
        nbt.setTag(NBT_PERSISTENT_KEY, data);
    }

    @Override
    public void readNBT(NBTTagCompound nbt) {
        NBTTagCompound data = nbt.getCompoundTag(NBT_PERSISTENT_KEY);
        for(int i = 0; i < 5; i++) this.tanks[i].readFromNBT(data, "" + i);
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerMachineCryoDistill(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIMachineCryoDistill(player.inventory, this);
    }
}
