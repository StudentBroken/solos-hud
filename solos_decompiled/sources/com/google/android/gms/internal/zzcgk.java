package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgk implements Parcelable.Creator<zzcgi> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcgi createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        Bundle bundleZzs = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    bundleZzs = com.google.android.gms.common.internal.safeparcel.zzb.zzs(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcgi(bundleZzs);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcgi[] newArray(int i) {
        return new zzcgi[i];
    }
}
