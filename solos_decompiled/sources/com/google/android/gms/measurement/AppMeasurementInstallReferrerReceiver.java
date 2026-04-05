package com.google.android.gms.measurement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import com.google.android.gms.internal.zzcho;
import com.google.android.gms.internal.zzchq;

/* JADX INFO: loaded from: classes70.dex */
public final class AppMeasurementInstallReferrerReceiver extends BroadcastReceiver implements zzchq {
    private zzcho zzbos;

    @Override // com.google.android.gms.internal.zzchq
    public final void doStartService(Context context, Intent intent) {
    }

    @Override // android.content.BroadcastReceiver
    @MainThread
    public final void onReceive(Context context, Intent intent) {
        if (this.zzbos == null) {
            this.zzbos = new zzcho(this);
        }
        this.zzbos.onReceive(context, intent);
    }
}
