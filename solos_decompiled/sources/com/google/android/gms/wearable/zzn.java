package com.google.android.gms.wearable;

import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.internal.zzeg;

/* JADX INFO: loaded from: classes6.dex */
final class zzn implements Runnable {
    private /* synthetic */ WearableListenerService.zzc zzbRC;
    private /* synthetic */ zzeg zzbRE;

    zzn(WearableListenerService.zzc zzcVar, zzeg zzegVar) {
        this.zzbRC = zzcVar;
        this.zzbRE = zzegVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WearableListenerService.this.onPeerConnected(this.zzbRE);
    }
}
