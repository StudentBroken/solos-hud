package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzbu;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbfv extends zzcvb implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static Api.zza<? extends zzcuw, zzcux> zzaEX = zzcus.zzajU;
    private final Context mContext;
    private final Handler mHandler;
    private final Api.zza<? extends zzcuw, zzcux> zzaAz;
    private com.google.android.gms.common.internal.zzq zzaCC;
    private zzcuw zzaDj;
    private final boolean zzaEY;
    private zzbfx zzaEZ;
    private Set<Scope> zzamg;

    @WorkerThread
    public zzbfv(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.zzaAz = zzaEX;
        this.zzaEY = true;
    }

    @WorkerThread
    public zzbfv(Context context, Handler handler, @NonNull com.google.android.gms.common.internal.zzq zzqVar, Api.zza<? extends zzcuw, zzcux> zzaVar) {
        this.mContext = context;
        this.mHandler = handler;
        this.zzaCC = (com.google.android.gms.common.internal.zzq) zzbr.zzb(zzqVar, "ClientSettings must not be null");
        this.zzamg = zzqVar.zzrl();
        this.zzaAz = zzaVar;
        this.zzaEY = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzc(zzcvj zzcvjVar) {
        ConnectionResult connectionResultZzpx = zzcvjVar.zzpx();
        if (connectionResultZzpx.isSuccess()) {
            zzbu zzbuVarZzAv = zzcvjVar.zzAv();
            ConnectionResult connectionResultZzpx2 = zzbuVarZzAv.zzpx();
            if (!connectionResultZzpx2.isSuccess()) {
                String strValueOf = String.valueOf(connectionResultZzpx2);
                Log.wtf("SignInCoordinator", new StringBuilder(String.valueOf(strValueOf).length() + 48).append("Sign-in succeeded with resolve account failure: ").append(strValueOf).toString(), new Exception());
                this.zzaEZ.zzh(connectionResultZzpx2);
                this.zzaDj.disconnect();
                return;
            }
            this.zzaEZ.zzb(zzbuVarZzAv.zzrG(), this.zzamg);
        } else {
            this.zzaEZ.zzh(connectionResultZzpx);
        }
        this.zzaDj.disconnect();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    @WorkerThread
    public final void onConnected(@Nullable Bundle bundle) {
        this.zzaDj.zza(this);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    @WorkerThread
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzaEZ.zzh(connectionResult);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    @WorkerThread
    public final void onConnectionSuspended(int i) {
        this.zzaDj.disconnect();
    }

    @WorkerThread
    public final void zza(zzbfx zzbfxVar) {
        if (this.zzaDj != null) {
            this.zzaDj.disconnect();
        }
        if (this.zzaEY) {
            GoogleSignInOptions googleSignInOptionsZzmM = com.google.android.gms.auth.api.signin.internal.zzy.zzaj(this.mContext).zzmM();
            this.zzamg = googleSignInOptionsZzmM == null ? new HashSet() : new HashSet(googleSignInOptionsZzmM.zzmy());
            this.zzaCC = new com.google.android.gms.common.internal.zzq(null, this.zzamg, null, 0, null, null, null, zzcux.zzbCQ);
        }
        this.zzaCC.zzc(Integer.valueOf(System.identityHashCode(this)));
        this.zzaDj = (zzcuw) this.zzaAz.zza(this.mContext, this.mHandler.getLooper(), this.zzaCC, this.zzaCC.zzrr(), this, this);
        this.zzaEZ = zzbfxVar;
        this.zzaDj.connect();
    }

    @Override // com.google.android.gms.internal.zzcvb, com.google.android.gms.internal.zzcvc
    @BinderThread
    public final void zzb(zzcvj zzcvjVar) {
        this.mHandler.post(new zzbfw(this, zzcvjVar));
    }

    public final void zzqG() {
        if (this.zzaDj != null) {
            this.zzaDj.disconnect();
        }
    }

    public final zzcuw zzqw() {
        return this.zzaDj;
    }
}
