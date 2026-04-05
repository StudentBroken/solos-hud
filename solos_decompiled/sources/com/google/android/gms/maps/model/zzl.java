package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes10.dex */
public final class zzl implements Parcelable.Creator<PolylineOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PolylineOptions createFromParcel(Parcel parcel) {
        float fZzl = 0.0f;
        ArrayList arrayListZzc = null;
        int iZzg = 0;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        Cap cap = null;
        Cap cap2 = null;
        boolean zZzc = false;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        int iZzg2 = 0;
        float fZzl2 = 0.0f;
        ArrayList arrayListZzc2 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    arrayListZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 4:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 5:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 6:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 7:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 8:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 9:
                    cap2 = (Cap) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Cap.CREATOR);
                    break;
                case 10:
                    cap = (Cap) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Cap.CREATOR);
                    break;
                case 11:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 12:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, PatternItem.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new PolylineOptions(arrayListZzc2, fZzl2, iZzg2, fZzl, zZzc3, zZzc2, zZzc, cap2, cap, iZzg, arrayListZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PolylineOptions[] newArray(int i) {
        return new PolylineOptions[i];
    }
}
