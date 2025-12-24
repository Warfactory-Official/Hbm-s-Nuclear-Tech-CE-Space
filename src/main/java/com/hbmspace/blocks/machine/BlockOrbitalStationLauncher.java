package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.items.ModItems;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.tileentity.machine.TileEntityOrbitalStationLauncher;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockOrbitalStationLauncher extends BlockOrbitalStation implements ITooltipProvider {

    public BlockOrbitalStationLauncher(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityOrbitalStationLauncher();
        if(meta >= 6) return new TileEntityProxyCombo(true, true, true);
        return null;
    }

    @Override
    public void breakBlock(World worldIn, @NotNull BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityOrbitalStationLauncher) {
            ((TileEntityOrbitalStationLauncher) te).isBreaking = true;
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!CelestialBody.inOrbit(world)) return false;

        int[] posC = this.findCore(world, pos.getX(), pos.getY(), pos.getZ());

        if(posC == null)
            return false;

        // If activating the side blocks, ignore, to allow placing
        if(Math.abs(posC[0] - pos.getX()) >= 2 || Math.abs(posC[2] - pos.getZ()) >= 2)
            return false;

        TileEntity te = world.getTileEntity(new BlockPos(posC[0], posC[1], posC[2]));

        if(!(te instanceof TileEntityOrbitalStationLauncher station))
            return false;

        if(world.isRemote) return true;

        if(station.hasDocked) {
            if(!station.hasRider) {
                station.enterCapsule(player);
            }
            return true;
        }

        return standardOpenBehavior(world, pos, player, 0);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {
        if(!CelestialBody.inOrbit(world)) {
            List<String> text = new ArrayList<>();
            text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! " + I18nUtil.resolveKey("atmosphere.noOrbit") + " ! ! !");
            ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
            return;
        }

        int[] pos = this.findCore(world, x, y, z);

        if(pos == null)
            return;

        TileEntity te = world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));

        if(!(te instanceof TileEntityOrbitalStationLauncher pad))
            return;

        List<String> text = new ArrayList<>();
        if(pad.hasDocked) {
            if(!pad.hasRider) {
                text.add(I18nUtil.resolveKey("station.enterRocket"));
            } else {
                text.add(TextFormatting.YELLOW + I18nUtil.resolveKey("station.occupiedRocket"));
            }

            ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
            return;
        }

        if(pad.rocket == null || !pad.rocket.validate()) return;

        text.add("Required fuels:");

        for(int i = 0; i < pad.tanks.length; i++) {
            FluidTankNTM tank = pad.tanks[i];
            if(tank.getTankType() == Fluids.NONE) continue;
            text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + tank.getTankType().getLocalizedName() + ": " + tank.getFill() + "/" + tank.getMaxFill() + "mB");
        }

        if(pad.solidFuel.max > 0) {
            text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + I18nUtil.resolveKey(ModItems.rocket_fuel.getTranslationKey() + ".name") + ": " + pad.solidFuel.level + "/" + pad.solidFuel.max + "kg");
        }

        if(text.size() <= 1) return;

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

}
