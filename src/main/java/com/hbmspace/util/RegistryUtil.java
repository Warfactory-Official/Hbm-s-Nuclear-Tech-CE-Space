package com.hbmspace.util;

import com.hbmspace.enums.EnumAddonTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistryUtil {

    public static <T extends IForgeRegistryEntry.Impl<?>> T forceRegistryName(T entry, String domain, String path) {
        EnumAddonTypes.setInstanceField(IForgeRegistryEntry.Impl.class, "registryName", entry, new ResourceLocation(domain, path));
        return entry;
    }
}
