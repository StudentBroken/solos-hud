package com.ua.sdk.util;

import android.os.Looper;
import java.util.Collection;
import java.util.Random;

/* JADX INFO: loaded from: classes65.dex */
public final class Utility {
    private Utility() {
    }

    public static <T> boolean containsNull(Collection<T> container) {
        if (container == null) {
            throw new NullPointerException();
        }
        for (T item : container) {
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    public static long generateId() {
        long id;
        Random random = new Random();
        do {
            id = Math.abs(random.nextLong());
        } while (id == 0);
        return id;
    }

    public static String randomString(String chars, int length) {
        StringBuilder sb = new StringBuilder();
        if (chars.length() > 0) {
            Random random = new Random();
            int size = chars.length();
            for (int i = 0; i < length; i++) {
                char c = chars.charAt(Math.abs(random.nextInt()) % size);
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isEmpty(Collection<T> container) {
        return container == null || container.isEmpty();
    }

    public static boolean isUiThread() {
        return Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isEqual(String a, String b) {
        return isEqual(a, b, false);
    }

    public static boolean isEqual(String a, String b, boolean ignoreCase) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return ignoreCase ? a.equalsIgnoreCase(b) : a.equals(b);
    }

    public static Double strToDouble(String str) {
        try {
            if (!isEmpty(str)) {
                return Double.valueOf(Double.parseDouble(str));
            }
        } catch (NumberFormatException nfe) {
            Log.debug("Potential Error, could not perform strToDouble convertion.", nfe);
        }
        return null;
    }

    public static Float strToFloat(String str) {
        try {
            if (!isEmpty(str)) {
                return Float.valueOf(Float.parseFloat(str));
            }
        } catch (NumberFormatException nfe) {
            Log.debug("Potential Error, could not perform strToDouble convertion.", nfe);
        }
        return null;
    }

    public static Integer strToInteger(String str) {
        try {
            if (!isEmpty(str)) {
                return Integer.valueOf(Integer.parseInt(str));
            }
        } catch (NumberFormatException nfe) {
            Log.debug("Potential Error, could not perform strToLong convertion.", nfe);
        }
        return null;
    }

    public static Long strToLong(String str) {
        try {
            if (!isEmpty(str)) {
                return Long.valueOf(Long.parseLong(str));
            }
        } catch (NumberFormatException nfe) {
            Log.debug("Potential Error, could not perform strToLong convertion.", nfe);
        }
        return null;
    }
}
