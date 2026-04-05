package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzes extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzes> CREATOR = new zzet();
    public final int statusCode;
    public final int zzbhh;

    public zzes(int i, int i2) {
        this.statusCode = i;
        this.zzbhh = i2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzbhh);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
