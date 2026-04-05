package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcft extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcft> CREATOR = new zzcfu();
    public final String packageName;
    public final String zzbha;
    public final String zzboU;
    public final String zzboV;
    public final long zzboW;
    public final long zzboX;
    public final String zzboY;
    public final boolean zzboZ;
    public final boolean zzbpa;
    public final long zzbpb;
    public final String zzbpc;
    public final long zzbpd;
    public final long zzbpe;
    public final int zzbpf;

    zzcft(String str, String str2, String str3, long j, String str4, long j2, long j3, String str5, boolean z, boolean z2, String str6, long j4, long j5, int i) {
        zzbr.zzcF(str);
        this.packageName = str;
        this.zzboU = TextUtils.isEmpty(str2) ? null : str2;
        this.zzbha = str3;
        this.zzbpb = j;
        this.zzboV = str4;
        this.zzboW = j2;
        this.zzboX = j3;
        this.zzboY = str5;
        this.zzboZ = z;
        this.zzbpa = z2;
        this.zzbpc = str6;
        this.zzbpd = j4;
        this.zzbpe = j5;
        this.zzbpf = i;
    }

    zzcft(String str, String str2, String str3, String str4, long j, long j2, String str5, boolean z, boolean z2, long j3, String str6, long j4, long j5, int i) {
        this.packageName = str;
        this.zzboU = str2;
        this.zzbha = str3;
        this.zzbpb = j3;
        this.zzboV = str4;
        this.zzboW = j;
        this.zzboX = j2;
        this.zzboY = str5;
        this.zzboZ = z;
        this.zzbpa = z2;
        this.zzbpc = str6;
        this.zzbpd = j4;
        this.zzbpe = j5;
        this.zzbpf = i;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.packageName, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzboU, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbha, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzboV, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzboW);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzboX);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, this.zzboY, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, this.zzboZ);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, this.zzbpa);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 11, this.zzbpb);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 12, this.zzbpc, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 13, this.zzbpd);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 14, this.zzbpe);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 15, this.zzbpf);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
