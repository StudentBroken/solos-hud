package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Scope;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
@WorkerThread
public interface zzbfx {
    void zzb(com.google.android.gms.common.internal.zzam zzamVar, Set<Scope> set);

    void zzh(ConnectionResult connectionResult);
}
