package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbhp implements Parcelable.Creator<zzbho> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbho createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        zzbhq zzbhqVar = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    zzbhqVar = (zzbhq) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzbhq.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzbho(iZzg, zzbhqVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbho[] newArray(int i) {
        return new zzbho[i];
    }
}
