package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzei extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzei> CREATOR = new zzej();
    public final int statusCode;
    public final zzak zzbSl;

    public zzei(int i, zzak zzakVar) {
        this.statusCode = i;
        this.zzbSl = zzakVar;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzbSl, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
