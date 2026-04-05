package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzckc implements ServiceConnection, com.google.android.gms.common.internal.zzf, com.google.android.gms.common.internal.zzg {
    final /* synthetic */ zzcjp zzbue;
    private volatile boolean zzbul;
    private volatile zzcgw zzbum;

    protected zzckc(zzcjp zzcjpVar) {
        this.zzbue = zzcjpVar;
    }

    static /* synthetic */ boolean zza(zzckc zzckcVar, boolean z) {
        zzckcVar.zzbul = false;
        return false;
    }

    @Override // com.google.android.gms.common.internal.zzf
    @MainThread
    public final void onConnected(@Nullable Bundle bundle) {
        zzbr.zzcz("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                zzcgp zzcgpVarZzrd = this.zzbum.zzrd();
                this.zzbum = null;
                this.zzbue.zzwD().zzj(new zzckf(this, zzcgpVarZzrd));
            } catch (DeadObjectException | IllegalStateException e) {
                this.zzbum = null;
                this.zzbul = false;
            }
        }
    }

    @Override // com.google.android.gms.common.internal.zzg
    @MainThread
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        zzbr.zzcz("MeasurementServiceConnection.onConnectionFailed");
        zzcgx zzcgxVarZzyO = this.zzbue.zzboi.zzyO();
        if (zzcgxVarZzyO != null) {
            zzcgxVarZzyO.zzyx().zzj("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzbul = false;
            this.zzbum = null;
        }
        this.zzbue.zzwD().zzj(new zzckh(this));
    }

    @Override // com.google.android.gms.common.internal.zzf
    @MainThread
    public final void onConnectionSuspended(int i) {
        zzbr.zzcz("MeasurementServiceConnection.onConnectionSuspended");
        this.zzbue.zzwE().zzyA().log("Service connection suspended");
        this.zzbue.zzwD().zzj(new zzckg(this));
    }

    @Override // android.content.ServiceConnection
    @MainThread
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        zzcgp zzcgrVar;
        zzbr.zzcz("MeasurementServiceConnection.onServiceConnected");
        synchronized (this) {
            if (iBinder == null) {
                this.zzbul = false;
                this.zzbue.zzwE().zzyv().log("Service connected with null binder");
                return;
            }
            try {
                String interfaceDescriptor = iBinder.getInterfaceDescriptor();
                if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                    if (iBinder == null) {
                        zzcgrVar = null;
                    } else {
                        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
                        zzcgrVar = iInterfaceQueryLocalInterface instanceof zzcgp ? (zzcgp) iInterfaceQueryLocalInterface : new zzcgr(iBinder);
                    }
                    try {
                        this.zzbue.zzwE().zzyB().log("Bound to IMeasurementService interface");
                    } catch (RemoteException e) {
                        this.zzbue.zzwE().zzyv().log("Service connect failed to get IMeasurementService");
                    }
                } else {
                    this.zzbue.zzwE().zzyv().zzj("Got binder with a wrong descriptor", interfaceDescriptor);
                    zzcgrVar = null;
                }
            } catch (RemoteException e2) {
                zzcgrVar = null;
            }
            if (zzcgrVar == null) {
                this.zzbul = false;
                try {
                    com.google.android.gms.common.stats.zza.zzrT();
                    this.zzbue.getContext().unbindService(this.zzbue.zzbtX);
                } catch (IllegalArgumentException e3) {
                }
            } else {
                this.zzbue.zzwD().zzj(new zzckd(this, zzcgrVar));
            }
        }
    }

    @Override // android.content.ServiceConnection
    @MainThread
    public final void onServiceDisconnected(ComponentName componentName) {
        zzbr.zzcz("MeasurementServiceConnection.onServiceDisconnected");
        this.zzbue.zzwE().zzyA().log("Service disconnected");
        this.zzbue.zzwD().zzj(new zzcke(this, componentName));
    }

    @WorkerThread
    public final void zzk(Intent intent) {
        this.zzbue.zzjB();
        Context context = this.zzbue.getContext();
        com.google.android.gms.common.stats.zza zzaVarZzrT = com.google.android.gms.common.stats.zza.zzrT();
        synchronized (this) {
            if (this.zzbul) {
                this.zzbue.zzwE().zzyB().log("Connection attempt already in progress");
            } else {
                this.zzbul = true;
                zzaVarZzrT.zza(context, intent, this.zzbue.zzbtX, 129);
            }
        }
    }

    @WorkerThread
    public final void zzzk() {
        this.zzbue.zzjB();
        Context context = this.zzbue.getContext();
        synchronized (this) {
            if (this.zzbul) {
                this.zzbue.zzwE().zzyB().log("Connection attempt already in progress");
                return;
            }
            if (this.zzbum != null) {
                this.zzbue.zzwE().zzyB().log("Already awaiting connection attempt");
                return;
            }
            this.zzbum = new zzcgw(context, Looper.getMainLooper(), this, this);
            this.zzbue.zzwE().zzyB().log("Connecting to remote service");
            this.zzbul = true;
            this.zzbum.zzqZ();
        }
    }
}
