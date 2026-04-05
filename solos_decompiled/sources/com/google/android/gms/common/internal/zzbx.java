package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbx implements Parcelable.Creator<zzbw> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbw createFromParcel(Parcel parcel) {
        int iZzg = 0;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        Scope[] scopeArr = null;
        int iZzg2 = 0;
        int iZzg3 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 4:
                    scopeArr = (Scope[]) com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i, Scope.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzbw(iZzg3, iZzg2, iZzg, scopeArr);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbw[] newArray(int i) {
        return new zzbw[i];
    }
}
