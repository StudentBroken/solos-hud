package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes6.dex */
public final class zzex implements Parcelable.Creator<zzew> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzew createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        long jZzi = 0;
        ArrayList arrayListZzc = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
                case 4:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, zzek.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzew(iZzg, jZzi, arrayListZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzew[] newArray(int i) {
        return new zzew[i];
    }
}
