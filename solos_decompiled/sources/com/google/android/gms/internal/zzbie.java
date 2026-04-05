package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbie implements Parcelable.Creator<zzbib> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbib createFromParcel(Parcel parcel) {
        ArrayList arrayListZzc = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        String strZzq = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, zzbic.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzbib(iZzg, strZzq, arrayListZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbib[] newArray(int i) {
        return new zzbib[i];
    }
}
