package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcq implements Parcelable.Creator<zzcr> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcr createFromParcel(Parcel parcel) {
        boolean zZzc = false;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        boolean zZzc2 = false;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 4:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcr(iZzg, zZzc2, zZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcr[] newArray(int i) {
        return new zzcr[i];
    }
}
