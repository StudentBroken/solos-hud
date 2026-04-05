package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzca;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbep<O extends Api.ApiOptions> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, zzbcv {
    private final zzbcf<O> zzaAM;
    private final Api.zze zzaCA;
    private boolean zzaDC;
    private /* synthetic */ zzben zzaEo;
    private final Api.zzb zzaEq;
    private final zzbdf zzaEr;
    private final int zzaEu;
    private final zzbfv zzaEv;
    private final Queue<zzbby> zzaEp = new LinkedList();
    private final Set<zzbch> zzaEs = new HashSet();
    private final Map<zzbfk<?>, zzbfr> zzaEt = new HashMap();
    private ConnectionResult zzaEw = null;

    @WorkerThread
    public zzbep(zzben zzbenVar, GoogleApi<O> googleApi) {
        this.zzaEo = zzbenVar;
        this.zzaCA = googleApi.zza(zzbenVar.mHandler.getLooper(), this);
        if (this.zzaCA instanceof zzca) {
            this.zzaEq = null;
        } else {
            this.zzaEq = this.zzaCA;
        }
        this.zzaAM = googleApi.zzpf();
        this.zzaEr = new zzbdf();
        this.zzaEu = googleApi.getInstanceId();
        if (this.zzaCA.zzmt()) {
            this.zzaEv = googleApi.zza(zzbenVar.mContext, zzbenVar.mHandler);
        } else {
            this.zzaEv = null;
        }
    }

    @WorkerThread
    private final void zzb(zzbby zzbbyVar) {
        zzbbyVar.zza(this.zzaEr, zzmt());
        try {
            zzbbyVar.zza((zzbep<?>) this);
        } catch (DeadObjectException e) {
            onConnectionSuspended(1);
            this.zzaCA.disconnect();
        }
    }

    @WorkerThread
    private final void zzi(ConnectionResult connectionResult) {
        Iterator<zzbch> it = this.zzaEs.iterator();
        while (it.hasNext()) {
            it.next().zza(this.zzaAM, connectionResult);
        }
        this.zzaEs.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzqo() {
        zzqr();
        zzi(ConnectionResult.zzazZ);
        zzqt();
        Iterator<zzbfr> it = this.zzaEt.values().iterator();
        while (it.hasNext()) {
            try {
                it.next().zzaBw.zzb(this.zzaEq, new TaskCompletionSource<>());
            } catch (DeadObjectException e) {
                onConnectionSuspended(1);
                this.zzaCA.disconnect();
            } catch (RemoteException e2) {
            }
        }
        while (this.zzaCA.isConnected() && !this.zzaEp.isEmpty()) {
            zzb(this.zzaEp.remove());
        }
        zzqu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzqp() {
        zzqr();
        this.zzaDC = true;
        this.zzaEr.zzpO();
        this.zzaEo.mHandler.sendMessageDelayed(Message.obtain(this.zzaEo.mHandler, 9, this.zzaAM), this.zzaEo.zzaDE);
        this.zzaEo.mHandler.sendMessageDelayed(Message.obtain(this.zzaEo.mHandler, 11, this.zzaAM), this.zzaEo.zzaDD);
        this.zzaEo.zzaEi = -1;
    }

    @WorkerThread
    private final void zzqt() {
        if (this.zzaDC) {
            this.zzaEo.mHandler.removeMessages(11, this.zzaAM);
            this.zzaEo.mHandler.removeMessages(9, this.zzaAM);
            this.zzaDC = false;
        }
    }

    private final void zzqu() {
        this.zzaEo.mHandler.removeMessages(12, this.zzaAM);
        this.zzaEo.mHandler.sendMessageDelayed(this.zzaEo.mHandler.obtainMessage(12, this.zzaAM), this.zzaEo.zzaEg);
    }

    @WorkerThread
    public final void connect() {
        zzbr.zza(this.zzaEo.mHandler);
        if (this.zzaCA.isConnected() || this.zzaCA.isConnecting()) {
            return;
        }
        if (this.zzaCA.zzpc() && this.zzaEo.zzaEi != 0) {
            this.zzaEo.zzaEi = this.zzaEo.zzaBf.isGooglePlayServicesAvailable(this.zzaEo.mContext);
            if (this.zzaEo.zzaEi != 0) {
                onConnectionFailed(new ConnectionResult(this.zzaEo.zzaEi, null));
                return;
            }
        }
        zzbet zzbetVar = new zzbet(this.zzaEo, this.zzaCA, this.zzaAM);
        if (this.zzaCA.zzmt()) {
            this.zzaEv.zza(zzbetVar);
        }
        this.zzaCA.zza(zzbetVar);
    }

    public final int getInstanceId() {
        return this.zzaEu;
    }

    final boolean isConnected() {
        return this.zzaCA.isConnected();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(@Nullable Bundle bundle) {
        if (Looper.myLooper() == this.zzaEo.mHandler.getLooper()) {
            zzqo();
        } else {
            this.zzaEo.mHandler.post(new zzbeq(this));
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    @WorkerThread
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        zzbr.zza(this.zzaEo.mHandler);
        if (this.zzaEv != null) {
            this.zzaEv.zzqG();
        }
        zzqr();
        this.zzaEo.zzaEi = -1;
        zzi(connectionResult);
        if (connectionResult.getErrorCode() == 4) {
            zzt(zzben.zzaEf);
            return;
        }
        if (this.zzaEp.isEmpty()) {
            this.zzaEw = connectionResult;
            return;
        }
        synchronized (zzben.zzuI) {
            if (this.zzaEo.zzaEl != null && this.zzaEo.zzaEm.contains(this.zzaAM)) {
                this.zzaEo.zzaEl.zzb(connectionResult, this.zzaEu);
            } else if (!this.zzaEo.zzc(connectionResult, this.zzaEu)) {
                if (connectionResult.getErrorCode() == 18) {
                    this.zzaDC = true;
                }
                if (this.zzaDC) {
                    this.zzaEo.mHandler.sendMessageDelayed(Message.obtain(this.zzaEo.mHandler, 9, this.zzaAM), this.zzaEo.zzaDE);
                } else {
                    String strValueOf = String.valueOf(this.zzaAM.zzpp());
                    zzt(new Status(17, new StringBuilder(String.valueOf(strValueOf).length() + 38).append("API: ").append(strValueOf).append(" is not available on this device.").toString()));
                }
            }
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        if (Looper.myLooper() == this.zzaEo.mHandler.getLooper()) {
            zzqp();
        } else {
            this.zzaEo.mHandler.post(new zzber(this));
        }
    }

    @WorkerThread
    public final void resume() {
        zzbr.zza(this.zzaEo.mHandler);
        if (this.zzaDC) {
            connect();
        }
    }

    @WorkerThread
    public final void signOut() {
        zzbr.zza(this.zzaEo.mHandler);
        zzt(zzben.zzaEe);
        this.zzaEr.zzpN();
        Iterator<zzbfk<?>> it = this.zzaEt.keySet().iterator();
        while (it.hasNext()) {
            zza(new zzbcd(it.next(), new TaskCompletionSource()));
        }
        zzi(new ConnectionResult(4));
        this.zzaCA.disconnect();
    }

    @Override // com.google.android.gms.internal.zzbcv
    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (Looper.myLooper() == this.zzaEo.mHandler.getLooper()) {
            onConnectionFailed(connectionResult);
        } else {
            this.zzaEo.mHandler.post(new zzbes(this, connectionResult));
        }
    }

    @WorkerThread
    public final void zza(zzbby zzbbyVar) {
        zzbr.zza(this.zzaEo.mHandler);
        if (this.zzaCA.isConnected()) {
            zzb(zzbbyVar);
            zzqu();
            return;
        }
        this.zzaEp.add(zzbbyVar);
        if (this.zzaEw == null || !this.zzaEw.hasResolution()) {
            connect();
        } else {
            onConnectionFailed(this.zzaEw);
        }
    }

    @WorkerThread
    public final void zza(zzbch zzbchVar) {
        zzbr.zza(this.zzaEo.mHandler);
        this.zzaEs.add(zzbchVar);
    }

    @WorkerThread
    public final void zzh(@NonNull ConnectionResult connectionResult) {
        zzbr.zza(this.zzaEo.mHandler);
        this.zzaCA.disconnect();
        onConnectionFailed(connectionResult);
    }

    public final boolean zzmt() {
        return this.zzaCA.zzmt();
    }

    public final Api.zze zzpH() {
        return this.zzaCA;
    }

    @WorkerThread
    public final void zzqb() {
        zzbr.zza(this.zzaEo.mHandler);
        if (this.zzaDC) {
            zzqt();
            zzt(this.zzaEo.zzaBf.isGooglePlayServicesAvailable(this.zzaEo.mContext) == 18 ? new Status(8, "Connection timed out while waiting for Google Play services update to complete.") : new Status(8, "API failed to connect while resuming due to an unknown error."));
            this.zzaCA.disconnect();
        }
    }

    public final Map<zzbfk<?>, zzbfr> zzqq() {
        return this.zzaEt;
    }

    @WorkerThread
    public final void zzqr() {
        zzbr.zza(this.zzaEo.mHandler);
        this.zzaEw = null;
    }

    @WorkerThread
    public final ConnectionResult zzqs() {
        zzbr.zza(this.zzaEo.mHandler);
        return this.zzaEw;
    }

    @WorkerThread
    public final void zzqv() {
        zzbr.zza(this.zzaEo.mHandler);
        if (this.zzaCA.isConnected() && this.zzaEt.size() == 0) {
            if (this.zzaEr.zzpM()) {
                zzqu();
            } else {
                this.zzaCA.disconnect();
            }
        }
    }

    final zzcuw zzqw() {
        if (this.zzaEv == null) {
            return null;
        }
        return this.zzaEv.zzqw();
    }

    @WorkerThread
    public final void zzt(Status status) {
        zzbr.zza(this.zzaEo.mHandler);
        Iterator<zzbby> it = this.zzaEp.iterator();
        while (it.hasNext()) {
            it.next().zzp(status);
        }
        this.zzaEp.clear();
    }
}
