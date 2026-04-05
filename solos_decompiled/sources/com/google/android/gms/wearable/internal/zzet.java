package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzet implements Parcelable.Creator<zzes> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzes createFromParcel(Parcel parcel) {
        int iZzg = 0;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg2 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzes(iZzg2, iZzg);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzes[] newArray(int i) {
        return new zzes[i];
    }
}
