package com.hbmspace.tileentity.machine;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.IConfigurableMachine;
import com.hbm.tileentity.machine.TileEntityCondenser;
import com.hbmspace.interfaces.AutoRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@AutoRegister
public class TileEntityRadiator extends TileEntityCondenser {

    public static int inputTankSize = 500;
    public static int outputTankSize = 500;

    public TileEntityRadiator() {
        tanks = new FluidTankNTM[2];
        tanks[0] = new FluidTankNTM(Fluids.SPENTSTEAM, inputTankSize);
        tanks[1] = new FluidTankNTM(Fluids.WATER, outputTankSize);
        // TODO
        //vacuumOptimised = true;
    }

    @Override
    public String getConfigName() {
        return "radiator";
    }

    @Override
    public void readIfPresent(JsonObject obj) {
        inputTankSize = IConfigurableMachine.grab(obj, "I:inputTankSize", inputTankSize);
        outputTankSize = IConfigurableMachine.grab(obj, "I:outputTankSize", outputTankSize);
    }

    @Override
    public void writeConfig(JsonWriter writer) throws IOException {
        writer.name("I:inputTankSize").value(inputTankSize);
        writer.name("I:outputTankSize").value(outputTankSize);
    }

    @Override
    public void subscribeToAllAround(FluidType type, TileEntity te) {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        this.trySubscribe(this.tanks[0].getTankType(), world, pos.getX() - dir.offsetX + rot.offsetX, pos.getY() + 1, pos.getZ() - dir.offsetZ + rot.offsetZ, dir);
        this.trySubscribe(this.tanks[0].getTankType(), world, pos.getX() - dir.offsetX + rot.offsetX, pos.getY() - 1, pos.getZ() - dir.offsetZ + rot.offsetZ, dir);
        this.trySubscribe(this.tanks[0].getTankType(), world, pos.getX() - dir.offsetX - rot.offsetX, pos.getY() + 1, pos.getZ() - dir.offsetZ - rot.offsetZ, dir);
        this.trySubscribe(this.tanks[0].getTankType(), world, pos.getX() - dir.offsetX - rot.offsetX, pos.getY() - 1, pos.getZ() - dir.offsetZ - rot.offsetZ, dir);
    }

    @Override
    public void sendFluidToAll(FluidTankNTM tank, TileEntity te) {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        this.sendFluid(this.tanks[1], world, pos.getX() - dir.offsetX + rot.offsetX, pos.getY() + 1, pos.getZ() - dir.offsetZ + rot.offsetZ, dir);
        this.sendFluid(this.tanks[1], world, pos.getX() - dir.offsetX + rot.offsetX, pos.getY() - 1, pos.getZ() - dir.offsetZ + rot.offsetZ, dir);
        this.sendFluid(this.tanks[1], world, pos.getX() - dir.offsetX - rot.offsetX, pos.getY() + 1, pos.getZ() - dir.offsetZ - rot.offsetZ, dir);
        this.sendFluid(this.tanks[1], world, pos.getX() - dir.offsetX - rot.offsetX, pos.getY() - 1, pos.getZ() - dir.offsetZ - rot.offsetZ, dir);
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {

        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 20,
                    pos.getY() - 1,
                    pos.getZ() - 20,
                    pos.getX() + 21,
                    pos.getY() + 1,
                    pos.getZ() + 21
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
