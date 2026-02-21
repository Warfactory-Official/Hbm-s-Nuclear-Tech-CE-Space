package com.hbmspace.tileentity.machine;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerOrbitalStationComputer;
import com.hbmspace.inventory.gui.GUIOrbitalStationComputer;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
@AutoRegister
public class TileEntityOrbitalStationComputer extends TileEntityMachineBase implements ITickable, ISpaceGuiProvider, IControlReceiver {

    public boolean hasDrive;

    public TileEntityOrbitalStationComputer() {
        super(1, false, false);
    }

    public boolean travelTo(CelestialBody body, ItemStack drive) {
        OrbitalStation station = OrbitalStation.getStationFromPosition(pos.getX(), pos.getZ());

        if(station.orbiting == body) return false;

        station.travelTo(body);
        inventory.setStackInSlot(0, drive);
        markChanged();

        return true;
    }

    public boolean isTravelling() {
        OrbitalStation station = OrbitalStation.getStationFromPosition(pos.getX(), pos.getZ());

        return station.state != OrbitalStation.StationState.ORBIT;
    }

    @Override
    public String getDefaultName() {
        return "container.orbitalStationComputer";
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            hasDrive = !inventory.getStackInSlot(0).isEmpty();
            networkPackNT(50);
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(hasDrive);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        hasDrive = buf.readBoolean();
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerOrbitalStationComputer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIOrbitalStationComputer(this);
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return true;
    }

    @Override
    public void receiveControl(NBTTagCompound data) {
        if(data.hasKey("name")) {
            OrbitalStation station = OrbitalStation.getStationFromPosition(pos.getX(), pos.getZ());
            station.name = data.getString("name");
        }

        if(data.hasKey("gravity")) {
            OrbitalStation station = OrbitalStation.getStationFromPosition(pos.getX(), pos.getZ());
            station.gravityMultiplier = data.getBoolean("gravity") ? 1 : 0;
        }
    }

    AxisAlignedBB bb = null;

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    pos.getX() + 1,
                    pos.getY() + 2,
                    pos.getZ() + 1
            );
        }

        return bb;
    }

}
