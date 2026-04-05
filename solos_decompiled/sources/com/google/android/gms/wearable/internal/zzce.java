package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzce extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzce> CREATOR = new zzcf();
    public final int statusCode;
    public final int zzbSH;

    public zzce(int i, int i2) {
        this.statusCode = i;
        this.zzbSH = i2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzbSH);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
