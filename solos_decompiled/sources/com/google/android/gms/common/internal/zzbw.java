package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbw extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbw> CREATOR = new zzbx();
    private final int zzaIu;
    private final int zzaIv;

    @Deprecated
    private final Scope[] zzaIw;
    private int zzakw;

    zzbw(int i, int i2, int i3, Scope[] scopeArr) {
        this.zzakw = i;
        this.zzaIu = i2;
        this.zzaIv = i3;
        this.zzaIw = scopeArr;
    }

    public zzbw(int i, int i2, Scope[] scopeArr) {
        this(1, i, i2, null);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.zzaIu);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzaIv);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, (Parcelable[]) this.zzaIw, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
