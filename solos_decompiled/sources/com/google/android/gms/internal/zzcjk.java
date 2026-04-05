package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes36.dex */
@TargetApi(14)
@MainThread
final class zzcjk implements Application.ActivityLifecycleCallbacks {
    private /* synthetic */ zzcix zzbtx;

    private zzcjk(zzcix zzcixVar) {
        this.zzbtx = zzcixVar;
    }

    /* synthetic */ zzcjk(zzcix zzcixVar, zzciy zzciyVar) {
        this(zzcixVar);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        Bundle bundle2;
        Uri data;
        try {
            this.zzbtx.zzwE().zzyB().log("onActivityCreated");
            Intent intent = activity.getIntent();
            if (intent != null && (data = intent.getData()) != null && data.isHierarchical()) {
                if (bundle == null) {
                    Bundle bundleZzq = this.zzbtx.zzwA().zzq(data);
                    this.zzbtx.zzwA();
                    String str = zzckx.zzl(intent) ? "gs" : "auto";
                    if (bundleZzq != null) {
                        this.zzbtx.zzd(str, "_cmp", bundleZzq);
                    }
                }
                String queryParameter = data.getQueryParameter("referrer");
                if (TextUtils.isEmpty(queryParameter)) {
                    return;
                }
                if (!(queryParameter.contains("gclid") && (queryParameter.contains("utm_campaign") || queryParameter.contains("utm_source") || queryParameter.contains("utm_medium") || queryParameter.contains("utm_term") || queryParameter.contains("utm_content")))) {
                    this.zzbtx.zzwE().zzyA().log("Activity created with data 'referrer' param without gclid and at least one utm field");
                    return;
                } else {
                    this.zzbtx.zzwE().zzyA().zzj("Activity created with referrer", queryParameter);
                    if (!TextUtils.isEmpty(queryParameter)) {
                        this.zzbtx.zzb("auto", "_ldl", queryParameter);
                    }
                }
            }
        } catch (Throwable th) {
            this.zzbtx.zzwE().zzyv().zzj("Throwable caught in onActivityCreated", th);
        }
        zzcjl zzcjlVarZzww = this.zzbtx.zzww();
        if (bundle == null || (bundle2 = bundle.getBundle("com.google.firebase.analytics.screen_service")) == null) {
            return;
        }
        zzcjo zzcjoVarZzq = zzcjlVarZzww.zzq(activity);
        zzcjoVarZzq.zzbop = bundle2.getLong("id");
        zzcjoVarZzq.zzbon = bundle2.getString("name");
        zzcjoVarZzq.zzboo = bundle2.getString("referrer_name");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
        this.zzbtx.zzww().onActivityDestroyed(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    @MainThread
    public final void onActivityPaused(Activity activity) {
        this.zzbtx.zzww().onActivityPaused(activity);
        zzckm zzckmVarZzwC = this.zzbtx.zzwC();
        zzckmVarZzwC.zzwD().zzj(new zzckq(zzckmVarZzwC, zzckmVarZzwC.zzkp().elapsedRealtime()));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    @MainThread
    public final void onActivityResumed(Activity activity) {
        this.zzbtx.zzww().onActivityResumed(activity);
        zzckm zzckmVarZzwC = this.zzbtx.zzwC();
        zzckmVarZzwC.zzwD().zzj(new zzckp(zzckmVarZzwC, zzckmVarZzwC.zzkp().elapsedRealtime()));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.zzbtx.zzww().onActivitySaveInstanceState(activity, bundle);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
    }
}
