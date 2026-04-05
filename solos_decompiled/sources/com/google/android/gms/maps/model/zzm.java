package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzm implements Parcelable.Creator<StreetViewPanoramaCamera> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ StreetViewPanoramaCamera createFromParcel(Parcel parcel) {
        float fZzl = 0.0f;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        float fZzl2 = 0.0f;
        float fZzl3 = 0.0f;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    fZzl3 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 3:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 4:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new StreetViewPanoramaCamera(fZzl3, fZzl2, fZzl);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ StreetViewPanoramaCamera[] newArray(int i) {
        return new StreetViewPanoramaCamera[i];
    }
}
