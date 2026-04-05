package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcz implements Parcelable.Creator<zzcy> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcy createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        ArrayList arrayListZzc = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, zzeg.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcy(iZzg, arrayListZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcy[] newArray(int i) {
        return new zzcy[i];
    }
}
