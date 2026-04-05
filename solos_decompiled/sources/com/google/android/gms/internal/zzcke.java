package com.google.android.gms.internal;

import android.content.ComponentName;

/* JADX INFO: loaded from: classes36.dex */
final class zzcke implements Runnable {
    private /* synthetic */ ComponentName val$name;
    private /* synthetic */ zzckc zzbuo;

    zzcke(zzckc zzckcVar, ComponentName componentName) {
        this.zzbuo = zzckcVar;
        this.val$name = componentName;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbuo.zzbue.onServiceDisconnected(this.val$name);
    }
}
