package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbdb implements zzbfb {
    private final zzben zzaAP;
    private final com.google.android.gms.common.internal.zzq zzaCC;
    private final Map<Api<?>, Boolean> zzaCF;
    private final zzbeb zzaCG;
    private final com.google.android.gms.common.zze zzaCH;
    private final Condition zzaCI;
    private final boolean zzaCJ;
    private final boolean zzaCK;
    private boolean zzaCM;
    private Map<zzbcf<?>, ConnectionResult> zzaCN;
    private Map<zzbcf<?>, ConnectionResult> zzaCO;
    private zzbde zzaCP;
    private ConnectionResult zzaCQ;
    private final Lock zzaCx;
    private final Looper zzrP;
    private final Map<Api.zzc<?>, zzbda<?>> zzaCD = new HashMap();
    private final Map<Api.zzc<?>, zzbda<?>> zzaCE = new HashMap();
    private final Queue<zzbck<?, ?>> zzaCL = new LinkedList();

    public zzbdb(Context context, Lock lock, Looper looper, com.google.android.gms.common.zze zzeVar, Map<Api.zzc<?>, Api.zze> map, com.google.android.gms.common.internal.zzq zzqVar, Map<Api<?>, Boolean> map2, Api.zza<? extends zzcuw, zzcux> zzaVar, ArrayList<zzbcu> arrayList, zzbeb zzbebVar, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        this.zzaCx = lock;
        this.zzrP = looper;
        this.zzaCI = lock.newCondition();
        this.zzaCH = zzeVar;
        this.zzaCG = zzbebVar;
        this.zzaCF = map2;
        this.zzaCC = zzqVar;
        this.zzaCJ = z;
        HashMap map3 = new HashMap();
        for (Api<?> api : map2.keySet()) {
            map3.put(api.zzpb(), api);
        }
        HashMap map4 = new HashMap();
        ArrayList<zzbcu> arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            zzbcu zzbcuVar = arrayList2.get(i);
            i++;
            zzbcu zzbcuVar2 = zzbcuVar;
            map4.put(zzbcuVar2.zzayY, zzbcuVar2);
        }
        boolean z5 = true;
        boolean z6 = false;
        boolean z7 = false;
        for (Map.Entry<Api.zzc<?>, Api.zze> entry : map.entrySet()) {
            Api api2 = (Api) map3.get(entry.getKey());
            Api.zze value = entry.getValue();
            if (value.zzpc()) {
                z2 = true;
                if (this.zzaCF.get(api2).booleanValue()) {
                    z3 = z5;
                    z4 = z6;
                } else {
                    z3 = z5;
                    z4 = true;
                }
            } else {
                z2 = z7;
                z3 = false;
                z4 = z6;
            }
            zzbda<?> zzbdaVar = new zzbda<>(context, api2, looper, value, (zzbcu) map4.get(api2), zzqVar, zzaVar);
            this.zzaCD.put(entry.getKey(), zzbdaVar);
            if (value.zzmt()) {
                this.zzaCE.put(entry.getKey(), zzbdaVar);
            }
            z7 = z2;
            z5 = z3;
            z6 = z4;
        }
        this.zzaCK = (!z7 || z5 || z6) ? false : true;
        this.zzaAP = zzben.zzqi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zza(zzbda<?> zzbdaVar, ConnectionResult connectionResult) {
        return !connectionResult.isSuccess() && !connectionResult.hasResolution() && this.zzaCF.get(zzbdaVar.zzpe()).booleanValue() && zzbdaVar.zzpH().zzpc() && this.zzaCH.isUserResolvableError(connectionResult.getErrorCode());
    }

    static /* synthetic */ boolean zza(zzbdb zzbdbVar, boolean z) {
        zzbdbVar.zzaCM = false;
        return false;
    }

    @Nullable
    private final ConnectionResult zzb(@NonNull Api.zzc<?> zzcVar) {
        this.zzaCx.lock();
        try {
            zzbda<?> zzbdaVar = this.zzaCD.get(zzcVar);
            if (this.zzaCN != null && zzbdaVar != null) {
                return this.zzaCN.get(zzbdaVar.zzpf());
            }
            this.zzaCx.unlock();
            return null;
        } finally {
            this.zzaCx.unlock();
        }
    }

    private final <T extends zzbck<? extends Result, ? extends Api.zzb>> boolean zzg(@NonNull T t) {
        Api.zzc<?> zzcVarZzpb = t.zzpb();
        ConnectionResult connectionResultZzb = zzb(zzcVarZzpb);
        if (connectionResultZzb == null || connectionResultZzb.getErrorCode() != 4) {
            return false;
        }
        t.zzr(new Status(4, null, this.zzaAP.zza(this.zzaCD.get(zzcVarZzpb).zzpf(), System.identityHashCode(this.zzaCG))));
        return true;
    }

    private final boolean zzpI() {
        this.zzaCx.lock();
        try {
            if (!this.zzaCM || !this.zzaCJ) {
                return false;
            }
            Iterator<Api.zzc<?>> it = this.zzaCE.keySet().iterator();
            while (it.hasNext()) {
                ConnectionResult connectionResultZzb = zzb(it.next());
                if (connectionResultZzb == null || !connectionResultZzb.isSuccess()) {
                    return false;
                }
            }
            this.zzaCx.unlock();
            return true;
        } finally {
            this.zzaCx.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzpJ() {
        if (this.zzaCC == null) {
            this.zzaCG.zzaDI = Collections.emptySet();
            return;
        }
        HashSet hashSet = new HashSet(this.zzaCC.zzrl());
        Map<Api<?>, com.google.android.gms.common.internal.zzr> mapZzrn = this.zzaCC.zzrn();
        for (Api<?> api : mapZzrn.keySet()) {
            ConnectionResult connectionResult = getConnectionResult(api);
            if (connectionResult != null && connectionResult.isSuccess()) {
                hashSet.addAll(mapZzrn.get(api).zzamg);
            }
        }
        this.zzaCG.zzaDI = hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzpK() {
        while (!this.zzaCL.isEmpty()) {
            zze(this.zzaCL.remove());
        }
        this.zzaCG.zzm(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    public final ConnectionResult zzpL() {
        ConnectionResult connectionResult;
        int i;
        int i2 = 0;
        ConnectionResult connectionResult2 = null;
        int i3 = 0;
        ConnectionResult connectionResult3 = null;
        for (zzbda<?> zzbdaVar : this.zzaCD.values()) {
            Api<?> apiZzpe = zzbdaVar.zzpe();
            ConnectionResult connectionResult4 = this.zzaCN.get(zzbdaVar.zzpf());
            if (!connectionResult4.isSuccess() && (!this.zzaCF.get(apiZzpe).booleanValue() || connectionResult4.hasResolution() || this.zzaCH.isUserResolvableError(connectionResult4.getErrorCode()))) {
                if (connectionResult4.getErrorCode() == 4 && this.zzaCJ) {
                    int priority = apiZzpe.zzoZ().getPriority();
                    if (connectionResult2 == null || i2 > priority) {
                        i2 = priority;
                        connectionResult2 = connectionResult4;
                    }
                } else {
                    int priority2 = apiZzpe.zzoZ().getPriority();
                    if (connectionResult3 == null || i3 > priority2) {
                        connectionResult = connectionResult4;
                        i = priority2;
                    } else {
                        i = i3;
                        connectionResult = connectionResult3;
                    }
                    i3 = i;
                    connectionResult3 = connectionResult;
                }
            }
        }
        return (connectionResult3 == null || connectionResult2 == null || i3 <= i2) ? connectionResult3 : connectionResult2;
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zzaCI.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        return isConnected() ? ConnectionResult.zzazZ : this.zzaCQ != null ? this.zzaCQ : new ConnectionResult(13, null);
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
                nanos = this.zzaCI.awaitNanos(nanos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
            Thread.currentThread().interrupt();
            return new ConnectionResult(15, null);
        }
        return isConnected() ? ConnectionResult.zzazZ : this.zzaCQ != null ? this.zzaCQ : new ConnectionResult(13, null);
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void connect() {
        this.zzaCx.lock();
        try {
            if (this.zzaCM) {
                return;
            }
            this.zzaCM = true;
            this.zzaCN = null;
            this.zzaCO = null;
            this.zzaCP = null;
            this.zzaCQ = null;
            this.zzaAP.zzpq();
            this.zzaAP.zza(this.zzaCD.values()).addOnCompleteListener(new zzbih(this.zzrP), new zzbdd(this));
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void disconnect() {
        this.zzaCx.lock();
        try {
            this.zzaCM = false;
            this.zzaCN = null;
            this.zzaCO = null;
            if (this.zzaCP != null) {
                this.zzaCP.cancel();
                this.zzaCP = null;
            }
            this.zzaCQ = null;
            while (!this.zzaCL.isEmpty()) {
                zzbck<?, ?> zzbckVarRemove = this.zzaCL.remove();
                zzbckVarRemove.zza((zzbgj) null);
                zzbckVarRemove.cancel();
            }
            this.zzaCI.signalAll();
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    @Override // com.google.android.gms.internal.zzbfb
    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return zzb(api.zzpb());
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0014  */
    @Override // com.google.android.gms.internal.zzbfb
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isConnected() {
        /*
            r2 = this;
            java.util.concurrent.locks.Lock r0 = r2.zzaCx
            r0.lock()
            java.util.Map<com.google.android.gms.internal.zzbcf<?>, com.google.android.gms.common.ConnectionResult> r0 = r2.zzaCN     // Catch: java.lang.Throwable -> L16
            if (r0 == 0) goto L14
            com.google.android.gms.common.ConnectionResult r0 = r2.zzaCQ     // Catch: java.lang.Throwable -> L16
            if (r0 != 0) goto L14
            r0 = 1
        Le:
            java.util.concurrent.locks.Lock r1 = r2.zzaCx
            r1.unlock()
            return r0
        L14:
            r0 = 0
            goto Le
        L16:
            r0 = move-exception
            java.util.concurrent.locks.Lock r1 = r2.zzaCx
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbdb.isConnected():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0014  */
    @Override // com.google.android.gms.internal.zzbfb
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isConnecting() {
        /*
            r2 = this;
            java.util.concurrent.locks.Lock r0 = r2.zzaCx
            r0.lock()
            java.util.Map<com.google.android.gms.internal.zzbcf<?>, com.google.android.gms.common.ConnectionResult> r0 = r2.zzaCN     // Catch: java.lang.Throwable -> L16
            if (r0 != 0) goto L14
            boolean r0 = r2.zzaCM     // Catch: java.lang.Throwable -> L16
            if (r0 == 0) goto L14
            r0 = 1
        Le:
            java.util.concurrent.locks.Lock r1 = r2.zzaCx
            r1.unlock()
            return r0
        L14:
            r0 = 0
            goto Le
        L16:
            r0 = move-exception
            java.util.concurrent.locks.Lock r1 = r2.zzaCx
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbdb.isConnecting():boolean");
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final boolean zza(zzbfu zzbfuVar) {
        this.zzaCx.lock();
        try {
            if (!this.zzaCM || zzpI()) {
                this.zzaCx.unlock();
                return false;
            }
            this.zzaAP.zzpq();
            this.zzaCP = new zzbde(this, zzbfuVar);
            this.zzaAP.zza(this.zzaCE.values()).addOnCompleteListener(new zzbih(this.zzrP), this.zzaCP);
            this.zzaCx.unlock();
            return true;
        } catch (Throwable th) {
            this.zzaCx.unlock();
            throw th;
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(@NonNull T t) {
        if (this.zzaCJ && zzg(t)) {
            return t;
        }
        if (isConnected()) {
            this.zzaCG.zzaDN.zzb(t);
            return (T) this.zzaCD.get(t.zzpb()).zza(t);
        }
        this.zzaCL.add(t);
        return t;
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(@NonNull T t) {
        Api.zzc<A> zzcVarZzpb = t.zzpb();
        if (this.zzaCJ && zzg(t)) {
            return t;
        }
        this.zzaCG.zzaDN.zzb(t);
        return (T) this.zzaCD.get(zzcVarZzpb).zzb(t);
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void zzpC() {
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void zzpj() {
        this.zzaCx.lock();
        try {
            this.zzaAP.zzpj();
            if (this.zzaCP != null) {
                this.zzaCP.cancel();
                this.zzaCP = null;
            }
            if (this.zzaCO == null) {
                this.zzaCO = new ArrayMap(this.zzaCE.size());
            }
            ConnectionResult connectionResult = new ConnectionResult(4);
            Iterator<zzbda<?>> it = this.zzaCE.values().iterator();
            while (it.hasNext()) {
                this.zzaCO.put(it.next().zzpf(), connectionResult);
            }
            if (this.zzaCN != null) {
                this.zzaCN.putAll(this.zzaCO);
            }
        } finally {
            this.zzaCx.unlock();
        }
    }
}
