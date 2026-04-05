package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

/* JADX INFO: loaded from: classes10.dex */
public final class zzah implements Parcelable.Creator<StreetViewPanoramaOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ StreetViewPanoramaOptions createFromParcel(Parcel parcel) {
        Integer numZzh = null;
        byte bZze = 0;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        byte bZze2 = 0;
        byte bZze3 = 0;
        byte bZze4 = 0;
        byte bZze5 = 0;
        LatLng latLng = null;
        String strZzq = null;
        StreetViewPanoramaCamera streetViewPanoramaCamera = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    streetViewPanoramaCamera = (StreetViewPanoramaCamera) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, StreetViewPanoramaCamera.CREATOR);
                    break;
                case 3:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLng.CREATOR);
                    break;
                case 5:
                    numZzh = com.google.android.gms.common.internal.safeparcel.zzb.zzh(parcel, i);
                    break;
                case 6:
                    bZze5 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 7:
                    bZze4 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 8:
                    bZze3 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 9:
                    bZze2 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 10:
                    bZze = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new StreetViewPanoramaOptions(streetViewPanoramaCamera, strZzq, latLng, numZzh, bZze5, bZze4, bZze3, bZze2, bZze);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ StreetViewPanoramaOptions[] newArray(int i) {
        return new StreetViewPanoramaOptions[i];
    }
}
