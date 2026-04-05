package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes51.dex */
public class EnumUtils {
    public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> enumClass) {
        Map<String, E> map = new LinkedHashMap<>();
        for (E e : enumClass.getEnumConstants()) {
            map.put(e.name(), e);
        }
        return map;
    }

    public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
        return new ArrayList(Arrays.asList(enumClass.getEnumConstants()));
    }

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
        if (enumName == null) {
            return false;
        }
        try {
            Enum.valueOf(enumClass, enumName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static <E extends Enum<E>> E getEnum(Class<E> cls, String str) {
        if (str == null) {
            return null;
        }
        try {
            return (E) Enum.valueOf(cls, str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, Iterable<E> values) {
        checkBitVectorable(enumClass);
        Validate.notNull(values);
        long total = 0;
        for (E constant : values) {
            total |= (long) (1 << constant.ordinal());
        }
        return total;
    }

    public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, E... values) {
        Validate.noNullElements(values);
        return generateBitVector(enumClass, Arrays.asList(values));
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> enumClass, long value) {
        Enum[] enumArr = (Enum[]) checkBitVectorable(enumClass).getEnumConstants();
        EnumSet<E> results = EnumSet.noneOf(enumClass);
        for (Enum r1 : enumArr) {
            if ((((long) (1 << r1.ordinal())) & value) != 0) {
                results.add(r1);
            }
        }
        return results;
    }

    private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> enumClass) {
        Validate.notNull(enumClass, "EnumClass must be defined.", new Object[0]);
        E[] constants = enumClass.getEnumConstants();
        Validate.isTrue(constants != null, "%s does not seem to be an Enum type", enumClass);
        Validate.isTrue(constants.length <= 64, "Cannot store %s %s values in %s bits", Integer.valueOf(constants.length), enumClass.getSimpleName(), 64);
        return enumClass;
    }
}
