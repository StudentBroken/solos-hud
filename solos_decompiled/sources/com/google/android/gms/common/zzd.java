package com.google.android.gms.common;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes67.dex */
public final class zzd implements Parcelable.Creator<zzc> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzc createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzc(strZzq, iZzg);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzc[] newArray(int i) {
        return new zzc[i];
    }
}
