package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;

/* JADX INFO: loaded from: classes67.dex */
public final class zzar extends zzed implements zzap {
    zzar(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ICancelToken");
    }

    @Override // com.google.android.gms.common.internal.zzap
    public final void cancel() throws RemoteException {
        zzc(2, zzY());
    }
}
