package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbho extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbho> CREATOR = new zzbhp();
    private final zzbhq zzaID;
    private int zzakw;

    zzbho(int i, zzbhq zzbhqVar) {
        this.zzakw = i;
        this.zzaID = zzbhqVar;
    }

    private zzbho(zzbhq zzbhqVar) {
        this.zzakw = 1;
        this.zzaID = zzbhqVar;
    }

    public static zzbho zza(zzbhw<?, ?> zzbhwVar) {
        if (zzbhwVar instanceof zzbhq) {
            return new zzbho((zzbhq) zzbhwVar);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.zzaID, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final zzbhw<?, ?> zzrJ() {
        if (this.zzaID != null) {
            return this.zzaID;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }
}
