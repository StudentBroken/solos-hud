package com.google.android.gms.wearable;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.WearableListenerService;

/* JADX INFO: loaded from: classes6.dex */
final class zzl implements Runnable {
    private /* synthetic */ DataHolder zzbRB;
    private /* synthetic */ WearableListenerService.zzc zzbRC;

    zzl(WearableListenerService.zzc zzcVar, DataHolder dataHolder) {
        this.zzbRC = zzcVar;
        this.zzbRB = dataHolder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DataEventBuffer dataEventBuffer = new DataEventBuffer(this.zzbRB);
        try {
            WearableListenerService.this.onDataChanged(dataEventBuffer);
        } finally {
            dataEventBuffer.release();
        }
    }
}
