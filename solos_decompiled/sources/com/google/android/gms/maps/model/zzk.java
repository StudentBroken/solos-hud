package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes10.dex */
public final class zzk implements Parcelable.Creator<PolygonOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PolygonOptions createFromParcel(Parcel parcel) {
        ArrayList arrayListZzc = null;
        float fZzl = 0.0f;
        int iZzg = 0;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        ArrayList arrayList = new ArrayList();
        boolean zZzc = false;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        int iZzg2 = 0;
        int iZzg3 = 0;
        float fZzl2 = 0.0f;
        ArrayList arrayListZzc2 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    arrayListZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, arrayList, getClass().getClassLoader());
                    break;
                case 4:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 5:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 6:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 7:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 8:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 9:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 10:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
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
        return new PolygonOptions(arrayListZzc2, arrayList, fZzl2, iZzg3, iZzg2, fZzl, zZzc3, zZzc2, zZzc, iZzg, arrayListZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PolygonOptions[] newArray(int i) {
        return new PolygonOptions[i];
    }
}
