package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcm extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcm> CREATOR = new zzcn();
    public final int statusCode;
    public final ParcelFileDescriptor zzbSK;

    public zzcm(int i, ParcelFileDescriptor parcelFileDescriptor) {
        this.statusCode = i;
        this.zzbSK = parcelFileDescriptor;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzbSK, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
