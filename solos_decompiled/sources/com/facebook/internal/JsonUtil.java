package com.facebook.internal;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
class JsonUtil {
    JsonUtil() {
    }

    static void jsonObjectClear(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            keys.next();
            keys.remove();
        }
    }

    static boolean jsonObjectContainsValue(JSONObject jsonObject, Object value) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            Object thisValue = jsonObject.opt(keys.next());
            if (thisValue != null && thisValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private static final class JSONObjectEntry implements Map.Entry<String, Object> {
        private final String key;
        private final Object value;

        JSONObjectEntry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        @SuppressLint({"FieldGetter"})
        public String getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object object) {
            throw new UnsupportedOperationException("JSONObjectEntry is immutable");
        }
    }

    static Set<Map.Entry<String, Object>> jsonObjectEntrySet(JSONObject jsonObject) {
        HashSet<Map.Entry<String, Object>> result = new HashSet<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.opt(key);
            result.add(new JSONObjectEntry(key, value));
        }
        return result;
    }

    static Set<String> jsonObjectKeySet(JSONObject jsonObject) {
        HashSet<String> result = new HashSet<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            result.add(keys.next());
        }
        return result;
    }

    static void jsonObjectPutAll(JSONObject jsonObject, Map<String, Object> map) {
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            try {
                jsonObject.putOpt(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    static Collection<Object> jsonObjectValues(JSONObject jsonObject) {
        ArrayList<Object> result = new ArrayList<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            result.add(jsonObject.opt(keys.next()));
        }
        return result;
    }
}
