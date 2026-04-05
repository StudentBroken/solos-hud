package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzf implements Parcelable.Creator<LatLng> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ LatLng createFromParcel(Parcel parcel) {
        double dZzn = 0.0d;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        double dZzn2 = 0.0d;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    dZzn2 = com.google.android.gms.common.internal.safeparcel.zzb.zzn(parcel, i);
                    break;
                case 3:
                    dZzn = com.google.android.gms.common.internal.safeparcel.zzb.zzn(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new LatLng(dZzn2, dZzn);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ LatLng[] newArray(int i) {
        return new LatLng[i];
    }
}
