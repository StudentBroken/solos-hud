package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes3.dex */
final class zzbfj extends Handler {
    private /* synthetic */ zzbfi zzaEQ;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzbfj(zzbfi zzbfiVar, Looper looper) {
        super(looper);
        this.zzaEQ = zzbfiVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        zzbr.zzaf(message.what == 1);
        this.zzaEQ.zzb((zzbfl) message.obj);
    }
}
