package com.hbmspace.tileentity.machine;

import com.hbm.tileentity.TileEntityMachineBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.blocks.machine.MachineStardar;
import com.hbmspace.interfaces.AutoRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class TileEntityDishControl extends TileEntityMachineBase implements ITickable {

    private TileEntityMachineStardar dish;
    public int[] linkPosition = new int[3];
    public boolean isLinked;
    private boolean foundLink = false;

    public TileEntityDishControl() {
        super(0, false, false);
    }

    @Override
    public String getDefaultName() {
        return "container.dishControl";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        isLinked = nbt.getBoolean("isLinked");
        linkPosition[0] = nbt.getInteger("linkPosX");
        linkPosition[1] = nbt.getInteger("linkPosY");
        linkPosition[2] = nbt.getInteger("linkPosZ");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setBoolean("isLinked", isLinked);

        if(linkPosition[0] != 0 || linkPosition[1] != 0 || linkPosition[2] != 0) {
            nbt.setInteger("linkPosX", linkPosition[0]);
            nbt.setInteger("linkPosY", linkPosition[1]);
            nbt.setInteger("linkPosZ", linkPosition[2]);
        }
        return super.writeToNBT(nbt);
    }

    @Override
    public void update() {
        if(world.isRemote) return;

        if(!foundLink && isLinked)
        {
            isLinked = establishLink(linkPosition[0], linkPosition[1], linkPosition[2]);
            foundLink = true;
        }

        this.networkPackNT(15);
    }

    @SideOnly(Side.CLIENT)
    public TileEntityMachineStardar getLinkedDishClientSafe() {
        if (dish == null && isLinked) {
            TileEntity te = world.getTileEntity(new BlockPos(linkPosition[0], linkPosition[1], linkPosition[2]));
            if (te instanceof TileEntityMachineStardar) {
                dish = (TileEntityMachineStardar) te;
            }
        }
        return dish;
    }

    @SideOnly(Side.CLIENT)
    public boolean starDarHasDisk() {
        TileEntityMachineStardar link = getLinkedDishClientSafe();
        return link != null && !link.inventory.getStackInSlot(0).isEmpty();
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(isLinked);
        buf.writeInt(linkPosition[0]);
        buf.writeInt(linkPosition[1]);
        buf.writeInt(linkPosition[2]);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        isLinked = buf.readBoolean();
        linkPosition[0] = buf.readInt();
        linkPosition[1] = buf.readInt();
        linkPosition[2] = buf.readInt();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new SPacketUpdateTileEntity(this.pos, 0, nbt);
    }

    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public void TryLink(ItemStack stack)
    {
        isLinked = linkWithSensor(stack);
    }

    private boolean linkWithSensor(ItemStack stack)
    {
        if(!stack.isEmpty() && stack.getTagCompound() != null) {
            // Get the StarDar coordinates from the sensor
            int x = stack.getTagCompound().getInteger("x");
            int y = stack.getTagCompound().getInteger("y");
            int z = stack.getTagCompound().getInteger("z");

            return establishLink(x, y, z);
        }

        return false;
    }

    private boolean establishLink(int x, int y, int z) {

        // Get the StarDar block
        BlockPos pos = new BlockPos(x, y, z);
        Block b = world.getBlockState(pos).getBlock();

        if(b == ModBlocksSpace.machine_stardar) {

            int[] posC = ((MachineStardar)ModBlocksSpace.machine_stardar).findCore(world, x, y, z);

            if(posC != null) {

                TileEntity tile = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

                if(tile instanceof TileEntityMachineStardar) {
                    // Dish linked successfully
                    linkPosition = posC;
                    return true;
                }
            }
        }

        return false;
    }

    AxisAlignedBB bb = null;
    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if(bb == null) {
            bb = new AxisAlignedBB(
                    pos.getX() - 1,
                    pos.getY(),
                    pos.getZ() - 1,
                    pos.getX() + 2,
                    pos.getY() + 1,
                    pos.getZ() + 2
            );
        }

        return bb;
    }
}
