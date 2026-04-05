package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
final class zzbhf extends zzbgz {
    private final zzbcl<Status> zzaIB;

    public zzbhf(zzbcl<Status> zzbclVar) {
        this.zzaIB = zzbclVar;
    }

    @Override // com.google.android.gms.internal.zzbgz, com.google.android.gms.internal.zzbhj
    public final void zzaC(int i) throws RemoteException {
        this.zzaIB.setResult(new Status(i));
    }
}
