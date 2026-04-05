package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcjl extends zzciv {
    protected zzcjo zzbtI;
    private volatile AppMeasurement.zzb zzbtJ;
    private AppMeasurement.zzb zzbtK;
    private long zzbtL;
    private final Map<Activity, zzcjo> zzbtM;
    private final CopyOnWriteArrayList<AppMeasurement.zza> zzbtN;
    private boolean zzbtO;
    private AppMeasurement.zzb zzbtP;
    private String zzbtQ;

    public zzcjl(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbtM = new ArrayMap();
        this.zzbtN = new CopyOnWriteArrayList<>();
    }

    @MainThread
    private final void zza(Activity activity, zzcjo zzcjoVar, boolean z) {
        boolean z2;
        boolean zZza = true;
        AppMeasurement.zzb zzbVar = this.zzbtJ != null ? this.zzbtJ : (this.zzbtK == null || Math.abs(super.zzkp().elapsedRealtime() - this.zzbtL) >= 1000) ? null : this.zzbtK;
        AppMeasurement.zzb zzbVar2 = zzbVar != null ? new AppMeasurement.zzb(zzbVar) : null;
        this.zzbtO = true;
        try {
            try {
                Iterator<AppMeasurement.zza> it = this.zzbtN.iterator();
                while (it.hasNext()) {
                    try {
                        zZza &= it.next().zza(zzbVar2, zzcjoVar);
                    } catch (Exception e) {
                        super.zzwE().zzyv().zzj("onScreenChangeCallback threw exception", e);
                    }
                }
                this.zzbtO = false;
                z2 = zZza;
            } catch (Throwable th) {
                this.zzbtO = false;
                throw th;
            }
        } catch (Exception e2) {
            z2 = zZza;
            super.zzwE().zzyv().zzj("onScreenChangeCallback loop threw exception", e2);
            this.zzbtO = false;
        }
        AppMeasurement.zzb zzbVar3 = this.zzbtJ == null ? this.zzbtK : this.zzbtJ;
        if (z2) {
            if (zzcjoVar.zzboo == null) {
                zzcjoVar.zzboo = zzeo(activity.getClass().getCanonicalName());
            }
            zzcjo zzcjoVar2 = new zzcjo(zzcjoVar);
            this.zzbtK = this.zzbtJ;
            this.zzbtL = super.zzkp().elapsedRealtime();
            this.zzbtJ = zzcjoVar2;
            super.zzwD().zzj(new zzcjm(this, z, zzbVar3, zzcjoVar2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zza(@NonNull zzcjo zzcjoVar) {
        super.zzwq().zzJ(super.zzkp().elapsedRealtime());
        if (super.zzwC().zzap(zzcjoVar.zzbtW)) {
            zzcjoVar.zzbtW = false;
        }
    }

    public static void zza(AppMeasurement.zzb zzbVar, Bundle bundle) {
        if (bundle == null || zzbVar == null || bundle.containsKey("_sc")) {
            return;
        }
        if (zzbVar.zzbon != null) {
            bundle.putString("_sn", zzbVar.zzbon);
        }
        bundle.putString("_sc", zzbVar.zzboo);
        bundle.putLong("_si", zzbVar.zzbop);
    }

    private static String zzeo(String str) {
        String[] strArrSplit = str.split("\\.");
        if (strArrSplit.length == 0) {
            return str.substring(0, 36);
        }
        String str2 = strArrSplit[strArrSplit.length - 1];
        return str2.length() > 36 ? str2.substring(0, 36) : str2;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @MainThread
    public final void onActivityDestroyed(Activity activity) {
        this.zzbtM.remove(activity);
    }

    @MainThread
    public final void onActivityPaused(Activity activity) {
        zzcjo zzcjoVarZzq = zzq(activity);
        this.zzbtK = this.zzbtJ;
        this.zzbtL = super.zzkp().elapsedRealtime();
        this.zzbtJ = null;
        super.zzwD().zzj(new zzcjn(this, zzcjoVarZzq));
    }

    @MainThread
    public final void onActivityResumed(Activity activity) {
        zza(activity, zzq(activity), false);
        super.zzwq().zzwm();
    }

    @MainThread
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        zzcjo zzcjoVar;
        if (bundle == null || (zzcjoVar = this.zzbtM.get(activity)) == null) {
            return;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putLong("id", zzcjoVar.zzbop);
        bundle2.putString("name", zzcjoVar.zzbon);
        bundle2.putString("referrer_name", zzcjoVar.zzboo);
        bundle.putBundle("com.google.firebase.analytics.screen_service", bundle2);
    }

    @MainThread
    public final void registerOnScreenChangeCallback(@NonNull AppMeasurement.zza zzaVar) {
        super.zzwo();
        if (zzaVar == null) {
            super.zzwE().zzyx().log("Attempting to register null OnScreenChangeCallback");
        } else {
            this.zzbtN.remove(zzaVar);
            this.zzbtN.add(zzaVar);
        }
    }

    @MainThread
    public final void setCurrentScreen(@NonNull Activity activity, @Size(max = 36, min = 1) @Nullable String str, @Size(max = 36, min = 1) @Nullable String str2) {
        if (activity == null) {
            super.zzwE().zzyx().log("setCurrentScreen must be called with a non-null activity");
            return;
        }
        super.zzwD();
        if (!zzchs.zzR()) {
            super.zzwE().zzyx().log("setCurrentScreen must be called from the main thread");
            return;
        }
        if (this.zzbtO) {
            super.zzwE().zzyx().log("Cannot call setCurrentScreen from onScreenChangeCallback");
            return;
        }
        if (this.zzbtJ == null) {
            super.zzwE().zzyx().log("setCurrentScreen cannot be called while no activity active");
            return;
        }
        if (this.zzbtM.get(activity) == null) {
            super.zzwE().zzyx().log("setCurrentScreen must be called with an activity in the activity lifecycle");
            return;
        }
        if (str2 == null) {
            str2 = zzeo(activity.getClass().getCanonicalName());
        }
        boolean zEquals = this.zzbtJ.zzboo.equals(str2);
        boolean zZzR = zzckx.zzR(this.zzbtJ.zzbon, str);
        if (zEquals && zZzR) {
            super.zzwE().zzyy().log("setCurrentScreen cannot be called with the same class and name");
            return;
        }
        if (str != null && (str.length() <= 0 || str.length() > zzcfy.zzxj())) {
            super.zzwE().zzyx().zzj("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
            return;
        }
        if (str2 != null && (str2.length() <= 0 || str2.length() > zzcfy.zzxj())) {
            super.zzwE().zzyx().zzj("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
            return;
        }
        super.zzwE().zzyB().zze("Setting current screen to name, class", str == null ? "null" : str, str2);
        zzcjo zzcjoVar = new zzcjo(str, str2, super.zzwA().zzzq());
        this.zzbtM.put(activity, zzcjoVar);
        zza(activity, zzcjoVar, true);
    }

    @MainThread
    public final void unregisterOnScreenChangeCallback(@NonNull AppMeasurement.zza zzaVar) {
        super.zzwo();
        this.zzbtN.remove(zzaVar);
    }

    @WorkerThread
    public final void zza(String str, AppMeasurement.zzb zzbVar) {
        super.zzjB();
        synchronized (this) {
            if (this.zzbtQ == null || this.zzbtQ.equals(str) || zzbVar != null) {
                this.zzbtQ = str;
                this.zzbtP = zzbVar;
            }
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    @MainThread
    final zzcjo zzq(@NonNull Activity activity) {
        zzbr.zzu(activity);
        zzcjo zzcjoVar = this.zzbtM.get(activity);
        if (zzcjoVar != null) {
            return zzcjoVar;
        }
        zzcjo zzcjoVar2 = new zzcjo(null, zzeo(activity.getClass().getCanonicalName()), super.zzwA().zzzq());
        this.zzbtM.put(activity, zzcjoVar2);
        return zzcjoVar2;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckx zzwA() {
        return super.zzwA();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchr zzwB() {
        return super.zzwB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckm zzwC() {
        return super.zzwC();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchs zzwD() {
        return super.zzwD();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgx zzwE() {
        return super.zzwE();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchi zzwF() {
        return super.zzwF();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfy zzwG() {
        return super.zzwG();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwn() {
        super.zzwn();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwp() {
        super.zzwp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfo zzwq() {
        return super.zzwq();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfv zzwr() {
        return super.zzwr();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcix zzws() {
        return super.zzws();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgs zzwt() {
        return super.zzwt();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgf zzwu() {
        return super.zzwu();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjp zzwv() {
        return super.zzwv();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjl zzww() {
        return super.zzww();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgt zzwx() {
        return super.zzwx();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfz zzwy() {
        return super.zzwy();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgv zzwz() {
        return super.zzwz();
    }

    @WorkerThread
    public final zzcjo zzzf() {
        zzkC();
        super.zzjB();
        return this.zzbtI;
    }

    public final AppMeasurement.zzb zzzg() {
        super.zzwo();
        AppMeasurement.zzb zzbVar = this.zzbtJ;
        if (zzbVar == null) {
            return null;
        }
        return new AppMeasurement.zzb(zzbVar);
    }
}
