package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzek extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzek> CREATOR = new zzel();
    private String label;
    private String packageName;
    private long zzbTe;

    public zzek(String str, String str2, long j) {
        this.packageName = str;
        this.label = str2;
        this.zzbTe = j;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.packageName, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.label, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbTe);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
