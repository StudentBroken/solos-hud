package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbci implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private static final zzbci zzaBL = new zzbci();
    private final AtomicBoolean zzaBM = new AtomicBoolean();
    private final AtomicBoolean zzaBN = new AtomicBoolean();
    private final ArrayList<zzbcj> mListeners = new ArrayList<>();
    private boolean zzafM = false;

    private zzbci() {
    }

    public static void zza(Application application) {
        synchronized (zzaBL) {
            if (!zzaBL.zzafM) {
                application.registerActivityLifecycleCallbacks(zzaBL);
                application.registerComponentCallbacks(zzaBL);
                zzaBL.zzafM = true;
            }
        }
    }

    private final void zzac(boolean z) {
        synchronized (zzaBL) {
            ArrayList<zzbcj> arrayList = this.mListeners;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                zzbcj zzbcjVar = arrayList.get(i);
                i++;
                zzbcjVar.zzac(z);
            }
        }
    }

    public static zzbci zzpt() {
        return zzaBL;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        boolean zCompareAndSet = this.zzaBM.compareAndSet(true, false);
        this.zzaBN.set(true);
        if (zCompareAndSet) {
            zzac(false);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityResumed(Activity activity) {
        boolean zCompareAndSet = this.zzaBM.compareAndSet(true, false);
        this.zzaBN.set(true);
        if (zCompareAndSet) {
            zzac(false);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
    }

    @Override // android.content.ComponentCallbacks
    public final void onConfigurationChanged(Configuration configuration) {
    }

    @Override // android.content.ComponentCallbacks
    public final void onLowMemory() {
    }

    @Override // android.content.ComponentCallbacks2
    public final void onTrimMemory(int i) {
        if (i == 20 && this.zzaBM.compareAndSet(false, true)) {
            this.zzaBN.set(true);
            zzac(true);
        }
    }

    public final void zza(zzbcj zzbcjVar) {
        synchronized (zzaBL) {
            this.mListeners.add(zzbcjVar);
        }
    }

    @TargetApi(16)
    public final boolean zzab(boolean z) {
        if (!this.zzaBN.get()) {
            if (!com.google.android.gms.common.util.zzs.zzrY()) {
                return true;
            }
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(runningAppProcessInfo);
            if (!this.zzaBN.getAndSet(true) && runningAppProcessInfo.importance > 100) {
                this.zzaBM.set(true);
            }
        }
        return this.zzaBM.get();
    }

    public final boolean zzpu() {
        return this.zzaBM.get();
    }
}
