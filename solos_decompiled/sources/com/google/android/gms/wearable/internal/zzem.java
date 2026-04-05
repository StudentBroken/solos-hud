package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzem extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzem> CREATOR = new zzen();
    public final int statusCode;
    public final zzcb zzbSR;

    public zzem(int i, zzcb zzcbVar) {
        this.statusCode = i;
        this.zzbSR = zzcbVar;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzbSR, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
