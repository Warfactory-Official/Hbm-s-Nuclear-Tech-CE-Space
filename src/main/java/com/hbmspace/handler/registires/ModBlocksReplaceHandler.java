package com.hbmspace.handler.registires;

import com.hbm.blocks.ModBlocks;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.generic.BlockOreFluid;
import com.hbmspace.enums.EnumAddonTypes;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import static com.hbmspace.blocks.ModBlocksSpace.ore_oil_empty;

public class ModBlocksReplaceHandler {

    public static void initReplacings(RegistryEvent.Register<Block> event) {
        Block ore_oil_override = new BlockOreFluid("ore_oil", ore_oil_empty, BlockOreFluid.ReserveType.OIL).setCreativeTab(MainRegistry.blockTab).setHardness(5.0F).setResistance(10.0F);
        Block ore_bedrock_oil_override = new BlockOreFluid("ore_bedrock_oil", null, BlockOreFluid.ReserveType.OIL).setCreativeTab(MainRegistry.blockTab).setBlockUnbreakable().setResistance(1_000_000);
        // Th3_Sl1ze: lmao, EnumAddonTypes was useful not only for enums..
        EnumAddonTypes.setInstanceField(IForgeRegistryEntry.Impl.class, "registryName", ore_oil_override, null);
        EnumAddonTypes.setInstanceField(IForgeRegistryEntry.Impl.class, "registryName", ore_bedrock_oil_override, null);

        ore_oil_override.setRegistryName("hbm", "ore_oil");
        ore_bedrock_oil_override.setRegistryName("hbm", "ore_bedrock_oil");
        event.getRegistry().register(ore_oil_override);
        event.getRegistry().register(ore_bedrock_oil_override);

        EnumAddonTypes.setStaticField(ModBlocks.class, "ore_oil", ore_oil_override);
        EnumAddonTypes.setStaticField(ModBlocks.class, "ore_bedrock_oil", ore_bedrock_oil_override);
    }
}
