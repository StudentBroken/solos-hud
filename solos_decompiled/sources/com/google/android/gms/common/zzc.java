package com.google.android.gms.common;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes67.dex */
public final class zzc extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzc> CREATOR = new zzd();
    private String name;
    private int version;

    public zzc(String str, int i) {
        this.name = str;
        this.version = i;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 1, this.name, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.version);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
