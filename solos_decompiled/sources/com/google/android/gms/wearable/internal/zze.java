package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zze implements Parcelable.Creator<zzd> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzd createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq = null;
        String strZzq2 = null;
        IntentFilter[] intentFilterArr = null;
        IBinder iBinderZzr = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                case 3:
                    intentFilterArr = (IntentFilter[]) com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i, IntentFilter.CREATOR);
                    break;
                case 4:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 5:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzd(iBinderZzr, intentFilterArr, strZzq2, strZzq);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzd[] newArray(int i) {
        return new zzd[i];
    }
}
