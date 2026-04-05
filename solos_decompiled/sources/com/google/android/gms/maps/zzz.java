package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;

/* JADX INFO: loaded from: classes10.dex */
public final class zzz implements Parcelable.Creator<GoogleMapOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GoogleMapOptions createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        byte bZze = -1;
        byte bZze2 = -1;
        int iZzg = 0;
        CameraPosition cameraPosition = null;
        byte bZze3 = -1;
        byte bZze4 = -1;
        byte bZze5 = -1;
        byte bZze6 = -1;
        byte bZze7 = -1;
        byte bZze8 = -1;
        byte bZze9 = -1;
        byte bZze10 = -1;
        byte bZze11 = -1;
        Float fZzm = null;
        Float fZzm2 = null;
        LatLngBounds latLngBounds = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    bZze = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 3:
                    bZze2 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 4:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 5:
                    cameraPosition = (CameraPosition) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, CameraPosition.CREATOR);
                    break;
                case 6:
                    bZze3 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 7:
                    bZze4 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 8:
                    bZze5 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 9:
                    bZze6 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 10:
                    bZze7 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 11:
                    bZze8 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 12:
                    bZze9 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 13:
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
                case 14:
                    bZze10 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 15:
                    bZze11 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 16:
                    fZzm = com.google.android.gms.common.internal.safeparcel.zzb.zzm(parcel, i);
                    break;
                case 17:
                    fZzm2 = com.google.android.gms.common.internal.safeparcel.zzb.zzm(parcel, i);
                    break;
                case 18:
                    latLngBounds = (LatLngBounds) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, LatLngBounds.CREATOR);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new GoogleMapOptions(bZze, bZze2, iZzg, cameraPosition, bZze3, bZze4, bZze5, bZze6, bZze7, bZze8, bZze9, bZze10, bZze11, fZzm, fZzm2, latLngBounds);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GoogleMapOptions[] newArray(int i) {
        return new GoogleMapOptions[i];
    }
}
