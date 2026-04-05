package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcfx implements Parcelable.Creator<zzcfw> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcfw createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        String strZzq = null;
        String strZzq2 = null;
        zzcku zzckuVar = null;
        long jZzi = 0;
        boolean zZzc = false;
        String strZzq3 = null;
        zzcgl zzcglVar = null;
        long jZzi2 = 0;
        zzcgl zzcglVar2 = null;
        long jZzi3 = 0;
        zzcgl zzcglVar3 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    zzckuVar = (zzcku) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzcku.CREATOR);
                    break;
                case 5:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 6:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 7:
                    strZzq3 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 8:
                    zzcglVar = (zzcgl) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzcgl.CREATOR);
                    break;
                case 9:
                    jZzi2 = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 10:
                    zzcglVar2 = (zzcgl) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzcgl.CREATOR);
                    break;
                case 11:
                    jZzi3 = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 12:
                    zzcglVar3 = (zzcgl) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzcgl.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcfw(iZzg, strZzq, strZzq2, zzckuVar, jZzi, zZzc, strZzq3, zzcglVar, jZzi2, zzcglVar2, jZzi3, zzcglVar3);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcfw[] newArray(int i) {
        return new zzcfw[i];
    }
}
