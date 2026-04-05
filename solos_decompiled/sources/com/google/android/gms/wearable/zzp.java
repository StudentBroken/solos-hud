package com.google.android.gms.wearable;

import com.google.android.gms.wearable.WearableListenerService;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
final class zzp implements Runnable {
    private /* synthetic */ WearableListenerService.zzc zzbRC;
    private /* synthetic */ List zzbRF;

    zzp(WearableListenerService.zzc zzcVar, List list) {
        this.zzbRC = zzcVar;
        this.zzbRF = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WearableListenerService.this.onConnectedNodes(this.zzbRF);
    }
}
