package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgm implements Parcelable.Creator<zzcgl> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcgl createFromParcel(Parcel parcel) {
        String strZzq = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        long jZzi = 0;
        zzcgi zzcgiVar = null;
        String strZzq2 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    zzcgiVar = (zzcgi) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzcgi.CREATOR);
                    break;
                case 4:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 5:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcgl(strZzq2, zzcgiVar, strZzq, jZzi);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcgl[] newArray(int i) {
        return new zzcgl[i];
    }
}
