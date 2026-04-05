package com.google.android.gms.internal;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.kopin.pupil.aria.app.TimedAppState;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes3.dex */
public final class zzben implements Handler.Callback {
    private static zzben zzaEh;
    private final Context mContext;
    private final Handler mHandler;
    private final GoogleApiAvailability zzaBf;
    public static final Status zzaEe = new Status(4, "Sign-out occurred while this API call was in progress.");
    private static final Status zzaEf = new Status(4, "The user must be signed in to make this API call.");
    private static final Object zzuI = new Object();
    private long zzaDE = 5000;
    private long zzaDD = 120000;
    private long zzaEg = TimedAppState.DEFAULT_CONFIRM_TIMEOUT;
    private int zzaEi = -1;
    private final AtomicInteger zzaEj = new AtomicInteger(1);
    private final AtomicInteger zzaEk = new AtomicInteger(0);
    private final Map<zzbcf<?>, zzbep<?>> zzaCD = new ConcurrentHashMap(5, 0.75f, 1);
    private zzbdi zzaEl = null;
    private final Set<zzbcf<?>> zzaEm = new com.google.android.gms.common.util.zzb();
    private final Set<zzbcf<?>> zzaEn = new com.google.android.gms.common.util.zzb();

    private zzben(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.mContext = context;
        this.mHandler = new Handler(looper, this);
        this.zzaBf = googleApiAvailability;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6));
    }

    public static zzben zzay(Context context) {
        zzben zzbenVar;
        synchronized (zzuI) {
            if (zzaEh == null) {
                HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                zzaEh = new zzben(context.getApplicationContext(), handlerThread.getLooper(), GoogleApiAvailability.getInstance());
            }
            zzbenVar = zzaEh;
        }
        return zzbenVar;
    }

    @WorkerThread
    private final void zzc(GoogleApi<?> googleApi) {
        Object objZzpf = googleApi.zzpf();
        zzbep<?> zzbepVar = this.zzaCD.get(objZzpf);
        if (zzbepVar == null) {
            zzbepVar = new zzbep<>(this, googleApi);
            this.zzaCD.put((zzbcf<?>) objZzpf, zzbepVar);
        }
        if (zzbepVar.zzmt()) {
            this.zzaEn.add((zzbcf<?>) objZzpf);
        }
        zzbepVar.connect();
    }

    public static zzben zzqi() {
        zzben zzbenVar;
        synchronized (zzuI) {
            zzbr.zzb(zzaEh, "Must guarantee manager is non-null before using getInstance");
            zzbenVar = zzaEh;
        }
        return zzbenVar;
    }

    public static void zzqj() {
        synchronized (zzuI) {
            if (zzaEh != null) {
                zzben zzbenVar = zzaEh;
                zzbenVar.zzaEk.incrementAndGet();
                zzbenVar.mHandler.sendMessageAtFrontOfQueue(zzbenVar.mHandler.obtainMessage(10));
            }
        }
    }

    @WorkerThread
    private final void zzql() {
        Iterator<zzbcf<?>> it = this.zzaEn.iterator();
        while (it.hasNext()) {
            this.zzaCD.remove(it.next()).signOut();
        }
        this.zzaEn.clear();
    }

    @Override // android.os.Handler.Callback
    @WorkerThread
    public final boolean handleMessage(Message message) {
        zzbep<?> next;
        switch (message.what) {
            case 1:
                this.zzaEg = ((Boolean) message.obj).booleanValue() ? TimedAppState.DEFAULT_CONFIRM_TIMEOUT : 300000L;
                this.mHandler.removeMessages(12);
                Iterator<zzbcf<?>> it = this.zzaCD.keySet().iterator();
                while (it.hasNext()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(12, it.next()), this.zzaEg);
                }
                break;
            case 2:
                zzbch zzbchVar = (zzbch) message.obj;
                Iterator<zzbcf<?>> it2 = zzbchVar.zzpr().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    } else {
                        zzbcf<?> next2 = it2.next();
                        zzbep<?> zzbepVar = this.zzaCD.get(next2);
                        if (zzbepVar == null) {
                            zzbchVar.zza(next2, new ConnectionResult(13));
                            break;
                        } else if (zzbepVar.isConnected()) {
                            zzbchVar.zza(next2, ConnectionResult.zzazZ);
                        } else if (zzbepVar.zzqs() != null) {
                            zzbchVar.zza(next2, zzbepVar.zzqs());
                        } else {
                            zzbepVar.zza(zzbchVar);
                        }
                    }
                }
                break;
            case 3:
                for (zzbep<?> zzbepVar2 : this.zzaCD.values()) {
                    zzbepVar2.zzqr();
                    zzbepVar2.connect();
                }
                break;
            case 4:
            case 8:
            case 13:
                zzbfp zzbfpVar = (zzbfp) message.obj;
                zzbep<?> zzbepVar3 = this.zzaCD.get(zzbfpVar.zzaEV.zzpf());
                if (zzbepVar3 == null) {
                    zzc(zzbfpVar.zzaEV);
                    zzbepVar3 = this.zzaCD.get(zzbfpVar.zzaEV.zzpf());
                }
                if (!zzbepVar3.zzmt() || this.zzaEk.get() == zzbfpVar.zzaEU) {
                    zzbepVar3.zza(zzbfpVar.zzaET);
                } else {
                    zzbfpVar.zzaET.zzp(zzaEe);
                    zzbepVar3.signOut();
                }
                break;
            case 5:
                int i = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                Iterator<zzbep<?>> it3 = this.zzaCD.values().iterator();
                while (true) {
                    if (it3.hasNext()) {
                        next = it3.next();
                        if (next.getInstanceId() == i) {
                        }
                    } else {
                        next = null;
                    }
                }
                if (next != null) {
                    String strValueOf = String.valueOf(this.zzaBf.getErrorString(connectionResult.getErrorCode()));
                    String strValueOf2 = String.valueOf(connectionResult.getErrorMessage());
                    next.zzt(new Status(17, new StringBuilder(String.valueOf(strValueOf).length() + 69 + String.valueOf(strValueOf2).length()).append("Error resolution was canceled by the user, original error message: ").append(strValueOf).append(": ").append(strValueOf2).toString()));
                } else {
                    Log.wtf("GoogleApiManager", new StringBuilder(76).append("Could not find API instance ").append(i).append(" while trying to fail enqueued calls.").toString(), new Exception());
                }
                break;
            case 6:
                if (this.mContext.getApplicationContext() instanceof Application) {
                    zzbci.zza((Application) this.mContext.getApplicationContext());
                    zzbci.zzpt().zza(new zzbeo(this));
                    if (!zzbci.zzpt().zzab(true)) {
                        this.zzaEg = 300000L;
                    }
                }
                break;
            case 7:
                zzc((GoogleApi<?>) message.obj);
                break;
            case 9:
                if (this.zzaCD.containsKey(message.obj)) {
                    this.zzaCD.get(message.obj).resume();
                }
                break;
            case 10:
                zzql();
                break;
            case 11:
                if (this.zzaCD.containsKey(message.obj)) {
                    this.zzaCD.get(message.obj).zzqb();
                }
                break;
            case 12:
                if (this.zzaCD.containsKey(message.obj)) {
                    this.zzaCD.get(message.obj).zzqv();
                }
                break;
            default:
                Log.w("GoogleApiManager", new StringBuilder(31).append("Unknown message id: ").append(message.what).toString());
                return false;
        }
        return true;
    }

    final PendingIntent zza(zzbcf<?> zzbcfVar, int i) {
        zzcuw zzcuwVarZzqw;
        zzbep<?> zzbepVar = this.zzaCD.get(zzbcfVar);
        if (zzbepVar != null && (zzcuwVarZzqw = zzbepVar.zzqw()) != null) {
            return PendingIntent.getActivity(this.mContext, i, zzcuwVarZzqw.zzmF(), VCardConfig.FLAG_CONVERT_PHONETIC_NAME_STRINGS);
        }
        return null;
    }

    public final <O extends Api.ApiOptions> Task<Void> zza(@NonNull GoogleApi<O> googleApi, @NonNull zzbfk<?> zzbfkVar) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(13, new zzbfp(new zzbcd(zzbfkVar, taskCompletionSource), this.zzaEk.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final <O extends Api.ApiOptions> Task<Void> zza(@NonNull GoogleApi<O> googleApi, @NonNull zzbfq<Api.zzb, ?> zzbfqVar, @NonNull zzbgk<Api.zzb, ?> zzbgkVar) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(8, new zzbfp(new zzbcb(new zzbfr(zzbfqVar, zzbgkVar), taskCompletionSource), this.zzaEk.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final Task<Void> zza(Iterable<? extends GoogleApi<?>> iterable) {
        zzbch zzbchVar = new zzbch(iterable);
        Iterator<? extends GoogleApi<?>> it = iterable.iterator();
        while (it.hasNext()) {
            zzbep<?> zzbepVar = this.zzaCD.get(it.next().zzpf());
            if (zzbepVar == null || !zzbepVar.isConnected()) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(2, zzbchVar));
                return zzbchVar.getTask();
            }
        }
        zzbchVar.zzps();
        return zzbchVar.getTask();
    }

    public final void zza(ConnectionResult connectionResult, int i) {
        if (zzc(connectionResult, i)) {
            return;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i, 0, connectionResult));
    }

    public final <O extends Api.ApiOptions> void zza(GoogleApi<O> googleApi, int i, zzbck<? extends Result, Api.zzb> zzbckVar) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, new zzbfp(new zzbca(i, zzbckVar), this.zzaEk.get(), googleApi)));
    }

    public final <O extends Api.ApiOptions, TResult> void zza(GoogleApi<O> googleApi, int i, zzbgc<Api.zzb, TResult> zzbgcVar, TaskCompletionSource<TResult> taskCompletionSource, zzbfy zzbfyVar) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, new zzbfp(new zzbcc(i, zzbgcVar, taskCompletionSource, zzbfyVar), this.zzaEk.get(), googleApi)));
    }

    public final void zza(@NonNull zzbdi zzbdiVar) {
        synchronized (zzuI) {
            if (this.zzaEl != zzbdiVar) {
                this.zzaEl = zzbdiVar;
                this.zzaEm.clear();
                this.zzaEm.addAll(zzbdiVar.zzpP());
            }
        }
    }

    public final void zzb(GoogleApi<?> googleApi) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7, googleApi));
    }

    final void zzb(@NonNull zzbdi zzbdiVar) {
        synchronized (zzuI) {
            if (this.zzaEl == zzbdiVar) {
                this.zzaEl = null;
                this.zzaEm.clear();
            }
        }
    }

    final boolean zzc(ConnectionResult connectionResult, int i) {
        return this.zzaBf.zza(this.mContext, connectionResult, i);
    }

    final void zzpj() {
        this.zzaEk.incrementAndGet();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(10));
    }

    public final void zzpq() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
    }

    public final int zzqk() {
        return this.zzaEj.getAndIncrement();
    }
}
