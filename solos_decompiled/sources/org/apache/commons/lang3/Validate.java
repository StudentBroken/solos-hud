package org.apache.commons.lang3;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes51.dex */
public class Validate {
    private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
    private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified inclusive range of %s to %s";
    private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
    private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
    private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";
    private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE = "The validated collection contains null element at index: %d";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE = "The validated collection index is invalid: %d";
    private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";

    public static void isTrue(boolean expression, String message, long value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, Long.valueOf(value)));
        }
    }

    public static void isTrue(boolean expression, String message, double value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, Double.valueOf(value)));
        }
    }

    public static void isTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException(DEFAULT_IS_TRUE_EX_MESSAGE);
        }
    }

    public static <T> T notNull(T t) {
        return (T) notNull(t, DEFAULT_IS_NULL_EX_MESSAGE, new Object[0]);
    }

    public static <T> T notNull(T object, String message, Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... values) {
        if (array == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return array;
    }

    public static <T> T[] notEmpty(T[] tArr) {
        return (T[]) notEmpty(tArr, DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE, new Object[0]);
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message, Object... values) {
        if (collection == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }

    public static <T extends Collection<?>> T notEmpty(T t) {
        return (T) notEmpty(t, DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE, new Object[0]);
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... values) {
        if (map == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }

    public static <T extends Map<?, ?>> T notEmpty(T t) {
        return (T) notEmpty(t, DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE, new Object[0]);
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T notEmpty(T t) {
        return (T) notEmpty(t, DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE, new Object[0]);
    }

    public static <T extends CharSequence> T notBlank(T chars, String message, Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (StringUtils.isBlank(chars)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T notBlank(T t) {
        return (T) notBlank(t, DEFAULT_NOT_BLANK_EX_MESSAGE, new Object[0]);
    }

    public static <T> T[] noNullElements(T[] array, String message, Object... values) {
        notNull(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                Object[] values2 = ArrayUtils.add((Integer[]) values, Integer.valueOf(i));
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }
        return array;
    }

    public static <T> T[] noNullElements(T[] tArr) {
        return (T[]) noNullElements(tArr, DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE, new Object[0]);
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable, String message, Object... values) {
        notNull(iterable);
        int i = 0;
        Iterator<?> it = iterable.iterator();
        while (it.hasNext()) {
            if (it.next() != null) {
                i++;
            } else {
                Object[] values2 = ArrayUtils.addAll(values, Integer.valueOf(i));
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }
        return iterable;
    }

    public static <T extends Iterable<?>> T noNullElements(T t) {
        return (T) noNullElements(t, DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE, new Object[0]);
    }

    public static <T> T[] validIndex(T[] array, int index, String message, Object... values) {
        notNull(array);
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return array;
    }

    public static <T> T[] validIndex(T[] tArr, int i) {
        return (T[]) validIndex(tArr, i, DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE, Integer.valueOf(i));
    }

    public static <T extends Collection<?>> T validIndex(T collection, int index, String message, Object... values) {
        notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return collection;
    }

    public static <T extends Collection<?>> T validIndex(T t, int i) {
        return (T) validIndex(t, i, DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE, Integer.valueOf(i));
    }

    public static <T extends CharSequence> T validIndex(T chars, int index, String message, Object... values) {
        notNull(chars);
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T validIndex(T t, int i) {
        return (T) validIndex(t, i, DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE, Integer.valueOf(i));
    }

    public static void validState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException(DEFAULT_VALID_STATE_EX_MESSAGE);
        }
    }

    public static void validState(boolean expression, String message, Object... values) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    public static void matchesPattern(CharSequence input, String pattern) {
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format(DEFAULT_MATCHES_PATTERN_EX, input, pattern));
        }
    }

    public static void matchesPattern(CharSequence input, String pattern, String message, Object... values) {
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static <T> void inclusiveBetween(T start, T end, Comparable<T> value) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static <T> void inclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static <T> void exclusiveBetween(T start, T end, Comparable<T> value) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    public static <T> void exclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj) {
        if (!type.isInstance(obj)) {
            Object[] objArr = new Object[2];
            objArr[0] = type.getName();
            objArr[1] = obj == null ? "null" : obj.getClass().getName();
            throw new IllegalArgumentException(String.format(DEFAULT_IS_INSTANCE_OF_EX_MESSAGE, objArr));
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message, Object... values) {
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void isAssignableFrom(Class<?> superType, Class<?> type) {
        if (!superType.isAssignableFrom(type)) {
            Object[] objArr = new Object[2];
            objArr[0] = type == null ? "null" : type.getName();
            objArr[1] = superType.getName();
            throw new IllegalArgumentException(String.format(DEFAULT_IS_ASSIGNABLE_EX_MESSAGE, objArr));
        }
    }

    public static void isAssignableFrom(Class<?> superType, Class<?> type, String message, Object... values) {
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
