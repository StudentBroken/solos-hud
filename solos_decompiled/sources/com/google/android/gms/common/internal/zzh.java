package com.google.android.gms.common.internal;

import android.app.PendingIntent;
import android.os.Handler;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes67.dex */
final class zzh extends Handler {
    private /* synthetic */ zzd zzaHg;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzh(zzd zzdVar, Looper looper) {
        super(looper);
        this.zzaHg = zzdVar;
    }

    private static void zza(Message message) {
        ((zzi) message.obj).unregister();
    }

    private static boolean zzb(Message message) {
        return message.what == 2 || message.what == 1 || message.what == 7;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        if (this.zzaHg.zzaHd.get() != message.arg1) {
            if (zzb(message)) {
                zza(message);
                return;
            }
            return;
        }
        if ((message.what == 1 || message.what == 7 || message.what == 4 || message.what == 5) && !this.zzaHg.isConnecting()) {
            zza(message);
            return;
        }
        if (message.what == 4) {
            this.zzaHg.zzaHb = new ConnectionResult(message.arg2);
            if (this.zzaHg.zzrg() && !this.zzaHg.zzaHc) {
                this.zzaHg.zza(3, (IInterface) null);
                return;
            }
            ConnectionResult connectionResult = this.zzaHg.zzaHb != null ? this.zzaHg.zzaHb : new ConnectionResult(8);
            this.zzaHg.zzaGS.zzf(connectionResult);
            this.zzaHg.onConnectionFailed(connectionResult);
            return;
        }
        if (message.what == 5) {
            ConnectionResult connectionResult2 = this.zzaHg.zzaHb != null ? this.zzaHg.zzaHb : new ConnectionResult(8);
            this.zzaHg.zzaGS.zzf(connectionResult2);
            this.zzaHg.onConnectionFailed(connectionResult2);
            return;
        }
        if (message.what == 3) {
            ConnectionResult connectionResult3 = new ConnectionResult(message.arg2, message.obj instanceof PendingIntent ? (PendingIntent) message.obj : null);
            this.zzaHg.zzaGS.zzf(connectionResult3);
            this.zzaHg.onConnectionFailed(connectionResult3);
            return;
        }
        if (message.what == 6) {
            this.zzaHg.zza(5, (IInterface) null);
            if (this.zzaHg.zzaGX != null) {
                this.zzaHg.zzaGX.onConnectionSuspended(message.arg2);
            }
            this.zzaHg.onConnectionSuspended(message.arg2);
            this.zzaHg.zza(5, 1, (IInterface) null);
            return;
        }
        if (message.what == 2 && !this.zzaHg.isConnected()) {
            zza(message);
        } else if (zzb(message)) {
            ((zzi) message.obj).zzri();
        } else {
            Log.wtf("GmsClient", new StringBuilder(45).append("Don't know how to handle message: ").append(message.what).toString(), new Exception());
        }
    }
}
