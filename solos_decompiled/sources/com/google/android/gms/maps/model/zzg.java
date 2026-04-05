package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class zzg implements Parcelable.Creator<MapStyleOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ MapStyleOptions createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new MapStyleOptions(strZzq);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ MapStyleOptions[] newArray(int i) {
        return new MapStyleOptions[i];
    }
}
