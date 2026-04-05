package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcr extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcr> CREATOR = new zzcq();
    private int statusCode;
    private boolean zzbSM;
    private boolean zzbSN;

    public zzcr(int i, boolean z, boolean z2) {
        this.statusCode = i;
        this.zzbSM = z;
        this.zzbSN = z2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzbSM);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbSN);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
