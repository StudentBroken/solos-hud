package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.support.annotation.BinderThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes67.dex */
public final class zzn extends zze {
    private /* synthetic */ zzd zzaHg;
    private IBinder zzaHk;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @BinderThread
    public zzn(zzd zzdVar, int i, IBinder iBinder, Bundle bundle) {
        super(zzdVar, i, bundle);
        this.zzaHg = zzdVar;
        this.zzaHk = iBinder;
    }

    @Override // com.google.android.gms.common.internal.zze
    protected final void zzj(ConnectionResult connectionResult) {
        if (this.zzaHg.zzaGY != null) {
            this.zzaHg.zzaGY.onConnectionFailed(connectionResult);
        }
        this.zzaHg.onConnectionFailed(connectionResult);
    }

    @Override // com.google.android.gms.common.internal.zze
    protected final boolean zzrh() {
        try {
            String interfaceDescriptor = this.zzaHk.getInterfaceDescriptor();
            if (!this.zzaHg.zzdb().equals(interfaceDescriptor)) {
                String strValueOf = String.valueOf(this.zzaHg.zzdb());
                Log.e("GmsClient", new StringBuilder(String.valueOf(strValueOf).length() + 34 + String.valueOf(interfaceDescriptor).length()).append("service descriptor mismatch: ").append(strValueOf).append(" vs. ").append(interfaceDescriptor).toString());
                return false;
            }
            IInterface iInterfaceZzd = this.zzaHg.zzd(this.zzaHk);
            if (iInterfaceZzd == null) {
                return false;
            }
            if (!this.zzaHg.zza(2, 4, iInterfaceZzd) && !this.zzaHg.zza(3, 4, iInterfaceZzd)) {
                return false;
            }
            this.zzaHg.zzaHb = null;
            Bundle bundleZzoA = this.zzaHg.zzoA();
            if (this.zzaHg.zzaGX != null) {
                this.zzaHg.zzaGX.onConnected(bundleZzoA);
            }
            return true;
        } catch (RemoteException e) {
            Log.w("GmsClient", "service probably died");
            return false;
        }
    }
}
