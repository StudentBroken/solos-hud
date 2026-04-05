package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcc implements Parcelable.Creator<zzcb> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcb createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        byte[] bArrZzt = null;
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
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcb(uri, bundleZzs, bArrZzt);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcb[] newArray(int i) {
        return new zzcb[i];
    }
}
