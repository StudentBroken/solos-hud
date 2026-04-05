package com.kopin.accessory.utility;

import com.kopin.accessory.utility.EnumConverter;
import java.lang.Enum;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes14.dex */
public class ReverseEnumMap<U, V extends Enum<V> & EnumConverter<U>> {
    private Map<U, V> map = new HashMap();

    public ReverseEnumMap(Class<V> cls) {
        for (Object obj : (Enum[]) cls.getEnumConstants()) {
            this.map.put((U) ((EnumConverter) obj).convert(), (V) obj);
        }
    }

    /* JADX WARN: Incorrect return type in method signature: (TU;)TV; */
    public Enum get(Object obj) {
        return (Enum) this.map.get(obj);
    }
}
