package com.hbmspace.tileentity.machine;

import com.hbm.handler.CompatHandler;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystemWorldSavedData;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerStardar;
import com.hbmspace.inventory.gui.GUIMachineStardar;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.tileentity.ISpaceGuiProvider;
import io.netty.buffer.ByteBuf;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Optional.InterfaceList({@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")})
@AutoRegister
public class TileEntityMachineStardar extends TileEntityMachineBase implements ITickable, ISpaceGuiProvider, IControlReceiver, SimpleComponent, CompatHandler.OCComponent {

    private static long pointAtTime = 0;

    // Used to point the dish on the client
    public float dishYaw = 0;
    public float dishPitch = 0;
    public float prevDishYaw = 0;
    public float prevDishPitch = 0;

    public boolean radarMode;

    // Sent by the server for the client to smoothly lerp to
    public static float targetYaw = 0;
    public static float targetPitch = 0;

    private float maxSpeedYaw = 0.5F;

    public int[] heightmap;
    public boolean updateHeightmap = false;
    private ItemStack previousStack;

    public TileEntityMachineStardar() {
        super(1, false, false);
        this.inventory = new ItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            if(world.getTotalWorldTime() >= pointAtTime) {
                pointAtTime = world.getTotalWorldTime() + world.rand.nextInt(300) + 300;

                targetYaw = MathHelper.wrapDegrees(world.rand.nextFloat() * 360);
                targetPitch = world.rand.nextFloat() * 80;
            }

            if(!inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ModItemsSpace.full_drive) {
                if(heightmap == null || !inventory.getStackInSlot(0).isItemEqual(previousStack)) {
                    previousStack = inventory.getStackInSlot(0);

                    ItemVOTVdrive.Destination destination = ItemVOTVdrive.getApproximateDestination(inventory.getStackInSlot(0));
                    CelestialBody body = destination.body.getBody();
                    ChunkPos chunk = destination.getChunk();

                    if(body != null) {
                        heightmap = new int[256*256];
                        updateHeightmap = true;

                        for(int cx = 0; cx < 16; cx++) {
                            for(int cz = 0; cz < 16; cz++) {
                                int[] map = body.getHeightmap(chunk.x + cx - 8, chunk.z + cz - 8);
                                int ox = cx * 16;
                                int oz = cz * 16;

                                for(int x = 0; x < 16; x++) {
                                    for(int z = 0; z < 16; z++) {
                                        heightmap[(z + oz) * 256 + (x + ox)] = map[z * 16 + x];
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if(heightmap != null) {
                    heightmap = null;
                    updateHeightmap = true;
                }
            }
            networkPackNT(250);
        } else {
            float yawOffset = MathHelper.wrapDegrees(targetYaw - dishYaw);
            float moveYaw = MathHelper.clamp(yawOffset, -maxSpeedYaw, maxSpeedYaw);

            float pitchOffset = targetPitch - dishPitch;
            float pitchSpeed = (moveYaw / yawOffset) * Math.abs(pitchOffset);
            float movePitch = MathHelper.clamp(pitchOffset, -pitchSpeed, pitchSpeed);

            prevDishYaw = dishYaw;
            prevDishPitch = dishPitch;
            dishYaw += moveYaw;
            dishPitch += movePitch;
        }
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(!world.isRemote) updateHeightmap = true; // new viewer, send them the heightmap just in case
        return new ContainerStardar(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIMachineStardar(player.inventory, this);
    }

    @Override
    public String getDefaultName() {
        return "container.machineStardar";
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("radarmode", radarMode);
        nbt.setFloat("yaw", targetYaw);
        nbt.setFloat("pitch", targetPitch);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        radarMode = nbt.getBoolean("radarmode");
        targetYaw = nbt.getFloat("yaw");
        targetPitch = nbt.getFloat("pitch");
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeFloat(targetYaw);
        buf.writeFloat(targetPitch);


        buf.writeBoolean(radarMode);
        buf.writeBoolean(updateHeightmap);
        if(updateHeightmap) {
            if(heightmap != null) {
                buf.writeInt(heightmap.length);
                for(int h : heightmap) {
                    buf.writeByte(h);
                }
            } else {
                buf.writeInt(0);
            }
        }
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        targetYaw = buf.readFloat();
        targetPitch = buf.readFloat();

        radarMode = buf.readBoolean();
        updateHeightmap = buf.readBoolean();
        if(updateHeightmap) {
            int count = buf.readInt();
            if(count > 0) {
                heightmap = new int[count];
                for(int i = 0; i < count; i++) {
                    heightmap[i] = buf.readUnsignedByte();
                }
            } else {
                heightmap = null;
            }
        }
    }

    private void processDrive(int targetDimensionId, int ix, int iz) {
        CelestialBody body = CelestialBody.getBodyOrNull(targetDimensionId);
        if(body == null && targetDimensionId != SpaceConfig.orbitDimension) return;

        if(inventory.getStackInSlot(0).isEmpty() || inventory.getStackInSlot(0).getItem() != ModItemsSpace.hard_drive) return;
        int meta = body != null ? body.getEnum().ordinal() : 0;

        inventory.setStackInSlot(0, new ItemStack(ModItemsSpace.full_drive, 1, meta));

        if((ix != 0 || iz != 0) && world.provider.getDimension() != SpaceConfig.orbitDimension) {
            inventory.getStackInSlot(0).setTagCompound(new NBTTagCompound());
            inventory.getStackInSlot(0).getTagCompound().setInteger("ax", ix);
            inventory.getStackInSlot(0).getTagCompound().setInteger("az", iz);
            inventory.getStackInSlot(0).getTagCompound().setBoolean("Processed", true);
        } else if(targetDimensionId == SpaceConfig.orbitDimension) {
            ChunkPos pos;

            // if we're on a station, return our current station as a drive
            if(world.provider.getDimension() == SpaceConfig.orbitDimension) {
                pos = new ChunkPos(MathHelper.floor((float)getPos().getX() / OrbitalStation.STATION_SIZE), MathHelper.floor((float)getPos().getZ() / OrbitalStation.STATION_SIZE));
            } else {
                pos = SolarSystemWorldSavedData.get(world).findFreeSpace();
            }

            inventory.getStackInSlot(0).setTagCompound(new NBTTagCompound());
            inventory.getStackInSlot(0).getTagCompound().setInteger("x", pos.x);
            inventory.getStackInSlot(0).getTagCompound().setInteger("z", pos.z);
            inventory.getStackInSlot(0).getTagCompound().setBoolean("Processed", true);
        }

        // Now point the dish at the target planet
        pointAtTime = world.rand.nextInt(300) + 300;
        targetYaw = MathHelper.wrapDegrees(world.rand.nextFloat() * 360);
        targetPitch = world.rand.nextFloat() * 80;

        this.markDirty();
    }

    private void updateDriveCoords(int x, int z) {
        if(inventory.getStackInSlot(0).isEmpty() || inventory.getStackInSlot(0).getItem() != ModItemsSpace.full_drive) return;

        ItemVOTVdrive.Destination destination = ItemVOTVdrive.getApproximateDestination(inventory.getStackInSlot(0));
        ItemVOTVdrive.setCoordinates(inventory.getStackInSlot(0), destination.x + x - 8 * 16, destination.z + z - 8 * 16);

        this.markDirty();
    }

    // This one is COOL
    @Override
    @Optional.Method(modid = "opencomputers")
    public String getComponentName() {
        return "ntm_stardar";
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getPlanetStats(Context context, Arguments args) {
        CelestialBody body = CelestialBody.getBody(args.checkString(0));
        if (body != null) {
            return new Object[]{
                    // wow, that's a lot (basically give a bunch of info about the planet/body specified)
                    body.name,
                    body.parent.name,
                    body.getStar().name,
                    body.tidallyLockedTo,
                    body.axialTilt,
                    body.canLand,
                    body.massKg,
                    body.getProcessingLevel(CelestialBody.getBody(world)),
                    body.radiusKm,
                    body.semiMajorAxisKm,
                    body.getSunPower(),
                    body.getSurfaceGravity(),
                    body.getRotationalPeriod(),
                    body.getOrbitalPeriod()
            };
        }
        return new Object[] {null, "No body with that name found."};
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getCurrentPlanet(Context context, Arguments args) {
        CelestialBody body = CelestialBody.getBody(world);
        // realistically if this is null
        // we have bigger problems lmao
        return new Object[] {body.name};
    }

    @Callback(direct = true)
    @Optional.Method(modid = "opencomputers")
    public Object[] getSatellites(Context context, Arguments args) {
        CelestialBody body = CelestialBody.getBody(args.checkString(0));
        if (body != null) {
            List<String> returnValues = new ArrayList<>();
            for (CelestialBody planet : body.satellites) {
                returnValues.add(planet.name);
                return returnValues.toArray();
            }
        }
        return new Object[]{null, "No body with that name found."};
    }

    // no `method()` or `invoke()` functions here because... this machine doesn't have any proxy blocks??
    // amazing

    @Override
    public void receiveControl(EntityPlayerMP player, NBTTagCompound data) {
        if(data.hasKey("pid")) {
            processDrive(data.getInteger("pid"), data.getInteger("ix"), data.getInteger("iz"));
        }

        if(data.hasKey("px") && data.hasKey("pz")) {
            updateDriveCoords(data.getInteger("px"), data.getInteger("pz"));
        }
        if(data.hasKey("radarmode")) {
            radarMode = data.getBoolean("radarmode");
        }
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return isUseableByPlayer(player);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }
}
