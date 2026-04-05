package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbib extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbib> CREATOR = new zzbie();
    final String className;
    private int versionCode;
    private ArrayList<zzbic> zzaIW;

    zzbib(int i, String str, ArrayList<zzbic> arrayList) {
        this.versionCode = i;
        this.className = str;
        this.zzaIW = arrayList;
    }

    zzbib(String str, Map<String, zzbhv<?, ?>> map) {
        ArrayList<zzbic> arrayList;
        this.versionCode = 1;
        this.className = str;
        if (map == null) {
            arrayList = null;
        } else {
            ArrayList<zzbic> arrayList2 = new ArrayList<>();
            for (String str2 : map.keySet()) {
                arrayList2.add(new zzbic(str2, map.get(str2)));
            }
            arrayList = arrayList2;
        }
        this.zzaIW = arrayList;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.className, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzaIW, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    final HashMap<String, zzbhv<?, ?>> zzrR() {
        HashMap<String, zzbhv<?, ?>> map = new HashMap<>();
        int size = this.zzaIW.size();
        for (int i = 0; i < size; i++) {
            zzbic zzbicVar = this.zzaIW.get(i);
            map.put(zzbicVar.key, zzbicVar.zzaIX);
        }
        return map;
    }
}
