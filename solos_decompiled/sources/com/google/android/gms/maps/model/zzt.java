package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzt implements Parcelable.Creator<TileOverlayOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ TileOverlayOptions createFromParcel(Parcel parcel) {
        float fZzl = 0.0f;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        IBinder iBinderZzr = null;
        boolean zZzc = false;
        boolean zZzc2 = true;
        float fZzl2 = 0.0f;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                case 3:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 4:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                case 5:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 6:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new TileOverlayOptions(iBinderZzr, zZzc, fZzl2, zZzc2, fZzl);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ TileOverlayOptions[] newArray(int i) {
        return new TileOverlayOptions[i];
    }
}
