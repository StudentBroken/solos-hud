package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbig implements Parcelable.Creator<zzbif> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbif createFromParcel(Parcel parcel) {
        zzbia zzbiaVar = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        Parcel parcelZzD = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    parcelZzD = com.google.android.gms.common.internal.safeparcel.zzb.zzD(parcel, i);
                    break;
                case 3:
                    zzbiaVar = (zzbia) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, zzbia.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzbif(iZzg, parcelZzD, zzbiaVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbif[] newArray(int i) {
        return new zzbif[i];
    }
}
