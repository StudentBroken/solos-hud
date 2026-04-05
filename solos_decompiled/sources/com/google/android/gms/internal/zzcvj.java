package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbu;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcvj extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcvj> CREATOR = new zzcvk();
    private final ConnectionResult zzaBS;
    private int zzakw;
    private final zzbu zzbCZ;

    public zzcvj(int i) {
        this(new ConnectionResult(8, null), null);
    }

    zzcvj(int i, ConnectionResult connectionResult, zzbu zzbuVar) {
        this.zzakw = i;
        this.zzaBS = connectionResult;
        this.zzbCZ = zzbuVar;
    }

    private zzcvj(ConnectionResult connectionResult, zzbu zzbuVar) {
        this(1, connectionResult, null);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.zzaBS, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzbCZ, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final zzbu zzAv() {
        return this.zzbCZ;
    }

    public final ConnectionResult zzpx() {
        return this.zzaBS;
    }
}
