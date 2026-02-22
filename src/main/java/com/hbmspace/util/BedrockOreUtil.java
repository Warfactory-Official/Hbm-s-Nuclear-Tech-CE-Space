package com.hbmspace.util;

import com.hbm.items.special.ItemBedrockOreNew;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.enums.EnumAddonBedrockOreTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import java.util.*;

public class BedrockOreUtil {
    private static final Map<Long, NoiseGeneratorPerlin> GENERATORS = new HashMap<>();

    private static NoiseGeneratorPerlin getGenerator(long seed) {
        return GENERATORS.computeIfAbsent(seed, key -> new NoiseGeneratorPerlin(new Random(seed), 4));
    }

    public static double getOreAmount(ItemStack stack, ItemBedrockOreNew.BedrockOreType type) {
        if(!stack.hasTagCompound()) return 1;
        NBTTagCompound data = stack.getTagCompound();
        return data.getDouble(type.suffix);
    }

    public static SolarSystem.Body getOreBody(ItemStack stack) {
        if(stack.getItemDamage() >= SolarSystem.Body.values().length || stack.getItemDamage() <= 0) return SolarSystem.Body.KERBIN;
        return SolarSystem.Body.values()[stack.getItemDamage()];
    }

    public static List<ItemBedrockOreNew.BedrockOreType> getTypesForBody(SolarSystem.Body body) {
        List<ItemBedrockOreNew.BedrockOreType> types = new ArrayList<>();
        ItemBedrockOreNew.BedrockOreType[] allTypes = EnumAddonBedrockOreTypes.ALL_TYPES;
        if (allTypes == null) allTypes = ItemBedrockOreNew.BedrockOreType.class.getEnumConstants();

        for (ItemBedrockOreNew.BedrockOreType type : allTypes) {
            String bodyName = EnumAddonBedrockOreTypes.BODY_MAP.get(type);
            if (bodyName != null && bodyName.equals(body.name())) {
                types.add(type);
            }
        }
        return types;
    }

    public static void setOreAmount(World world, ItemStack stack, int x, int z, double mult) {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound data = stack.getTagCompound();

        SolarSystem.Body body = CelestialBody.getEnum(world);
        stack.setItemDamage(body.ordinal());

        for(ItemBedrockOreNew.BedrockOreType type : getTypesForBody(body)) {
            data.setDouble(type.suffix, getOreLevel(world, x, z, type) * mult);
        }
    }

    public static double getOreLevel(World world, int x, int z, ItemBedrockOreNew.BedrockOreType type) {
        long seed = world.getSeed() + world.provider.getDimension();

        NoiseGeneratorPerlin level = getGenerator(seed);
        // Use type.ordinal() since index is no longer explicitly defined in the enum
        NoiseGeneratorPerlin ore = getGenerator(seed - 4096 + type.ordinal());

        double scale = 0.01D;

        return MathHelper.clamp(Math.abs(level.getValue(x * scale, z * scale) * ore.getValue(x * scale, z * scale)) * 0.05, 0.0, 2.0);
    }

}
