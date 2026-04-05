package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzdb implements Parcelable.Creator<zzda> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzda createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        zzcb zzcbVar = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    zzcbVar = (zzcb) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzcb.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzda(iZzg, zzcbVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzda[] newArray(int i) {
        return new zzda[i];
    }
}
