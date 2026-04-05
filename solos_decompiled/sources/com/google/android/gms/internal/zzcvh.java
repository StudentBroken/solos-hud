package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbs;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcvh extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcvh> CREATOR = new zzcvi();
    private int zzakw;
    private zzbs zzbCY;

    zzcvh(int i, zzbs zzbsVar) {
        this.zzakw = i;
        this.zzbCY = zzbsVar;
    }

    public zzcvh(zzbs zzbsVar) {
        this(1, zzbsVar);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.zzbCY, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
