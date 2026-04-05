package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzb implements Parcelable.Creator<Cap> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Cap createFromParcel(Parcel parcel) {
        Float fZzm = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        IBinder iBinderZzr = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                case 4:
                    fZzm = com.google.android.gms.common.internal.safeparcel.zzb.zzm(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new Cap(iZzg, iBinderZzr, fZzm);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Cap[] newArray(int i) {
        return new Cap[i];
    }
}
