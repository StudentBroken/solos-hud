package com.ua.sdk.util;

import java.util.Collection;

/* JADX INFO: loaded from: classes65.dex */
public final class Validate {
    private Validate() {
    }

    public static void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument " + name + " cannot be null");
        }
    }

    public static void notNullOrEmpty(String arg, String name) {
        if (Utility.isEmpty(arg)) {
            throw new IllegalArgumentException("Argument " + name + " cannot be null or empty");
        }
    }

    public static <T> void notEmpty(Collection<T> container, String name) {
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container '" + name + "' cannot be empty");
        }
    }

    public static <T> void containsNoNulls(Collection<T> container, String name) {
        notNull(container, name);
        if (Utility.containsNull(container)) {
            throw new NullPointerException("Container '" + name + "' cannot contain null values");
        }
    }

    public static <T> void notEmptyAndContainsNoNulls(Collection<T> container, String name) {
        notEmpty(container, name);
        containsNoNulls(container, name);
    }
}
