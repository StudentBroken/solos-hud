package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.ConnectionConfiguration;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcv implements Parcelable.Creator<zzcu> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcu createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        ConnectionConfiguration connectionConfiguration = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    connectionConfiguration = (ConnectionConfiguration) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, ConnectionConfiguration.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcu(iZzg, connectionConfiguration);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcu[] newArray(int i) {
        return new zzcu[i];
    }
}
