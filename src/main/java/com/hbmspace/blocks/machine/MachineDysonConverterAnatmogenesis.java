package com.hbmspace.blocks.machine;

import com.hbm.api.block.IToolable;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.trait.FluidTraitSimple;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.util.AstronomyUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.tileentity.machine.TileEntityDysonConverterAnatmogenesis;
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

public class MachineDysonConverterAnatmogenesis extends BlockDummyableSpace implements ILookOverlay, IToolable {

    public MachineDysonConverterAnatmogenesis(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityDysonConverterAnatmogenesis();
        if(meta >= 6) return new TileEntityProxyCombo(false, false, false);
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {2, 0, 5, 5, 1, 1};
    }

    @Override
    public int getOffset() {
        return 5;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, BlockPos pos) {
        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null) return;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityDysonConverterAnatmogenesis converter)) return;

        CBT_Atmosphere atmosphere = CelestialBody.getTrait(world, CBT_Atmosphere.class);
        double pressure = atmosphere != null ? atmosphere.getPressure(converter.fluid) : 0;
        if(pressure < 0.0001) pressure = 0;
        pressure = Math.round(pressure * 1_000.0) / 1_000.0;

        List<String> text = new ArrayList<>();

        text.add("Current rate: " + ((double)converter.gasProduced * 20 * 60 * 60 / AstronomyUtil.MB_PER_ATM) + "atm per hour");
        text.add("Current gas: " + converter.fluid.getLocalizedName() + " - " + pressure);
        text.add("Current mode: " + (converter.isEmitting ? "EMITTING" : "CAPTURING"));

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public boolean onScrew(World world, EntityPlayer player, int x, int y, int z, EnumFacing side, float fX, float fY, float fZ, EnumHand hand, ToolType tool) {
        if(tool != ToolType.SCREWDRIVER) return false;

        if(world.isRemote) return true;

        int[] pos = this.findCore(world, x, y, z);

        if(pos == null) return false;

        TileEntity te = world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));

        if(!(te instanceof TileEntityDysonConverterAnatmogenesis converter)) return false;

        converter.isEmitting = !converter.isEmitting;
        converter.markDirty();

        return true;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote)
            return true;

        if(!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof IItemFluidIdentifier) {
            int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

            if(posC == null) return false;

            TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

            if(!(te instanceof TileEntityDysonConverterAnatmogenesis converter))
                return false;

            FluidType type = ((IItemFluidIdentifier) player.getHeldItem(hand).getItem()).getType(world, pos.getX(), pos.getY(), pos.getZ(), player.getHeldItem(hand));
            if(type.hasTrait(FluidTraitSimple.FT_Gaseous.class) || type.hasTrait(FluidTraitSimple.FT_Gaseous_ART.class)) {
                converter.fluid = type;
                converter.markDirty();
                player.sendMessage(
                        new TextComponentString("Changed type to ")
                                .setStyle(new Style().setColor(TextFormatting.YELLOW))
                                .appendSibling(new TextComponentTranslation(type.getConditionalName()))
                                .appendSibling(new TextComponentString("!"))
                );
            }

            return true;
        }

        return false;
    }

}
