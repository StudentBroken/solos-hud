package com.google.android.gms.auth.api.signin.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;

/* JADX INFO: loaded from: classes3.dex */
public final class zzm implements Parcelable.Creator<zzn> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzn createFromParcel(Parcel parcel) {
        int iZzg = 0;
        int iZzd = zzb.zzd(parcel);
        Bundle bundleZzs = null;
        int iZzg2 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg2 = zzb.zzg(parcel, i);
                    break;
                case 2:
                    iZzg = zzb.zzg(parcel, i);
                    break;
                case 3:
                    bundleZzs = zzb.zzs(parcel, i);
                    break;
                default:
                    zzb.zzb(parcel, i);
                    break;
            }
        }
        zzb.zzF(parcel, iZzd);
        return new zzn(iZzg2, iZzg, bundleZzs);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzn[] newArray(int i) {
        return new zzn[i];
    }
}
