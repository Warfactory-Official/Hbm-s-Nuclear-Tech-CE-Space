package com.hbmspace.items.weapon;

import com.hbm.items.ModItems;
import com.hbm.items.weapon.ItemMissile;
import com.hbm.util.I18nUtil;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Th3_Sl1ze: a space version, of course. We're changing some attributes slightly here
// I thought of mixin for injecting the required attributes onto existing parts, but then I thought... eh, that will look like a fucking spaghetti
public class ItemCustomMissilePart extends ItemMissile {

    /**
     * == Chips ==
     * [0]: inaccuracy
     *
     * == Warheads ==
     * [0]: type
     * [1]: strength/radius/cluster count
     * [2]: weight
     *
     * == Fuselages ==
     * [0]: type
     * [1]: tank size
     *
     * == Stability ==
     * [0]: inaccuracy mod
     *
     * == Thrusters ===
     * [0]: type
     * [1]: consumption
     * [2]: lift strength
     * ROCKET SPECIFIC
     * [3]: thrust (N)
     * [4]: ISP (s)
     */

    public ItemCustomMissilePart(String s) {
        super(s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        setMaxStackSize(64);
    }

    public ItemCustomMissilePart makeChip(float inaccuracy) {
        this.type = ItemMissile.PartType.CHIP;
        this.top = ItemMissile.PartSize.ANY;
        this.bottom = ItemMissile.PartSize.ANY;
        this.attributes = new Object[]{inaccuracy};
        parts.put(this.hashCode(), this);
        return this;
    }

    public ItemCustomMissilePart makeWarhead(WarheadType type, float punch, int weight, PartSize size) {
        this.type = ItemMissile.PartType.WARHEAD;
        this.top = ItemMissile.PartSize.NONE;
        this.bottom = size;
        this.mass = weight;
        this.attributes = new Object[]{type, punch};
        parts.put(this.hashCode(), this);
        return this;
    }

    public ItemCustomMissilePart makeFuselage(FuelType type, float fuel, int mass, PartSize top, PartSize bottom) {
        this.type = ItemMissile.PartType.FUSELAGE;
        this.top = top;
        this.bottom = bottom;
        this.mass = mass;
        this.attributes = new Object[]{type, fuel};
        parts.put(this.hashCode(), this);
        return this;
    }

    public ItemCustomMissilePart makeStability(float inaccuracy, PartSize size) {
        this.type = ItemMissile.PartType.FINS;
        this.top = size;
        this.bottom = size;
        this.attributes = new Object[]{inaccuracy};
        parts.put(this.hashCode(), this);
        return this;
    }

    public ItemCustomMissilePart makeThruster(FuelType type, float consumption, float lift, PartSize size, int thrust, int mass, int isp) {
        this.type = PartType.THRUSTER;
        this.top = size;
        this.bottom = PartSize.NONE;
        this.mass = mass;
        this.attributes = new Object[] { type, consumption, lift, thrust, isp };
        parts.put(this.hashCode(), this);

        return this;
    }

    public static final Map<Item, Object[]> THRUSTER_ATTRIBUTES = new HashMap<>();

    public static void initSpaceThrusters() {
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_10_kerosene, new Object[] { ItemMissile.FuelType.KEROSENE, 1F, 1_500, 16_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_10_solid,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 1_500, 60_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_10_xenon,    new Object[] { ItemMissile.FuelType.XENON,    1F, 1_500, 2_000, 4200 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_kerosene,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 7_500,   120_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_kerosene_dual,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 2_500,   200_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_kerosene_triple,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 5_000,   280_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_solid,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 5_000,   220_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_solid_hexdecuple,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 5_000,   260_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_hydrogen,    new Object[] { ItemMissile.FuelType.HYDROGEN,    1F, 7_500,   100_000, 380 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_hydrogen_dual,    new Object[] { ItemMissile.FuelType.HYDROGEN,    1F, 2_500,   200_000, 380 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire_short,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 5_000,   800_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 5_000,   1_000_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire_large,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 7_500,   1_200_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire_large_rad,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 7_500,   1_200_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_kerosene,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 100_000,   1_536_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_kerosene_dual,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 100_000,   1_934_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_kerosene_triple,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 100_000,   2_542_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_solid,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 100_000,   1_400_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_solid_multi,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 100_000,   1_830_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_solid_multier,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 100_000,   2_320_000, 195 });
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        if(this == ModItemsSpace.rp_pod_20) return;

        if(title != null)
            list.add(TextFormatting.DARK_PURPLE + "\"" + title + "\"");

        try {
            switch (type) {
                case CHIP ->
                        list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.inaccuracy") + ": " + TextFormatting.GRAY + (Float) attributes[0] * 100 + "%");
                case WARHEAD -> {
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.size") + ": " + TextFormatting.GRAY + getSize(bottom));
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.type") + ": " + TextFormatting.GRAY + getWarhead((WarheadType) attributes[0]));
                    if (attributes[0] != WarheadType.APOLLO && attributes[0] != WarheadType.SATELLITE)
                        list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.strength") + ": " + TextFormatting.GRAY + attributes[1]);
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.mass") + ": " + TextFormatting.GRAY + mass + "kg");
                }
                case FUSELAGE -> {
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.topSize") + ": " + TextFormatting.GRAY + getSize(top));
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.bottomSize") + ": " + TextFormatting.GRAY + getSize(bottom));
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.fuelType") + ": " + TextFormatting.GRAY + getFuelName());
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.fuelAmount") + ": " + TextFormatting.GRAY + attributes[1] + "mB");
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.mass") + ": " + TextFormatting.GRAY + mass + "kg");
                }
                case FINS -> {
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.size") + ": " + TextFormatting.GRAY + getSize(top));
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.inaccuracy") + ": " + TextFormatting.GRAY + (Float) attributes[0] * 100 + "%");
                }
                case THRUSTER -> {
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.size") + ": " + TextFormatting.GRAY + getSize(top));
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.fuelType") + ": " + TextFormatting.GRAY + getFuelName());
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.fuelConsumption") + ": " + TextFormatting.GRAY + attributes[1] + "l/tick");
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.maxPayload") + ": " + TextFormatting.GRAY + attributes[2] + "kg");
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.thrust") + ": " + TextFormatting.GRAY + attributes[3] + "N");
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.isp") + ": " + TextFormatting.GRAY + attributes[4] + "s");
                    list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.mass") + ": " + TextFormatting.GRAY + mass + "kg");
                }
            }
        } catch(Exception ex) {
            list.add(I18nUtil.resolveKey("error.generic"));
        }

        // if(type != PartType.CHIP)
        // 	list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.health") + ": " + TextFormatting.GRAY + health + "HP");

        if(this.rarity != null)
            list.add(TextFormatting.BOLD + I18nUtil.resolveKey("item.missile.part.rarity") + ": " + TextFormatting.GRAY + this.rarity.name);
        if(author != null)
            list.add(TextFormatting.WHITE + "   " + I18nUtil.resolveKey("item.missile.part.by") + " " + author);
        if(witty != null)
            list.add(TextFormatting.GOLD + "   " + TextFormatting.ITALIC + "\"" + witty + "\"");
    }

    public String getFuelName() {
        if(!(attributes[0] instanceof FuelType)) return TextFormatting.BOLD + I18nUtil.resolveKey("general.na");

        return switch ((FuelType) attributes[0]) {
            case ANY -> TextFormatting.GRAY + I18nUtil.resolveKey("item.custom_missile_part.fuel.any");
            case KEROSENE ->
                    TextFormatting.LIGHT_PURPLE + I18nUtil.resolveKey("item.custom_missile_part.fuel.kerosene");
            case METHALOX -> TextFormatting.YELLOW + I18nUtil.resolveKey("item.custom_missile_part.fuel.kerolox");
            case KEROLOX -> TextFormatting.LIGHT_PURPLE + I18nUtil.resolveKey("item.custom_missile_part.fuel.methalox");
            case SOLID -> TextFormatting.GOLD + I18nUtil.resolveKey("item.custom_missile_part.fuel.solid");
            case HYDROGEN -> TextFormatting.DARK_AQUA + I18nUtil.resolveKey("item.custom_missile_part.fuel.hydrogen");
            case XENON -> TextFormatting.DARK_PURPLE + I18nUtil.resolveKey("item.custom_missile_part.fuel.xenon");
            case BALEFIRE -> TextFormatting.GREEN + I18nUtil.resolveKey("item.custom_missile_part.fuel.balefire");
            case HYDRAZINE -> TextFormatting.AQUA + I18nUtil.resolveKey("item.custom_missile_part.fuel.hydrazine");
        };
    }
}
