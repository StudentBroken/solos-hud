package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcfw extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcfw> CREATOR = new zzcfx();
    public String packageName;
    private int versionCode;
    public String zzbpg;
    public zzcku zzbph;
    public long zzbpi;
    public boolean zzbpj;
    public String zzbpk;
    public zzcgl zzbpl;
    public long zzbpm;
    public zzcgl zzbpn;
    public long zzbpo;
    public zzcgl zzbpp;

    zzcfw(int i, String str, String str2, zzcku zzckuVar, long j, boolean z, String str3, zzcgl zzcglVar, long j2, zzcgl zzcglVar2, long j3, zzcgl zzcglVar3) {
        this.versionCode = i;
        this.packageName = str;
        this.zzbpg = str2;
        this.zzbph = zzckuVar;
        this.zzbpi = j;
        this.zzbpj = z;
        this.zzbpk = str3;
        this.zzbpl = zzcglVar;
        this.zzbpm = j2;
        this.zzbpn = zzcglVar2;
        this.zzbpo = j3;
        this.zzbpp = zzcglVar3;
    }

    zzcfw(zzcfw zzcfwVar) {
        this.versionCode = 1;
        zzbr.zzu(zzcfwVar);
        this.packageName = zzcfwVar.packageName;
        this.zzbpg = zzcfwVar.zzbpg;
        this.zzbph = zzcfwVar.zzbph;
        this.zzbpi = zzcfwVar.zzbpi;
        this.zzbpj = zzcfwVar.zzbpj;
        this.zzbpk = zzcfwVar.zzbpk;
        this.zzbpl = zzcfwVar.zzbpl;
        this.zzbpm = zzcfwVar.zzbpm;
        this.zzbpn = zzcfwVar.zzbpn;
        this.zzbpo = zzcfwVar.zzbpo;
        this.zzbpp = zzcfwVar.zzbpp;
    }

    zzcfw(String str, String str2, zzcku zzckuVar, long j, boolean z, String str3, zzcgl zzcglVar, long j2, zzcgl zzcglVar2, long j3, zzcgl zzcglVar3) {
        this.versionCode = 1;
        this.packageName = str;
        this.zzbpg = str2;
        this.zzbph = zzckuVar;
        this.zzbpi = j;
        this.zzbpj = z;
        this.zzbpk = str3;
        this.zzbpl = zzcglVar;
        this.zzbpm = j2;
        this.zzbpn = zzcglVar2;
        this.zzbpo = j3;
        this.zzbpp = zzcglVar3;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.packageName, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzbpg, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, (Parcelable) this.zzbph, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzbpi);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzbpj);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzbpk, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, (Parcelable) this.zzbpl, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, this.zzbpm);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, (Parcelable) this.zzbpn, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 11, this.zzbpo);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 12, (Parcelable) this.zzbpp, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
