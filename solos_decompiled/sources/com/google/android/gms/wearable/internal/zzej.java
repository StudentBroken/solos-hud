package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzej implements Parcelable.Creator<zzei> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzei createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        zzak zzakVar = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    zzakVar = (zzak) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzak.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzei(iZzg, zzakVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzei[] newArray(int i) {
        return new zzei[i];
    }
}
