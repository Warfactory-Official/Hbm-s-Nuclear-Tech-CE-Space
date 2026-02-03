package com.hbmspace.items.enums;

import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemWatzPellet;
import com.hbm.util.Function;
import net.minecraftforge.common.util.EnumHelper;

import java.lang.reflect.Field;

import static com.hbm.lib.internal.UnsafeHolder.U;

public class EnumAddonWatzTypes {

    public static ItemWatzPellet.EnumWatzType PU241;
    public static ItemWatzPellet.EnumWatzType AMF;
    public static ItemWatzPellet.EnumWatzType AMRG;
    public static ItemWatzPellet.EnumWatzType CMRG;
    public static ItemWatzPellet.EnumWatzType CMF;
    public static ItemWatzPellet.EnumWatzType BK247;
    public static ItemWatzPellet.EnumWatzType CF252;
    public static ItemWatzPellet.EnumWatzType CF251;
    public static ItemWatzPellet.EnumWatzType ES253;

    private static final Class<?>[] PARAM_TYPES = new Class<?>[] {
            int.class, int.class, double.class, double.class, double.class,
            Function.class, Function.class, Function.class
    };

    public static void init() {
        PU241 = addWatzType("PU241", 0x78817E, 394240, 1950D, 25D, 0.0025D,
                new Function.FunctionLinear(1.30D),
                new Function.FunctionSqrt(2.66D / 18D).withOff(24D * 24D),
                null);

        AMF = addWatzType("AMF", 0x93767B, 0x66474D, 2333D, 44D, 0.003D,
                new Function.FunctionLinear(1.33D),
                new Function.FunctionSqrt(4.11D / 22.2D).withOff(27D * 27D),
                null);

        AMRG = addWatzType("AMRG", 0x93767B, 0x66474D, 2888D, 48D, 0.0035D,
                new Function.FunctionLinear(1.33D),
                new Function.FunctionSqrt(4.33D / 25.5D).withOff(28D * 28D),
                null);

        CMRG = addWatzType("CMRG", 0xD8C2C4, 0xAD9799, 2999D, 50D, 0.005D,
                new Function.FunctionLinear(1.5D),
                new Function.FunctionSqrt(5.5D / 25.5D).withOff(30D * 28D),
                null);

        CMF = addWatzType("CMF", 0xD8C2C4, 0xAD9799, 2444D, 48D, 0.0045D,
                new Function.FunctionLinear(1.8D),
                new Function.FunctionSqrt(5.0D / 20D).withOff(26D * 24D),
                null);

        BK247 = addWatzType("BK247", 0xC2C9C7, 0x8D9592, 3000D, 55D, 0.012D,
                new Function.FunctionLinear(1.5D),
                new Function.FunctionSqrt(6.0D / 23.5D).withOff(10D * 10D),
                null);

        CF251 = addWatzType("CF251", 0x7879B4, 0x4D4E89, 1250D, 60D, 0.001D,
                new Function.FunctionLinear(1.7D),
                new Function.FunctionSqrt(6.65D / 23.5D).withOff(10D * 10D),
                null);

        CF252 = addWatzType("CF252", 0x7879B4, 0x4D4E89, 1050D, 120D, 0.0015D,
                new Function.FunctionLinear(1.8D),
                new Function.FunctionSqrt(8.85D / 28.8D).withOff(10D * 10D),
                null);

        ES253 = addWatzType("ES253", 0xB9BFB2, 0x594E44, 3750D, 70D, 0.0001D,
                new Function.FunctionLinear(1.3D),
                new Function.FunctionSqrt(7.0D / 27.7D).withOff(10D * 10D),
                null);

        updateValuesArray();
        updateItemEnumFields();
    }

    private static ItemWatzPellet.EnumWatzType addWatzType(String name, Object... params) {
        return EnumHelper.addEnum(ItemWatzPellet.EnumWatzType.class, name, PARAM_TYPES, params);
    }

    private static void updateItemEnumFields() {
        try {
            ItemWatzPellet.EnumWatzType[] newValues = ItemWatzPellet.EnumWatzType.values();

            Field theEnumField = findFieldInHierarchy(ItemWatzPellet.class, "theEnum");
            setFieldValueUnsafe(theEnumField, ModItems.watz_pellet, newValues);
            setFieldValueUnsafe(theEnumField, ModItems.watz_pellet_depleted, newValues);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Field findFieldInHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Continue to superclass
            }
        }
        throw new NoSuchFieldException("Could not find field: " + fieldName);
    }

    private static void setFieldValueUnsafe(Field field, Object target, Object value) {
        long offset = U.objectFieldOffset(field);
        U.putReference(target, offset, value);
    }

    private static void setStaticFieldValueUnsafe(Field field, Object value) {
        Object base = U.staticFieldBase(field);
        long offset = U.staticFieldOffset(field);
        U.putReference(base, offset, value);
    }

    private static void updateValuesArray() {
        try {
            Field valuesField = ItemWatzPellet.EnumWatzType.class.getDeclaredField("VALUES");
            setStaticFieldValueUnsafe(valuesField, ItemWatzPellet.EnumWatzType.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}