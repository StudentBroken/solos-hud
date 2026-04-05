package com.google.android.gms.internal;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbfd {
    private final Object zzaEH;

    public zzbfd(Activity activity) {
        zzbr.zzb(activity, "Activity must not be null");
        this.zzaEH = activity;
    }

    public final boolean zzqA() {
        return this.zzaEH instanceof FragmentActivity;
    }

    public final Activity zzqB() {
        return (Activity) this.zzaEH;
    }

    public final FragmentActivity zzqC() {
        return (FragmentActivity) this.zzaEH;
    }
}
