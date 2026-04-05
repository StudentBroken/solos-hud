package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.HashMap;

/* JADX INFO: loaded from: classes67.dex */
final class zzah extends zzaf implements Handler.Callback {
    private final Context mApplicationContext;
    private final Handler mHandler;
    private final HashMap<zzag, zzai> zzaHR = new HashMap<>();
    private final com.google.android.gms.common.stats.zza zzaHS = com.google.android.gms.common.stats.zza.zzrT();
    private final long zzaHT = 5000;
    private final long zzaHU = 300000;

    zzah(Context context) {
        this.mApplicationContext = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), this);
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        switch (message.what) {
            case 0:
                synchronized (this.zzaHR) {
                    zzag zzagVar = (zzag) message.obj;
                    zzai zzaiVar = this.zzaHR.get(zzagVar);
                    if (zzaiVar != null && zzaiVar.zzrB()) {
                        if (zzaiVar.isBound()) {
                            zzaiVar.zzcC("GmsClientSupervisor");
                        }
                        this.zzaHR.remove(zzagVar);
                    }
                    break;
                }
                return true;
            case 1:
                synchronized (this.zzaHR) {
                    zzag zzagVar2 = (zzag) message.obj;
                    zzai zzaiVar2 = this.zzaHR.get(zzagVar2);
                    if (zzaiVar2 != null && zzaiVar2.getState() == 3) {
                        String strValueOf = String.valueOf(zzagVar2);
                        Log.wtf("GmsClientSupervisor", new StringBuilder(String.valueOf(strValueOf).length() + 47).append("Timeout waiting for ServiceConnection callback ").append(strValueOf).toString(), new Exception());
                        ComponentName componentName = zzaiVar2.getComponentName();
                        if (componentName == null) {
                            componentName = zzagVar2.getComponentName();
                        }
                        zzaiVar2.onServiceDisconnected(componentName == null ? new ComponentName(zzagVar2.getPackage(), "unknown") : componentName);
                    }
                    break;
                }
                return true;
            default:
                return false;
        }
    }

    @Override // com.google.android.gms.common.internal.zzaf
    protected final boolean zza(zzag zzagVar, ServiceConnection serviceConnection, String str) {
        boolean zIsBound;
        zzbr.zzb(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzaHR) {
            zzai zzaiVar = this.zzaHR.get(zzagVar);
            if (zzaiVar != null) {
                this.mHandler.removeMessages(0, zzagVar);
                if (!zzaiVar.zza(serviceConnection)) {
                    zzaiVar.zza(serviceConnection, str);
                    switch (zzaiVar.getState()) {
                        case 1:
                            serviceConnection.onServiceConnected(zzaiVar.getComponentName(), zzaiVar.getBinder());
                            break;
                        case 2:
                            zzaiVar.zzcB(str);
                            break;
                    }
                } else {
                    String strValueOf = String.valueOf(zzagVar);
                    throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 81).append("Trying to bind a GmsServiceConnection that was already connected before.  config=").append(strValueOf).toString());
                }
            } else {
                zzaiVar = new zzai(this, zzagVar);
                zzaiVar.zza(serviceConnection, str);
                zzaiVar.zzcB(str);
                this.zzaHR.put(zzagVar, zzaiVar);
            }
            zIsBound = zzaiVar.isBound();
        }
        return zIsBound;
    }

    @Override // com.google.android.gms.common.internal.zzaf
    protected final void zzb(zzag zzagVar, ServiceConnection serviceConnection, String str) {
        zzbr.zzb(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzaHR) {
            zzai zzaiVar = this.zzaHR.get(zzagVar);
            if (zzaiVar == null) {
                String strValueOf = String.valueOf(zzagVar);
                throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 50).append("Nonexistent connection status for service config: ").append(strValueOf).toString());
            }
            if (!zzaiVar.zza(serviceConnection)) {
                String strValueOf2 = String.valueOf(zzagVar);
                throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf2).length() + 76).append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=").append(strValueOf2).toString());
            }
            zzaiVar.zzb(serviceConnection, str);
            if (zzaiVar.zzrB()) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, zzagVar), this.zzaHT);
            }
        }
    }
}
