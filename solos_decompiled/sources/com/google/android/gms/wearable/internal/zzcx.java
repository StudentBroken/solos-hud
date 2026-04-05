package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.ConnectionConfiguration;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcx implements Parcelable.Creator<zzcw> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcw createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        ConnectionConfiguration[] connectionConfigurationArr = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    connectionConfigurationArr = (ConnectionConfiguration[]) com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i, ConnectionConfiguration.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzcw(iZzg, connectionConfigurationArr);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzcw[] newArray(int i) {
        return new zzcw[i];
    }
}
