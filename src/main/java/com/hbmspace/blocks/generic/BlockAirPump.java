package com.hbmspace.blocks.generic;

import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.trait.FluidTraitSimple;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.render.block.BlockBakeFrame;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockContainerBakeableSpace;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.IBlockSealable;
import com.hbmspace.tileentity.machine.TileEntityAirPump;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockAirPump extends BlockContainerBakeableSpace implements ILookOverlay, IBlockSealable {

    public BlockAirPump(Material m, String s) {
        super(m, s, BlockBakeFrame.bottomTop("vent_chlorine_seal_side", "vent_chlorine_seal_top", "vent_chlorine_seal_side"));
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World p_149915_1_, int p_149915_2_) {
        return new TileEntityAirPump();
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {

        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if(!(tile instanceof TileEntityAirPump pump)) return;

        CBT_Atmosphere atmosphere = pump.currentAtmosphere;

        List<String> text = new ArrayList<>();

        text.add(I18nUtil.resolveKey("hbmfluid." + pump.tank.getTankType().getName().toLowerCase()) + ": " + pump.tank.getFill() + "/" + pump.tank.getMaxFill() + "mB");

        if(pump.tank.getFill() <= 10) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.noTank") + " ! ! !");
        } else if(!pump.hasSeal()) {
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.noSeal") + " ! ! !");
        }

        text.add(I18nUtil.resolveKey("atmosphere.name") + ": ");

        boolean hasPressure = false;

        if(atmosphere != null) {
            for(CBT_Atmosphere.FluidEntry entry : atmosphere.fluids) {
                if(entry.pressure > 0.01) {
                    double pressure = BobMathUtil.roundDecimal(entry.pressure, 3);
                    text.add(TextFormatting.AQUA + " - " + I18nUtil.resolveKey("hbmfluid." + entry.fluid.getName().toLowerCase()) + " - " + pressure + "atm");
                    hasPressure = true;
                }
            }
        }

        if(!hasPressure) {
            text.add(TextFormatting.AQUA + " - " + I18nUtil.resolveKey("atmosphere.vacuum"));
        }

        if(pump.isRecycling()) {
            text.add(TextFormatting.GREEN + I18nUtil.resolveKey("atmosphere.recycling"));
        }

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            return true;
        }

        TileEntity tile = world.getTileEntity(pos);

        if(!(tile instanceof TileEntityAirPump pump)) return false;

        if(!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof IItemFluidIdentifier) {
            FluidType type = ((IItemFluidIdentifier) player.getHeldItem(hand).getItem()).getType(world, pos.getX(), pos.getY(), pos.getZ(), player.getHeldItem(hand));
            if(type.hasTrait(FluidTraitSimple.FT_Gaseous.class)) {
                pump.tank.setTankType(type);
                pump.markDirty();
                player.sendMessage(
                        new TextComponentString("Changed type to ")
                                .setStyle(new Style().setColor(TextFormatting.YELLOW))
                                .appendSibling(new TextComponentTranslation(type.getConditionalName()))
                                .appendSibling(new TextComponentString("!"))
                );
            }
        }

        return true;
    }

    @Override
    public boolean isSealed(World world, int x, int y, int z) {
        return false;
    }
}
