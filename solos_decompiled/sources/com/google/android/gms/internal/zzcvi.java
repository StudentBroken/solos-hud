package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbs;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcvi implements Parcelable.Creator<zzcvh> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcvh createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        zzbs zzbsVar = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    zzbsVar = (zzbs) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzbs.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcvh(iZzg, zzbsVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcvh[] newArray(int i) {
        return new zzcvh[i];
    }
}
