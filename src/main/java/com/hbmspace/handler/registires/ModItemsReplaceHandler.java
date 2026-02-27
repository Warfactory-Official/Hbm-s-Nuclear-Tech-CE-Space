package com.hbmspace.handler.registires;

import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
import com.hbmspace.accessors.ICanSealAccessor;
import com.hbmspace.accessors.IHaveCorrosionProtAccessor;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;

public class ModItemsReplaceHandler {

    public static void initReplacings(RegistryEvent.Register<Item> event) {
        replaceInsert(event);
        replaceArmors(event);
        replaceItemBlocks(event);
    }

    private static void replaceInsert(RegistryEvent.Register<Item> event) {
        if (ModItemsSpace.insert_cmb instanceof IHaveCorrosionProtAccessor cmb_corprot) cmb_corprot.withCorrosionProtection();
        if (ModItems.insert_yharonite instanceof IHaveCorrosionProtAccessor yhar_corprot) yhar_corprot.withCorrosionProtection();
        ModItems.ALL_ITEMS.remove(ModItemsSpace.insert_cmb);
        ModItemsSpace.ALL_ITEMS.add(ModItemsSpace.insert_cmb);
        event.getRegistry().register(ModItemsSpace.insert_cmb);
    }

    private static void replaceArmors(RegistryEvent.Register<Item> event) {
        if(ModItems.t51_helmet instanceof ICanSealAccessor t51_seal) t51_seal.setSealed(true);
        if(ModItems.t51_plate instanceof ICanSealAccessor t51_seal) t51_seal.setSealed(true);
        if(ModItems.t51_legs instanceof ICanSealAccessor t51_seal) t51_seal.setSealed(true);
        if(ModItems.t51_boots instanceof ICanSealAccessor t51_seal) t51_seal.setSealed(true);
        if(ModItems.steamsuit_helmet instanceof ICanSealAccessor steam_seal) steam_seal.setSealed(true);
        if(ModItems.steamsuit_plate instanceof ICanSealAccessor steam_seal) steam_seal.setSealed(true);
        if(ModItems.steamsuit_legs instanceof ICanSealAccessor steam_seal) steam_seal.setSealed(true);
        if(ModItems.steamsuit_boots instanceof ICanSealAccessor steam_seal) steam_seal.setSealed(true);
        if(ModItems.envsuit_helmet instanceof ICanSealAccessor env_seal) env_seal.setSealed(true);
        if(ModItems.envsuit_plate instanceof ICanSealAccessor env_seal) env_seal.setSealed(true);
        if(ModItems.envsuit_legs instanceof ICanSealAccessor env_seal) env_seal.setSealed(true);
        if(ModItems.envsuit_boots instanceof ICanSealAccessor env_seal) env_seal.setSealed(true);
        if(ModItems.dieselsuit_helmet instanceof ICanSealAccessor diesel_seal) diesel_seal.setSealed(true);
        if(ModItems.dieselsuit_plate instanceof ICanSealAccessor diesel_seal) diesel_seal.setSealed(true);
        if(ModItems.dieselsuit_legs instanceof ICanSealAccessor diesel_seal) diesel_seal.setSealed(true);
        if(ModItems.dieselsuit_boots instanceof ICanSealAccessor diesel_seal) diesel_seal.setSealed(true);
        if(ModItems.trenchmaster_helmet instanceof ICanSealAccessor trench_seal) trench_seal.setSealed(true);
        if(ModItems.trenchmaster_plate instanceof ICanSealAccessor trench_seal) trench_seal.setSealed(true);
        if(ModItems.trenchmaster_legs instanceof ICanSealAccessor trench_seal) trench_seal.setSealed(true);
        if(ModItems.trenchmaster_boots instanceof ICanSealAccessor trench_seal) trench_seal.setSealed(true);
        if(ModItems.dns_helmet instanceof ICanSealAccessor dns_seal) dns_seal.setSealed(true);
        if(ModItems.dns_plate instanceof ICanSealAccessor dns_seal) dns_seal.setSealed(true);
        if(ModItems.dns_legs instanceof ICanSealAccessor dns_seal) dns_seal.setSealed(true);
        if(ModItems.dns_boots instanceof ICanSealAccessor dns_seal) dns_seal.setSealed(true);
        if(ModItems.fau_helmet instanceof ICanSealAccessor fau_seal) fau_seal.setSealed(true);
        if(ModItems.fau_plate instanceof ICanSealAccessor fau_seal) fau_seal.setSealed(true);
        if(ModItems.fau_legs instanceof ICanSealAccessor fau_seal) fau_seal.setSealed(true);
        if(ModItems.fau_boots instanceof ICanSealAccessor fau_seal) fau_seal.setSealed(true);
        if(ModItems.rpa_helmet instanceof ICanSealAccessor rpa_seal) rpa_seal.setSealed(true);
        if(ModItems.rpa_plate instanceof ICanSealAccessor rpa_seal) rpa_seal.setSealed(true);
        if(ModItems.rpa_legs instanceof ICanSealAccessor rpa_seal) rpa_seal.setSealed(true);
        if(ModItems.rpa_boots instanceof ICanSealAccessor rpa_seal) rpa_seal.setSealed(true);
        if(ModItems.ajr_helmet instanceof ICanSealAccessor ajr_seal) ajr_seal.setSealed(true);
        if(ModItems.ajr_plate instanceof ICanSealAccessor ajr_seal) ajr_seal.setSealed(true);
        if(ModItems.ajr_legs instanceof ICanSealAccessor ajr_seal) ajr_seal.setSealed(true);
        if(ModItems.ajr_boots instanceof ICanSealAccessor ajr_seal) ajr_seal.setSealed(true);
        if(ModItems.ajro_helmet instanceof ICanSealAccessor ajro_seal) ajro_seal.setSealed(true);
        if(ModItems.ajro_plate instanceof ICanSealAccessor ajro_seal) ajro_seal.setSealed(true);
        if(ModItems.ajro_legs instanceof ICanSealAccessor ajro_seal) ajro_seal.setSealed(true);
        if(ModItems.ajro_boots instanceof ICanSealAccessor ajro_seal) ajro_seal.setSealed(true);
        if(ModItems.hev_helmet instanceof ICanSealAccessor hev_seal) hev_seal.setSealed(true);
        if(ModItems.hev_plate instanceof ICanSealAccessor hev_seal) hev_seal.setSealed(true);
        if(ModItems.hev_legs instanceof ICanSealAccessor hev_seal) hev_seal.setSealed(true);
        if(ModItems.hev_boots instanceof ICanSealAccessor hev_seal) hev_seal.setSealed(true);
    }

    private static void replaceItemBlocks(RegistryEvent.Register<Item> event) {
        ItemBlock ore_oil_item = new ItemBlock(ModBlocks.ore_oil) {
            @Override
            public int getMetadata(int damage) {
                return damage;
            }
        };
        ore_oil_item.setHasSubtypes(true);
        ore_oil_item.setMaxDamage(0);

        try {
            Field regNameField = IForgeRegistryEntry.Impl.class.getDeclaredField("registryName");
            regNameField.setAccessible(true);
            regNameField.set(ore_oil_item, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ore_oil_item.setRegistryName("hbm", "ore_oil");
        event.getRegistry().register(ore_oil_item);
    }
}
