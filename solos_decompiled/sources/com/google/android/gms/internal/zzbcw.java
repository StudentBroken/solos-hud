package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbr;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/* JADX INFO: loaded from: classes3.dex */
final class zzbcw implements zzbfb {
    private final Context mContext;
    private final zzbeb zzaCn;
    private final zzbej zzaCo;
    private final zzbej zzaCp;
    private final Map<Api.zzc<?>, zzbej> zzaCq;
    private final Api.zze zzaCs;
    private Bundle zzaCt;
    private final Lock zzaCx;
    private final Looper zzrP;
    private final Set<zzbfu> zzaCr = Collections.newSetFromMap(new WeakHashMap());
    private ConnectionResult zzaCu = null;
    private ConnectionResult zzaCv = null;
    private boolean zzaCw = false;
    private int zzaCy = 0;

    private zzbcw(Context context, zzbeb zzbebVar, Lock lock, Looper looper, com.google.android.gms.common.zze zzeVar, Map<Api.zzc<?>, Api.zze> map, Map<Api.zzc<?>, Api.zze> map2, com.google.android.gms.common.internal.zzq zzqVar, Api.zza<? extends zzcuw, zzcux> zzaVar, Api.zze zzeVar2, ArrayList<zzbcu> arrayList, ArrayList<zzbcu> arrayList2, Map<Api<?>, Boolean> map3, Map<Api<?>, Boolean> map4) {
        this.mContext = context;
        this.zzaCn = zzbebVar;
        this.zzaCx = lock;
        this.zzrP = looper;
        this.zzaCs = zzeVar2;
        this.zzaCo = new zzbej(context, this.zzaCn, lock, looper, zzeVar, map2, null, map4, null, arrayList2, new zzbcy(this, null));
        this.zzaCp = new zzbej(context, this.zzaCn, lock, looper, zzeVar, map, zzqVar, map3, zzaVar, arrayList, new zzbcz(this, null));
        ArrayMap arrayMap = new ArrayMap();
        Iterator<Api.zzc<?>> it = map2.keySet().iterator();
        while (it.hasNext()) {
            arrayMap.put(it.next(), this.zzaCo);
        }
        Iterator<Api.zzc<?>> it2 = map.keySet().iterator();
        while (it2.hasNext()) {
            arrayMap.put(it2.next(), this.zzaCp);
        }
        this.zzaCq = Collections.unmodifiableMap(arrayMap);
    }

    public static zzbcw zza(Context context, zzbeb zzbebVar, Lock lock, Looper looper, com.google.android.gms.common.zze zzeVar, Map<Api.zzc<?>, Api.zze> map, com.google.android.gms.common.internal.zzq zzqVar, Map<Api<?>, Boolean> map2, Api.zza<? extends zzcuw, zzcux> zzaVar, ArrayList<zzbcu> arrayList) {
        Api.zze zzeVar2 = null;
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        for (Map.Entry<Api.zzc<?>, Api.zze> entry : map.entrySet()) {
            Api.zze value = entry.getValue();
            if (value.zzmE()) {
                zzeVar2 = value;
            }
            if (value.zzmt()) {
                arrayMap.put(entry.getKey(), value);
            } else {
                arrayMap2.put(entry.getKey(), value);
            }
        }
        zzbr.zza(!arrayMap.isEmpty(), "CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        for (Api<?> api : map2.keySet()) {
            Api.zzc<?> zzcVarZzpb = api.zzpb();
            if (arrayMap.containsKey(zzcVarZzpb)) {
                arrayMap3.put(api, map2.get(api));
            } else {
                if (!arrayMap2.containsKey(zzcVarZzpb)) {
                    throw new IllegalStateException("Each API in the isOptionalMap must have a corresponding client in the clients map.");
                }
                arrayMap4.put(api, map2.get(api));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList<zzbcu> arrayList4 = arrayList;
        int size = arrayList4.size();
        int i = 0;
        while (i < size) {
            zzbcu zzbcuVar = arrayList4.get(i);
            i++;
            zzbcu zzbcuVar2 = zzbcuVar;
            if (arrayMap3.containsKey(zzbcuVar2.zzayY)) {
                arrayList2.add(zzbcuVar2);
            } else {
                if (!arrayMap4.containsKey(zzbcuVar2.zzayY)) {
                    throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the isOptionalMap");
                }
                arrayList3.add(zzbcuVar2);
            }
        }
        return new zzbcw(context, zzbebVar, lock, looper, zzeVar, arrayMap, arrayMap2, zzqVar, zzaVar, zzeVar2, arrayList2, arrayList3, arrayMap3, arrayMap4);
    }

    private final void zza(ConnectionResult connectionResult) {
        switch (this.zzaCy) {
            case 2:
                this.zzaCn.zzc(connectionResult);
            case 1:
                zzpE();
                break;
            default:
                Log.wtf("CompositeGAC", "Attempted to call failure callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                break;
        }
        this.zzaCy = 0;
    }

    private static boolean zzb(ConnectionResult connectionResult) {
        return connectionResult != null && connectionResult.isSuccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzd(int i, boolean z) {
        this.zzaCn.zze(i, z);
        this.zzaCv = null;
        this.zzaCu = null;
    }

    private final boolean zzf(zzbck<? extends Result, ? extends Api.zzb> zzbckVar) {
        Object objZzpb = zzbckVar.zzpb();
        zzbr.zzb(this.zzaCq.containsKey(objZzpb), "GoogleApiClient is not configured to use the API required for this call.");
        return this.zzaCq.get(objZzpb).equals(this.zzaCp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzl(Bundle bundle) {
        if (this.zzaCt == null) {
            this.zzaCt = bundle;
        } else if (bundle != null) {
            this.zzaCt.putAll(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzpD() {
        if (!zzb(this.zzaCu)) {
            if (this.zzaCu != null && zzb(this.zzaCv)) {
                this.zzaCp.disconnect();
                zza(this.zzaCu);
                return;
            } else {
                if (this.zzaCu == null || this.zzaCv == null) {
                    return;
                }
                ConnectionResult connectionResult = this.zzaCu;
                if (this.zzaCp.zzaDZ < this.zzaCo.zzaDZ) {
                    connectionResult = this.zzaCv;
                }
                zza(connectionResult);
                return;
            }
        }
        if (zzb(this.zzaCv) || zzpF()) {
            switch (this.zzaCy) {
                case 2:
                    this.zzaCn.zzm(this.zzaCt);
                case 1:
                    zzpE();
                    break;
                default:
                    Log.wtf("CompositeGAC", "Attempted to call success callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new AssertionError());
                    break;
            }
            this.zzaCy = 0;
            return;
        }
        if (this.zzaCv != null) {
            if (this.zzaCy == 1) {
                zzpE();
            } else {
                zza(this.zzaCv);
                this.zzaCo.disconnect();
            }
        }
    }

    private final void zzpE() {
        Iterator<zzbfu> it = this.zzaCr.iterator();
        while (it.hasNext()) {
            it.next().zzmD();
        }
        this.zzaCr.clear();
    }

    private final boolean zzpF() {
        return this.zzaCv != null && this.zzaCv.getErrorCode() == 4;
    }

    @Nullable
    private final PendingIntent zzpG() {
        if (this.zzaCs == null) {
            return null;
        }
        return PendingIntent.getActivity(this.mContext, System.identityHashCode(this.zzaCn), this.zzaCs.zzmF(), VCardConfig.FLAG_CONVERT_PHONETIC_NAME_STRINGS);
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final ConnectionResult blockingConnect() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void connect() {
        this.zzaCy = 2;
        this.zzaCw = false;
        this.zzaCv = null;
        this.zzaCu = null;
        this.zzaCo.connect();
        this.zzaCp.connect();
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void disconnect() {
        this.zzaCv = null;
        this.zzaCu = null;
        this.zzaCy = 0;
        this.zzaCo.disconnect();
        this.zzaCp.disconnect();
        zzpE();
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append((CharSequence) str).append("authClient").println(":");
        this.zzaCp.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
        printWriter.append((CharSequence) str).append("anonClient").println(":");
        this.zzaCo.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
    }

    @Override // com.google.android.gms.internal.zzbfb
    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return this.zzaCq.get(api.zzpb()).equals(this.zzaCp) ? zzpF() ? new ConnectionResult(4, zzpG()) : this.zzaCp.getConnectionResult(api) : this.zzaCo.getConnectionResult(api);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0026  */
    @Override // com.google.android.gms.internal.zzbfb
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isConnected() {
        /*
            r2 = this;
            r0 = 1
            java.util.concurrent.locks.Lock r1 = r2.zzaCx
            r1.lock()
            com.google.android.gms.internal.zzbej r1 = r2.zzaCo     // Catch: java.lang.Throwable -> L28
            boolean r1 = r1.isConnected()     // Catch: java.lang.Throwable -> L28
            if (r1 == 0) goto L26
            com.google.android.gms.internal.zzbej r1 = r2.zzaCp     // Catch: java.lang.Throwable -> L28
            boolean r1 = r1.isConnected()     // Catch: java.lang.Throwable -> L28
            if (r1 != 0) goto L20
            boolean r1 = r2.zzpF()     // Catch: java.lang.Throwable -> L28
            if (r1 != 0) goto L20
            int r1 = r2.zzaCy     // Catch: java.lang.Throwable -> L28
            if (r1 != r0) goto L26
        L20:
            java.util.concurrent.locks.Lock r1 = r2.zzaCx
            r1.unlock()
            return r0
        L26:
            r0 = 0
            goto L20
        L28:
            r0 = move-exception
            java.util.concurrent.locks.Lock r1 = r2.zzaCx
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbcw.isConnected():boolean");
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final boolean isConnecting() {
        this.zzaCx.lock();
        try {
            return this.zzaCy == 2;
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final boolean zza(zzbfu zzbfuVar) {
        this.zzaCx.lock();
        try {
            if ((!isConnecting() && !isConnected()) || this.zzaCp.isConnected()) {
                this.zzaCx.unlock();
                return false;
            }
            this.zzaCr.add(zzbfuVar);
            if (this.zzaCy == 0) {
                this.zzaCy = 1;
            }
            this.zzaCv = null;
            this.zzaCp.connect();
            return true;
        } finally {
            this.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(@NonNull T t) {
        if (!zzf((zzbck<? extends Result, ? extends Api.zzb>) t)) {
            return (T) this.zzaCo.zzd(t);
        }
        if (!zzpF()) {
            return (T) this.zzaCp.zzd(t);
        }
        t.zzr(new Status(4, null, zzpG()));
        return t;
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(@NonNull T t) {
        if (!zzf((zzbck<? extends Result, ? extends Api.zzb>) t)) {
            return (T) this.zzaCo.zze(t);
        }
        if (!zzpF()) {
            return (T) this.zzaCp.zze(t);
        }
        t.zzr(new Status(4, null, zzpG()));
        return t;
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void zzpC() {
        this.zzaCo.zzpC();
        this.zzaCp.zzpC();
    }

    @Override // com.google.android.gms.internal.zzbfb
    public final void zzpj() {
        this.zzaCx.lock();
        try {
            boolean zIsConnecting = isConnecting();
            this.zzaCp.disconnect();
            this.zzaCv = new ConnectionResult(4);
            if (zIsConnecting) {
                new Handler(this.zzrP).post(new zzbcx(this));
            } else {
                zzpE();
            }
        } finally {
            this.zzaCx.unlock();
        }
    }
}
