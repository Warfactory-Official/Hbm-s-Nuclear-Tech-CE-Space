package com.hbmspace.blocks.machine;

import java.util.ArrayList;
import java.util.List;

import com.hbm.api.block.IToolable;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.packet.toclient.BufPacket;
import com.hbm.packet.PacketDispatcher;
import com.hbm.render.block.BlockBakeFrame;
import com.hbm.tileentity.IBufPacketReceiver;

import com.hbm.util.ChatBuilder;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockContainerBakeableSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.orbit.WorldProviderOrbit;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.util.AstronomyUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;

public class BlockAtmosphereEditor extends BlockContainerBakeableSpace implements IToolable, ITooltipProvider, ILookOverlay {

    public BlockAtmosphereEditor(Material material, String s) {
        super(material, s, BlockBakeFrame.cubeAll("atmosphere_editor"));
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int metadata) {
        return new TileEntityAtmosphereEditor();
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag) {
        tooltip.add(TextFormatting.GOLD + "Use screwdriver to turn on and off");
        tooltip.add(TextFormatting.GOLD + "Use hand drill to increase/decrease throughput");
        tooltip.add(TextFormatting.GOLD + "Use defuser to switch emission/capture mode");
        tooltip.add(TextFormatting.GOLD + "Use fluid identifier to change fluid");
    }

    @Override
    public void printHook(Pre event, World world, BlockPos pos) {
        if(world.provider instanceof WorldProviderOrbit) return;

        TileEntity te = world.getTileEntity(pos);

        if(!(te instanceof TileEntityAtmosphereEditor editor))
            return;

        CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
        double pressure = atmosphere != null ? atmosphere.getPressure(editor.fluid) : 0;
        if(pressure < 0.0001) pressure = 0;
        pressure = Math.round(pressure * 1_000.0) / 1_000.0;

        List<String> text = new ArrayList<>();

        text.add("State: " + (editor.isOn ? "RUNNING" : "OFF"));
        text.add("Current gas: " + editor.fluid.getLocalizedName() + " - " + pressure);
        text.add("Current mode: " + (editor.isEmitting ? "EMITTING" : "CAPTURING"));
        text.add("Current throughput: " + Math.pow(10, editor.throughputFactor) / AstronomyUtil.MB_PER_ATM);

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public boolean onScrew(World world, EntityPlayer player, int x, int y, int z, EnumFacing side, float fX, float fY, float fZ, EnumHand hand, ToolType tool) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if(!(te instanceof TileEntityAtmosphereEditor editor))
            return false;

        if(tool == ToolType.SCREWDRIVER) {
            editor.isOn = !editor.isOn;
            editor.markDirty();

            return true;
        } else if(tool == ToolType.HAND_DRILL) {
            editor.throughputFactor += (player.isSneaking() ? -1 : 1);
            editor.markDirty();

            return true;
        } else if(tool == ToolType.DEFUSER) {
            editor.isEmitting = !editor.isEmitting;
            editor.markDirty();

            return true;
        }

        return false;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote)
            return true;

        if(!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof IItemFluidIdentifier) {
            TileEntity te = world.getTileEntity(pos);

            if(!(te instanceof TileEntityAtmosphereEditor editor))
                return false;

            FluidType type = ((IItemFluidIdentifier) player.getHeldItem(hand).getItem()).getType(world, pos.getX(), pos.getY(), pos.getZ(), player.getHeldItem(hand));
            editor.fluid = type;
            editor.markDirty();
            player.sendMessage(ChatBuilder.start("Changed type to ").color(TextFormatting.YELLOW).nextTranslation(type.getConditionalName()).next("!").flush());

            return true;
        }

        return false;
    }
    @AutoRegister
    public static class TileEntityAtmosphereEditor extends TileEntity implements IBufPacketReceiver, ITickable {

        private boolean isOn = false;
        private int throughputFactor = 10;
        private FluidType fluid = com.hbmspace.inventory.fluid.Fluids.EARTHAIR;
        private boolean isEmitting = true;

        @Override
        public void update() {
            if(world.isRemote) return;

            if(isOn && world.getTotalWorldTime() % 5 == 0) {
                if(isEmitting) {
                    CelestialBody.release(world, fluid, Math.pow(10, throughputFactor));
                } else {
                    CelestialBody.capture(world, fluid, Math.pow(10, throughputFactor));
                }
            }

            PacketDispatcher.wrapper.sendToAllAround(new BufPacket(pos.getX(), pos.getY(), pos.getZ(), this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
        }

        @Override
        public void readFromNBT(@NotNull NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            isOn = nbt.getBoolean("isOn");
            throughputFactor = nbt.getInteger("throughput");
            fluid = Fluids.fromID(nbt.getInteger("fluid"));
            isEmitting = nbt.getBoolean("emit");
        }

        @Override
        public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            nbt.setBoolean("isOn", isOn);
            nbt.setInteger("throughput", throughputFactor);
            nbt.setInteger("fluid", fluid.getID());
            nbt.setBoolean("emit", isEmitting);
            return super.writeToNBT(nbt);
        }

        @Override
        public void serialize(ByteBuf buf) {
            buf.writeBoolean(isOn);
            buf.writeInt(throughputFactor);
            buf.writeInt(fluid.getID());
            buf.writeBoolean(isEmitting);
        }

        @Override
        public void deserialize(ByteBuf buf) {
            isOn = buf.readBoolean();
            throughputFactor = buf.readInt();
            fluid = Fluids.fromID(buf.readInt());
            isEmitting = buf.readBoolean();
        }

    }

}

