package com.hbmspace.enums;


import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.PlantEnums;
import com.hbm.blocks.generic.BlockMeta;
import com.hbm.blocks.generic.BlockNTMFlower;
import com.hbm.render.block.BlockBakeFrame;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class EnumAddonFlowerPlantTypes {

    public static PlantEnums.EnumFlowerPlantType STRAWBERRY;
    public static PlantEnums.EnumFlowerPlantType MINT;

    private static final Class<?>[] PARAM_TYPES = new Class<?>[] { boolean.class };

    public static void init() {
        STRAWBERRY = addPlantType("STRAWBERRY", false);
        MINT = addPlantType("MINT", false);

        EnumAddonTypes.updateStaticValuesField(PlantEnums.EnumFlowerPlantType.class, "VALUES");
        EnumAddonTypes.updateInstanceField(PlantEnums.EnumFlowerPlantType.class,
                BlockNTMFlower.class, "blockEnum",
                ModBlocks.plant_flower);

        EnumAddonTypes.setInstanceField(
                BlockMeta.class,
                "META_COUNT",
                ModBlocks.plant_flower,
                (short) PlantEnums.EnumFlowerPlantType.values().length
        );

        BlockBakeFrame[] newFrames = Arrays.stream(PlantEnums.EnumFlowerPlantType.values())
                .sorted(Comparator.comparing(Enum::ordinal))
                .map(Enum::name)
                .map(name -> "hbm:" + name.toLowerCase(Locale.US))
                .map(texture -> BlockBakeFrame.cross("plant_flower_" + texture.split(":")[1]))
                .toArray(BlockBakeFrame[]::new);

        EnumAddonTypes.setInstanceField(
                BlockMeta.class,
                "blockFrames",
                ModBlocks.plant_flower,
                newFrames
        );
    }

    private static PlantEnums.EnumFlowerPlantType addPlantType(String name, boolean needsOil) {
        return EnumAddonTypes.addEnum(PlantEnums.EnumFlowerPlantType.class, name, PARAM_TYPES, needsOil);
    }

}