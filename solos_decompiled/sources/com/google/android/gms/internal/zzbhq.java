package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbhq extends com.google.android.gms.common.internal.safeparcel.zza implements zzbhw<String, Integer> {
    public static final Parcelable.Creator<zzbhq> CREATOR = new zzbhs();
    private final HashMap<String, Integer> zzaIE;
    private final SparseArray<String> zzaIF;
    private final ArrayList<zzbhr> zzaIG;
    private int zzakw;

    public zzbhq() {
        this.zzakw = 1;
        this.zzaIE = new HashMap<>();
        this.zzaIF = new SparseArray<>();
        this.zzaIG = null;
    }

    zzbhq(int i, ArrayList<zzbhr> arrayList) {
        this.zzakw = i;
        this.zzaIE = new HashMap<>();
        this.zzaIF = new SparseArray<>();
        this.zzaIG = null;
        zzd(arrayList);
    }

    private final void zzd(ArrayList<zzbhr> arrayList) {
        ArrayList<zzbhr> arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            zzbhr zzbhrVar = arrayList2.get(i);
            i++;
            zzbhr zzbhrVar2 = zzbhrVar;
            zzi(zzbhrVar2.zzaIH, zzbhrVar2.zzaII);
        }
    }

    @Override // com.google.android.gms.internal.zzbhw
    public final /* synthetic */ String convertBack(Integer num) {
        String str = this.zzaIF.get(num.intValue());
        return (str == null && this.zzaIE.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        ArrayList arrayList = new ArrayList();
        for (String str : this.zzaIE.keySet()) {
            arrayList.add(new zzbhr(str, this.zzaIE.get(str).intValue()));
        }
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, arrayList, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final zzbhq zzi(String str, int i) {
        this.zzaIE.put(str, Integer.valueOf(i));
        this.zzaIF.put(i, str);
        return this;
    }
}
