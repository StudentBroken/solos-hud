package com.google.android.gms.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
class zzchg extends BroadcastReceiver {
    private static String zzais = zzchg.class.getName();
    private boolean mRegistered;
    private boolean zzait;
    private final zzchx zzboi;

    zzchg(zzchx zzchxVar) {
        zzbr.zzu(zzchxVar);
        this.zzboi = zzchxVar;
    }

    @Override // android.content.BroadcastReceiver
    @MainThread
    public void onReceive(Context context, Intent intent) {
        this.zzboi.zzkC();
        String action = intent.getAction();
        this.zzboi.zzwE().zzyB().zzj("NetworkBroadcastReceiver received action", action);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            this.zzboi.zzwE().zzyx().zzj("NetworkBroadcastReceiver received unknown action", action);
            return;
        }
        boolean zZzlP = this.zzboi.zzyS().zzlP();
        if (this.zzait != zZzlP) {
            this.zzait = zZzlP;
            this.zzboi.zzwD().zzj(new zzchh(this, zZzlP));
        }
    }

    @WorkerThread
    public final void unregister() {
        this.zzboi.zzkC();
        this.zzboi.zzwD().zzjB();
        this.zzboi.zzwD().zzjB();
        if (this.mRegistered) {
            this.zzboi.zzwE().zzyB().log("Unregistering connectivity change receiver");
            this.mRegistered = false;
            this.zzait = false;
            try {
                this.zzboi.getContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                this.zzboi.zzwE().zzyv().zzj("Failed to unregister the network broadcast receiver", e);
            }
        }
    }

    @WorkerThread
    public final void zzlM() {
        this.zzboi.zzkC();
        this.zzboi.zzwD().zzjB();
        if (this.mRegistered) {
            return;
        }
        this.zzboi.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.zzait = this.zzboi.zzyS().zzlP();
        this.zzboi.zzwE().zzyB().zzj("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzait));
        this.mRegistered = true;
    }
}
