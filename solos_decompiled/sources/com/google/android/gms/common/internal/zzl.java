package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;

/* JADX INFO: loaded from: classes67.dex */
public final class zzl implements ServiceConnection {
    private /* synthetic */ zzd zzaHg;
    private final int zzaHj;

    public zzl(zzd zzdVar, int i) {
        this.zzaHg = zzdVar;
        this.zzaHj = i;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        zzay zzbaVar;
        if (iBinder == null) {
            this.zzaHg.zzaz(16);
            return;
        }
        synchronized (this.zzaHg.zzaGQ) {
            zzd zzdVar = this.zzaHg;
            if (iBinder == null) {
                zzbaVar = null;
            } else {
                IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                zzbaVar = (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzay)) ? new zzba(iBinder) : (zzay) iInterfaceQueryLocalInterface;
            }
            zzdVar.zzaGR = zzbaVar;
        }
        this.zzaHg.zza(0, (Bundle) null, this.zzaHj);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        synchronized (this.zzaHg.zzaGQ) {
            this.zzaHg.zzaGR = null;
        }
        this.zzaHg.mHandler.sendMessage(this.zzaHg.mHandler.obtainMessage(6, this.zzaHj, 1));
    }
}
