package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes67.dex */
public final class Scope extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<Scope> CREATOR = new zze();
    private final String zzaBn;
    private int zzakw;

    Scope(int i, String str) {
        zzbr.zzh(str, "scopeUri must not be null or empty");
        this.zzakw = i;
        this.zzaBn = str;
    }

    public Scope(String str) {
        this(1, str);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Scope) {
            return this.zzaBn.equals(((Scope) obj).zzaBn);
        }
        return false;
    }

    public final int hashCode() {
        return this.zzaBn.hashCode();
    }

    public final String toString() {
        return this.zzaBn;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzaBn, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final String zzpn() {
        return this.zzaBn;
    }
}
