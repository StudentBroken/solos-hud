package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzdy implements Parcelable.Creator<zzdx> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzdx createFromParcel(Parcel parcel) {
        String strZzq = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq2 = null;
        int iZzg = 0;
        byte[] bArrZzt = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    bArrZzt = com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, i);
                    break;
                case 5:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzdx(iZzg, strZzq2, bArrZzt, strZzq);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzdx[] newArray(int i) {
        return new zzdx[i];
    }
}
