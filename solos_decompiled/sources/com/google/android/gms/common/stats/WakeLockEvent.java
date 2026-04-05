package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class WakeLockEvent extends StatsEvent {
    public static final Parcelable.Creator<WakeLockEvent> CREATOR = new zzd();
    private final long mTimeout;
    private final float zzaJA;
    private long zzaJB;
    private final long zzaJp;
    private int zzaJq;
    private final String zzaJr;
    private final String zzaJs;
    private final String zzaJt;
    private final int zzaJu;
    private final List<String> zzaJv;
    private final String zzaJw;
    private final long zzaJx;
    private int zzaJy;
    private final String zzaJz;
    private int zzakw;

    WakeLockEvent(int i, long j, int i2, String str, int i3, List<String> list, String str2, long j2, int i4, String str3, String str4, float f, long j3, String str5) {
        this.zzakw = i;
        this.zzaJp = j;
        this.zzaJq = i2;
        this.zzaJr = str;
        this.zzaJs = str3;
        this.zzaJt = str5;
        this.zzaJu = i3;
        this.zzaJB = -1L;
        this.zzaJv = list;
        this.zzaJw = str2;
        this.zzaJx = j2;
        this.zzaJy = i4;
        this.zzaJz = str4;
        this.zzaJA = f;
        this.mTimeout = j3;
    }

    public WakeLockEvent(long j, int i, String str, int i2, List<String> list, String str2, long j2, int i3, String str3, String str4, float f, long j3, String str5) {
        this(2, j, i, str, i2, list, str2, j2, i3, str3, str4, f, j3, str5);
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final int getEventType() {
        return this.zzaJq;
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final long getTimeMillis() {
        return this.zzaJp;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzaJp);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzaJr, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 5, this.zzaJu);
        com.google.android.gms.common.internal.safeparcel.zzd.zzb(parcel, 6, this.zzaJv, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, this.zzaJx);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, this.zzaJs, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 11, this.zzaJq);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 12, this.zzaJw, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 13, this.zzaJz, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 14, this.zzaJy);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 15, this.zzaJA);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 16, this.mTimeout);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 17, this.zzaJt, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final long zzrU() {
        return this.zzaJB;
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final String zzrV() {
        String strValueOf = String.valueOf("\t");
        String strValueOf2 = String.valueOf(this.zzaJr);
        String strValueOf3 = String.valueOf("\t");
        int i = this.zzaJu;
        String strValueOf4 = String.valueOf("\t");
        String strJoin = this.zzaJv == null ? "" : TextUtils.join(",", this.zzaJv);
        String strValueOf5 = String.valueOf("\t");
        int i2 = this.zzaJy;
        String strValueOf6 = String.valueOf("\t");
        String str = this.zzaJs == null ? "" : this.zzaJs;
        String strValueOf7 = String.valueOf("\t");
        String str2 = this.zzaJz == null ? "" : this.zzaJz;
        String strValueOf8 = String.valueOf("\t");
        float f = this.zzaJA;
        String strValueOf9 = String.valueOf("\t");
        String str3 = this.zzaJt == null ? "" : this.zzaJt;
        return new StringBuilder(String.valueOf(strValueOf).length() + 37 + String.valueOf(strValueOf2).length() + String.valueOf(strValueOf3).length() + String.valueOf(strValueOf4).length() + String.valueOf(strJoin).length() + String.valueOf(strValueOf5).length() + String.valueOf(strValueOf6).length() + String.valueOf(str).length() + String.valueOf(strValueOf7).length() + String.valueOf(str2).length() + String.valueOf(strValueOf8).length() + String.valueOf(strValueOf9).length() + String.valueOf(str3).length()).append(strValueOf).append(strValueOf2).append(strValueOf3).append(i).append(strValueOf4).append(strJoin).append(strValueOf5).append(i2).append(strValueOf6).append(str).append(strValueOf7).append(str2).append(strValueOf8).append(f).append(strValueOf9).append(str3).toString();
    }
}
