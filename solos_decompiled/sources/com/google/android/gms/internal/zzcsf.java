package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcsf implements Parcelable.Creator<zzcrz> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcrz createFromParcel(Parcel parcel) {
        byte[][] bArrZzu = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int[] iArrZzw = null;
        byte[][] bArrZzu2 = null;
        byte[][] bArrZzu3 = null;
        byte[][] bArrZzu4 = null;
        byte[][] bArrZzu5 = null;
        byte[] bArrZzt = null;
        String strZzq = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    bArrZzt = com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, i);
                    break;
                case 4:
                    bArrZzu5 = com.google.android.gms.common.internal.safeparcel.zzb.zzu(parcel, i);
                    break;
                case 5:
                    bArrZzu4 = com.google.android.gms.common.internal.safeparcel.zzb.zzu(parcel, i);
                    break;
                case 6:
                    bArrZzu3 = com.google.android.gms.common.internal.safeparcel.zzb.zzu(parcel, i);
                    break;
                case 7:
                    bArrZzu2 = com.google.android.gms.common.internal.safeparcel.zzb.zzu(parcel, i);
                    break;
                case 8:
                    iArrZzw = com.google.android.gms.common.internal.safeparcel.zzb.zzw(parcel, i);
                    break;
                case 9:
                    bArrZzu = com.google.android.gms.common.internal.safeparcel.zzb.zzu(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcrz(strZzq, bArrZzt, bArrZzu5, bArrZzu4, bArrZzu3, bArrZzu2, iArrZzw, bArrZzu);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcrz[] newArray(int i) {
        return new zzcrz[i];
    }
}
