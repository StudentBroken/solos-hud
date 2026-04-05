package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzbu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbdp implements zzbei {
    private final Context mContext;
    private final Api.zza<? extends zzcuw, zzcux> zzaBg;
    private final com.google.android.gms.common.internal.zzq zzaCC;
    private final Map<Api<?>, Boolean> zzaCF;
    private final com.google.android.gms.common.zze zzaCH;
    private ConnectionResult zzaCQ;
    private final Lock zzaCx;
    private final zzbej zzaDb;
    private int zzaDe;
    private int zzaDg;
    private zzcuw zzaDj;
    private boolean zzaDk;
    private boolean zzaDl;
    private boolean zzaDm;
    private com.google.android.gms.common.internal.zzam zzaDn;
    private boolean zzaDo;
    private boolean zzaDp;
    private int zzaDf = 0;
    private final Bundle zzaDh = new Bundle();
    private final Set<Api.zzc> zzaDi = new HashSet();
    private ArrayList<Future<?>> zzaDq = new ArrayList<>();

    public zzbdp(zzbej zzbejVar, com.google.android.gms.common.internal.zzq zzqVar, Map<Api<?>, Boolean> map, com.google.android.gms.common.zze zzeVar, Api.zza<? extends zzcuw, zzcux> zzaVar, Lock lock, Context context) {
        this.zzaDb = zzbejVar;
        this.zzaCC = zzqVar;
        this.zzaCF = map;
        this.zzaCH = zzeVar;
        this.zzaBg = zzaVar;
        this.zzaCx = lock;
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(zzcvj zzcvjVar) {
        if (zzan(0)) {
            ConnectionResult connectionResultZzpx = zzcvjVar.zzpx();
            if (!connectionResultZzpx.isSuccess()) {
                if (!zzd(connectionResultZzpx)) {
                    zze(connectionResultZzpx);
                    return;
                } else {
                    zzpX();
                    zzpV();
                    return;
                }
            }
            zzbu zzbuVarZzAv = zzcvjVar.zzAv();
            ConnectionResult connectionResultZzpx2 = zzbuVarZzAv.zzpx();
            if (!connectionResultZzpx2.isSuccess()) {
                String strValueOf = String.valueOf(connectionResultZzpx2);
                Log.wtf("GoogleApiClientConnecting", new StringBuilder(String.valueOf(strValueOf).length() + 48).append("Sign-in succeeded with resolve account failure: ").append(strValueOf).toString(), new Exception());
                zze(connectionResultZzpx2);
            } else {
                this.zzaDm = true;
                this.zzaDn = zzbuVarZzAv.zzrG();
                this.zzaDo = zzbuVarZzAv.zzrH();
                this.zzaDp = zzbuVarZzAv.zzrI();
                zzpV();
            }
        }
    }

    private final void zzad(boolean z) {
        if (this.zzaDj != null) {
            if (this.zzaDj.isConnected() && z) {
                this.zzaDj.zzAo();
            }
            this.zzaDj.disconnect();
            this.zzaDn = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zzan(int i) {
        if (this.zzaDf == i) {
            return true;
        }
        Log.w("GoogleApiClientConnecting", this.zzaDb.zzaCn.zzqe());
        String strValueOf = String.valueOf(this);
        Log.w("GoogleApiClientConnecting", new StringBuilder(String.valueOf(strValueOf).length() + 23).append("Unexpected callback in ").append(strValueOf).toString());
        Log.w("GoogleApiClientConnecting", new StringBuilder(33).append("mRemainingConnections=").append(this.zzaDg).toString());
        String strValueOf2 = String.valueOf(zzao(this.zzaDf));
        String strValueOf3 = String.valueOf(zzao(i));
        Log.wtf("GoogleApiClientConnecting", new StringBuilder(String.valueOf(strValueOf2).length() + 70 + String.valueOf(strValueOf3).length()).append("GoogleApiClient connecting is in step ").append(strValueOf2).append(" but received callback for step ").append(strValueOf3).toString(), new Exception());
        zze(new ConnectionResult(8, null));
        return false;
    }

    private static String zzao(int i) {
        switch (i) {
            case 0:
                return "STEP_SERVICE_BINDINGS_AND_SIGN_IN";
            case 1:
                return "STEP_GETTING_REMOTE_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void zzb(com.google.android.gms.common.ConnectionResult r6, com.google.android.gms.common.api.Api<?> r7, boolean r8) {
        /*
            r5 = this;
            r1 = 0
            r0 = 1
            com.google.android.gms.common.api.Api$zzd r2 = r7.zzoZ()
            int r3 = r2.getPriority()
            if (r8 == 0) goto L15
            boolean r2 = r6.hasResolution()
            if (r2 == 0) goto L2f
            r2 = r0
        L13:
            if (r2 == 0) goto L3f
        L15:
            com.google.android.gms.common.ConnectionResult r2 = r5.zzaCQ
            if (r2 == 0) goto L1d
            int r2 = r5.zzaDe
            if (r3 >= r2) goto L3f
        L1d:
            if (r0 == 0) goto L23
            r5.zzaCQ = r6
            r5.zzaDe = r3
        L23:
            com.google.android.gms.internal.zzbej r0 = r5.zzaDb
            java.util.Map<com.google.android.gms.common.api.Api$zzc<?>, com.google.android.gms.common.ConnectionResult> r0 = r0.zzaDW
            com.google.android.gms.common.api.Api$zzc r1 = r7.zzpb()
            r0.put(r1, r6)
            return
        L2f:
            com.google.android.gms.common.zze r2 = r5.zzaCH
            int r4 = r6.getErrorCode()
            android.content.Intent r2 = r2.zzak(r4)
            if (r2 == 0) goto L3d
            r2 = r0
            goto L13
        L3d:
            r2 = r1
            goto L13
        L3f:
            r0 = r1
            goto L1d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbdp.zzb(com.google.android.gms.common.ConnectionResult, com.google.android.gms.common.api.Api, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zzd(ConnectionResult connectionResult) {
        return this.zzaDk && !connectionResult.hasResolution();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zze(ConnectionResult connectionResult) {
        zzpY();
        zzad(!connectionResult.hasResolution());
        this.zzaDb.zzg(connectionResult);
        this.zzaDb.zzaEa.zzc(connectionResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zzpU() {
        this.zzaDg--;
        if (this.zzaDg > 0) {
            return false;
        }
        if (this.zzaDg < 0) {
            Log.w("GoogleApiClientConnecting", this.zzaDb.zzaCn.zzqe());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            zze(new ConnectionResult(8, null));
            return false;
        }
        if (this.zzaCQ == null) {
            return true;
        }
        this.zzaDb.zzaDZ = this.zzaDe;
        zze(this.zzaCQ);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzpV() {
        if (this.zzaDg != 0) {
            return;
        }
        if (!this.zzaDl || this.zzaDm) {
            ArrayList arrayList = new ArrayList();
            this.zzaDf = 1;
            this.zzaDg = this.zzaDb.zzaDH.size();
            for (Api.zzc<?> zzcVar : this.zzaDb.zzaDH.keySet()) {
                if (!this.zzaDb.zzaDW.containsKey(zzcVar)) {
                    arrayList.add(this.zzaDb.zzaDH.get(zzcVar));
                } else if (zzpU()) {
                    zzpW();
                }
            }
            if (arrayList.isEmpty()) {
                return;
            }
            this.zzaDq.add(zzbem.zzqh().submit(new zzbdv(this, arrayList)));
        }
    }

    private final void zzpW() {
        this.zzaDb.zzqg();
        zzbem.zzqh().execute(new zzbdq(this));
        if (this.zzaDj != null) {
            if (this.zzaDo) {
                this.zzaDj.zza(this.zzaDn, this.zzaDp);
            }
            zzad(false);
        }
        Iterator<Api.zzc<?>> it = this.zzaDb.zzaDW.keySet().iterator();
        while (it.hasNext()) {
            this.zzaDb.zzaDH.get(it.next()).disconnect();
        }
        this.zzaDb.zzaEa.zzm(this.zzaDh.isEmpty() ? null : this.zzaDh);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzpX() {
        this.zzaDl = false;
        this.zzaDb.zzaCn.zzaDI = Collections.emptySet();
        for (Api.zzc<?> zzcVar : this.zzaDi) {
            if (!this.zzaDb.zzaDW.containsKey(zzcVar)) {
                this.zzaDb.zzaDW.put(zzcVar, new ConnectionResult(17, null));
            }
        }
    }

    private final void zzpY() {
        ArrayList<Future<?>> arrayList = this.zzaDq;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Future<?> future = arrayList.get(i);
            i++;
            future.cancel(true);
        }
        this.zzaDq.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Set<Scope> zzpZ() {
        if (this.zzaCC == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet(this.zzaCC.zzrl());
        Map<Api<?>, com.google.android.gms.common.internal.zzr> mapZzrn = this.zzaCC.zzrn();
        for (Api<?> api : mapZzrn.keySet()) {
            if (!this.zzaDb.zzaDW.containsKey(api.zzpb())) {
                hashSet.addAll(mapZzrn.get(api).zzamg);
            }
        }
        return hashSet;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void begin() {
        zzbdq zzbdqVar = null;
        this.zzaDb.zzaDW.clear();
        this.zzaDl = false;
        this.zzaCQ = null;
        this.zzaDf = 0;
        this.zzaDk = true;
        this.zzaDm = false;
        this.zzaDo = false;
        HashMap map = new HashMap();
        boolean z = false;
        for (Api<?> api : this.zzaCF.keySet()) {
            Api.zze zzeVar = this.zzaDb.zzaDH.get(api.zzpb());
            boolean z2 = (api.zzoZ().getPriority() == 1) | z;
            boolean zBooleanValue = this.zzaCF.get(api).booleanValue();
            if (zzeVar.zzmt()) {
                this.zzaDl = true;
                if (zBooleanValue) {
                    this.zzaDi.add(api.zzpb());
                } else {
                    this.zzaDk = false;
                }
            }
            map.put(zzeVar, new zzbdr(this, api, zBooleanValue));
            z = z2;
        }
        if (z) {
            this.zzaDl = false;
        }
        if (this.zzaDl) {
            this.zzaCC.zzc(Integer.valueOf(System.identityHashCode(this.zzaDb.zzaCn)));
            zzbdy zzbdyVar = new zzbdy(this, zzbdqVar);
            this.zzaDj = (zzcuw) this.zzaBg.zza(this.mContext, this.zzaDb.zzaCn.getLooper(), this.zzaCC, this.zzaCC.zzrr(), zzbdyVar, zzbdyVar);
        }
        this.zzaDg = this.zzaDb.zzaDH.size();
        this.zzaDq.add(zzbem.zzqh().submit(new zzbds(this, map)));
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void connect() {
    }

    @Override // com.google.android.gms.internal.zzbei
    public final boolean disconnect() {
        zzpY();
        zzad(true);
        this.zzaDb.zzg(null);
        return true;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void onConnected(Bundle bundle) {
        if (zzan(1)) {
            if (bundle != null) {
                this.zzaDh.putAll(bundle);
            }
            if (zzpU()) {
                zzpW();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void onConnectionSuspended(int i) {
        zze(new ConnectionResult(8, null));
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (zzan(1)) {
            zzb(connectionResult, api, z);
            if (zzpU()) {
                zzpW();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzbei
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(T t) {
        this.zzaDb.zzaCn.zzaCL.add(t);
        return t;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
