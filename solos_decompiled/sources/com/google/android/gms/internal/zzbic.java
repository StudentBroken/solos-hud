package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbic extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbic> CREATOR = new zzbhz();
    final String key;
    private int versionCode;
    final zzbhv<?, ?> zzaIX;

    zzbic(int i, String str, zzbhv<?, ?> zzbhvVar) {
        this.versionCode = i;
        this.key = str;
        this.zzaIX = zzbhvVar;
    }

    zzbic(String str, zzbhv<?, ?> zzbhvVar) {
        this.versionCode = 1;
        this.key = str;
        this.zzaIX = zzbhvVar;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.key, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzaIX, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
