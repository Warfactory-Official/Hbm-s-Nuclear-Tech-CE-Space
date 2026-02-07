package com.hbmspace.enums;

import net.minecraftforge.common.util.EnumHelper;

import java.lang.reflect.Field;

import static com.hbm.lib.internal.UnsafeHolder.U;

/**
 * Generic utility for adding values to any enum at runtime and updating
 * fields that cache the enum's values array.
 *
 * @author Th3_Sl1ze
 */
public final class EnumAddonTypes {

    private EnumAddonTypes() {}

    public static void init() {
        EnumAddonWatzTypes.init();
        EnumAddonWasteTypes.init();
        EnumAddonRBMKColumn.init();
        EnumAddonRBMKRodTypes.init();
    }

    /**
     * Adds a new constant to the given enum type.
     *
     * @param enumClass  the enum class to extend
     * @param name       the name of the new constant (must be unique)
     * @param paramTypes the parameter types of the enum constructor to invoke
     *                   (excluding the synthetic {@code String name, int ordinal})
     * @param args       the arguments matching {@code paramTypes}
     * @param <E>        enum type
     * @return the newly created enum constant
     */
    public static <E extends Enum<E>> E addEnum(Class<E> enumClass, String name,
                                                Class<?>[] paramTypes, Object... args) {
        return EnumHelper.addEnum(enumClass, name, paramTypes, args);
    }

    /**
     * Replaces a <b>static</b> field's value with the current {@code values()}
     * array of the given enum.  Useful for fields like
     * {@code private static final MyEnum[] VALUES = values();}.
     *
     * @param enumClass the enum whose {@code values()} to use
     * @param fieldName the name of the static field to update
     * @param <E>       enum type
     */
    public static <E extends Enum<E>> void updateStaticValuesField(Class<E> enumClass,
                                                                   String fieldName) {
        updateStaticValuesField(enumClass, enumClass, fieldName);
    }

    /**
     * Replaces a <b>static</b> field declared in {@code ownerClass} with the
     * current {@code values()} array of {@code enumClass}.
     *
     * @param enumClass  the enum whose {@code values()} to use
     * @param ownerClass the class that declares the static field
     * @param fieldName  the name of the static field
     * @param <E>        enum type
     */
    public static <E extends Enum<E>> void updateStaticValuesField(Class<E> enumClass,
                                                                   Class<?> ownerClass,
                                                                   String fieldName) {
        try {
            Field field = findFieldInHierarchy(ownerClass, fieldName);
            setStaticFieldUnsafe(field, enumClass.getEnumConstants());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces an <b>instance</b> field on one or more objects with the current
     * {@code values()} array of the given enum.  The field is looked up starting
     * from {@code declaringClass} and walking up the hierarchy.
     *
     * @param enumClass      the enum whose {@code values()} to use
     * @param declaringClass the class (or a subclass) where the field is declared
     * @param fieldName      the name of the instance field
     * @param targets        one or more object instances to update
     * @param <E>            enum type
     */
    public static <E extends Enum<E>> void updateInstanceField(Class<E> enumClass,
                                                               Class<?> declaringClass,
                                                               String fieldName,
                                                               Object... targets) {
        try {
            Field field = findFieldInHierarchy(declaringClass, fieldName);
            Object newValues = enumClass.getEnumConstants();
            for (Object target : targets) {
                setInstanceFieldUnsafe(field, target, newValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets any static field to an arbitrary value via Unsafe.
     *
     * @param ownerClass the class declaring the field
     * @param fieldName  the field name
     * @param value      the new value
     */
    public static void setStaticField(Class<?> ownerClass, String fieldName, Object value) {
        try {
            Field field = findFieldInHierarchy(ownerClass, fieldName);
            setStaticFieldUnsafe(field, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets any instance field on the given target to an arbitrary value via
     * Unsafe.
     *
     * @param declaringClass the class (or superclass) where the field is declared
     * @param fieldName      the field name
     * @param target         the object instance
     * @param value          the new value
     */
    public static void setInstanceField(Class<?> declaringClass, String fieldName,
                                        Object target, Object value) {
        try {
            Field field = findFieldInHierarchy(declaringClass, fieldName);
            setInstanceFieldUnsafe(field, target, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Walks the class hierarchy upwards looking for a declared field.
     *
     * @throws NoSuchFieldException if the field is not found in any superclass
     */
    private static Field findFieldInHierarchy(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName
                + "' not found in hierarchy of " + clazz.getName());
    }

    private static void setStaticFieldUnsafe(Field field, Object value) {
        Object base = U.staticFieldBase(field);
        long offset = U.staticFieldOffset(field);
        U.putReference(base, offset, value);
    }

    private static void setInstanceFieldUnsafe(Field field, Object target, Object value) {
        long offset = U.objectFieldOffset(field);
        U.putReference(target, offset, value);
    }
}