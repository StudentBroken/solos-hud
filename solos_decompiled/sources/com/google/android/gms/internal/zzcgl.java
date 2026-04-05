package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgl extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcgl> CREATOR = new zzcgm();
    public final String name;
    public final zzcgi zzbpQ;
    public final long zzbpR;
    public final String zzbpg;

    zzcgl(zzcgl zzcglVar, long j) {
        zzbr.zzu(zzcglVar);
        this.name = zzcglVar.name;
        this.zzbpQ = zzcglVar.zzbpQ;
        this.zzbpg = zzcglVar.zzbpg;
        this.zzbpR = j;
    }

    public zzcgl(String str, zzcgi zzcgiVar, String str2, long j) {
        this.name = str;
        this.zzbpQ = zzcgiVar;
        this.zzbpg = str2;
        this.zzbpR = j;
    }

    public final String toString() {
        String str = this.zzbpg;
        String str2 = this.name;
        String strValueOf = String.valueOf(this.zzbpQ);
        return new StringBuilder(String.valueOf(str).length() + 21 + String.valueOf(str2).length() + String.valueOf(strValueOf).length()).append("origin=").append(str).append(",name=").append(str2).append(",params=").append(strValueOf).toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.name, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzbpQ, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbpg, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzbpR);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
