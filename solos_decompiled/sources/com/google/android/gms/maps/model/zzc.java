package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes10.dex */
public final class zzc implements Parcelable.Creator<CircleOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ CircleOptions createFromParcel(Parcel parcel) {
        ArrayList arrayListZzc = null;
        float fZzl = 0.0f;
        boolean zZzc = false;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        double dZzn = 0.0d;
        boolean zZzc2 = false;
        int iZzg = 0;
        int iZzg2 = 0;
        float fZzl2 = 0.0f;
        LatLng latLng = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    dZzn = com.google.android.gms.common.internal.safeparcel.zzb.zzn(parcel, i);
                    break;
                case 4:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 5:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 6:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 7:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 8:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 9:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 10:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, PatternItem.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new CircleOptions(latLng, dZzn, fZzl2, iZzg2, iZzg, fZzl, zZzc2, zZzc, arrayListZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ CircleOptions[] newArray(int i) {
        return new CircleOptions[i];
    }
}
