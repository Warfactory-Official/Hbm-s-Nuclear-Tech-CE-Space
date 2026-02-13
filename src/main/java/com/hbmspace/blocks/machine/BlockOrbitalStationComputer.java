package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.handler.ThreeInts;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.main.MainRegistry;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.api.tile.IPropulsion;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.orbit.OrbitalStation;
import com.hbmspace.items.ItemVOTVdrive;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStationComputer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockOrbitalStationComputer extends BlockDummyableSpace implements ILookOverlay {

    public BlockOrbitalStationComputer(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        return new TileEntityOrbitalStationComputer();
    }

    @Override
    public int[] getDimensions() {
        return new int[] {0, 0, 0, 0, 0, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!CelestialBody.inOrbit(world)) return false;

        if (!world.isRemote) {
            int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

            if (posC == null)
                return false;

            TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

            if (!(te instanceof TileEntityOrbitalStationComputer computer))
                return false;

            if (computer.isTravelling())
                return false;

            ItemStack heldStack = player.getHeldItem(hand);

            if (!heldStack.isEmpty() && heldStack.getItem() instanceof ItemVOTVdrive) {
                if (!computer.inventory.getStackInSlot(0).isEmpty())
                    return false;

                ItemVOTVdrive.Destination destination = ItemVOTVdrive.getDestination(heldStack);

                if (destination.body == SolarSystem.Body.ORBIT)
                    return false;

                if (computer.travelTo(destination.body.getBody(), heldStack.copy())) {
                    heldStack.setCount(0);
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), HBMSoundHandler.upgradePlug, SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else {
                    return false;
                }
            } else if (heldStack.isEmpty() && !computer.inventory.getStackInSlot(0).isEmpty()) {
                if (!player.inventory.addItemStackToInventory(computer.inventory.getStackInSlot(0).copy())) {
                    player.dropItem(computer.inventory.getStackInSlot(0).copy(), false);
                }
                computer.inventory.setStackInSlot(0, ItemStack.EMPTY);
                computer.markChanged();
            } else {
                FMLNetworkHandler.openGui(player, MainRegistry.instance, 0, world, posC[0], posC[1], posC[2]);
            }


        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        if(!CelestialBody.inOrbit(world)) {
            List<String> text = new ArrayList<>();
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.noOrbit") + " ! ! !");
            ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
            return;
        }

        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityOrbitalStationComputer computer)) return;

        OrbitalStation station = OrbitalStation.clientStation;
        double progress = station.getUnscaledProgress(0);
        List<String> text = new ArrayList<>();

        if(!station.hasEngines) {
            text.add(TextFormatting.RED + "No engines available");
        } else if(!station.errorsAt.isEmpty()) {
            for(ThreeInts errorAt : station.errorsAt) {
                TileEntity error = world.getTileEntity(new BlockPos(errorAt.x, errorAt.y, errorAt.z));
                if(!(error instanceof IPropulsion)) continue;
                ((IPropulsion) error).addErrors(text);
            }
        } else if(progress > 0) {
            if(station.state == OrbitalStation.StationState.LEAVING) {
                text.add(TextFormatting.AQUA + I18nUtil.resolveKey("station.engage") + ": " + TextFormatting.RESET + I18nUtil.resolveKey("body." + station.target.name));
            } else if(station.state == OrbitalStation.StationState.ARRIVING) {
                text.add(TextFormatting.AQUA + I18nUtil.resolveKey("station.disengage"));
            } else {
                text.add(TextFormatting.AQUA + I18nUtil.resolveKey("station.travelling") + ": " + TextFormatting.RESET + I18nUtil.resolveKey("body." + station.target.name));
            }
            text.add(TextFormatting.AQUA + I18nUtil.resolveKey("station.progress") + ": " + TextFormatting.RESET + "" + Math.round(progress * 100) + "%");
        } else if(computer.hasDrive) {
            if(!Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty()) {
                text.add(I18nUtil.resolveKey("station.removeDrive"));
            } else {
                text.add(I18nUtil.resolveKey("station.interactDrive"));
            }
        } else {
            text.add(I18nUtil.resolveKey("station.insertDrive"));
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

}
