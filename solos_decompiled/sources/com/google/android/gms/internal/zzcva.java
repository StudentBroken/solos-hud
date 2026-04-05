package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcva implements Parcelable.Creator<zzcuz> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcuz createFromParcel(Parcel parcel) {
        int iZzg = 0;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        Intent intent = null;
        int iZzg2 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    intent = (Intent) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Intent.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcuz(iZzg2, iZzg, intent);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcuz[] newArray(int i) {
        return new zzcuz[i];
    }
}
