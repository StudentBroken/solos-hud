package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes67.dex */
final class zzai implements ServiceConnection {
    private ComponentName zzaHQ;
    private boolean zzaHW;
    private final zzag zzaHX;
    private /* synthetic */ zzah zzaHY;
    private IBinder zzaHl;
    private final Set<ServiceConnection> zzaHV = new HashSet();
    private int mState = 2;

    public zzai(zzah zzahVar, zzag zzagVar) {
        this.zzaHY = zzahVar;
        this.zzaHX = zzagVar;
    }

    public final IBinder getBinder() {
        return this.zzaHl;
    }

    public final ComponentName getComponentName() {
        return this.zzaHQ;
    }

    public final int getState() {
        return this.mState;
    }

    public final boolean isBound() {
        return this.zzaHW;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.zzaHY.zzaHR) {
            this.zzaHY.mHandler.removeMessages(1, this.zzaHX);
            this.zzaHl = iBinder;
            this.zzaHQ = componentName;
            Iterator<ServiceConnection> it = this.zzaHV.iterator();
            while (it.hasNext()) {
                it.next().onServiceConnected(componentName, iBinder);
            }
            this.mState = 1;
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        synchronized (this.zzaHY.zzaHR) {
            this.zzaHY.mHandler.removeMessages(1, this.zzaHX);
            this.zzaHl = null;
            this.zzaHQ = componentName;
            Iterator<ServiceConnection> it = this.zzaHV.iterator();
            while (it.hasNext()) {
                it.next().onServiceDisconnected(componentName);
            }
            this.mState = 2;
        }
    }

    public final void zza(ServiceConnection serviceConnection, String str) {
        com.google.android.gms.common.stats.zza unused = this.zzaHY.zzaHS;
        Context unused2 = this.zzaHY.mApplicationContext;
        this.zzaHX.zzrA();
        this.zzaHV.add(serviceConnection);
    }

    public final boolean zza(ServiceConnection serviceConnection) {
        return this.zzaHV.contains(serviceConnection);
    }

    public final void zzb(ServiceConnection serviceConnection, String str) {
        com.google.android.gms.common.stats.zza unused = this.zzaHY.zzaHS;
        Context unused2 = this.zzaHY.mApplicationContext;
        this.zzaHV.remove(serviceConnection);
    }

    public final void zzcB(String str) {
        this.mState = 3;
        com.google.android.gms.common.stats.zza unused = this.zzaHY.zzaHS;
        this.zzaHW = com.google.android.gms.common.stats.zza.zza(this.zzaHY.mApplicationContext, str, this.zzaHX.zzrA(), this, 129);
        if (this.zzaHW) {
            this.zzaHY.mHandler.sendMessageDelayed(this.zzaHY.mHandler.obtainMessage(1, this.zzaHX), this.zzaHY.zzaHU);
        } else {
            this.mState = 2;
            try {
                com.google.android.gms.common.stats.zza unused2 = this.zzaHY.zzaHS;
                this.zzaHY.mApplicationContext.unbindService(this);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    public final void zzcC(String str) {
        this.zzaHY.mHandler.removeMessages(1, this.zzaHX);
        com.google.android.gms.common.stats.zza unused = this.zzaHY.zzaHS;
        this.zzaHY.mApplicationContext.unbindService(this);
        this.zzaHW = false;
        this.mState = 2;
    }

    public final boolean zzrB() {
        return this.zzaHV.isEmpty();
    }
}
