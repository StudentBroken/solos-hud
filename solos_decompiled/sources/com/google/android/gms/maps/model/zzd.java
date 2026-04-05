package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzd implements Parcelable.Creator<GroundOverlayOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GroundOverlayOptions createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        IBinder iBinderZzr = null;
        LatLng latLng = null;
        float fZzl = 0.0f;
        float fZzl2 = 0.0f;
        LatLngBounds latLngBounds = null;
        float fZzl3 = 0.0f;
        float fZzl4 = 0.0f;
        boolean zZzc = false;
        float fZzl5 = 0.0f;
        float fZzl6 = 0.0f;
        float fZzl7 = 0.0f;
        boolean zZzc2 = false;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                case 3:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 4:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 5:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 6:
                    latLngBounds = (LatLngBounds) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLngBounds.CREATOR);
                    break;
                case 7:
                    fZzl3 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 8:
                    fZzl4 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 9:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 10:
                    fZzl5 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 11:
                    fZzl6 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 12:
                    fZzl7 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 13:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new GroundOverlayOptions(iBinderZzr, latLng, fZzl, fZzl2, latLngBounds, fZzl3, fZzl4, zZzc, fZzl5, fZzl6, fZzl7, zZzc2);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GroundOverlayOptions[] newArray(int i) {
        return new GroundOverlayOptions[i];
    }
}
