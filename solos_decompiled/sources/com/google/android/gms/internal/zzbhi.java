package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbhi extends com.google.android.gms.common.internal.zzaa<zzbhl> {
    public zzbhi(Context context, Looper looper, com.google.android.gms.common.internal.zzq zzqVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 39, zzqVar, connectionCallbacks, onConnectionFailedListener);
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonService");
        return iInterfaceQueryLocalInterface instanceof zzbhl ? (zzbhl) iInterfaceQueryLocalInterface : new zzbhm(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzd
    public final String zzda() {
        return "com.google.android.gms.common.service.START";
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final String zzdb() {
        return "com.google.android.gms.common.internal.service.ICommonService";
    }
}
