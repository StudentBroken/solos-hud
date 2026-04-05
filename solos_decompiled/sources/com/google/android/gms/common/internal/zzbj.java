package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbj {
    private final List<String> zzaIj;
    private final Object zzaay;

    private zzbj(Object obj) {
        this.zzaay = zzbr.zzu(obj);
        this.zzaIj = new ArrayList();
    }

    public final String toString() {
        StringBuilder sbAppend = new StringBuilder(100).append(this.zzaay.getClass().getSimpleName()).append('{');
        int size = this.zzaIj.size();
        for (int i = 0; i < size; i++) {
            sbAppend.append(this.zzaIj.get(i));
            if (i < size - 1) {
                sbAppend.append(", ");
            }
        }
        return sbAppend.append('}').toString();
    }

    public final zzbj zzg(String str, Object obj) {
        List<String> list = this.zzaIj;
        String str2 = (String) zzbr.zzu(str);
        String strValueOf = String.valueOf(String.valueOf(obj));
        list.add(new StringBuilder(String.valueOf(str2).length() + 1 + String.valueOf(strValueOf).length()).append(str2).append("=").append(strValueOf).toString());
        return this;
    }
}
