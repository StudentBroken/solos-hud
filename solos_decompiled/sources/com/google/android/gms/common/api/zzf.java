package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes67.dex */
public final class zzf implements Parcelable.Creator<Status> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Status createFromParcel(Parcel parcel) {
        PendingIntent pendingIntent = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        int iZzg2 = 0;
        String strZzq = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 3:
                    pendingIntent = (PendingIntent) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, PendingIntent.CREATOR);
                    break;
                case 1000:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new Status(iZzg2, iZzg, strZzq, pendingIntent);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Status[] newArray(int i) {
        return new Status[i];
    }
}
