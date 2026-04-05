package com.google.android.gms.wearable;

import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.internal.zzdx;

/* JADX INFO: loaded from: classes6.dex */
final class zzm implements Runnable {
    private /* synthetic */ WearableListenerService.zzc zzbRC;
    private /* synthetic */ zzdx zzbRD;

    zzm(WearableListenerService.zzc zzcVar, zzdx zzdxVar) {
        this.zzbRC = zzcVar;
        this.zzbRD = zzdxVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WearableListenerService.this.onMessageReceived(this.zzbRD);
    }
}
