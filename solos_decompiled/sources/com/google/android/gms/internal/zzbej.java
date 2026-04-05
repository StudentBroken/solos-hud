package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbej implements zzbcv, zzbfb {
    private final Context mContext;
    private Api.zza<? extends zzcuw, zzcux> zzaBg;
    private com.google.android.gms.common.internal.zzq zzaCC;
    private Map<Api<?>, Boolean> zzaCF;
    private final com.google.android.gms.common.zze zzaCH;
    final zzbeb zzaCn;
    private final Lock zzaCx;
    final Map<Api.zzc<?>, Api.zze> zzaDH;
    private final Condition zzaDU;
    private final zzbel zzaDV;
    private volatile zzbei zzaDX;
    int zzaDZ;
    final zzbfc zzaEa;
    final Map<Api.zzc<?>, ConnectionResult> zzaDW = new HashMap();
    private ConnectionResult zzaDY = null;

    public zzbej(Context context, zzbeb zzbebVar, Lock lock, Looper looper, com.google.android.gms.common.zze zzeVar, Map<Api.zzc<?>, Api.zze> map, com.google.android.gms.common.internal.zzq zzqVar, Map<Api<?>, Boolean> map2, Api.zza<? extends zzcuw, zzcux> zzaVar, ArrayList<zzbcu> arrayList, zzbfc zzbfcVar) {
        this.mContext = context;
        this.zzaCx = lock;
        this.zzaCH = zzeVar;
        this.zzaDH = map;
        this.zzaCC = zzqVar;
        this.zzaCF = map2;
        this.zzaBg = zzaVar;
        this.zzaCn = zzbebVar;
        this.zzaEa = zzbfcVar;
        ArrayList<zzbcu> arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            zzbcu zzbcuVar = arrayList2.get(i);
            i++;
            zzbcuVar.zza(this);
        }
        this.zzaDV = new zzbel(this, looper);
        this.zzaDU = lock.newCondition();
        this.zzaDX = new zzbea(this);
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zzaDU.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        return isConnected() ? ConnectionResult.zzazZ : this.zzaDY != null ? this.zzaDY : new ConnectionResult(13, null);
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final ConnectionResult blockingConnect(long j, TimeUnit timeUnit) {
        connect();
        long nanos = timeUnit.toNanos(j);
        while (isConnecting()) {
            if (nanos <= 0) {
                disconnect();
                return new ConnectionResult(14, null);
            }
            try {
                nanos = this.zzaDU.awaitNanos(nanos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
            Thread.currentThread().interrupt();
            return new ConnectionResult(15, null);
        }
        return isConnected() ? ConnectionResult.zzazZ : this.zzaDY != null ? this.zzaDY : new ConnectionResult(13, null);
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void connect() {
        this.zzaDX.connect();
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void disconnect() {
        if (this.zzaDX.disconnect()) {
            this.zzaDW.clear();
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String strConcat = String.valueOf(str).concat("  ");
        printWriter.append((CharSequence) str).append("mState=").println(this.zzaDX);
        for (Api<?> api : this.zzaCF.keySet()) {
            printWriter.append((CharSequence) str).append((CharSequence) api.getName()).println(":");
            this.zzaDH.get(api.zzpb()).dump(strConcat, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        Api.zzc<?> zzcVarZzpb = api.zzpb();
        if (this.zzaDH.containsKey(zzcVarZzpb)) {
            if (this.zzaDH.get(zzcVarZzpb).isConnected()) {
                return ConnectionResult.zzazZ;
            }
            if (this.zzaDW.containsKey(zzcVarZzpb)) {
                return this.zzaDW.get(zzcVarZzpb);
            }
        }
        return null;
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final boolean isConnected() {
        return this.zzaDX instanceof zzbdm;
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final boolean isConnecting() {
        return this.zzaDX instanceof zzbdp;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(@Nullable Bundle bundle) {
        this.zzaCx.lock();
        try {
            this.zzaDX.onConnected(bundle);
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        this.zzaCx.lock();
        try {
            this.zzaDX.onConnectionSuspended(i);
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbcv
    public final void zza(@NonNull ConnectionResult connectionResult, @NonNull Api<?> api, boolean z) {
        this.zzaCx.lock();
        try {
            this.zzaDX.zza(connectionResult, api, z);
        } finally {
            this.zzaCx.unlock();
        }
    }

    final void zza(zzbek zzbekVar) {
        this.zzaDV.sendMessage(this.zzaDV.obtainMessage(1, zzbekVar));
    }

    final void zza(RuntimeException runtimeException) {
        this.zzaDV.sendMessage(this.zzaDV.obtainMessage(2, runtimeException));
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final boolean zza(zzbfu zzbfuVar) {
        return false;
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(@NonNull T t) {
        t.zzpA();
        return (T) this.zzaDX.zzd(t);
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(@NonNull T t) {
        t.zzpA();
        return (T) this.zzaDX.zze(t);
    }

    final void zzg(ConnectionResult connectionResult) {
        this.zzaCx.lock();
        try {
            this.zzaDY = connectionResult;
            this.zzaDX = new zzbea(this);
            this.zzaDX.begin();
            this.zzaDU.signalAll();
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void zzpC() {
        if (isConnected()) {
            ((zzbdm) this.zzaDX).zzpS();
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void zzpj() {
    }

    final void zzqf() {
        this.zzaCx.lock();
        try {
            this.zzaDX = new zzbdp(this, this.zzaCC, this.zzaCF, this.zzaCH, this.zzaBg, this.zzaCx, this.mContext);
            this.zzaDX.begin();
            this.zzaDU.signalAll();
        } finally {
            this.zzaCx.unlock();
        }
    }

    final void zzqg() {
        this.zzaCx.lock();
        try {
            this.zzaCn.zzqc();
            this.zzaDX = new zzbdm(this);
            this.zzaDX.begin();
            this.zzaDU.signalAll();
        } finally {
            this.zzaCx.unlock();
        }
    }
}
