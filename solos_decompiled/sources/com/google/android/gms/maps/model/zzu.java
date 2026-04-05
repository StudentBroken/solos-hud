package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzu implements Parcelable.Creator<VisibleRegion> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ VisibleRegion createFromParcel(Parcel parcel) {
        LatLngBounds latLngBounds = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        LatLng latLng = null;
        LatLng latLng2 = null;
        LatLng latLng3 = null;
        LatLng latLng4 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    latLng4 = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    latLng3 = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 4:
                    latLng2 = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 5:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 6:
                    latLngBounds = (LatLngBounds) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLngBounds.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new VisibleRegion(latLng4, latLng3, latLng2, latLng, latLngBounds);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ VisibleRegion[] newArray(int i) {
        return new VisibleRegion[i];
    }
}
