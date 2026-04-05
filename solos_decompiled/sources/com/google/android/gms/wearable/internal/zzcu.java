package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.ConnectionConfiguration;

/* JADX INFO: loaded from: classes6.dex */
@Deprecated
public final class zzcu extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcu> CREATOR = new zzcv();
    private int statusCode;
    private ConnectionConfiguration zzbSO;

    public zzcu(int i, ConnectionConfiguration connectionConfiguration) {
        this.statusCode = i;
        this.zzbSO = connectionConfiguration;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzbSO, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
