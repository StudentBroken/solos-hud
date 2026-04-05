package com.google.android.gms.wearable.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzeo extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzeo> CREATOR = new zzep();
    private int zzakw;
    private zzdk zzbRL;

    zzeo(int i, IBinder iBinder) {
        zzdk zzdmVar = null;
        this.zzakw = i;
        if (iBinder == null) {
            this.zzbRL = null;
            return;
        }
        if (iBinder != null) {
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wearable.internal.IWearableListener");
            zzdmVar = iInterfaceQueryLocalInterface instanceof zzdk ? (zzdk) iInterfaceQueryLocalInterface : new zzdm(iBinder);
        }
        this.zzbRL = zzdmVar;
    }

    public zzeo(zzdk zzdkVar) {
        this.zzakw = 1;
        this.zzbRL = zzdkVar;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzbRL == null ? null : this.zzbRL.asBinder(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
