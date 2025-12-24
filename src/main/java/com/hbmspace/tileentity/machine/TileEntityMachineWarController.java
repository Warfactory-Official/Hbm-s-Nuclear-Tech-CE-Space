package com.hbmspace.tileentity.machine;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.items.ISatChip;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.saveddata.satellites.SatelliteSavedData;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerMachineWarController;
import com.hbmspace.inventory.gui.GUIWarController;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.saveddata.satellites.SatelliteWar;
import com.hbmspace.tileentity.TESpaceUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@AutoRegister
public class TileEntityMachineWarController extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2, ISpaceGuiProvider, IControlReceiver{

    public int id;

    public TileEntityMachineWarController() {
        super(4, false, true);
    }

    @Override
    public String getDefaultName() {
        return "container.warController";
    }

    @Override
    public long getPower() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setPower(long power) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getMaxPower() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isLoaded() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerMachineWarController(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIWarController(player.inventory, this);
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            id = ISatChip.getFreqS(inventory.getStackInSlot(2));
            if(inventory.getStackInSlot(1).isEmpty() || inventory.getStackInSlot(1).getItem() != ModItemsSpace.full_drive) return;

            SatelliteSavedData data = TESpaceUtil.getData(world, pos.getX(), pos.getZ());

            SolarSystem.Body target = ItemVOTVdrive.getDestination(inventory.getStackInSlot(1)).body;
            CelestialBody body = target.getBody();

            Satellite sat = data.getSatFromFreq(id);

            if(sat instanceof SatelliteWar satelliteWar) {

                satelliteWar.setTarget(body);
            }
        }
    }

    @Override
    public void receiveControl(NBTTagCompound data) {
        if(data.hasKey("xcoord") && data.hasKey("zcoord")) {
            updateDriveCoords(data.getInteger("xcoord"), data.getInteger("zcoord"));
        }
    }

    private void updateDriveCoords(int x, int z) {
        if(inventory.getStackInSlot(1).isEmpty() || inventory.getStackInSlot(1).getItem() != ModItemsSpace.full_drive) return;

        ItemVOTVdrive.setCoordinates(inventory.getStackInSlot(1), x, z);

        this.markDirty();
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return isUseableByPlayer(player);
    }

}
