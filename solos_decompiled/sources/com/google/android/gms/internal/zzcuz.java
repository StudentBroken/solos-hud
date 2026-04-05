package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcuz extends com.google.android.gms.common.internal.safeparcel.zza implements Result {
    public static final Parcelable.Creator<zzcuz> CREATOR = new zzcva();
    private int zzakw;
    private int zzbCV;
    private Intent zzbCW;

    public zzcuz() {
        this(0, null);
    }

    zzcuz(int i, int i2, Intent intent) {
        this.zzakw = i;
        this.zzbCV = i2;
        this.zzbCW = intent;
    }

    private zzcuz(int i, Intent intent) {
        this(2, 0, null);
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.zzbCV == 0 ? Status.zzaBo : Status.zzaBs;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.zzbCV);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzbCW, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
