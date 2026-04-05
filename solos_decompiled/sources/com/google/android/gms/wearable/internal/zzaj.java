package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzaj implements Parcelable.Creator<zzai> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzai createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        int iZzg2 = 0;
        zzak zzakVar = null;
        int iZzg3 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    zzakVar = (zzak) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzak.CREATOR);
                    break;
                case 3:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 4:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 5:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzai(zzakVar, iZzg2, iZzg, iZzg3);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzai[] newArray(int i) {
        return new zzai[i];
    }
}
