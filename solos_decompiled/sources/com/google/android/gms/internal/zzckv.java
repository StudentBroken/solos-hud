package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes36.dex */
public final class zzckv implements Parcelable.Creator<zzcku> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcku createFromParcel(Parcel parcel) {
        Double dZzo = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        long jZzi = 0;
        String strZzq = null;
        String strZzq2 = null;
        Float fZzm = null;
        Long lZzj = null;
        String strZzq3 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    strZzq3 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 4:
                    lZzj = com.google.android.gms.common.internal.safeparcel.zzb.zzj(parcel, i);
                    break;
                case 5:
                    fZzm = com.google.android.gms.common.internal.safeparcel.zzb.zzm(parcel, i);
                    break;
                case 6:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 7:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 8:
                    dZzo = com.google.android.gms.common.internal.safeparcel.zzb.zzo(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcku(iZzg, strZzq3, jZzi, lZzj, fZzm, strZzq2, strZzq, dZzo);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcku[] newArray(int i) {
        return new zzcku[i];
    }
}
