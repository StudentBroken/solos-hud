package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzm implements Parcelable.Creator<zzl> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzl createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        String strZzq = null;
        String strZzq2 = null;
        String strZzq3 = null;
        String strZzq4 = null;
        String strZzq5 = null;
        String strZzq6 = null;
        byte bZze = 0;
        byte bZze2 = 0;
        byte bZze3 = 0;
        byte bZze4 = 0;
        String strZzq7 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 5:
                    strZzq3 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 6:
                    strZzq4 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 7:
                    strZzq5 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 8:
                    strZzq6 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 9:
                    bZze = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 10:
                    bZze2 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 11:
                    bZze3 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 12:
                    bZze4 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 13:
                    strZzq7 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzl(iZzg, strZzq, strZzq2, strZzq3, strZzq4, strZzq5, strZzq6, bZze, bZze2, bZze3, bZze4, strZzq7);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzl[] newArray(int i) {
        return new zzl[i];
    }
}
