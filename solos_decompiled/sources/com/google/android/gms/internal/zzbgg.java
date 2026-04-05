package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
final class zzbgg extends Handler {
    private /* synthetic */ zzbge zzaFk;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzbgg(zzbge zzbgeVar, Looper looper) {
        super(looper);
        this.zzaFk = zzbgeVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        switch (message.what) {
            case 0:
                PendingResult<?> pendingResult = (PendingResult) message.obj;
                synchronized (this.zzaFk.zzaBY) {
                    if (pendingResult == null) {
                        this.zzaFk.zzaFd.zzv(new Status(13, "Transform returned null"));
                    } else if (pendingResult instanceof zzbft) {
                        this.zzaFk.zzaFd.zzv(((zzbft) pendingResult).getStatus());
                    } else {
                        this.zzaFk.zzaFd.zza(pendingResult);
                    }
                    break;
                }
                return;
            case 1:
                RuntimeException runtimeException = (RuntimeException) message.obj;
                String strValueOf = String.valueOf(runtimeException.getMessage());
                Log.e("TransformedResultImpl", strValueOf.length() != 0 ? "Runtime exception on the transformation worker thread: ".concat(strValueOf) : new String("Runtime exception on the transformation worker thread: "));
                throw runtimeException;
            default:
                Log.e("TransformedResultImpl", new StringBuilder(70).append("TransformationResultHandler received unknown message type: ").append(message.what).toString());
                return;
        }
    }
}
