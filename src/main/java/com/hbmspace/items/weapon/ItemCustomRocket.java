package com.hbmspace.items.weapon;

import com.hbmspace.handler.RocketStruct;
import com.hbm.items.ISatChip;
import com.hbm.items.ItemBase;
import com.hbm.items.ModItems;
import com.hbm.util.I18nUtil;
import com.hbmspace.items.ItemBakedSpace;
import com.hbmspace.items.ModItemsSpace;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemCustomRocket extends ItemBakedSpace implements ISatChip {

    public ItemCustomRocket(String s){
        super(s);
    }

    public static ItemStack build(RocketStruct rocket) {
        ItemStack stack = new ItemStack(ModItemsSpace.rocket_custom);

        stack.setTagCompound(new NBTTagCompound());
        rocket.writeToNBT(stack.getTagCompound());

        return stack;
    }

    public static ItemStack build(RocketStruct rocket, boolean hasFuel) {
        ItemStack stack = build(rocket);

        setFuel(stack, hasFuel);

        return stack;
    }

    public static RocketStruct get(ItemStack stack) {
        if(stack == null || !(stack.getItem() instanceof ItemCustomRocket) || stack.getTagCompound() == null)
            return null;

        return RocketStruct.readFromNBT(stack.getTagCompound());
    }

    public static boolean hasFuel(ItemStack stack) {
        if(stack == null || stack.getTagCompound() == null) return false;
        return stack.getTagCompound().getBoolean("hasFuel");
    }

    public static void setFuel(ItemStack stack, boolean hasFuel) {
        if(stack == null) return;
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setBoolean("hasFuel", hasFuel);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World world, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        RocketStruct rocket = get(stack);

        if(rocket == null) return;

        list.add(ChatFormatting.BOLD + "Payload: " + ChatFormatting.GRAY + I18nUtil.resolveKey(rocket.capsule.getTranslationKey() + ".name"));
        list.add(ChatFormatting.BOLD + "Stages: " + ChatFormatting.GRAY + rocket.stages.size());

        if(hasFuel(stack)) {
            list.add(ChatFormatting.GRAY + "Is fully fueled");
        }

        if(getFreq(stack) != 0) {
            list.add(ChatFormatting.BOLD + "Satellite Frequency: " + ChatFormatting.GRAY + getFreq(stack));
        }
    }

}
