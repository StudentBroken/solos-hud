package com.google.android.gms.measurement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.google.android.gms.internal.zzcho;
import com.google.android.gms.internal.zzchq;

/* JADX INFO: loaded from: classes70.dex */
public final class AppMeasurementReceiver extends WakefulBroadcastReceiver implements zzchq {
    private zzcho zzbos;

    @Override // com.google.android.gms.internal.zzchq
    @MainThread
    public final void doStartService(Context context, Intent intent) {
        startWakefulService(context, intent);
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
