package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcd implements Parcelable.Creator<zzcc> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcc createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcc(iZzg);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcc[] newArray(int i) {
        return new zzcc[i];
    }
}
