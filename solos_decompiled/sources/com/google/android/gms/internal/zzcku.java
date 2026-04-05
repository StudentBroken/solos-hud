package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcku extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcku> CREATOR = new zzckv();
    public final String name;
    private int versionCode;
    private String zzaIH;
    public final String zzbpg;
    public final long zzbuC;
    private Long zzbuD;
    private Float zzbuE;
    private Double zzbuF;

    zzcku(int i, String str, long j, Long l, Float f, String str2, String str3, Double d) {
        this.versionCode = i;
        this.name = str;
        this.zzbuC = j;
        this.zzbuD = l;
        this.zzbuE = null;
        if (i == 1) {
            this.zzbuF = f != null ? Double.valueOf(f.doubleValue()) : null;
        } else {
            this.zzbuF = d;
        }
        this.zzaIH = str2;
        this.zzbpg = str3;
    }

    zzcku(zzckw zzckwVar) {
        this(zzckwVar.mName, zzckwVar.zzbuG, zzckwVar.mValue, zzckwVar.mOrigin);
    }

    zzcku(String str, long j, Object obj, String str2) {
        zzbr.zzcF(str);
        this.versionCode = 2;
        this.name = str;
        this.zzbuC = j;
        this.zzbpg = str2;
        if (obj == null) {
            this.zzbuD = null;
            this.zzbuE = null;
            this.zzbuF = null;
            this.zzaIH = null;
            return;
        }
        if (obj instanceof Long) {
            this.zzbuD = (Long) obj;
            this.zzbuE = null;
            this.zzbuF = null;
            this.zzaIH = null;
            return;
        }
        if (obj instanceof String) {
            this.zzbuD = null;
            this.zzbuE = null;
            this.zzbuF = null;
            this.zzaIH = (String) obj;
            return;
        }
        if (!(obj instanceof Double)) {
            throw new IllegalArgumentException("User attribute given of un-supported type");
        }
        this.zzbuD = null;
        this.zzbuE = null;
        this.zzbuF = (Double) obj;
        this.zzaIH = null;
    }

    public final Object getValue() {
        if (this.zzbuD != null) {
            return this.zzbuD;
        }
        if (this.zzbuF != null) {
            return this.zzbuF;
        }
        if (this.zzaIH != null) {
            return this.zzaIH;
        }
        return null;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.name, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzbuC);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbuD, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, (Float) null, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzaIH, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzbpg, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, this.zzbuF, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
