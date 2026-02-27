package com.hbmspace.handler.registires;

import com.hbm.blocks.ModBlocks;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.blocks.generic.BlockOreFluid;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static com.hbmspace.blocks.ModBlocksSpace.ore_oil_empty;

public class ModBlocksReplaceHandler {

    public static void initReplacings(RegistryEvent.Register<Block> event) {
        Block ore_oil_override = new BlockOreFluid("ore_oil", ore_oil_empty, BlockOreFluid.ReserveType.OIL).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
        Block ore_bedrock_oil_override = new BlockOreFluid("ore_bedrock_oil", null, BlockOreFluid.ReserveType.OIL).setCreativeTab(MainRegistry.blockTab).setBlockUnbreakable().setResistance(1_000_000);;

        try {
            Field regNameField = IForgeRegistryEntry.Impl.class.getDeclaredField("registryName");
            regNameField.setAccessible(true);
            regNameField.set(ore_oil_override, null); // it doesn't fucking comply to any logic, but it works. WORKS, mein Gott
            regNameField.set(ore_bedrock_oil_override, null); // it doesn't fucking comply to any logic, but it works. WORKS, mein Gott
        } catch (Exception e) {
            e.printStackTrace();
        }

        ore_oil_override.setRegistryName("hbm", "ore_oil");
        ore_bedrock_oil_override.setRegistryName("hbm", "ore_bedrock_oil");
        event.getRegistry().register(ore_oil_override);
        event.getRegistry().register(ore_bedrock_oil_override);

        try {
            Field field_oil = ModBlocks.class.getDeclaredField("ore_oil");
            Field field_boil = ModBlocks.class.getDeclaredField("ore_bedrock_oil");
            field_oil.setAccessible(true);
            field_boil.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field_oil, field_oil.getModifiers() & ~Modifier.FINAL);
            modifiersField.setInt(field_boil, field_boil.getModifiers() & ~Modifier.FINAL);

            field_oil.set(null, ore_oil_override);
            field_boil.set(null, ore_bedrock_oil_override);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
