package com.hbmspace.blocks.bomb;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.items.ModItems;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.bomb.TileEntityLaunchPadRocket;
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

public class LaunchPadRocket extends BlockDummyableSpace implements ILookOverlay, ITooltipProvider {

    public LaunchPadRocket(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityLaunchPadRocket();
        if(meta >= 6) return new TileEntityProxyCombo().inventory().power().fluid();
        return null;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.standardOpenBehavior(world, pos.getX(), pos.getY(), pos.getZ(), player, 0);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {2, 0, 7, 6, 6, 6};
    }

    @Override
    public int getOffset() {
        return 6;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        // Main body
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {2, 0, 6, 6, 4, 4}, this, dir);

        MultiblockHandlerXR.fillSpace(world, x - dir.offsetX * 2, y, z - dir.offsetZ * 2, new int[] {2, 0, 4, 0, 6, 6}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * 2, y, z + dir.offsetZ * 2, new int[] {2, 0, 0, 4, 6, 6}, this, dir);

        // Inputs
        BlockDummyable.safeRem = true;
        for(int or = 1; or < 5; or++) {
            for(int oy = 0; oy < 3; oy++) {
                BlockPos pos = new BlockPos(x - rot.offsetX * or - dir.offsetX * 7, y + oy, z - rot.offsetZ * or - dir.offsetZ * 7);
                world.setBlockState(pos, this.getDefaultState().withProperty(BlockDummyable.META, dir.getOpposite().ordinal() + extra), 3);
            }
        }
        BlockPos pos = new BlockPos(x + rot.offsetX * 3 - dir.offsetX * 7, y, z + rot.offsetZ * 3 - dir.offsetZ * 7);
        world.setBlockState(pos, this.getDefaultState().withProperty(BlockDummyable.META, dir.getOpposite().ordinal() + extra), 3);
        BlockDummyable.safeRem = false;
    }

    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {

        int[] pos = this.findCore(world, x, y, z);

        if(pos == null)
            return;

        TileEntity te = world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));

        if(!(te instanceof TileEntityLaunchPadRocket pad))
            return;

        if(pad.rocket == null) return;

        if(y - pos[1] > 2) return; // Don't show tooltip on support tower

        List<String> text = new ArrayList<>();
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

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

}
