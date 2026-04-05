package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zze implements Parcelable.Creator<LatLngBounds> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ LatLngBounds createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        LatLng latLng = null;
        LatLng latLng2 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    latLng2 = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new LatLngBounds(latLng2, latLng);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ LatLngBounds[] newArray(int i) {
        return new LatLngBounds[i];
    }
}
