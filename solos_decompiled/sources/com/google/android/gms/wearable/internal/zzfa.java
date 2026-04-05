package com.google.android.gms.wearable.internal;

import com.google.android.gms.wearable.CapabilityInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes6.dex */
final class zzfa {
    /* JADX INFO: Access modifiers changed from: private */
    public static Map<String, CapabilityInfo> zzN(List<zzaa> list) {
        HashMap map = new HashMap(list.size() << 1);
        for (zzaa zzaaVar : list) {
            map.put(zzaaVar.getName(), new zzw(zzaaVar));
        }
        return map;
    }
}
