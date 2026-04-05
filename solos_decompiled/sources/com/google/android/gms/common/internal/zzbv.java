package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbv implements Parcelable.Creator<zzbu> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbu createFromParcel(Parcel parcel) {
        ConnectionResult connectionResult = null;
        boolean zZzc = false;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        boolean zZzc2 = false;
        IBinder iBinderZzr = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                case 3:
                    connectionResult = (ConnectionResult) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, ConnectionResult.CREATOR);
                    break;
                case 4:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
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
        return new zzbu(iZzg, iBinderZzr, connectionResult, zZzc2, zZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbu[] newArray(int i) {
        return new zzbu[i];
    }
}
