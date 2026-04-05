package com.google.android.gms.common;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzat;
import com.google.android.gms.dynamic.IObjectWrapper;

/* JADX INFO: loaded from: classes67.dex */
public final class zzm extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzm> CREATOR = new zzn();
    private final String zzaAn;
    private final zzg zzaAo;
    private final boolean zzaAp;

    zzm(String str, IBinder iBinder, boolean z) {
        this.zzaAn = str;
        this.zzaAo = zzG(iBinder);
        this.zzaAp = z;
    }

    zzm(String str, zzg zzgVar, boolean z) {
        this.zzaAn = str;
        this.zzaAo = zzgVar;
        this.zzaAp = z;
    }

    private static zzg zzG(IBinder iBinder) {
        zzh zzhVar;
        if (iBinder == null) {
            return null;
        }
        try {
            IObjectWrapper iObjectWrapperZzoW = zzat.zzI(iBinder).zzoW();
            byte[] bArr = iObjectWrapperZzoW == null ? null : (byte[]) com.google.android.gms.dynamic.zzn.zzE(iObjectWrapperZzoW);
            if (bArr != null) {
                zzhVar = new zzh(bArr);
            } else {
                Log.e("GoogleCertificatesQuery", "Could not unwrap certificate");
                zzhVar = null;
            }
            return zzhVar;
        } catch (RemoteException e) {
            Log.e("GoogleCertificatesQuery", "Could not unwrap certificate", e);
            return null;
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        IBinder iBinderAsBinder;
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 1, this.zzaAn, false);
        if (this.zzaAo == null) {
            Log.w("GoogleCertificatesQuery", "certificate binder is null");
            iBinderAsBinder = null;
        } else {
            iBinderAsBinder = this.zzaAo.asBinder();
        }
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, iBinderAsBinder, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzaAp);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
