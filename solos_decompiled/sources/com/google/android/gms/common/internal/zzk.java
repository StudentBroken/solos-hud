package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/* JADX INFO: loaded from: classes67.dex */
public final class zzk extends zzaw {
    private zzd zzaHi;
    private final int zzaHj;

    public zzk(@NonNull zzd zzdVar, int i) {
        this.zzaHi = zzdVar;
        this.zzaHj = i;
    }

    @Override // com.google.android.gms.common.internal.zzav
    @BinderThread
    public final void zza(int i, @Nullable Bundle bundle) {
        Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
    }

    @Override // com.google.android.gms.common.internal.zzav
    @BinderThread
    public final void zza(int i, @NonNull IBinder iBinder, @Nullable Bundle bundle) {
        zzbr.zzb(this.zzaHi, "onPostInitComplete can be called only once per call to getRemoteService");
        this.zzaHi.zza(i, iBinder, bundle, this.zzaHj);
        this.zzaHi = null;
    }
}
