package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbhr extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbhr> CREATOR = new zzbht();
    private int versionCode;
    final String zzaIH;
    final int zzaII;

    zzbhr(int i, String str, int i2) {
        this.versionCode = i;
        this.zzaIH = str;
        this.zzaII = i2;
    }

    zzbhr(String str, int i) {
        this.versionCode = 1;
        this.zzaIH = str;
        this.zzaII = i;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzaIH, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzaII);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
