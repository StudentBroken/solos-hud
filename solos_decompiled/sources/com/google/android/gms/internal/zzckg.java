package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;

/* JADX INFO: loaded from: classes36.dex */
final class zzckg implements Runnable {
    private /* synthetic */ zzckc zzbuo;

    zzckg(zzckc zzckcVar) {
        this.zzbuo = zzckcVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzcjp zzcjpVar = this.zzbuo.zzbue;
        Context context = this.zzbuo.zzbue.getContext();
        zzcfy.zzxD();
        zzcjpVar.onServiceDisconnected(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
