package com.google.android.gms.wearable.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzep implements Parcelable.Creator<zzeo> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzeo createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        IBinder iBinderZzr = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzeo(iZzg, iBinderZzr);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzeo[] newArray(int i) {
        return new zzeo[i];
    }
}
