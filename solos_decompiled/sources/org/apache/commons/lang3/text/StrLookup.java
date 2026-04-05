package org.apache.commons.lang3.text;

import java.util.Map;

/* JADX INFO: loaded from: classes51.dex */
public abstract class StrLookup<V> {
    private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup(null);
    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP;

    public abstract String lookup(String str);

    static {
        StrLookup<String> lookup;
        try {
            Map<?, ?> propMap = System.getProperties();
            lookup = new MapStrLookup<>(propMap);
        } catch (SecurityException e) {
            lookup = NONE_LOOKUP;
        }
        SYSTEM_PROPERTIES_LOOKUP = lookup;
    }

    public static StrLookup<?> noneLookup() {
        return NONE_LOOKUP;
    }

    public static StrLookup<String> systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    public static <V> StrLookup<V> mapLookup(Map<String, V> map) {
        return new MapStrLookup(map);
    }

    protected StrLookup() {
    }

    static class MapStrLookup<V> extends StrLookup<V> {
        private final Map<String, V> map;

        MapStrLookup(Map<String, V> map) {
            this.map = map;
        }

        @Override // org.apache.commons.lang3.text.StrLookup
        public String lookup(String key) {
            Object obj;
            if (this.map == null || (obj = this.map.get(key)) == null) {
                return null;
            }
            return obj.toString();
        }
    }
}
