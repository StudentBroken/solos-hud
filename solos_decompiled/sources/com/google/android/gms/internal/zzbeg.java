package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/* JADX INFO: loaded from: classes3.dex */
final class zzbeg extends Handler {
    private /* synthetic */ zzbeb zzaDP;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbeg(zzbeb zzbebVar, Looper looper) {
        super(looper);
        this.zzaDP = zzbebVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                this.zzaDP.zzqb();
                break;
            case 2:
                this.zzaDP.resume();
                break;
            default:
                Log.w("GoogleApiClientImpl", new StringBuilder(31).append("Unknown message id: ").append(message.what).toString());
                break;
        }
    }
}
