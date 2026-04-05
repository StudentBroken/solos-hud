package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzeh implements Parcelable.Creator<zzeg> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzeg createFromParcel(Parcel parcel) {
        boolean zZzc = false;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq = null;
        String strZzq2 = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 5:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzeg(strZzq2, strZzq, iZzg, zZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzeg[] newArray(int i) {
        return new zzeg[i];
    }
}
