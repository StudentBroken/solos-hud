package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzdf implements Parcelable.Creator<zzde> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzde createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        zzeg zzegVar = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    zzegVar = (zzeg) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzeg.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzde(iZzg, zzegVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzde[] newArray(int i) {
        return new zzde[i];
    }
}
