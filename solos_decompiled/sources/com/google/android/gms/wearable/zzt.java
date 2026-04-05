package com.google.android.gms.wearable;

import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.internal.zzai;

/* JADX INFO: loaded from: classes6.dex */
final class zzt implements Runnable {
    private /* synthetic */ WearableListenerService.zzc zzbRC;
    private /* synthetic */ zzai zzbRJ;

    zzt(WearableListenerService.zzc zzcVar, zzai zzaiVar) {
        this.zzbRC = zzcVar;
        this.zzbRJ = zzaiVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbRJ.zza(WearableListenerService.this);
    }
}
