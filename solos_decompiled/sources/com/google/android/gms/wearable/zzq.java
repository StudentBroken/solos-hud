package com.google.android.gms.wearable;

import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.internal.zzaa;

/* JADX INFO: loaded from: classes6.dex */
final class zzq implements Runnable {
    private /* synthetic */ WearableListenerService.zzc zzbRC;
    private /* synthetic */ zzaa zzbRG;

    zzq(WearableListenerService.zzc zzcVar, zzaa zzaaVar) {
        this.zzbRC = zzcVar;
        this.zzbRG = zzaaVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WearableListenerService.this.onCapabilityChanged(this.zzbRG);
    }
}
