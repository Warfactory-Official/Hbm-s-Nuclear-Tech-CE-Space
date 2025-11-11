package com.hbmspace.tileentity.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FT_Rocket;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.util.ParticleUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.inventory.container.ContainerTransporterRocket;
import com.hbmspace.inventory.gui.GUITransporterRocket;
import com.hbmspace.items.ItemVOTVdrive;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
@AutoRegister
public class TileEntityTransporterRocket extends TileEntityTransporterBase {

    public boolean hasRocket = true;
    public int launchTicks = 0;

    public int threshold = 0;

    public TileEntityTransporterRocket() {
        super(16, 8, 128_000, 0, 2, 64_000);

        tanks[8].setTankType(Fluids.HYDROGEN);
        tanks[9].setTankType(Fluids.OXYGEN);
    }

    @Override
    public String getDefaultName() {
        return getTransporterName();
    }

    @Override
    public void update() {
        super.update();

        // If our transporter state sync is incorrect, fix whichever one gets updated first
        if(!world.isRemote && linkedTransporter != null && linkedTransporter instanceof TileEntityTransporterRocket) {
            if(hasRocket == ((TileEntityTransporterRocket) linkedTransporter).hasRocket) {
                hasRocket = !hasRocket;
            }
        }

        launchTicks = MathHelper.clamp(launchTicks + (hasRocket ? -1 : 1), hasRocket ? -20 : 0, 100);

        if(world.isRemote && launchTicks > 0 && launchTicks < 100) {
            ParticleUtil.spawnGasFlame(world, pos.getX() + 0.5, pos.getY() + 0.5 + launchTicks, pos.getZ() + 0.5, 0.0, -1.0, 0.0);

            if(launchTicks < 10) {
                ExplosionLarge.spawnShock(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1 + world.rand.nextInt(3), 1 + world.rand.nextGaussian());
            }
        }

    }

    public int getThreshold() {
        return threshold == 0 ? 0 : (int)Math.pow(2, threshold - 1);
    }

    private final int MASS_MULT = 100;

    // Check that we have enough fuel to send to our destination
    @Override
    protected boolean canSend(TileEntityTransporterBase linkedTransporter) {
        if(launchTicks > -20) return false;
        if(((TileEntityTransporterRocket)linkedTransporter).launchTicks < 100) return false;
        if(!hasRocket) return false;

        int mass = itemCount();
        if(mass < getThreshold()) return false;

        FT_Rocket fuelStats = tanks[8].getTankType().getTrait(FT_Rocket.class);
        if(fuelStats == null) fuelStats = tanks[9].getTankType().getTrait(FT_Rocket.class);

        if(fuelStats == null) return false;

        ItemVOTVdrive.Target from = CelestialBody.getTarget(world, pos.getX(), pos.getZ());
        ItemVOTVdrive.Target to = CelestialBody.getTarget(linkedTransporter.getWorld(), linkedTransporter.getPos().getX(), linkedTransporter.getPos().getZ());

        int sendCost = Math.min(64_000, SolarSystem.getCostBetween(from.body, to.body, mass * MASS_MULT, (int)fuelStats.getThrust(), fuelStats.getISP(), from.inOrbit, to.inOrbit));

        return tanks[8].getFill() >= sendCost && tanks[9].getFill() >= sendCost;
    }

    @Override
    protected void hasSent(TileEntityTransporterBase linkedTransporter, int quantitySent) {
        // Recalculate send cost from what was actually successfully sent
        FT_Rocket fuelStats = tanks[8].getTankType().getTrait(FT_Rocket.class);
        if(fuelStats == null) fuelStats = tanks[9].getTankType().getTrait(FT_Rocket.class);

        ItemVOTVdrive.Target from = CelestialBody.getTarget(world, pos.getX(), pos.getZ());
        ItemVOTVdrive.Target to = CelestialBody.getTarget(linkedTransporter.getWorld(), linkedTransporter.getPos().getX(), linkedTransporter.getPos().getZ());

        int sendCost = Math.min(64_000, SolarSystem.getCostBetween(from.body, to.body, quantitySent * MASS_MULT, (int)fuelStats.getThrust(), fuelStats.getISP(), from.inOrbit, to.inOrbit));

        tanks[8].setFill(tanks[8].getFill() - sendCost);
        tanks[9].setFill(tanks[9].getFill() - sendCost);

        hasRocket = false;
        ((TileEntityTransporterRocket)linkedTransporter).hasRocket = true;
    }

    @Override
    protected void hasConnected(TileEntityTransporterBase linkedTransporter) {
        hasRocket = true;
        ((TileEntityTransporterRocket)linkedTransporter).hasRocket = false;
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerTransporterRocket(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUITransporterRocket(player.inventory, this);
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeBoolean(hasRocket);
        buf.writeInt(threshold);
        super.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        hasRocket = buf.readBoolean();
        threshold = buf.readInt();
        super.deserialize(buf);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hasRocket = nbt.getBoolean("rocket");
        threshold = nbt.getInteger("threshold");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("rocket", hasRocket);
        nbt.setInteger("threshold", threshold);
        return super.writeToNBT(nbt);
    }

    @Override
    protected DirPos[] getConPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        return new DirPos[] {
                new DirPos(pos.getX() + dir.offsetX * 2 - rot.offsetX, pos.getY(), pos.getZ() + dir.offsetZ * 2 - rot.offsetZ, dir.getOpposite()),
                new DirPos(pos.getX() + dir.offsetX * 2 + rot.offsetX, pos.getY(), pos.getZ() + dir.offsetZ * 2 + rot.offsetZ, dir.getOpposite()),
                new DirPos(pos.getX() - dir.offsetX * 2 - rot.offsetX, pos.getY(), pos.getZ() - dir.offsetZ * 2 - rot.offsetZ, dir),
                new DirPos(pos.getX() - dir.offsetX * 2 + rot.offsetX, pos.getY(), pos.getZ() - dir.offsetZ * 2 + rot.offsetZ, dir),

                new DirPos(pos.getX() + dir.offsetX - rot.offsetX, pos.getY() + 1, pos.getZ() + dir.offsetZ - rot.offsetZ, ForgeDirection.UP),
                new DirPos(pos.getX() + dir.offsetX + rot.offsetX, pos.getY() + 1, pos.getZ() + dir.offsetZ + rot.offsetZ, ForgeDirection.UP),
                new DirPos(pos.getX() - dir.offsetX - rot.offsetX, pos.getY() + 1, pos.getZ() - dir.offsetZ - rot.offsetZ, ForgeDirection.UP),
                new DirPos(pos.getX() - dir.offsetX + rot.offsetX, pos.getY() + 1, pos.getZ() - dir.offsetZ + rot.offsetZ, ForgeDirection.UP),
        };
    }

    @Override
    protected DirPos[] getTankPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        return new DirPos[] {
                new DirPos(pos.getX() + dir.offsetX - rot.offsetX * 3, pos.getY(), pos.getZ() + dir.offsetZ - rot.offsetZ * 3, rot),
                new DirPos(pos.getX() - dir.offsetX - rot.offsetX * 3, pos.getY(), pos.getZ() - dir.offsetZ - rot.offsetZ * 3, rot),
        };
    }

    @Override
    protected DirPos[] getInsertPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);

        return new DirPos[] {
                new DirPos(pos.getX() - dir.offsetX * 2, pos.getY(), pos.getZ() - dir.offsetZ * 2, dir),
        };
    }

    @Override
    protected DirPos[] getExtractPos() {
        ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);

        return new DirPos[] {
                new DirPos(pos.getX() + dir.offsetX * 2, pos.getY(), pos.getZ() + dir.offsetZ * 2, dir.getOpposite()),
        };
    }

    @Override
    public void receiveControl(NBTTagCompound nbt) {
        super.receiveControl(nbt);
        if(nbt.hasKey("threshold"))
            threshold = nbt.getInteger("threshold");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

}
