package com.hbmspace.items.machine;

import com.hbm.items.ISatChip;
import com.hbm.saveddata.satellites.Satellite;
import com.hbm.util.I18nUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.items.weapon.ItemCustomMissilePart;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemSatelliteSpace extends ItemCustomMissilePart implements ISatChip {

    private boolean canLaunchByHand;

    public ItemSatelliteSpace(String s) {
        this(16_000, s);
    }

    public ItemSatelliteSpace(int mass, String s) {
        super(s);
        makeWarhead(WarheadType.SATELLITE, 15F, mass, PartSize.SIZE_20);
        if(mass <= 16_000) canLaunchByHand = true;
    }

    public ItemSatelliteSpace(int mass, WarheadType type, String s) {
        super(s);
        makeWarhead(type, 15F, mass, PartSize.SIZE_20);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World world, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, world, list, flagIn);

        list.add(I18nUtil.resolveKey("item.sat.desc.frequency") + ": " + getFreq(stack));

        /*if(this == ModItems.sat_foeq)
            list.add(I18nUtil.resolveKey("item.sat.desc.foeq"));

        if(this == ModItems.sat_gerald) {
            list.add(I18nUtil.resolveKey("item.sat.desc.gerald.single_use"));
            list.add(I18nUtil.resolveKey("item.sat.desc.gerald.orbital_module"));
            list.add(I18nUtil.resolveKey("item.sat.desc.gerald.melter"));
        }

        if(this == ModItems.sat_laser)
            list.add(I18nUtil.resolveKey("item.sat.desc.laser"));

        if(this == ModItems.sat_mapper)
            list.add(I18nUtil.resolveKey("item.sat.desc.mapper"));

        if(this == ModItems.sat_miner)
            list.add(I18nUtil.resolveKey("item.sat.desc.miner"));

        if(this == ModItems.sat_lunar_miner)
            list.add(I18nUtil.resolveKey("item.sat.desc.lunar_miner"));

        if(this == ModItems.sat_radar)
            list.add(I18nUtil.resolveKey("item.sat.desc.radar"));

        if(this == ModItems.sat_resonator)
            list.add(I18nUtil.resolveKey("item.sat.desc.resonator"));

        if(this == ModItems.sat_scanner)
            list.add(I18nUtil.resolveKey("item.sat.desc.scanner"));*/

        if(this == ModItemsSpace.sat_war)
            list.add(I18nUtil.resolveKey("item.sat.desc.war"));

        if(this == ModItemsSpace.sat_dyson_relay)
            list.add(I18nUtil.resolveKey("item.sat.desc.dyson_relay"));

        if(canLaunchByHand) {
            list.add(TextFormatting.GOLD + I18nUtil.resolveKey("item.sat.desc.launch_by_hand"));

            if(CelestialBody.inOrbit(world))
                list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.sat.desc.deploy_orbit"));
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(!canLaunchByHand) return new ActionResult<>(EnumActionResult.PASS, stack);
        if(!CelestialBody.inOrbit(world)) return new ActionResult<>(EnumActionResult.PASS, stack);

        if(!world.isRemote) {
            int targetDimensionId = CelestialBody.getTarget(world, (int)player.posX, (int)player.posZ).body.dimensionId;
            WorldServer targetWorld = DimensionManager.getWorld(targetDimensionId);
            if(targetWorld == null) {
                DimensionManager.initDimension(targetDimensionId);
                targetWorld = DimensionManager.getWorld(targetDimensionId);

                if(targetWorld == null) return new ActionResult<>(EnumActionResult.PASS, stack);
            }

            Satellite.orbit(targetWorld, Satellite.getIDFromItem(stack.getItem()), getFreq(stack), player.posX, player.posY, player.posZ);

            player.sendMessage(new TextComponentString("Satellite launched successfully!"));
        }

        stack.shrink(1);

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

}
