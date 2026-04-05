package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.support.annotation.NonNull;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgw extends com.google.android.gms.common.internal.zzd<zzcgp> {
    public zzcgw(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, com.google.android.gms.common.internal.zzg zzgVar) {
        super(context, looper, 93, zzfVar, zzgVar, null);
    }

    @Override // com.google.android.gms.common.internal.zzd
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
        return iInterfaceQueryLocalInterface instanceof zzcgp ? (zzcgp) iInterfaceQueryLocalInterface : new zzcgr(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzd
    @NonNull
    protected final String zzda() {
        return "com.google.android.gms.measurement.START";
    }

    @Override // com.google.android.gms.common.internal.zzd
    @NonNull
    protected final String zzdb() {
        return "com.google.android.gms.measurement.internal.IMeasurementService";
    }
}
