package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbr;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbeb extends GoogleApiClient implements zzbfc {
    private final Context mContext;
    private final int zzaBd;
    private final GoogleApiAvailability zzaBf;
    private Api.zza<? extends zzcuw, zzcux> zzaBg;
    private com.google.android.gms.common.internal.zzq zzaCC;
    private Map<Api<?>, Boolean> zzaCF;
    private final Lock zzaCx;
    private final com.google.android.gms.common.internal.zzad zzaDA;
    private volatile boolean zzaDC;
    private final zzbeg zzaDF;
    private zzbew zzaDG;
    final Map<Api.zzc<?>, Api.zze> zzaDH;
    private final ArrayList<zzbcu> zzaDK;
    private Integer zzaDL;
    final zzbgh zzaDN;
    private final Looper zzrP;
    private zzbfb zzaDB = null;
    final Queue<zzbck<?, ?>> zzaCL = new LinkedList();
    private long zzaDD = 120000;
    private long zzaDE = 5000;
    Set<Scope> zzaDI = new HashSet();
    private final zzbfm zzaDJ = new zzbfm();
    Set<zzbge> zzaDM = null;
    private final com.google.android.gms.common.internal.zzae zzaDO = new zzbec(this);
    private boolean zzaBj = false;

    public zzbeb(Context context, Lock lock, Looper looper, com.google.android.gms.common.internal.zzq zzqVar, GoogleApiAvailability googleApiAvailability, Api.zza<? extends zzcuw, zzcux> zzaVar, Map<Api<?>, Boolean> map, List<GoogleApiClient.ConnectionCallbacks> list, List<GoogleApiClient.OnConnectionFailedListener> list2, Map<Api.zzc<?>, Api.zze> map2, int i, int i2, ArrayList<zzbcu> arrayList, boolean z) {
        this.zzaDL = null;
        this.mContext = context;
        this.zzaCx = lock;
        this.zzaDA = new com.google.android.gms.common.internal.zzad(looper, this.zzaDO);
        this.zzrP = looper;
        this.zzaDF = new zzbeg(this, looper);
        this.zzaBf = googleApiAvailability;
        this.zzaBd = i;
        if (this.zzaBd >= 0) {
            this.zzaDL = Integer.valueOf(i2);
        }
        this.zzaCF = map;
        this.zzaDH = map2;
        this.zzaDK = arrayList;
        this.zzaDN = new zzbgh(this.zzaDH);
        Iterator<GoogleApiClient.ConnectionCallbacks> it = list.iterator();
        while (it.hasNext()) {
            this.zzaDA.registerConnectionCallbacks(it.next());
        }
        Iterator<GoogleApiClient.OnConnectionFailedListener> it2 = list2.iterator();
        while (it2.hasNext()) {
            this.zzaDA.registerConnectionFailedListener(it2.next());
        }
        this.zzaCC = zzqVar;
        this.zzaBg = zzaVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resume() {
        this.zzaCx.lock();
        try {
            if (this.zzaDC) {
                zzqa();
            }
        } finally {
            this.zzaCx.unlock();
        }
    }

    public static int zza(Iterable<Api.zze> iterable, boolean z) {
        boolean z2 = false;
        boolean z3 = false;
        for (Api.zze zzeVar : iterable) {
            if (zzeVar.zzmt()) {
                z3 = true;
            }
            z2 = zzeVar.zzmE() ? true : z2;
        }
        if (z3) {
            return (z2 && z) ? 2 : 1;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(GoogleApiClient googleApiClient, zzbfz zzbfzVar, boolean z) {
        zzbha.zzaIA.zzd(googleApiClient).setResultCallback(new zzbef(this, zzbfzVar, z, googleApiClient));
    }

    private final void zzap(int i) {
        if (this.zzaDL == null) {
            this.zzaDL = Integer.valueOf(i);
        } else if (this.zzaDL.intValue() != i) {
            String strValueOf = String.valueOf(zzaq(i));
            String strValueOf2 = String.valueOf(zzaq(this.zzaDL.intValue()));
            throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 51 + String.valueOf(strValueOf2).length()).append("Cannot use sign-in mode: ").append(strValueOf).append(". Mode was already set to ").append(strValueOf2).toString());
        }
        if (this.zzaDB != null) {
            return;
        }
        boolean z = false;
        boolean z2 = false;
        for (Api.zze zzeVar : this.zzaDH.values()) {
            if (zzeVar.zzmt()) {
                z2 = true;
            }
            z = zzeVar.zzmE() ? true : z;
        }
        switch (this.zzaDL.intValue()) {
            case 1:
                if (!z2) {
                    throw new IllegalStateException("SIGN_IN_MODE_REQUIRED cannot be used on a GoogleApiClient that does not contain any authenticated APIs. Use connect() instead.");
                }
                if (z) {
                    throw new IllegalStateException("Cannot use SIGN_IN_MODE_REQUIRED with GOOGLE_SIGN_IN_API. Use connect(SIGN_IN_MODE_OPTIONAL) instead.");
                }
                break;
            case 2:
                if (z2) {
                    if (this.zzaBj) {
                        this.zzaDB = new zzbdb(this.mContext, this.zzaCx, this.zzrP, this.zzaBf, this.zzaDH, this.zzaCC, this.zzaCF, this.zzaBg, this.zzaDK, this, true);
                        return;
                    } else {
                        this.zzaDB = zzbcw.zza(this.mContext, this, this.zzaCx, this.zzrP, this.zzaBf, this.zzaDH, this.zzaCC, this.zzaCF, this.zzaBg, this.zzaDK);
                        return;
                    }
                }
                break;
        }
        if (!this.zzaBj || z) {
            this.zzaDB = new zzbej(this.mContext, this, this.zzaCx, this.zzrP, this.zzaBf, this.zzaDH, this.zzaCC, this.zzaCF, this.zzaBg, this.zzaDK, this);
        } else {
            this.zzaDB = new zzbdb(this.mContext, this.zzaCx, this.zzrP, this.zzaBf, this.zzaDH, this.zzaCC, this.zzaCF, this.zzaBg, this.zzaDK, this, false);
        }
    }

    private static String zzaq(int i) {
        switch (i) {
            case 1:
                return "SIGN_IN_MODE_REQUIRED";
            case 2:
                return "SIGN_IN_MODE_OPTIONAL";
            case 3:
                return "SIGN_IN_MODE_NONE";
            default:
                return "UNKNOWN";
        }
    }

    private final void zzqa() {
        this.zzaDA.zzrz();
        this.zzaDB.connect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzqb() {
        this.zzaCx.lock();
        try {
            if (zzqc()) {
                zzqa();
            }
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final ConnectionResult blockingConnect() {
        zzbr.zza(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        this.zzaCx.lock();
        try {
            if (this.zzaBd >= 0) {
                zzbr.zza(this.zzaDL != null, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zzaDL == null) {
                this.zzaDL = Integer.valueOf(zza(this.zzaDH.values(), false));
            } else if (this.zzaDL.intValue() == 2) {
                throw new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            zzap(this.zzaDL.intValue());
            this.zzaDA.zzrz();
            return this.zzaDB.blockingConnect();
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit) {
        zzbr.zza(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        zzbr.zzb(timeUnit, "TimeUnit must not be null");
        this.zzaCx.lock();
        try {
            if (this.zzaDL == null) {
                this.zzaDL = Integer.valueOf(zza(this.zzaDH.values(), false));
            } else if (this.zzaDL.intValue() == 2) {
                throw new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            zzap(this.zzaDL.intValue());
            this.zzaDA.zzrz();
            return this.zzaDB.blockingConnect(j, timeUnit);
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final PendingResult<Status> clearDefaultAccountAndReconnect() {
        zzbr.zza(isConnected(), "GoogleApiClient is not connected yet.");
        zzbr.zza(this.zzaDL.intValue() != 2, "Cannot use clearDefaultAccountAndReconnect with GOOGLE_SIGN_IN_API");
        zzbfz zzbfzVar = new zzbfz(this);
        if (this.zzaDH.containsKey(zzbha.zzajT)) {
            zza(this, zzbfzVar, false);
        } else {
            AtomicReference atomicReference = new AtomicReference();
            GoogleApiClient googleApiClientBuild = new GoogleApiClient.Builder(this.mContext).addApi(zzbha.API).addConnectionCallbacks(new zzbed(this, atomicReference, zzbfzVar)).addOnConnectionFailedListener(new zzbee(this, zzbfzVar)).setHandler(this.zzaDF).build();
            atomicReference.set(googleApiClientBuild);
            googleApiClientBuild.connect();
        }
        return zzbfzVar;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void connect() {
        this.zzaCx.lock();
        try {
            if (this.zzaBd >= 0) {
                zzbr.zza(this.zzaDL != null, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zzaDL == null) {
                this.zzaDL = Integer.valueOf(zza(this.zzaDH.values(), false));
            } else if (this.zzaDL.intValue() == 2) {
                throw new IllegalStateException("Cannot call connect() when SignInMode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            connect(this.zzaDL.intValue());
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void connect(int i) {
        boolean z = true;
        this.zzaCx.lock();
        if (i != 3 && i != 1 && i != 2) {
            z = false;
        }
        try {
            zzbr.zzb(z, new StringBuilder(33).append("Illegal sign-in mode: ").append(i).toString());
            zzap(i);
            zzqa();
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void disconnect() {
        this.zzaCx.lock();
        try {
            this.zzaDN.release();
            if (this.zzaDB != null) {
                this.zzaDB.disconnect();
            }
            this.zzaDJ.release();
            for (zzbck<?, ?> zzbckVar : this.zzaCL) {
                zzbckVar.zza((zzbgj) null);
                zzbckVar.cancel();
            }
            this.zzaCL.clear();
            if (this.zzaDB == null) {
                return;
            }
            zzqc();
            this.zzaDA.zzry();
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append((CharSequence) str).append("mContext=").println(this.mContext);
        printWriter.append((CharSequence) str).append("mResuming=").print(this.zzaDC);
        printWriter.append(" mWorkQueue.size()=").print(this.zzaCL.size());
        printWriter.append(" mUnconsumedApiCalls.size()=").println(this.zzaDN.zzaFn.size());
        if (this.zzaDB != null) {
            this.zzaDB.dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    @NonNull
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        this.zzaCx.lock();
        try {
            if (!isConnected() && !this.zzaDC) {
                throw new IllegalStateException("Cannot invoke getConnectionResult unless GoogleApiClient is connected");
            }
            if (!this.zzaDH.containsKey(api.zzpb())) {
                throw new IllegalArgumentException(String.valueOf(api.getName()).concat(" was never registered with GoogleApiClient"));
            }
            ConnectionResult connectionResult = this.zzaDB.getConnectionResult(api);
            if (connectionResult == null) {
                if (this.zzaDC) {
                    connectionResult = ConnectionResult.zzazZ;
                } else {
                    Log.w("GoogleApiClientImpl", zzqe());
                    Log.wtf("GoogleApiClientImpl", String.valueOf(api.getName()).concat(" requested in getConnectionResult is not connected but is not present in the failed  connections map"), new Exception());
                    connectionResult = new ConnectionResult(8, null);
                }
            }
            return connectionResult;
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final Looper getLooper() {
        return this.zzrP;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean hasConnectedApi(@NonNull Api<?> api) {
        if (!isConnected()) {
            return false;
        }
        Api.zze zzeVar = this.zzaDH.get(api.zzpb());
        return zzeVar != null && zzeVar.isConnected();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean isConnected() {
        return this.zzaDB != null && this.zzaDB.isConnected();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean isConnecting() {
        return this.zzaDB != null && this.zzaDB.isConnecting();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean isConnectionCallbacksRegistered(@NonNull GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        return this.zzaDA.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean isConnectionFailedListenerRegistered(@NonNull GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        return this.zzaDA.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void reconnect() {
        disconnect();
        connect();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void registerConnectionCallbacks(@NonNull GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        this.zzaDA.registerConnectionCallbacks(connectionCallbacks);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void registerConnectionFailedListener(@NonNull GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zzaDA.registerConnectionFailedListener(onConnectionFailedListener);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {
        zzbfd zzbfdVar = new zzbfd(fragmentActivity);
        if (this.zzaBd < 0) {
            throw new IllegalStateException("Called stopAutoManage but automatic lifecycle management is not enabled.");
        }
        zzbcg.zza(zzbfdVar).zzal(this.zzaBd);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void unregisterConnectionCallbacks(@NonNull GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        this.zzaDA.unregisterConnectionCallbacks(connectionCallbacks);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void unregisterConnectionFailedListener(@NonNull GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zzaDA.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    @NonNull
    public final <C extends Api.zze> C zza(@NonNull Api.zzc<C> zzcVar) {
        C c = (C) this.zzaDH.get(zzcVar);
        zzbr.zzb(c, "Appropriate Api was not requested.");
        return c;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void zza(zzbge zzbgeVar) {
        this.zzaCx.lock();
        try {
            if (this.zzaDM == null) {
                this.zzaDM = new HashSet();
            }
            this.zzaDM.add(zzbgeVar);
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean zza(@NonNull Api<?> api) {
        return this.zzaDH.containsKey(api.zzpb());
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean zza(zzbfu zzbfuVar) {
        return this.zzaDB != null && this.zzaDB.zza(zzbfuVar);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void zzb(zzbge zzbgeVar) {
        this.zzaCx.lock();
        try {
            if (this.zzaDM == null) {
                Log.wtf("GoogleApiClientImpl", "Attempted to remove pending transform when no transforms are registered.", new Exception());
            } else if (!this.zzaDM.remove(zzbgeVar)) {
                Log.wtf("GoogleApiClientImpl", "Failed to remove pending transform - this may lead to memory leaks!", new Exception());
            } else if (!zzqd()) {
                this.zzaDB.zzpC();
            }
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfc
    public final void zzc(ConnectionResult connectionResult) {
        if (!com.google.android.gms.common.zze.zze(this.mContext, connectionResult.getErrorCode())) {
            zzqc();
        }
        if (this.zzaDC) {
            return;
        }
        this.zzaDA.zzk(connectionResult);
        this.zzaDA.zzry();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(@NonNull T t) {
        zzbr.zzb(t.zzpb() != null, "This task can not be enqueued (it's probably a Batch or malformed)");
        boolean zContainsKey = this.zzaDH.containsKey(t.zzpb());
        String name = t.zzpe() != null ? t.zzpe().getName() : "the API";
        zzbr.zzb(zContainsKey, new StringBuilder(String.valueOf(name).length() + 65).append("GoogleApiClient is not configured to use ").append(name).append(" required for this call.").toString());
        this.zzaCx.lock();
        try {
            if (this.zzaDB == null) {
                this.zzaCL.add(t);
            } else {
                t = (T) this.zzaDB.zzd(t);
            }
            return t;
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(@NonNull T t) {
        zzbr.zzb(t.zzpb() != null, "This task can not be executed (it's probably a Batch or malformed)");
        boolean zContainsKey = this.zzaDH.containsKey(t.zzpb());
        String name = t.zzpe() != null ? t.zzpe().getName() : "the API";
        zzbr.zzb(zContainsKey, new StringBuilder(String.valueOf(name).length() + 65).append("GoogleApiClient is not configured to use ").append(name).append(" required for this call.").toString());
        this.zzaCx.lock();
        try {
            if (this.zzaDB == null) {
                throw new IllegalStateException("GoogleApiClient is not connected yet.");
            }
            if (this.zzaDC) {
                this.zzaCL.add(t);
                while (!this.zzaCL.isEmpty()) {
                    zzbck<?, ?> zzbckVarRemove = this.zzaCL.remove();
                    this.zzaDN.zzb(zzbckVarRemove);
                    zzbckVarRemove.zzr(Status.zzaBq);
                }
            } else {
                t = (T) this.zzaDB.zze(t);
            }
            return t;
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfc
    public final void zze(int i, boolean z) {
        if (i == 1 && !z && !this.zzaDC) {
            this.zzaDC = true;
            if (this.zzaDG == null) {
                this.zzaDG = GoogleApiAvailability.zza(this.mContext.getApplicationContext(), new zzbeh(this));
            }
            this.zzaDF.sendMessageDelayed(this.zzaDF.obtainMessage(1), this.zzaDD);
            this.zzaDF.sendMessageDelayed(this.zzaDF.obtainMessage(2), this.zzaDE);
        }
        this.zzaDN.zzqK();
        this.zzaDA.zzaA(i);
        this.zzaDA.zzry();
        if (i == 2) {
            zzqa();
        }
    }

    @Override // com.google.android.gms.internal.zzbfc
    public final void zzm(Bundle bundle) {
        while (!this.zzaCL.isEmpty()) {
            zze(this.zzaCL.remove());
        }
        this.zzaDA.zzn(bundle);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final <L> zzbfi<L> zzp(@NonNull L l) {
        this.zzaCx.lock();
        try {
            return this.zzaDJ.zza(l, this.zzrP, "NO_TYPE");
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void zzpj() {
        if (this.zzaDB != null) {
            this.zzaDB.zzpj();
        }
    }

    final boolean zzqc() {
        if (!this.zzaDC) {
            return false;
        }
        this.zzaDC = false;
        this.zzaDF.removeMessages(2);
        this.zzaDF.removeMessages(1);
        if (this.zzaDG != null) {
            this.zzaDG.unregister();
            this.zzaDG = null;
        }
        return true;
    }

    final boolean zzqd() {
        this.zzaCx.lock();
        try {
            if (this.zzaDM != null) {
                z = this.zzaDM.isEmpty() ? false : true;
            }
            return z;
        } finally {
            this.zzaCx.unlock();
        }
    }

    final String zzqe() {
        StringWriter stringWriter = new StringWriter();
        dump("", null, new PrintWriter(stringWriter), null);
        return stringWriter.toString();
    }
}
