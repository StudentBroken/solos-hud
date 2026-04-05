package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/* JADX INFO: loaded from: classes3.dex */
final class zzbel extends Handler {
    private /* synthetic */ zzbej zzaEc;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbel(zzbej zzbejVar, Looper looper) {
        super(looper);
        this.zzaEc = zzbejVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                ((zzbek) message.obj).zzc(this.zzaEc);
                return;
            case 2:
                throw ((RuntimeException) message.obj);
            default:
                Log.w("GACStateManager", new StringBuilder(31).append("Unknown message id: ").append(message.what).toString());
                return;
        }
    }
}
