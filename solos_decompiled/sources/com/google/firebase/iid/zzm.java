package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* JADX INFO: loaded from: classes35.dex */
final class zzm extends Handler {
    private /* synthetic */ zzl zzcnD;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzm(zzl zzlVar, Looper looper) {
        super(looper);
        this.zzcnD = zzlVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        this.zzcnD.zzc(message);
    }
}
