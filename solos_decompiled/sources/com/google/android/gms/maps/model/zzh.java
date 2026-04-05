package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzh implements Parcelable.Creator<MarkerOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ MarkerOptions createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        LatLng latLng = null;
        String strZzq = null;
        String strZzq2 = null;
        IBinder iBinderZzr = null;
        float fZzl = 0.0f;
        float fZzl2 = 0.0f;
        boolean zZzc = false;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        float fZzl3 = 0.0f;
        float fZzl4 = 0.5f;
        float fZzl5 = 0.0f;
        float fZzl6 = 1.0f;
        float fZzl7 = 0.0f;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 5:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                case 6:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 7:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 8:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 9:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 10:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 11:
                    fZzl3 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 12:
                    fZzl4 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 13:
                    fZzl5 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 14:
                    fZzl6 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 15:
                    fZzl7 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new MarkerOptions(latLng, strZzq, strZzq2, iBinderZzr, fZzl, fZzl2, zZzc, zZzc2, zZzc3, fZzl3, fZzl4, fZzl5, fZzl6, fZzl7);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ MarkerOptions[] newArray(int i) {
        return new MarkerOptions[i];
    }
}
