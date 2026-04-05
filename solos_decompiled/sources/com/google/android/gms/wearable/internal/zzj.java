package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzj implements Parcelable.Creator<zzi> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzi createFromParcel(Parcel parcel) {
        byte bZze = 0;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq = null;
        byte bZze2 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    bZze2 = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 3:
                    bZze = com.google.android.gms.common.internal.safeparcel.zzb.zze(parcel, i);
                    break;
                case 4:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzi(bZze2, bZze, strZzq);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzi[] newArray(int i) {
        return new zzi[i];
    }
}
