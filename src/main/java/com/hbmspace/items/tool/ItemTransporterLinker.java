package com.hbmspace.items.tool;

import com.hbm.main.MainRegistry;
import com.hbm.packet.PacketDispatcher;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.util.BobMathUtil;
import com.hbm.util.CompatExternal;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.inventory.gui.GUITransporterLinker;
import com.hbmspace.items.ItemBakedSpace;
import com.hbmspace.packet.toclient.TransporterLinkerPacket;
import com.hbmspace.tileentity.machine.TileEntityTransporterBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemTransporterLinker extends ItemBakedSpace implements IGUIProvider {

    public ItemTransporterLinker(String s) {
        super(s);
    }

    @SideOnly(Side.CLIENT)
    public static List<TransporterInfo> currentTransporters;

    @Override
    public void addInformation(@NotNull ItemStack stack, World world, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        list.add("Sneak-click to save transporter");
        list.add("Use on transporter to link to a saved transporter");
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = CompatExternal.getCoreFromPos(world, pos);
        ItemStack stack = player.getHeldItem(hand);

        if(!(tile instanceof TileEntityTransporterBase transporter)) {
            return EnumActionResult.FAIL;
        }

        if(player.isSneaking()) {
            if(!world.isRemote) {
                addTransporter(stack, world, transporter);
                player.sendMessage(new TextComponentString("Added transporter to linker"));
            }
        } else if(world.isRemote) {
            lastTransporter = TransporterInfo.from(world.provider.getDimension(), transporter);
            player.openGui(MainRegistry.instance, 0, world, 0, 0, 0);
        }

        return EnumActionResult.SUCCESS;
    }

    public void onUpdate(@NotNull ItemStack stack, World world, @NotNull Entity entity, int i, boolean b) {
        if(world.isRemote || !(entity instanceof EntityPlayerMP))
            return;

        if(((EntityPlayerMP)entity).getHeldItemMainhand() != stack)
            return;

        List<TransporterInfo> transporters = getTransporters(stack);

        if(entity.ticksExisted % 2 == 0) {
            PacketDispatcher.wrapper.sendTo(new TransporterLinkerPacket(transporters), (EntityPlayerMP) entity);
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand) { return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand)); }

    @Override
    public Container provideContainer(int i, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    private TransporterInfo lastTransporter;

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int i, EntityPlayer player, World world, int x, int y, int z) {
        return new GUITransporterLinker(player, currentTransporters, lastTransporter);
    }

    // Trivially comparable transporter info, for hashsets and client/server communications
    public static class TransporterInfo {

        public String name;
        public ResourceLocation planet;
        public TransporterInfo linkedTo;

        // Comparables
        public int dimensionId;
        public int x, y, z;

        public TransporterInfo(String name, int dimensionId, int x, int y, int z) {
            this.name = name;
            this.dimensionId = dimensionId;
            this.x = x;
            this.y = y;
            this.z = z;

            planet = CelestialBody.getBody(dimensionId).texture;
        }

        public static TransporterInfo from(int dimensionId, TileEntityTransporterBase transporter) {
            TransporterInfo info = new TransporterInfo(transporter.getTransporterName(), dimensionId, transporter.getPos().getX(), transporter.getPos().getY(), transporter.getPos().getZ());
            info.linkedTo = transporter.getLinkedTransporter();
            return info;
        }

        public void writeToNBT(NBTTagCompound nbt) {
            writeToNBT(nbt, true);
        }

        private void writeToNBT(NBTTagCompound nbt, boolean recurse) {
            nbt.setString("name", name);
            nbt.setInteger("dimensionId", dimensionId);
            nbt.setInteger("x", x);
            nbt.setInteger("y", y);
            nbt.setInteger("z", z);

            if(recurse && linkedTo != null) {
                NBTTagCompound linked = new NBTTagCompound();
                linkedTo.writeToNBT(linked, false);
                nbt.setTag("linked", linked);
            }
        }

        public static TransporterInfo readFromNBT(NBTTagCompound nbt) {
            TransporterInfo info = new TransporterInfo(nbt.getString("name"), nbt.getInteger("dimensionId"), nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
            if(nbt.hasKey("linked")) {
                info.linkedTo = readFromNBT(nbt.getCompoundTag("linked"));
            }
            return info;
        }

        @Override
        public int hashCode() {
            return Objects.hash(dimensionId, x, y, z);
        }

        @Override
        public boolean equals(Object other) {
            if(this == other) return true;
            if(other == null) return false;
            if(this.getClass() != other.getClass()) return false;
            TransporterInfo info = (TransporterInfo) other;
            return dimensionId == info.dimensionId
                    && x == info.x
                    && y == info.y
                    && z == info.z;
        }

    }

    private void addTransporter(ItemStack stack, World world, TileEntityTransporterBase transporter) {
        int dimensionId = world.provider.getDimension();

        Set<TransporterInfo> transporters = loadTransporters(stack);

        transporters.add(TransporterInfo.from(dimensionId, transporter));

        saveTransporters(stack, transporters);
    }

    private List<TransporterInfo> getTransporters(ItemStack stack) {
        Set<TransporterInfo> transporterData = loadTransporters(stack);
        return new ArrayList<>(transporterData);
    }

    private static Set<TransporterInfo> loadTransporters(ItemStack stack) {
        if(stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());

        Set<TransporterInfo> transporterCoordinates = new HashSet<>();

        int[] dimensionsToLoad = stack.getTagCompound().getIntArray("dimensions");
        for(int dimensionId : dimensionsToLoad) {

            NBTTagCompound dimensionTag = stack.getTagCompound().getCompoundTag("d" + dimensionId);
            int[] coordinateList = dimensionTag.getIntArray("coords");
            World world = DimensionManager.getWorld(dimensionId);

            if(world == null) continue;

            // 3 dimension strides
            for(int i = 0; i < coordinateList.length; i += 3) {
                TileEntity te = world.getTileEntity(new BlockPos(coordinateList[i], coordinateList[i+1], coordinateList[i+2]));
                if(te instanceof TileEntityTransporterBase) {
                    transporterCoordinates.add(TransporterInfo.from(dimensionId, (TileEntityTransporterBase) te));
                }
            }
        }

        return transporterCoordinates;
    }

    private static void saveTransporters(ItemStack stack, Set<TransporterInfo> transporters) {
        if(stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());

        Map<Integer, List<Integer>> data = new HashMap<>();

        for(TransporterInfo info : transporters) {
            if(!data.containsKey(info.dimensionId))
                data.put(info.dimensionId, new ArrayList<>());

            data.get(info.dimensionId).addAll(Arrays.asList(info.x, info.y, info.z));
        }

        stack.getTagCompound().setIntArray("dimensions", BobMathUtil.intCollectionToArray(data.keySet()));

        for(Map.Entry<Integer, List<Integer>> entry : data.entrySet()) {
            NBTTagCompound dimensionTag = new NBTTagCompound();
            dimensionTag.setIntArray("coords", BobMathUtil.intCollectionToArray(entry.getValue()));
            stack.getTagCompound().setTag("d" + entry.getKey(), dimensionTag);
        }
    }

}
