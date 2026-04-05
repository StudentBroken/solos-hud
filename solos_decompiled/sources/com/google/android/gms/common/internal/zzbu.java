package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbu extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbu> CREATOR = new zzbv();
    private ConnectionResult zzaBS;
    private boolean zzaDo;
    private IBinder zzaIs;
    private boolean zzaIt;
    private int zzakw;

    zzbu(int i, IBinder iBinder, ConnectionResult connectionResult, boolean z, boolean z2) {
        this.zzakw = i;
        this.zzaIs = iBinder;
        this.zzaBS = connectionResult;
        this.zzaDo = z;
        this.zzaIt = z2;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzbu)) {
            return false;
        }
        zzbu zzbuVar = (zzbu) obj;
        return this.zzaBS.equals(zzbuVar.zzaBS) && zzrG().equals(zzbuVar.zzrG());
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzaIs, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzaBS, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzaDo);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzaIt);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final ConnectionResult zzpx() {
        return this.zzaBS;
    }

    public final zzam zzrG() {
        IBinder iBinder = this.zzaIs;
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
        return iInterfaceQueryLocalInterface instanceof zzam ? (zzam) iInterfaceQueryLocalInterface : new zzao(iBinder);
    }

    public final boolean zzrH() {
        return this.zzaDo;
    }

    public final boolean zzrI() {
        return this.zzaIt;
    }
}
