package com.hbmspace.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityHydroponic;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineHydroponic extends BlockDummyableSpace implements ILookOverlay, ITooltipProvider {

    public MachineHydroponic(Material mat, String s) {
        super(mat, s);

        // Middle
        this.bounding.add(new AxisAlignedBB(-0.5, 0, -1.5, 0.5, 3, 1.5));

        // Sides
        this.bounding.add(new AxisAlignedBB(-1.125, 1, -2.5, 1.125, 3, -1.5));
        this.bounding.add(new AxisAlignedBB(-1.125, 1, 1.5, 1.125, 3, 2.5));

        // Side butts
        this.bounding.add(new AxisAlignedBB(-0.75, 0, -2.5, 0.75, 1, -1.5));
        this.bounding.add(new AxisAlignedBB(-0.75, 0, 1.5, 0.75, 1, 2.5));

    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityHydroponic();
        if(meta >= 6) return new TileEntityProxyCombo(true, true, true);
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {2, 0, 0, 0, 2, 2};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public boolean checkRequirement(World world, int x, int y, int z, ForgeDirection dir, int o) {
        if(!super.checkRequirement(world, x, y, z, dir, o)) return false;

        return MultiblockHandlerXR.checkSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[]{2, -1, 1, 1, 2, 2}, x, y, z, dir);
    }

    @Override
    protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        // Hollow machines hate this weird trick
        // ABRA CHUPACADABRA

        // Front/Back
        MultiblockHandlerXR.fillSpace(world, x + rot.offsetX * 2 + dir.offsetX, y + 2, z + rot.offsetZ * 2 + dir.offsetZ, new int[] {0, 1, 0, 0, 0, 4}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x + rot.offsetX * 2 - dir.offsetX, y + 2, z + rot.offsetZ * 2 - dir.offsetZ, new int[] {0, 1, 0, 0, 0, 4}, this, dir);

        // Top
        MultiblockHandlerXR.fillSpace(world, x + rot.offsetX * 2, y + 2, z + rot.offsetZ * 2, new int[] {0, 0, 0, 0, 0, 4}, this, dir);

        // Side caps
        MultiblockHandlerXR.fillSpace(world, x + rot.offsetX * 2, y + 1, z + rot.offsetZ * 2, new int[] {1, 0, 1, 1, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x - rot.offsetX * 2, y + 1, z - rot.offsetZ * 2, new int[] {1, 0, 1, 1, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {2, 0, 0, 0, -2, 2}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {2, 0, 0, 0, 2, -2}, this, dir);

        // Top connector needs to point laterally to avoid disconnecting when light blocks are added
        MultiblockHandlerXR.fillSpace(world, x + dir.offsetX, y + 2, z + dir.offsetZ, new int[] {0, 0, 1, 0, 0, 0}, this, dir);

        // Base
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 0, 0, 0, 2, 2}, this, dir);

        makeExtra(world, x + rot.offsetX * 2, y, z + rot.offsetZ * 2);
        makeExtra(world, x - rot.offsetX * 2, y, z - rot.offsetZ * 2);
        makeExtra(world, x, y + 2, z);
    }

    // Only the blocks immediately horizontally adjacent to the core can actually sustain plants, so we do a 4 block search in all cardinals
    @Override
    public boolean canSustainPlant(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing direction, @NotNull IPlantable plantable) {
        return this.getMetaFromState(state) >= 12
                || this.getMetaFromState(world.getBlockState(pos.east())) >= 12
                || this.getMetaFromState(world.getBlockState(pos.west())) >= 12
                || this.getMetaFromState(world.getBlockState(pos.south())) >= 12
                || this.getMetaFromState(world.getBlockState(pos.north())) >= 12;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {
        int[] pos = this.findCore(world, x, y, z);

        if(pos == null)
            return;

        TileEntity te = world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));

        if(!(te instanceof TileEntityHydroponic hydro))
            return;

        List<String> text = new ArrayList<>();

        text.add((hydro.getPower() <= 200 ? TextFormatting.RED : TextFormatting.GREEN) + "Power: " + BobMathUtil.getShortNumber(hydro.getPower()) + "HE");

        text.add(TextFormatting.GREEN + "-> " + TextFormatting.RESET + hydro.tanks[0].getTankType().getLocalizedName() + ": " + hydro.tanks[0].getFill() + "/" + hydro.tanks[0].getMaxFill() + "mB");
        text.add(TextFormatting.RED + "<- " + TextFormatting.RESET + hydro.tanks[1].getTankType().getLocalizedName() + ": " + hydro.tanks[1].getFill() + "/" + hydro.tanks[1].getMaxFill() + "mB");

        ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getTranslationKey() + ".name"), 0xffff00, 0x404000, text);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.standardOpenBehavior(world, pos, player, 0);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

}
