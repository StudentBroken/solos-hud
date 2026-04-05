package com.google.android.gms.wearable;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzh implements Parcelable.Creator<PutDataRequest> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PutDataRequest createFromParcel(Parcel parcel) {
        byte[] bArrZzt = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        long jZzi = 0;
        Bundle bundleZzs = null;
        Uri uri = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Uri.CREATOR);
                    break;
                case 3:
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
                case 4:
                    bundleZzs = com.google.android.gms.common.internal.safeparcel.zzb.zzs(parcel, i);
                    break;
                case 5:
                    bArrZzt = com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, i);
                    break;
                case 6:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new PutDataRequest(uri, bundleZzs, bArrZzt, jZzi);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PutDataRequest[] newArray(int i) {
        return new PutDataRequest[i];
    }
}
