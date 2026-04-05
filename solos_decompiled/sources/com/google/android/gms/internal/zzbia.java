package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbia extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbia> CREATOR = new zzbid();
    private final HashMap<String, Map<String, zzbhv<?, ?>>> zzaIT;
    private final ArrayList<zzbib> zzaIU = null;
    private final String zzaIV;
    private int zzakw;

    zzbia(int i, ArrayList<zzbib> arrayList, String str) {
        this.zzakw = i;
        HashMap<String, Map<String, zzbhv<?, ?>>> map = new HashMap<>();
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            zzbib zzbibVar = arrayList.get(i2);
            map.put(zzbibVar.className, zzbibVar.zzrR());
        }
        this.zzaIT = map;
        this.zzaIV = (String) zzbr.zzu(str);
        zzrP();
    }

    private final void zzrP() {
        Iterator<String> it = this.zzaIT.keySet().iterator();
        while (it.hasNext()) {
            Map<String, zzbhv<?, ?>> map = this.zzaIT.get(it.next());
            Iterator<String> it2 = map.keySet().iterator();
            while (it2.hasNext()) {
                map.get(it2.next()).zza(this);
            }
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : this.zzaIT.keySet()) {
            sb.append(str).append(":\n");
            Map<String, zzbhv<?, ?>> map = this.zzaIT.get(str);
            for (String str2 : map.keySet()) {
                sb.append("  ").append(str2).append(": ");
                sb.append(map.get(str2));
            }
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        ArrayList arrayList = new ArrayList();
        for (String str : this.zzaIT.keySet()) {
            arrayList.add(new zzbib(str, this.zzaIT.get(str)));
        }
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, arrayList, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzaIV, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final Map<String, zzbhv<?, ?>> zzcJ(String str) {
        return this.zzaIT.get(str);
    }

    public final String zzrQ() {
        return this.zzaIV;
    }
}
