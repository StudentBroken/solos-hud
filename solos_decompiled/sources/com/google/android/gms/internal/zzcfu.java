package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcfu implements Parcelable.Creator<zzcft> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcft createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq = null;
        String strZzq2 = null;
        String strZzq3 = null;
        String strZzq4 = null;
        long jZzi = 0;
        long jZzi2 = 0;
        String strZzq5 = null;
        boolean zZzc = true;
        boolean zZzc2 = false;
        long jZzi3 = -2147483648L;
        String strZzq6 = null;
        long jZzi4 = 0;
        long jZzi5 = 0;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    strZzq3 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 5:
                    strZzq4 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 6:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 7:
                    jZzi2 = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 8:
                    strZzq5 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 9:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 10:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 11:
                    jZzi3 = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 12:
                    strZzq6 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 13:
                    jZzi4 = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 14:
                    jZzi5 = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 15:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcft(strZzq, strZzq2, strZzq3, strZzq4, jZzi, jZzi2, strZzq5, zZzc, zZzc2, jZzi3, strZzq6, jZzi4, jZzi5, iZzg);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcft[] newArray(int i) {
        return new zzcft[i];
    }
}
