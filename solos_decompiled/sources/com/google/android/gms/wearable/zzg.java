package com.google.android.gms.wearable;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzg implements Parcelable.Creator<ConnectionConfiguration> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ ConnectionConfiguration createFromParcel(Parcel parcel) {
        String strZzq = null;
        boolean zZzc = false;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq2 = null;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        int iZzg = 0;
        int iZzg2 = 0;
        String strZzq3 = null;
        String strZzq4 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    strZzq4 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    strZzq3 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 5:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 6:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 7:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 8:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 9:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 10:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new ConnectionConfiguration(strZzq4, strZzq3, iZzg2, iZzg, zZzc3, zZzc2, strZzq2, zZzc, strZzq);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ ConnectionConfiguration[] newArray(int i) {
        return new ConnectionConfiguration[i];
    }
}
