package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbfl;
import com.google.android.gms.wearable.MessageApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzgc implements zzbfl<MessageApi.MessageListener> {
    private /* synthetic */ zzdx zzbRD;

    zzgc(zzdx zzdxVar) {
        this.zzbRD = zzdxVar;
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final void zzpR() {
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final /* synthetic */ void zzq(MessageApi.MessageListener messageListener) {
        messageListener.onMessageReceived(this.zzbRD);
    }
}
