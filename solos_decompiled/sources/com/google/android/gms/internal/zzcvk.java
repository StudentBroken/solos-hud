package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbu;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcvk implements Parcelable.Creator<zzcvj> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcvj createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        ConnectionResult connectionResult = null;
        int iZzg = 0;
        zzbu zzbuVar = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    connectionResult = (ConnectionResult) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, ConnectionResult.CREATOR);
                    break;
                case 3:
                    zzbuVar = (zzbu) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzbu.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcvj(iZzg, connectionResult, zzbuVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcvj[] newArray(int i) {
        return new zzcvj[i];
    }
}
