package com.hbmspace.handler.jei;

import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.items.special.ItemBedrockOreNew;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.dim.trait.CBT_Water;
import com.hbmspace.util.BedrockOreUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CelestialJeiRecipes {
    public static HashMap<Object, Object> getAtmoRecipes() {
        HashMap<Object, Object> map = new HashMap<>();

        for (SolarSystem.Body bodyEnum : SolarSystem.Body.values()) {
            CelestialBody body = bodyEnum.getBody();
            if (body == null) continue;

            CBT_Atmosphere atmosphere = body.getDefaultTrait(CBT_Atmosphere.class);
            if (atmosphere == null) continue;

            ItemStack[] outputs = new ItemStack[atmosphere.fluids.size()];
            for (int i = 0; i < outputs.length; i++) {
                CBT_Atmosphere.FluidEntry entry = atmosphere.fluids.get(i);
                outputs[i] = ItemFluidIcon.make(entry.fluid, (int) entry.pressure);
            }

            map.put(body, outputs);
        }

        return map;
    }

    public static HashMap<Object, Object> getBedrockRecipes() {
        HashMap<Object, Object> map = new HashMap<>();

        for (SolarSystem.Body bodyEnum : SolarSystem.Body.values()) {
            CelestialBody body = bodyEnum.getBody();
            if (body == null) continue;

            ArrayList<ItemStack> outputs = new ArrayList<>();

            List<ItemBedrockOreNew.BedrockOreType> types = BedrockOreUtil.getTypesForBody(bodyEnum);
            if (!types.isEmpty()) {
                outputs.add(new ItemStack(ModItems.bedrock_ore_base, 1, bodyEnum.ordinal()));
            }

            if (body.hasIce) {
                outputs.add(new ItemStack(Blocks.PACKED_ICE, 32));
            }

            if (!outputs.isEmpty()) {
                map.put(body, outputs.toArray(new ItemStack[0]));
            }
        }

        return map;
    }

    public static HashMap<Object, Object> getOilRecipes() {
        HashMap<Object, Object> map = new HashMap<>();

        for (SolarSystem.Body bodyEnum : SolarSystem.Body.values()) {
            CelestialBody body = bodyEnum.getBody();
            if (body == null) continue;

            ArrayList<ItemStack> outputs = new ArrayList<>();

            switch (bodyEnum) {
                case KERBIN:
                case DUNA:
                    outputs.add(ItemFluidIcon.make(Fluids.OIL, 1000));
                    outputs.add(ItemFluidIcon.make(Fluids.GAS, 200));
                    break;
                case EVE:
                    outputs.add(ItemFluidIcon.make(Fluids.GAS, 1000));
                    outputs.add(ItemFluidIcon.make(Fluids.PETROLEUM, 200));
                    break;
                case LAYTHE:
                    outputs.add(ItemFluidIcon.make(Fluids.OIL_DS, 1000));
                    outputs.add(ItemFluidIcon.make(Fluids.GAS, 200));
                    break;
                case TEKTO:
                    outputs.add(ItemFluidIcon.make(com.hbmspace.inventory.fluid.Fluids.TCRUDE, 1000));
                    outputs.add(ItemFluidIcon.make(com.hbmspace.inventory.fluid.Fluids.HGAS, 200));
                    break;
                case MUN:
                case MINMUS:
                case IKE:
                    outputs.add(ItemFluidIcon.make(com.hbmspace.inventory.fluid.Fluids.BRINE, 1000));
                    break;
                default: break;
            }

            if (outputs.isEmpty()) continue;

            map.put(body, outputs.toArray(new ItemStack[0]));
        }

        return map;
    }

    // Th3_Sl1ze: yes, I intended to name it "walter"
    public static HashMap<Object, Object> getWalterRecipes() {
        HashMap<Object, Object> map = new HashMap<>();

        for (SolarSystem.Body bodyEnum : SolarSystem.Body.values()) {
            CelestialBody body = bodyEnum.getBody();
            if (body == null) continue;

            CBT_Water table = body.getDefaultTrait(CBT_Water.class);
            if (table == null) continue;

            map.put(body, ItemFluidIcon.make(table.fluid, table.fluid == Fluids.WATER ? 10_000 : 1_000));
        }

        return map;
    }
}
