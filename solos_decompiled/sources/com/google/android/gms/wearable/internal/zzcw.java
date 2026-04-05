package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.ConnectionConfiguration;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcw extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcw> CREATOR = new zzcx();
    private int statusCode;
    private ConnectionConfiguration[] zzbSP;

    public zzcw(int i, ConnectionConfiguration[] connectionConfigurationArr) {
        this.statusCode = i;
        this.zzbSP = connectionConfigurationArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable[]) this.zzbSP, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
